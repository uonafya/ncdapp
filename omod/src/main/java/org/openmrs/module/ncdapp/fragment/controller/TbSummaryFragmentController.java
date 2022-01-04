package org.openmrs.module.ncdapp.fragment.controller;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.ncdapp.NcdappUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TbSummaryFragmentController {
	
	public void controller(FragmentModel model) {
		
		PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
		PatientCalculationContext context = patientCalculationService.createCalculationContext();
		context.setNow(new Date());
		
		KenyaEmrService kenyaEmrService = Context.getService(KenyaEmrService.class);
		
		List<Integer> cohort = new ArrayList<Integer>();
		
		//loop through all and get their patient ids
		if (NcdappUtils.getBasePatientsToWorkWith(kenyaEmrService.getDefaultLocation()).size() > 0) {
			for (Encounter encounter : NcdappUtils.getBasePatientsToWorkWith(kenyaEmrService.getDefaultLocation())) {
				cohort.add(encounter.getPatient().getPatientId());
			}
		}
		
		//exclude dead patients
		Set<Integer> alivePatients = Filters.alive(cohort, context);
		Set<Integer> male = NcdappUtils.male(alivePatients, context);
		Set<Integer> female = Filters.female(alivePatients, context);
		
		//declare concepts
		Concept screened_for_tb = Dictionary.getConcept("1659AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Concept on_treatment = Dictionary.getConcept("1662AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Concept tb_diagnosed = Dictionary.getConcept("159393AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		
		//get the attribute to be displayed to the page
		model.addAttribute("srnM", getCountScreenedForTb(screened_for_tb, male, context));
		model.addAttribute("scrF", getCountScreenedForTb(screened_for_tb, female, context));
		model.addAttribute("ptvM",
		    getCountTbPositive(screened_for_tb, male, context, Arrays.asList(on_treatment, tb_diagnosed)));
		model.addAttribute("ptvF",
		    getCountTbPositive(screened_for_tb, female, context, Arrays.asList(on_treatment, tb_diagnosed)));
	}
	
	private Integer getCountScreenedForTb(Concept q, Set<Integer> cohort, PatientCalculationContext context) {
		Set<Integer> allSet = new HashSet<Integer>();
		
		CalculationResultMap map = Calculations.lastObs(q, cohort, context);
		for (Integer pId : cohort) {
			Concept concept = EmrCalculationUtils.codedObsResultForPatient(map, pId);
			Obs obs = EmrCalculationUtils.obsResultForPatient(map, pId);
			if (obs != null) {
				allSet.add(pId);
			}
		}
		return allSet.size();
	}
	
	private Integer getCountTbPositive(Concept q, Set<Integer> cohort, PatientCalculationContext context, List<Concept> list) {
		Set<Integer> allSet = new HashSet<Integer>();
		
		CalculationResultMap map = Calculations.lastObs(q, cohort, context);
		for (Integer pId : cohort) {
			Obs obs = EmrCalculationUtils.obsResultForPatient(map, pId);
			if (obs != null && list.contains(obs.getValueCoded())) {
				allSet.add(pId);
			}
		}
		return allSet.size();
	}
}

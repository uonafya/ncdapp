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

public class NewlyDiagnosedFragmentController {
	
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
		Concept diseaseType = Dictionary.getConcept("74eb8e8d-d078-4fa3-8973-2d710d8f46df");
		Concept newDm = Dictionary.getConcept("ac58e607-21b9-4c5b-aa67-fa63ff789a12");
		Concept newHtn = Dictionary.getConcept("8d64fd71-8b49-4ecb-8cb8-033597fef7c1");
		
		//get the attribute to be displayed to the page
		model.addAttribute("dTMDM", getCountOfDiseaseType(diseaseType, male, context, Arrays.asList(newDm)));
		model.addAttribute("dTMHTN", getCountOfDiseaseType(diseaseType, male, context, Arrays.asList(newHtn)));
		model.addAttribute("dTFDM", getCountOfDiseaseType(diseaseType, female, context, Arrays.asList(newDm)));
		model.addAttribute("dTFHTN", getCountOfDiseaseType(diseaseType, female, context, Arrays.asList(newHtn)));
	}
	
	private Integer getCountOfDiseaseType(Concept q, Set<Integer> cohort, PatientCalculationContext context,
	        List<Concept> list) {
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

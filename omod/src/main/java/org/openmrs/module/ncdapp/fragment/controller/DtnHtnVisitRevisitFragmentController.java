package org.openmrs.module.ncdapp.fragment.controller;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.ncdapp.NcdappUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DtnHtnVisitRevisitFragmentController {
	
	public void controller(FragmentModel model,
	        @FragmentParam(value = "requiredLocations", required = false) List<Integer> overral_location,
	        @FragmentParam(value = "allPatients", required = false) List<Patient> allPatients) {
		
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
		EncounterType initial = Context.getEncounterService().getEncounterTypeByUuid("cb5f27f0-18f8-11eb-88d7-fb1a7178f8ea");
		EncounterType followup = Context.getEncounterService()
		        .getEncounterTypeByUuid("f1573d1c-18f8-11eb-a453-63d51e56f5cb");
		
		//get the attribute to be displayed to the page
		model.addAttribute("vM", getCountVisit(initial, followup, male, context));
		model.addAttribute("vF", getCountVisit(initial, followup, female, context));
		model.addAttribute("rvM", getCountRevisit(followup, male, context));
		model.addAttribute("rvF", getCountRevisit(followup, female, context));
		
	}
	
	private Integer getCountVisit(EncounterType q1, EncounterType q2, Set<Integer> cohort, PatientCalculationContext context) {
		Set<Integer> allSet = new HashSet<Integer>();
		
		CalculationResultMap map1 = Calculations.lastEncounter(q1, cohort, context);
		CalculationResultMap map2 = Calculations.lastEncounter(q2, cohort, context);
		for (Integer pId : cohort) {
			Encounter encounter1 = EmrCalculationUtils.encounterResultForPatient(map1, pId);
			Encounter encounter2 = EmrCalculationUtils.encounterResultForPatient(map2, pId);
			if (encounter1 != null && encounter2 == null) {
				allSet.add(pId);
			}
		}
		return allSet.size();
	}
	
	private Integer getCountRevisit(EncounterType q2, Set<Integer> cohort, PatientCalculationContext context) {
		Set<Integer> allSet = new HashSet<Integer>();
		
		CalculationResultMap map2 = Calculations.lastEncounter(q2, cohort, context);
		for (Integer pId : cohort) {
			Encounter encounter2 = EmrCalculationUtils.encounterResultForPatient(map2, pId);
			if (encounter2 != null) {
				allSet.add(pId);
			}
		}
		return allSet.size();
	}
}

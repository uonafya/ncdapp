package org.openmrs.module.ncdapp.fragment.controller;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
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

public class NcdSummaryFragmentController {
	
	public void controller(FragmentModel model) {
		
		PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
		PatientCalculationContext context = patientCalculationService.createCalculationContext();
		context.setNow(new Date());
		
		KenyaEmrService kenyaEmrService = Context.getService(KenyaEmrService.class);
		
		EncounterType screeningForm = Context.getEncounterService().getEncounterTypeByUuid(
		    "af5dbd36-18f9-11eb-ae6b-7f4c0920f004");
		EncounterType initialForm = Context.getEncounterService().getEncounterTypeByUuid(
		    "cb5f27f0-18f8-11eb-88d7-fb1a7178f8ea");
		EncounterType followupForm = Context.getEncounterService().getEncounterTypeByUuid(
		    "f1573d1c-18f8-11eb-a453-63d51e56f5cb");
		
		List<Integer> cohort = new ArrayList<Integer>();
		List<Encounter> allEncounters = Context.getEncounterService().getEncounters(null,
		    kenyaEmrService.getDefaultLocation(), null, null, null, Arrays.asList(screeningForm, initialForm, followupForm),
		    null, null, null, false);
		//loop through all and get their patient ids
		if (allEncounters.size() > 0) {
			for (Encounter encounter : allEncounters) {
				cohort.add(encounter.getPatient().getPatientId());
			}
		}
		
		//exclude dead patients
		Set<Integer> alivePatients = Filters.alive(cohort, context);
		Set<Integer> male = NcdappUtils.male(alivePatients, context);
		Set<Integer> female = Filters.female(alivePatients, context);
		
		Concept diabetic = Dictionary.getConcept(Dictionary.METHOD_OF_DELIVERY);
		Concept newDiabetic = Dictionary.getConcept(Dictionary.METHOD_OF_DELIVERY);
		Concept knownDiabetic = Dictionary.getConcept(Dictionary.METHOD_OF_DELIVERY);
		Concept hypertension = Dictionary.getConcept(Dictionary.METHOD_OF_DELIVERY);
		Concept newHypertension = Dictionary.getConcept(Dictionary.METHOD_OF_DELIVERY);
		Concept knownHypertension = Dictionary.getConcept(Dictionary.METHOD_OF_DELIVERY);
		
		//start formulating the values to be displayed on the viewer for diabetes
		model.addAttribute("diabeticMaleZeroTo5",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, male, context, 0, 5));
		model.addAttribute("diabeticMale6To18",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, male, context, 6, 18));
		model.addAttribute("diabeticMale19To35",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, male, context, 19, 35));
		model.addAttribute("diabeticMale36To60",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, male, context, 36, 60));
		model.addAttribute("diabeticMale60To120",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, male, context, 61, 200));
		model.addAttribute("diabeticMaleTotals",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, male, context, 0, 200));
		
		model.addAttribute("diabeticFemaleZeroTo5",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, female, context, 0, 5));
		model.addAttribute("diabeticFemale6To18",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, female, context, 6, 18));
		model.addAttribute("diabeticFemale19To35",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, female, context, 19, 35));
		model.addAttribute("diabeticFemale36To60",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, female, context, 36, 60));
		model.addAttribute("diabeticFemale60To120",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, female, context, 61, 200));
		model.addAttribute("diabeticFemaleTotals",
		    getDiabeticPatients(diabetic, newDiabetic, knownDiabetic, female, context, 0, 200));
		
		//hypertension
		model.addAttribute("hypertensionMaleZeroTo5",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, male, context, 0, 5));
		model.addAttribute("hypertensionMale6To18",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, male, context, 6, 18));
		model.addAttribute("hypertensionMale19To35",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, male, context, 19, 35));
		model.addAttribute("hypertensionMale36To60",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, male, context, 36, 60));
		model.addAttribute("hypertensionMale60To120",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, male, context, 61, 200));
		model.addAttribute("hypertensionMaleTotals",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, male, context, 0, 200));
		
		model.addAttribute("hypertensionFemaleZeroTo5",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, female, context, 0, 5));
		model.addAttribute("hypertensionFemale6To18",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, female, context, 6, 18));
		model.addAttribute("hypertensionFemale19To35",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, female, context, 19, 35));
		model.addAttribute("hypertensionFemale36To60",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, female, context, 36, 60));
		model.addAttribute("hypertensionFemale60To120",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, female, context, 61, 200));
		model.addAttribute("hypertensionFemaleTotals",
		    getDiabeticPatients(hypertension, newHypertension, knownHypertension, female, context, 0, 200));
		
	}
	
	private Integer getDiabeticPatients(Concept q, Concept a1, Concept a2, Set<Integer> cohort,
	        PatientCalculationContext context, int minAge, int maxAge) {
		Set<Integer> allSet = new HashSet<Integer>();
		CalculationResultMap diabeticMap = Calculations.lastObs(q, cohort, context);
		
		for (Integer pId : cohort) {
			Obs obs = EmrCalculationUtils.obsResultForPatient(diabeticMap, pId);
			if (obs != null && (obs.getValueCoded().equals(a1) || obs.getValueCoded().equals(a2))) {
				if (obs.getPerson().getAge() != null && obs.getPerson().getAge() >= minAge
				        && obs.getPerson().getAge() <= maxAge) {
					allSet.add(pId);
				}
			}
			
		}
		return allSet.size();
	}
}

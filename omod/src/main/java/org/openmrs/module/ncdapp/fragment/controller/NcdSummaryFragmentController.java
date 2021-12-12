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
		
		Concept diseaseType = Dictionary.getConcept("74eb8e8d-d078-4fa3-8973-2d710d8f46df");
		Concept newDiabetic = Dictionary.getConcept("ac58e607-21b9-4c5b-aa67-fa63ff789a12");
		Concept knownDiabetic = Dictionary.getConcept("0df832b1-d411-4252-9809-f8a7bde67cde");
		Concept newComorbid = Dictionary.getConcept("b6004d83-a6c4-441c-b95e-e633d399d2fd");
		Concept knownComorbid = Dictionary.getConcept("366f36f1-b1b3-403e-b219-3eef404aa48a");
		Concept newHypertension = Dictionary.getConcept("8d64fd71-8b49-4ecb-8cb8-033597fef7c1");
		Concept knownHypertension = Dictionary.getConcept("e744c6e9-14c6-4cfd-92ce-19387ae87b12");
		//BMI concepts
		Concept weight = Dictionary.getConcept(Dictionary.WEIGHT_KG);
		Concept height = Dictionary.getConcept(Dictionary.HEIGHT_CM);
		//Blood pressure concepts
		Concept systtollic = Dictionary.getConcept("5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Concept diastollic = Dictionary.getConcept("5086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		
		//start formulating the values to be displayed on the viewer for diabetes
		//DM new
		model.addAttribute("diabeticNewMaleZeroTo5", getDiabeticPatients(diseaseType, newDiabetic, male, context, 0, 5));
		model.addAttribute("diabeticNewFemaleZeroTo5", getDiabeticPatients(diseaseType, newDiabetic, female, context, 0, 5));
		model.addAttribute("diabeticNewMale6To18", getDiabeticPatients(diseaseType, newDiabetic, male, context, 6, 18));
		model.addAttribute("diabeticNewFemale6To18", getDiabeticPatients(diseaseType, newDiabetic, female, context, 6, 18));
		model.addAttribute("diabeticNewMale19To35", getDiabeticPatients(diseaseType, newDiabetic, male, context, 19, 35));
		model.addAttribute("diabeticNewFemale19To35", getDiabeticPatients(diseaseType, newDiabetic, female, context, 19, 35));
		model.addAttribute("diabeticNewMale36To60", getDiabeticPatients(diseaseType, newDiabetic, male, context, 36, 60));
		model.addAttribute("diabeticNewFemale36To60", getDiabeticPatients(diseaseType, newDiabetic, female, context, 36, 60));
		model.addAttribute("diabeticNewMale60To120", getDiabeticPatients(diseaseType, newDiabetic, male, context, 61, 200));
		model.addAttribute("diabeticNewFemale60To120",
		    getDiabeticPatients(diseaseType, newDiabetic, female, context, 61, 200));
		//DM known
		model.addAttribute("diabeticKnownMaleZeroTo5", getDiabeticPatients(diseaseType, knownDiabetic, male, context, 0, 5));
		model.addAttribute("diabeticKnownFemaleZeroTo5",
		    getDiabeticPatients(diseaseType, knownDiabetic, female, context, 0, 5));
		model.addAttribute("diabeticKnownMale6To18", getDiabeticPatients(diseaseType, knownDiabetic, male, context, 6, 18));
		model.addAttribute("diabeticKnownFemalee6To18",
		    getDiabeticPatients(diseaseType, knownDiabetic, female, context, 6, 18));
		model.addAttribute("diabeticKnownMale19To35", getDiabeticPatients(diseaseType, knownDiabetic, male, context, 19, 35));
		model.addAttribute("diabeticKnownFemale19To35",
		    getDiabeticPatients(diseaseType, knownDiabetic, female, context, 19, 35));
		model.addAttribute("diabeticKnownMale36To60", getDiabeticPatients(diseaseType, knownDiabetic, male, context, 36, 60));
		model.addAttribute("diabeticKnownFemale36To60",
		    getDiabeticPatients(diseaseType, knownDiabetic, female, context, 36, 60));
		model.addAttribute("diabeticKnownMale60To120",
		    getDiabeticPatients(diseaseType, knownDiabetic, male, context, 61, 200));
		model.addAttribute("diabeticKnownFemale60To120",
		    getDiabeticPatients(diseaseType, knownDiabetic, female, context, 61, 200));
		
		//hypertension new
		model.addAttribute("hypertensionNewMaleZeroTo5",
		    getDiabeticPatients(diseaseType, newHypertension, male, context, 0, 5));
		model.addAttribute("hypertensionNewFemaleZeroTo5",
		    getDiabeticPatients(diseaseType, newHypertension, female, context, 0, 5));
		model.addAttribute("hypertensionNewMale6To18",
		    getDiabeticPatients(diseaseType, newHypertension, male, context, 6, 18));
		model.addAttribute("hypertensionNewFemale6To18",
		    getDiabeticPatients(diseaseType, newHypertension, female, context, 6, 18));
		model.addAttribute("hypertensionNewMale19To35",
		    getDiabeticPatients(diseaseType, newHypertension, male, context, 19, 35));
		model.addAttribute("hypertensionNewFemale19To35",
		    getDiabeticPatients(diseaseType, newHypertension, female, context, 19, 35));
		model.addAttribute("hypertensionNewMale36To60",
		    getDiabeticPatients(diseaseType, newHypertension, male, context, 36, 60));
		model.addAttribute("hypertensionNewFemale36To60",
		    getDiabeticPatients(diseaseType, newHypertension, female, context, 36, 60));
		model.addAttribute("hypertensionNewMale60To120",
		    getDiabeticPatients(diseaseType, newHypertension, male, context, 61, 200));
		model.addAttribute("hypertensionNewFemale60To120",
		    getDiabeticPatients(diseaseType, newHypertension, female, context, 61, 200));
		//Hypertension known
		model.addAttribute("hypertensionKnownMaleZeroTo5",
		    getDiabeticPatients(diseaseType, knownHypertension, male, context, 0, 5));
		model.addAttribute("hypertensionKnownFemaleZeroTo5",
		    getDiabeticPatients(diseaseType, knownHypertension, female, context, 0, 5));
		model.addAttribute("hypertensionKnownMale6To18",
		    getDiabeticPatients(diseaseType, knownHypertension, male, context, 6, 18));
		model.addAttribute("hypertensionKnownFemale6To18",
		    getDiabeticPatients(diseaseType, knownHypertension, female, context, 6, 18));
		model.addAttribute("hypertensionKnownMale19To35",
		    getDiabeticPatients(diseaseType, knownHypertension, male, context, 19, 35));
		model.addAttribute("hypertensionKnownFemale19To35",
		    getDiabeticPatients(diseaseType, knownHypertension, female, context, 19, 35));
		model.addAttribute("hypertensionKnownMale36To60",
		    getDiabeticPatients(diseaseType, knownHypertension, male, context, 36, 60));
		model.addAttribute("hypertensionKnownFemale36To60",
		    getDiabeticPatients(diseaseType, knownHypertension, female, context, 36, 60));
		model.addAttribute("hypertensionKnownMale60To120",
		    getDiabeticPatients(diseaseType, knownHypertension, male, context, 61, 200));
		model.addAttribute("hypertensionKnownFemale60To120",
		    getDiabeticPatients(diseaseType, knownHypertension, female, context, 61, 200));
		
		//comorbid new
		model.addAttribute("newComorbidMaleZeroTo5", getDiabeticPatients(diseaseType, newComorbid, male, context, 0, 5));
		model.addAttribute("newComorbidFemaleZeroTo5", getDiabeticPatients(diseaseType, newComorbid, female, context, 0, 5));
		model.addAttribute("newComorbidMale6To18", getDiabeticPatients(diseaseType, newComorbid, male, context, 6, 18));
		model.addAttribute("newComorbidFemale6To18", getDiabeticPatients(diseaseType, newComorbid, female, context, 6, 18));
		model.addAttribute("newComorbidMale19To35", getDiabeticPatients(diseaseType, newComorbid, male, context, 19, 35));
		model.addAttribute("newComorbidFemale19To35", getDiabeticPatients(diseaseType, newComorbid, female, context, 19, 35));
		model.addAttribute("newComorbidMale36To60", getDiabeticPatients(diseaseType, newComorbid, male, context, 36, 60));
		model.addAttribute("newComorbidFemale36To60", getDiabeticPatients(diseaseType, newComorbid, female, context, 36, 60));
		model.addAttribute("newComorbidMale60To120", getDiabeticPatients(diseaseType, newComorbid, male, context, 61, 200));
		model.addAttribute("newComorbidFemale60To120",
		    getDiabeticPatients(diseaseType, newComorbid, female, context, 61, 200));
		//comorbid known
		model.addAttribute("knownComorbidMaleZeroTo5", getDiabeticPatients(diseaseType, knownComorbid, male, context, 0, 5));
		model.addAttribute("knownComorbidFemaleZeroTo5",
		    getDiabeticPatients(diseaseType, knownComorbid, female, context, 0, 5));
		model.addAttribute("knownComorbidMale6To18", getDiabeticPatients(diseaseType, knownComorbid, male, context, 6, 18));
		model.addAttribute("knownComorbidFemale6To18",
		    getDiabeticPatients(diseaseType, knownComorbid, female, context, 6, 18));
		model.addAttribute("knownComorbidMale19To35", getDiabeticPatients(diseaseType, knownComorbid, male, context, 19, 35));
		model.addAttribute("knownComorbidFemale19To35",
		    getDiabeticPatients(diseaseType, knownComorbid, female, context, 19, 35));
		model.addAttribute("knownComorbidMale36To60", getDiabeticPatients(diseaseType, knownComorbid, male, context, 36, 60));
		model.addAttribute("knownComorbidFemale36To60",
		    getDiabeticPatients(diseaseType, knownComorbid, female, context, 36, 60));
		
		model.addAttribute("knownComorbidMale60To120",
		    getDiabeticPatients(diseaseType, knownComorbid, male, context, 61, 200));
		model.addAttribute("knownComorbidFemale60To120",
		    getDiabeticPatients(diseaseType, knownComorbid, female, context, 61, 200));
		
		//Totals
		model.addAttribute("diabeticNewMaleTotals", getDiabeticPatients(diseaseType, newDiabetic, male, context, 0, 200));
		model.addAttribute("diabeticNewFemaleTotals", getDiabeticPatients(diseaseType, newDiabetic, female, context, 0, 200));
		model.addAttribute("diabeticKnownMaleTotals", getDiabeticPatients(diseaseType, knownDiabetic, male, context, 0, 200));
		model.addAttribute("diabeticKnownFemaleTotals",
		    getDiabeticPatients(diseaseType, knownDiabetic, female, context, 0, 200));
		model.addAttribute("hypertensionNewMaleTotals",
		    getDiabeticPatients(diseaseType, newHypertension, male, context, 0, 200));
		model.addAttribute("hypertensionNewFemaleTotals",
		    getDiabeticPatients(diseaseType, newHypertension, female, context, 0, 200));
		model.addAttribute("hypertensionKnownMaleTotals",
		    getDiabeticPatients(diseaseType, knownHypertension, male, context, 0, 200));
		model.addAttribute("hypertensionKnownFemaleTotals",
		    getDiabeticPatients(diseaseType, knownHypertension, female, context, 0, 200));
		model.addAttribute("newComorbidMaleTotals", getDiabeticPatients(diseaseType, newComorbid, male, context, 0, 200));
		model.addAttribute("newComorbidFemaleTotals", getDiabeticPatients(diseaseType, newComorbid, female, context, 0, 200));
		model.addAttribute("knownComorbidMaleTotals", getDiabeticPatients(diseaseType, knownComorbid, male, context, 0, 200));
		model.addAttribute("knownComorbidFemaleTotals",
		    getDiabeticPatients(diseaseType, knownComorbid, female, context, 0, 200));
		
		////////////////////BMI parameters done here ////////////////////////////////////////////////////////////////////////////
		model.addAttribute("belowOrEqualTo18M", getBmi(weight, height, male, context, 0.0, 18.0));
		model.addAttribute("over18Below25M", getBmi(weight, height, male, context, 18.5, 24.9));
		model.addAttribute("twenty5Below30M", getBmi(weight, height, male, context, 25.0, 29.9));
		model.addAttribute("over30M", getBmi(weight, height, male, context, 30.0, 60.0));
		
		model.addAttribute("belowOrEqualTo18F", getBmi(weight, height, female, context, 0.0, 18.0));
		model.addAttribute("over18Below25F", getBmi(weight, height, female, context, 18.5, 24.9));
		model.addAttribute("twenty5Below30F", getBmi(weight, height, female, context, 25.0, 29.9));
		model.addAttribute("over30F", getBmi(weight, height, female, context, 30.0, 60.0));
		
		/////Blood pressure done here////////////////////////////////
		model.addAttribute("above14090M", getPressure(systtollic, diastollic, male, context, 140, 90));
		model.addAttribute("above14090F", getPressure(systtollic, diastollic, female, context, 140, 90));
		
	}
	
	private Integer getDiabeticPatients(Concept q, Concept a1, Set<Integer> cohort, PatientCalculationContext context,
	        int minAge, int maxAge) {
		Set<Integer> allSet = new HashSet<Integer>();
		CalculationResultMap diabeticMap = Calculations.lastObs(q, cohort, context);
		
		for (Integer pId : cohort) {
			Obs obs = EmrCalculationUtils.obsResultForPatient(diabeticMap, pId);
			if (obs != null && (obs.getValueCoded().equals(a1))) {
				if (obs.getPerson().getAge() != null && obs.getPerson().getAge() >= minAge
				        && obs.getPerson().getAge() <= maxAge) {
					allSet.add(pId);
				}
			}
			
		}
		return allSet.size();
	}
	
	private Integer getBmi(Concept q1, Concept q2, Set<Integer> cohort, PatientCalculationContext context, double minBmi,
	        double maxBmi) {
		Set<Integer> allSet = new HashSet<Integer>();
		CalculationResultMap weightMap = Calculations.lastObs(q1, cohort, context);
		CalculationResultMap heightMap = Calculations.lastObs(q2, cohort, context);
		
		for (Integer pId : cohort) {
			Double weightObs = EmrCalculationUtils.numericObsResultForPatient(weightMap, pId);
			Double heightObs = EmrCalculationUtils.numericObsResultForPatient(heightMap, pId);
			Obs wObs = EmrCalculationUtils.obsResultForPatient(weightMap, pId);
			if (weightObs != null && heightObs != null && wObs != null) {
				//calculate the BMI here
				Double bmi = calculateBmi(weightObs, heightObs);
				if (bmi >= minBmi && bmi <= maxBmi) {
					allSet.add(pId);
				}
			}
			
		}
		return allSet.size();
	}
	
	private Double calculateBmi(Double w, Double h) {
		Double bmi = 0.0;
		
		if (w != null && h != null) {
			double convertedHeihgt = h / 100;
			double productHeight = convertedHeihgt * convertedHeihgt;
			bmi = w / productHeight;
		}
		
		return bmi;
	}
	
	private Integer getPressure(Concept q1, Concept q2, Set<Integer> cohort, PatientCalculationContext context,
	        double systollic, double diastollic) {
		Set<Integer> allSet = new HashSet<Integer>();
		CalculationResultMap systo = Calculations.lastObs(q1, cohort, context);
		CalculationResultMap diasto = Calculations.lastObs(q2, cohort, context);
		
		for (Integer pId : cohort) {
			Double sys = EmrCalculationUtils.numericObsResultForPatient(systo, pId);
			Double dia = EmrCalculationUtils.numericObsResultForPatient(diasto, pId);
			Obs wObs = EmrCalculationUtils.obsResultForPatient(systo, pId);
			if (sys != null && dia != null && wObs != null) {
				if (sys > systollic && dia > diastollic) {
					allSet.add(pId);
				}
			}
			
		}
		return allSet.size();
	}
	
}

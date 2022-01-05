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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreatmentFragmentController {
	
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
		
		//declare concepts here to passed
		/*Concept medicaiton = Dictionary.getConcept(Dictionary.MEDICATION_ORDERED);
		Concept diet = Dictionary.getConcept(Dictionary.DIET);
		Concept physical_exercise = Dictionary.getConcept(Dictionary.PHYSICAL_EXERCISE);
		Concept oglas_metformin = Dictionary.getConcept(Dictionary.OGLAS_METFORMIN);
		Concept oglas_gilberclamide = Dictionary.getConcept(Dictionary.OGLAS_GILBERCLAMIDE);
		Concept oglas_other = Dictionary.getConcept(Dictionary.OGLAS_OTHER);
		Concept insulins_70_30 = Dictionary.getConcept(Dictionary.INSULIN_70_30);
		Concept insulins_soluble = Dictionary.getConcept(Dictionary.INSULIN_SOLUBLE);
		Concept insulins_nph_type_1 = Dictionary.getConcept(Dictionary.INSULIN_NPH_TYPE_1);
		Concept insulins_nph_type_2 = Dictionary.getConcept(Dictionary.INSULIN_NPH_TYPE_2);
		Concept insulins_other_medication = Dictionary.getConcept(Dictionary.INSULIN_OTHER_MEDICATION);
		Concept herbal = Dictionary.getConcept(Dictionary.HERBAL);
		Concept other_non_coded = Dictionary.getConcept(Dictionary.OTHER_NON_CODED);*/
		
		//anthypertensive drugs listed here
		
		//create lists for possible answers per category
		/*List<Concept> diet_and_physical_activities = Arrays.asList(diet, physical_exercise);
		List<Concept> oglas = Arrays.asList(oglas_metformin, oglas_gilberclamide, oglas_other);
		List<Concept> insulin = Arrays.asList(insulins_70_30, insulins_soluble, insulins_nph_type_1, insulins_nph_type_2, insulins_other_medication);
		List<Concept> herbals = Arrays.asList(herbal);
		List<Concept> other = Arrays.asList(other_non_coded);
		List<Concept> anthypertensive = new ArrayList<>();
		anthypertensive.add(Dictionary.getConcept(Dictionary.Captopril));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Enalapril));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Lisinopril));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Perindopril));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Ramipril));
		anthypertensive.add(Dictionary.getConcept(Dictionary.other_ace));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Candesartan));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Irbesartan));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Losartan));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Telmisartan));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Valsartan));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Olmesartan));
		anthypertensive.add(Dictionary.getConcept(Dictionary.other_arb));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Atenolol));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Labetolol));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Propranolol));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Carvedilol));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Metoprolol));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Bisoprolol));
		anthypertensive.add(Dictionary.getConcept(Dictionary.other_b));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Amlodipine));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Felodipine));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Nifedipine));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Chlorthalidone));
		anthypertensive.add(Dictionary.getConcept(Dictionary.HydrochlorothiazideHCTZ));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Indapamide));
		anthypertensive.add(Dictionary.getConcept(Dictionary.other_d1));
		anthypertensive.add(Dictionary.getConcept(Dictionary.other_d2));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Methyldopa));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Hydralazine));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Prazocin));
		anthypertensive.add(Dictionary.getConcept(Dictionary.other_z));
		anthypertensive.add(Dictionary.getConcept(Dictionary.Nebivolol));*/
		
		//map the models and keys to be used on the UI
		/*model.addAttribute("dpM", getCount(medicaiton, diet_and_physical_activities, male, context, overral_location));
		model.addAttribute("ogM", getCount(medicaiton, oglas, male, context, overral_location));
		model.addAttribute("insM", getCount(medicaiton, insulin, male, context, overral_location));
		model.addAttribute("hM", getCount(medicaiton, herbals, male, context, overral_location));
		model.addAttribute("otM", getCount(medicaiton, other, male, context, overral_location));
		model.addAttribute("antHm", getCount(medicaiton, anthypertensive, male, context, overral_location));

		model.addAttribute("dpF", getCount(medicaiton, diet_and_physical_activities, female, context, overral_location));
		model.addAttribute("ogF", getCount(medicaiton, oglas, female, context, overral_location));
		model.addAttribute("insF", getCount(medicaiton, insulin, female, context, overral_location));
		model.addAttribute("hF", getCount(medicaiton, herbals, female, context, overral_location));
		model.addAttribute("otF", getCount(medicaiton, other, female, context, overral_location));
		model.addAttribute("antHf", getCount(medicaiton, anthypertensive, female, context, overral_location));
		*/
		
	}
	
	private Integer getCount(Concept q, List<Concept> options, Set<Integer> cohort, PatientCalculationContext context,
	        List<Integer> loc) {
		Set<Integer> allSet = new HashSet<Integer>();
		CalculationResultMap problem_added = Calculations.lastObs(q, cohort, context);
		for (Integer pId : cohort) {
			Obs obs = EmrCalculationUtils.obsResultForPatient(problem_added, pId);
			if (obs != null && (options.contains(obs.getValueCoded())) && loc.contains(obs.getLocation().getLocationId())) {
				allSet.add(pId);
			}
		}
		return allSet.size();
	}
}

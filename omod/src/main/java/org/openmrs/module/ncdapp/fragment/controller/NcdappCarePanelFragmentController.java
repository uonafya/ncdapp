package org.openmrs.module.ncdapp.fragment.controller;

import org.openmrs.Patient;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.ncdapp.calculation.DiseaseTypeCalculation;
import org.openmrs.module.ncdapp.calculation.PointOfEntryCalculation;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.HashMap;
import java.util.Map;

public class NcdappCarePanelFragmentController {
	
	public void controller(@FragmentParam("patient") Patient patient, @FragmentParam("complete") Boolean complete,
	        FragmentModel model) {
		Map<String, CalculationResult> calculationResults = new HashMap<String, CalculationResult>();
		calculationResults.put("diseaseType",
		    EmrCalculationUtils.evaluateForPatient(DiseaseTypeCalculation.class, null, patient));
		calculationResults.put("pointOfEntry",
		    EmrCalculationUtils.evaluateForPatient(PointOfEntryCalculation.class, null, patient));
		model.addAttribute("calculations", calculationResults);
		
	}
}

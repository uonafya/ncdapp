package org.openmrs.module.ncdapp.fragment.controller;

import org.openmrs.Patient;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.kenyaemr.calculation.library.hiv.BMIAtLastVisitCalculation;
import org.openmrs.module.ncdapp.calculation.CurrentDmHtnComplicationCalculation;
import org.openmrs.module.ncdapp.calculation.CurrentDmHtnDrugsCalculation;
import org.openmrs.module.ncdapp.calculation.CurrentDmHtnLabOrdersCalculation;
import org.openmrs.module.ncdapp.calculation.DiastollicBpCalculation;
import org.openmrs.module.ncdapp.calculation.DiseaseTypeCalculation;
import org.openmrs.module.ncdapp.calculation.FastingBloodSugarCalculation;
import org.openmrs.module.ncdapp.calculation.HBA1cCalculation;
import org.openmrs.module.ncdapp.calculation.PulseRateCalculation;
import org.openmrs.module.ncdapp.calculation.RandomBloodSugarCalculation;
import org.openmrs.module.ncdapp.calculation.SystollicBpCalculation;
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
		calculationResults.put("systollicBp",
		    EmrCalculationUtils.evaluateForPatient(SystollicBpCalculation.class, null, patient));
		calculationResults.put("diastollicBp",
		    EmrCalculationUtils.evaluateForPatient(DiastollicBpCalculation.class, null, patient));
		calculationResults.put("pulse", EmrCalculationUtils.evaluateForPatient(PulseRateCalculation.class, null, patient));
		calculationResults.put("rbs",
		    EmrCalculationUtils.evaluateForPatient(RandomBloodSugarCalculation.class, null, patient));
		calculationResults.put("fbs",
		    EmrCalculationUtils.evaluateForPatient(FastingBloodSugarCalculation.class, null, patient));
		calculationResults.put("hba1c", EmrCalculationUtils.evaluateForPatient(HBA1cCalculation.class, null, patient));
		calculationResults
		        .put("bmi", EmrCalculationUtils.evaluateForPatient(BMIAtLastVisitCalculation.class, null, patient));
		calculationResults.put("durgs",
		    EmrCalculationUtils.evaluateForPatient(CurrentDmHtnDrugsCalculation.class, null, patient));
		calculationResults.put("complications",
		    EmrCalculationUtils.evaluateForPatient(CurrentDmHtnComplicationCalculation.class, null, patient));
		calculationResults.put("labs",
		    EmrCalculationUtils.evaluateForPatient(CurrentDmHtnLabOrdersCalculation.class, null, patient));
		
		model.addAttribute("calculations", calculationResults);
		
	}
}

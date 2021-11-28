package org.openmrs.module.ncdapp.fragment.controller;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.PatientProgram;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.wrapper.EncounterWrapper;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.LinkedHashMap;
import java.util.Map;

public class NcdappCompletionSummaryFragmentController {
	
	public String controller(@FragmentParam("patientProgram") PatientProgram enrollment,
	        @FragmentParam(value = "encounter", required = false) Encounter encounter,
	        @FragmentParam("showClinicalData") boolean showClinicalData, FragmentModel model) {
		
		Map<String, Object> dataPoints = new LinkedHashMap<String, Object>();
		
		dataPoints.put("Completed", enrollment.getDateCompleted());
		
		if (showClinicalData && enrollment.getOutcome() != null) {
			dataPoints.put("Outcome", enrollment.getOutcome());
		}
		
		if (encounter != null) {
			EncounterWrapper wrapper = new EncounterWrapper(encounter);
			
			Obs reasonObs = wrapper.firstObs(Dictionary.getConcept(Dictionary.REASON_FOR_PROGRAM_DISCONTINUATION));
			if (reasonObs != null) {
				dataPoints.put("Reason", reasonObs.getValueCoded());
			}
		}
		
		model.put("dataPoints", dataPoints);
		return "view/dataPoints";
	}
}

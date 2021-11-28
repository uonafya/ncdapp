package org.openmrs.module.ncdapp.fragment.controller;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.PatientProgram;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.wrapper.EncounterWrapper;
import org.openmrs.module.kenyaemr.wrapper.Enrollment;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.LinkedHashMap;
import java.util.Map;

public class NcdappEnrollmentSummaryFragmentController {
	
	public String controller(@FragmentParam("patientProgram") PatientProgram patientProgram,
	        @FragmentParam(value = "encounter", required = false) Encounter encounter,
	        @FragmentParam("showClinicalData") boolean showClinicalData, FragmentModel model) {
		
		Map<String, Object> dataPoints = new LinkedHashMap<String, Object>();
		dataPoints.put("Enrolled", patientProgram.getDateEnrolled());
		
		if (encounter != null) {
			EncounterWrapper wrapper = new EncounterWrapper(encounter);
			
			Obs o = wrapper.firstObs(Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT));
			if (o != null) {
				dataPoints.put("Entry point", o.getValueCoded());
			}
		}
		
		if (showClinicalData) {
			Enrollment enrollment = new Enrollment(patientProgram);
			
			Obs o = enrollment.firstObs(Dictionary.getConcept(Dictionary.CURRENT_WHO_STAGE));
			if (o != null) {
				dataPoints.put("WHO stage", o.getValueCoded());
			}
		}
		
		model.put("dataPoints", dataPoints);
		return "view/dataPoints";
	}
}

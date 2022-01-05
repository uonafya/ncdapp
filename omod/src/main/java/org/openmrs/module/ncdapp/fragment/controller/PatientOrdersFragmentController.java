package org.openmrs.module.ncdapp.fragment.controller;

import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.InventoryCommonService;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class PatientOrdersFragmentController {
	
	public static String PROPERTY_DRUGUNIT = "patientdashboard.dosingUnitConceptId";
	
	public void get(@RequestParam("patientId") Patient patient, PageModel model) {
		InventoryCommonService inventoryCommonService = Context.getService(InventoryCommonService.class);
		List<Concept> drugFrequencyConcept = inventoryCommonService.getDrugFrequency();
		model.addAttribute("drugFrequencyList", drugFrequencyConcept);
		
		model.addAttribute("patient", patient);
		
	}
	
}

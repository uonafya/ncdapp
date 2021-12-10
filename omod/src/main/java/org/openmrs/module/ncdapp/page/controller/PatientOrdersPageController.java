package org.openmrs.module.ncdapp.page.controller;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.InventoryCommonService;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.ncdapp.NcdappConstants;
import org.openmrs.ui.framework.page.PageModel;

import java.util.List;

@AppPage(NcdappConstants.APP_NCD_APP)
public class PatientOrdersPageController {
	
	public static String PROPERTY_DRUGUNIT = "patientdashboard.dosingUnitConceptId";
	
	public void get(PageModel model) {
		InventoryCommonService inventoryCommonService = Context.getService(InventoryCommonService.class);
		List<Concept> drugFrequencyConcept = inventoryCommonService.getDrugFrequency();
		model.addAttribute("drugFrequencyList", drugFrequencyConcept);
	}
	
}

package org.openmrs.module.ncdapp.page.controller;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.InventoryCommonService;
import org.openmrs.ui.framework.page.PageModel;

import java.util.List;

public class prescriptionOrdersPageController {
	
	public void get(PageModel model) {
		//fetch drug frequencies
		InventoryCommonService inventoryCommonService = Context.getService(InventoryCommonService.class);
		List<Concept> drugFrequencyConcept = inventoryCommonService.getDrugFrequency();
		model.addAttribute("drugFrequencyList", drugFrequencyConcept);
		
	}
}

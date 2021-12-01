package org.openmrs.module.ncdapp.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.BillableService;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class LaboratoryOrdersFragmentController {
	
	public List<SimpleObject> getInvestigations(@RequestParam(value = "q") String name, UiUtils ui) {
		BillingService investigations = Context.getService(BillingService.class);
		List<BillableService> investigation = investigations.searchService(name);
		List<SimpleObject> investigationsList = SimpleObject.fromCollection(investigation, ui, "conceptId", "name");
		return investigationsList;
		
	}
}

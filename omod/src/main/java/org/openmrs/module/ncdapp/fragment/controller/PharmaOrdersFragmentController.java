package org.openmrs.module.ncdapp.fragment.controller;

import org.openmrs.Concept;
import org.openmrs.ConceptSet;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.InventoryCommonService;
import org.openmrs.module.hospitalcore.PatientDashboardService;
import org.openmrs.module.hospitalcore.model.InventoryDrug;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.patientdashboardapp.model.Option;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PharmaOrdersFragmentController {
	
	public List<SimpleObject> getDrugs(@RequestParam(value = "q") String name, UiUtils ui) {
		List<InventoryDrug> drugs = Context.getService(PatientDashboardService.class).findDrug(name);
		List<SimpleObject> drugList = SimpleObject.fromCollection(drugs, ui, "id", "name");
		return drugList;
	}
	
	public List<SimpleObject> getFormulationByDrugName(@RequestParam(value = "drugName") String drugName, UiUtils ui) {
		
		InventoryCommonService inventoryCommonService = (InventoryCommonService) Context
		        .getService(InventoryCommonService.class);
		InventoryDrug drug = inventoryCommonService.getDrugByName(drugName);
		
		List<SimpleObject> formulationsList = null;
		
		if (drug != null) {
			List<InventoryDrugFormulation> formulations = new ArrayList<InventoryDrugFormulation>(drug.getFormulations());
			formulationsList = SimpleObject.fromCollection(formulations, ui, "id", "name", "dozage");
		}
		
		return formulationsList;
	}
	
	public static String PROPERTY_DRUGUNIT = "patientdashboard.dosingUnitConceptId";
	
	public List<SimpleObject> getDrugUnit(UiUtils uiUtils) {
		Concept drugUnit = Context.getConceptService().getConcept(
		    Context.getAdministrationService().getGlobalProperty(PROPERTY_DRUGUNIT));
		Collection<ConceptSet> unit = drugUnit.getConceptSets();
		List<Option> drugUnitOptions = new ArrayList<Option>();
		for (ConceptSet conceptSet : unit) {
			drugUnitOptions.add(new Option(conceptSet.getConcept()));
		}
		return SimpleObject.fromCollection(drugUnitOptions, uiUtils, "id", "label", "uuid");
	}
	
}

package org.openmrs.module.ncdapp.fragment.controller;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.openmrs.*;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.*;
import org.openmrs.module.hospitalcore.model.*;
import org.openmrs.module.hospitalcore.util.PatientDashboardConstants;
import org.openmrs.module.ncdapp.model.Prescription;
import org.openmrs.module.ncdapp.model.Procedure;
import org.openmrs.module.patientdashboardapp.model.Option;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrdersFragmentController {
	
	public List<SimpleObject> getProcedures(@RequestParam(value = "q") String name, UiUtils ui) {
		List<Concept> procedures = Context.getService(PatientDashboardService.class).searchProcedure(name);
		List<Procedure> proceduresPriority = new ArrayList<Procedure>();
		for (Concept myConcept : procedures) {
			proceduresPriority.add(new Procedure(myConcept));
		}
		
		List<SimpleObject> proceduresList = SimpleObject
		        .fromCollection(proceduresPriority, ui, "id", "label", "schedulable");
		return proceduresList;
	}
	
	public List<SimpleObject> getInvestigations(@RequestParam(value = "q") String name, UiUtils ui) {
		List<Concept> investigations = Context.getService(PatientDashboardService.class).searchInvestigation(name);
		List<SimpleObject> investigationsList = SimpleObject.fromCollection(investigations, ui, "id", "name");
		return investigationsList;
		
	}
	
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
	
	//method to convert drugs
	public List<Prescription> getPrescriptions(String json) {
		ObjectMapper mapper = new ObjectMapper();
		List<Prescription> list = null;
		try {
			list = Arrays.asList(mapper.readValue(json, Prescription[].class));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void orders(@RequestParam(value = "patientId", required = false) Integer patientId,
	        @RequestParam(value = "drugOrder", required = false) String drugOrder,
	        @RequestParam(value = "selectedProcedureList[]", required = false) Integer[] selectedProcedureList,
	        @RequestParam(value = "selectedInvestigationList[]", required = false) Integer[] selectedInvestigationList) {
		
		List<Prescription> prescriptionList = getPrescriptions(drugOrder);
		
		HospitalCoreService hcs = (HospitalCoreService) Context.getService(HospitalCoreService.class);
		Patient patient = Context.getPatientService().getPatient(patientId);
		BillingService billingService = Context.getService(BillingService.class);
		AdministrationService administrationService = Context.getAdministrationService();
		GlobalProperty procedure = administrationService
		        .getGlobalPropertyObject(PatientDashboardConstants.PROPERTY_POST_FOR_PROCEDURE);
		GlobalProperty investigationn = administrationService
		        .getGlobalPropertyObject(PatientDashboardConstants.PROPERTY_FOR_INVESTIGATION);
		User user = Context.getAuthenticatedUser();
		Date date = new Date();
		PatientDashboardService patientDashboardService = Context.getService(PatientDashboardService.class);
		
		Obs obsGroup = null;
		obsGroup = hcs.getObsGroupCurrentDate(patient.getPersonId());
		Encounter encounter = new Encounter();
		encounter = Context.getEncounterService().getEncounter(patientId);
		
		if (!ArrayUtils.isEmpty(selectedProcedureList)) {
			Concept cProcedure = Context.getConceptService().getConceptByName(procedure.getPropertyValue());
			
			for (Integer pId : selectedProcedureList) {
				Obs oProcedure = new Obs();
				oProcedure.setObsGroup(obsGroup);
				oProcedure.setConcept(cProcedure);
				oProcedure.setValueCoded(Context.getConceptService().getConcept(pId));
				oProcedure.setCreator(user);
				oProcedure.setDateCreated(date);
				oProcedure.setEncounter(encounter);
				oProcedure.setPerson(patient);
				encounter.addObs(oProcedure);
			}
			
		}
		
		if (!ArrayUtils.isEmpty(selectedInvestigationList)) {
			Concept coninvt = Context.getConceptService().getConceptByName(investigationn.getPropertyValue());
			
			for (Integer pId : selectedInvestigationList) {
				Obs obsInvestigation = new Obs();
				obsInvestigation.setObsGroup(obsGroup);
				obsInvestigation.setConcept(coninvt);
				obsInvestigation.setValueCoded(Context.getConceptService().getConcept(pId));
				obsInvestigation.setCreator(user);
				obsInvestigation.setDateCreated(date);
				obsInvestigation.setEncounter(encounter);
				obsInvestigation.setPerson(patient);
				encounter.addObs(obsInvestigation);
			}
			
		}
		
		PatientServiceBill bill = new PatientServiceBill();
		
		bill.setCreatedDate(new Date());
		bill.setPatient(patient);
		bill.setCreator(Context.getAuthenticatedUser());
		
		PatientServiceBillItem item;
		BillableService service;
		BigDecimal amount = new BigDecimal(0);
		
		Integer[] al1 = selectedProcedureList;
		Integer[] al2 = selectedInvestigationList;
		Integer[] merge = null;
		if (al1 != null && al2 != null) {
			merge = new Integer[al1.length + al2.length];
			int j = 0, k = 0, l = 0;
			int max = Math.max(al1.length, al2.length);
			for (int i = 0; i < max; i++) {
				if (j < al1.length)
					merge[l++] = al1[j++];
				if (k < al2.length)
					merge[l++] = al2[k++];
			}
		} else if (al1 != null) {
			merge = selectedProcedureList;
		} else if (al2 != null) {
			merge = selectedInvestigationList;
		}
		
		boolean serviceAvailable = false;
		if (merge != null) {
			for (Integer iId : merge) {
				Concept c = Context.getConceptService().getConcept(iId);
				service = billingService.getServiceByConceptId(c.getConceptId());
				if (service != null) {
					serviceAvailable = true;
					amount = service.getPrice();
					item = new PatientServiceBillItem();
					item.setCreatedDate(new Date());
					item.setName(service.getName());
					item.setPatientServiceBill(bill);
					item.setQuantity(1);
					item.setService(service);
					item.setUnitPrice(service.getPrice());
					item.setAmount(amount);
					item.setActualAmount(amount);
					//item.setOrderType("SERVICE");
					bill.addBillItem(item);
				}
			}
			bill.setAmount(amount);
			bill.setActualAmount(amount);
			bill.setEncounter(encounter);
			if (serviceAvailable == true) {
				bill = billingService.savePatientServiceBill(bill);
			}
			
			PatientServiceBill patientServiceBill = billingService.getPatientServiceBillById(bill.getPatientServiceBillId());
			if (patientServiceBill != null) {
				billingService.saveBillEncounterAndOrder(patientServiceBill);
			}
		}
		
		if (!ArrayUtils.isEmpty(selectedProcedureList)) {
			Concept conpro = Context.getConceptService().getConceptByName(procedure.getPropertyValue());
			for (Integer pId : selectedProcedureList) {
				BillableService billableService = billingService.getServiceByConceptId(pId);
				OpdTestOrder opdTestOrder = new OpdTestOrder();
				opdTestOrder.setPatient(patient);
				opdTestOrder.setEncounter(encounter);
				opdTestOrder.setConcept(conpro);
				opdTestOrder.setTypeConcept(DepartmentConcept.TYPES[1]);
				opdTestOrder.setValueCoded(Context.getConceptService().getConcept(pId));
				opdTestOrder.setCreator(user);
				opdTestOrder.setCreatedOn(date);
				opdTestOrder.setBillingStatus(0);
				opdTestOrder.setBillableService(billableService);
				opdTestOrder.setFromDept("DM/HTN");
				patientDashboardService.saveOrUpdateOpdOrder(opdTestOrder);
			}
			
		}
		
		if (!ArrayUtils.isEmpty(selectedInvestigationList)) {
			Concept coninvt = Context.getConceptService().getConceptByName(investigationn.getPropertyValue());
			for (Integer iId : selectedInvestigationList) {
				BillableService billableService = billingService.getServiceByConceptId(iId);
				OpdTestOrder opdTestOrder = new OpdTestOrder();
				opdTestOrder.setPatient(patient);
				opdTestOrder.setEncounter(encounter);
				opdTestOrder.setConcept(coninvt);
				opdTestOrder.setTypeConcept(DepartmentConcept.TYPES[2]);
				opdTestOrder.setValueCoded(Context.getConceptService().getConcept(iId));
				opdTestOrder.setCreator(user);
				opdTestOrder.setCreatedOn(date);
				opdTestOrder.setBillingStatus(0);
				opdTestOrder.setBillableService(billableService);
				opdTestOrder.setScheduleDate(date);
				opdTestOrder.setFromDept("DM/HTN");
				patientDashboardService.saveOrUpdateOpdOrder(opdTestOrder);
			}
		}
		
		for (Prescription p : prescriptionList) {
			
			InventoryCommonService inventoryCommonService = Context.getService(InventoryCommonService.class);
			InventoryDrug inventoryDrug = inventoryCommonService.getDrugByName(p.getName());
			InventoryDrugFormulation inventoryDrugFormulation = inventoryCommonService.getDrugFormulationById(p
			        .getFormulation());
			Concept freCon = Context.getConceptService().getConcept(p.getFrequency().trim());
			Concept dosageUnitConcept = Context.getConceptService().getConcept(p.getDrugUnit());
			
			OpdDrugOrder opdDrugOrder = new OpdDrugOrder();
			opdDrugOrder.setPatient(patient);
			opdDrugOrder.setEncounter(encounter);
			opdDrugOrder.setInventoryDrug(inventoryDrug);
			opdDrugOrder.setInventoryDrugFormulation(inventoryDrugFormulation);
			opdDrugOrder.setFrequency(freCon);
			opdDrugOrder.setNoOfDays(p.getDays());
			opdDrugOrder.setDosage(p.getDosage());
			opdDrugOrder.setDosageUnit(dosageUnitConcept);
			opdDrugOrder.setComments(p.getComment());
			opdDrugOrder.setCreator(user);
			opdDrugOrder.setCreatedOn(date);
			opdDrugOrder.setReferralWardName("DM/HTN");
			patientDashboardService.saveOrUpdateOpdDrugOrder(opdDrugOrder);
		}
	}
}

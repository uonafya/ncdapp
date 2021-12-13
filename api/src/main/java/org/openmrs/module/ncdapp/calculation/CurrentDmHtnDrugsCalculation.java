package org.openmrs.module.ncdapp.calculation;

import org.openmrs.Encounter;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.hospitalcore.PatientDashboardService;
import org.openmrs.module.hospitalcore.model.OpdDrugOrder;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CurrentDmHtnDrugsCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map,
	        PatientCalculationContext context) {
		
		CalculationResultMap ret = new CalculationResultMap();
		
		CalculationResultMap lastEncounter = Calculations.lastEncounter(Context.getEncounterService()
		        .getEncounterTypeByUuid("ba45c278-f290-11ea-9666-1b3e6e848887"), cohort, context);
		for (Integer pId : cohort) {
			StringBuilder drugs = new StringBuilder();
			
			Encounter lastOpdEncounter = EmrCalculationUtils.encounterResultForPatient(lastEncounter, pId);
			
			List<OpdDrugOrder> opdDrugs = Context.getService(PatientDashboardService.class)
			        .getOpdDrugOrder(lastOpdEncounter);
			
			String drugName = "";
			String inventoryFormulation = "";
			String dosage = "";
			String frequency = "";
			
			for (OpdDrugOrder opdDrugOrder : opdDrugs) {
				if (opdDrugOrder != null) {
					if (opdDrugOrder.getInventoryDrug() != null) {
						drugName = opdDrugOrder.getInventoryDrug().getName();
					}
					if (opdDrugOrder.getInventoryDrugFormulation() != null) {
						inventoryFormulation = opdDrugOrder.getInventoryDrugFormulation().getName();
					}
					if (opdDrugOrder.getDosage() != null) {
						dosage = opdDrugOrder.getDosage();
					}
					if (opdDrugOrder.getFrequency() != null && opdDrugOrder.getFrequency().getName() != null) {
						frequency = opdDrugOrder.getFrequency().getName().getName();
					}
				}
				drugs.append(drugName).append(" ").append(inventoryFormulation).append(" ").append(dosage).append(" ")
				        .append(frequency).append("\n");
			}
			ret.put(pId, new SimpleResult(drugs, this));
		}
		
		return ret;
	}
}

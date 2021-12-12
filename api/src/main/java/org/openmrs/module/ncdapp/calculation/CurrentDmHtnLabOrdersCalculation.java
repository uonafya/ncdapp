package org.openmrs.module.ncdapp.calculation;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.ncdapp.NcdappUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class CurrentDmHtnLabOrdersCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map,
	        PatientCalculationContext context) {
		
		CalculationResultMap ret = new CalculationResultMap();
		
		CalculationResultMap lastLabEncounter = Calculations.lastEncounter(Context.getEncounterService()
		        .getEncounterTypeByUuid("11d3f37a-f282-11ea-a825-1b5b1ff1b854"), cohort, context);
		for (Integer pId : cohort) {
			StringBuilder labResults = new StringBuilder();
			
			Encounter lastLabEncounterDetails = EmrCalculationUtils.encounterResultForPatient(lastLabEncounter, pId);
			
			Set<Obs> Obs = lastLabEncounterDetails.getObs();
			String valueCoded = "";
			String valueText = "";
			String valueNumeric = "";
			String obsDateTime = "";
			String testName = "";
			for (Obs labObs : Obs) {
				if (labObs != null && labObs.getConcept() != null && labObs.getConcept().getName() != null
				        && labObs.getConcept().getName().getName() != null) {
					testName = labObs.getConcept().getName().getName();
				}
				if (labObs != null && labObs.getValueCoded() != null) {
					valueCoded = labObs.getValueCoded().getName().getName();
				}
				if (labObs != null && labObs.getValueText() != null) {
					valueText = labObs.getValueText();
				}
				if (labObs != null && labObs.getValueNumeric() != null) {
					valueNumeric = String.valueOf(labObs.getValueNumeric());
				}
				if (labObs != null && labObs.getObsDatetime() != null) {
					obsDateTime = NcdappUtils.formatDate(labObs.getObsDatetime());
				}
				labResults.append(testName).append(" ").append(valueCoded).append(" ").append(valueNumeric).append(" ")
				        .append(valueText).append(" ").append(obsDateTime).append("\n");
			}
			ret.put(pId, new SimpleResult(labResults, this));
		}
		
		return ret;
	}
}

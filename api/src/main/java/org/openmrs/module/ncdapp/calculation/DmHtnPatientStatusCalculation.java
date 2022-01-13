package org.openmrs.module.ncdapp.calculation;

import org.openmrs.Concept;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;

import java.util.Collection;
import java.util.Map;

public class DmHtnPatientStatusCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map,
	        PatientCalculationContext context) {
		CalculationResultMap ret = new CalculationResultMap();
		
		CalculationResultMap reasonsForDiscontinuation = Calculations.lastObs(
		    Dictionary.getConcept("161555AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), cohort, context);
		
		for (Integer pid : cohort) {
			String value = "";
			
			Concept codedValue = EmrCalculationUtils.codedObsResultForPatient(reasonsForDiscontinuation, pid);
			
			if (codedValue != null) {
				if (codedValue.equals(Dictionary.getConcept("159492AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))) {
					value = "Transferred Out";
				} else if (codedValue.equals(Dictionary.getConcept("160034AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))) {
					value = "Died";
				} else if (codedValue.equals(Dictionary.getConcept("5240AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))) {
					value = "Lost to follow";
				} else if (codedValue.equals(Dictionary.getConcept("819AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))) {
					value = "Cannot afford treatment";
				} else if (codedValue.equals(Dictionary.getConcept("5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))) {
					value = "Other";
				} else if (codedValue.equals(Dictionary.getConcept("1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))) {
					value = "Unknown";
				} else {
					value = "NA";
				}
			}
			
			ret.put(pid, new SimpleResult(value, this));
		}
		
		return ret;
	}
	
}

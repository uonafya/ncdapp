package org.openmrs.module.ncdapp.calculation;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.Dictionary;

import java.util.Collection;
import java.util.Map;

public class CurrentDmHtnComplicationCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> collection, Map<String, Object> map,
	        PatientCalculationContext patientCalculationContext) {
		CalculationResultMap ret = new CalculationResultMap();
		
		CalculationResultMap complications = Calculations.lastObs(
		    Dictionary.getConcept("6042AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), collection, patientCalculationContext);
		ret.putAll(complications);
		return ret;
	}
}

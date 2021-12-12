package org.openmrs.module.ncdapp.calculation;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.Dictionary;

import java.util.Collection;
import java.util.Map;

public class HBA1cCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> collection, Map<String, Object> map,
	        PatientCalculationContext patientCalculationContext) {
		CalculationResultMap ret = new CalculationResultMap();
		
		CalculationResultMap hba1c = Calculations.lastObs(Dictionary.getConcept("159644AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
		    collection, patientCalculationContext);
		ret.putAll(hba1c);
		return ret;
	}
}

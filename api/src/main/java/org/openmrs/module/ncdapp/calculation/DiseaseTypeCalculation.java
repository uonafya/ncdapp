package org.openmrs.module.ncdapp.calculation;

import org.openmrs.calculation.Calculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.Dictionary;

import java.util.Collection;
import java.util.Map;

public class DiseaseTypeCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> collection, Map<String, Object> map,
	        PatientCalculationContext patientCalculationContext) {
		CalculationResultMap ret = new CalculationResultMap();
		
		CalculationResultMap getDiseaseType = Calculations.lastObs(
		    Dictionary.getConcept("74eb8e8d-d078-4fa3-8973-2d710d8f46df"), collection, patientCalculationContext);
		ret.putAll(getDiseaseType);
		return ret;
	}
}

package org.openmrs.module.ncdapp.calculation;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Obs;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.Dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CurrentDmHtnComplicationCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> collection, Map<String, Object> map,
	        PatientCalculationContext patientCalculationContext) {
		CalculationResultMap ret = new CalculationResultMap();
		List<String> results = new ArrayList<String>();
		
		CalculationResultMap complications = Calculations.allObs(
		    Dictionary.getConcept("6042AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), collection, patientCalculationContext);
		for (Integer pId : collection) {
			ListResult listResult = (ListResult) complications.get(pId);
			
			List<Obs> obsList = CalculationUtils.extractResultValues(listResult);
			
			for (Obs obs : obsList) {
				results.add(obs.getValueCoded().getName().getName());
			}
			
			ret.put(pId, new SimpleResult(StringUtils.join(results, ","), this));
		}
		return ret;
	}
}

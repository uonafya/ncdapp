package org.openmrs.module.ncdapp.calculation;

import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;

import java.util.Collection;
import java.util.Map;

public class VillageCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	        PatientCalculationContext context) {
		
		CalculationResultMap ret = new CalculationResultMap();
		
		PersonService personService = Context.getPersonService();
		
		for (Integer ptId : cohort) {
			Person person = personService.getPerson(ptId);
			if (person.getPersonAddress() != null) {
				ret.put(ptId, new SimpleResult(person.getPersonAddress().getCityVillage(), this));
			}
		}
		
		return ret;
	}
}

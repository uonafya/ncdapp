package org.openmrs.module.ncdapp;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyacore.calculation.Calculations;

import java.util.Collection;
import java.util.Set;

public class NcdappUtils {
	
	public static Set<Integer> male(Collection<Integer> cohort, PatientCalculationContext context) {
		return CalculationUtils.patientsThatPass(Calculations.genders(cohort, context), "M");
	}
}

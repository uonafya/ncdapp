package org.openmrs.module.ncdapp;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyacore.calculation.Calculations;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

public class NcdappUtils {
	
	public static Set<Integer> male(Collection<Integer> cohort, PatientCalculationContext context) {
		return CalculationUtils.patientsThatPass(Calculations.genders(cohort, context), "M");
	}
	
	public static Double calculateBmi(Double w, Double h) {
		Double bmi = 0.0;
		
		if (w != null && h != null) {
			double convertedHeihgt = h / 100;
			double productHeight = convertedHeihgt * convertedHeihgt;
			bmi = w / productHeight;
		}
		
		return bmi;
	}
	
	public static String formatDate(Date date) {
		
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		
		return formatter.format(date);
	}
}

package org.openmrs.module.ncdapp;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;

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
	
	public static CohortDefinition allDmHtnForScreeningPatientCohort(int enc1) {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("Active Patients in the DM and HTN screening encounter type");
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		cd.setQuery("SELECT p.patient_id FROM patient p INNER JOIN encounter e ON p.patient_id=e.patient_id WHERE e.encounter_datetime BETWEEN :startDate AND :endDate AND  e.encounter_type IN("
		        + enc1 + ")");
		return cd;
	}
	
	public static CohortDefinition allDmHtnProgramPatientCohort(int program) {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("Active Patients in the DM and HTN Program");
		cd.setQuery("SELECT p.patient_id FROM patient p INNER JOIN patient_program pg ON p.patient_id=pg.patient_id INNER JOIN program prg ON prg.program_id=pg.program_id WHERE prg.program_id="
		        + program);
		return cd;
	}
}

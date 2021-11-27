package org.openmrs.module.ncdapp.reporting.cohort;

import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Moh740CohortDefinition {
	
	public CohortDefinition getAll() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("All patients");
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setQuery("SELECT patient_id FROM patient");
		return cd;
	}
}

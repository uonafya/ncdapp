package org.openmrs.module.ncdapp.reporting.indicator;

import org.openmrs.module.ncdapp.reporting.cohort.Moh740CohortDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.kenyaemr.reporting.EmrReportingUtils.cohortIndicator;
import static org.openmrs.module.kenyacore.report.ReportUtils.map;

@Component
public class Moh740IndicatorDefinition {
	
	private Moh740CohortDefinition moh740CohortDefinition;
	
	@Autowired
	public Moh740IndicatorDefinition(Moh740CohortDefinition moh740CohortDefinition) {
		this.moh740CohortDefinition = moh740CohortDefinition;
	}
	
	public CohortIndicator getAllPatients() {
		return cohortIndicator("All patients",
		    map(moh740CohortDefinition.getAll(), "startDate=${startDate},endDate=${endDate}"));
	}
}

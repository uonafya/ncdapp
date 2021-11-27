package org.openmrs.module.ncdapp.reporting.dataset;

import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.ncdapp.reporting.indicator.Moh740IndicatorDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Moh740DatasetDefinition {
	
	private Moh740IndicatorDefinition moh740IndicatorDefinition;
	
	@Autowired
	public Moh740DatasetDefinition(Moh740IndicatorDefinition moh740IndicatorDefinition) {
		this.moh740IndicatorDefinition = moh740IndicatorDefinition;
	}
	
	public DataSetDefinition getMoh740Data() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		String indParam = "startDate=${startDate},endDate=${endDate}";
		dsd.setName("MOH740");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		dsd.addColumn("Total", "Total all", ReportUtils.map(moh740IndicatorDefinition.getAllPatients(), indParam), "");
		return dsd;
	}
}

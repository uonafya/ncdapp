package org.openmrs.module.ncdapp.reports;

import org.openmrs.Program;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.ncdapp.NcdappUtils;
import org.openmrs.module.ncdapp.reporting.dataset.Moh740DatasetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.kenyacore.report.ReportUtils.map;

@Component
@Builds({ "ncdapp.ncd.report.moh740" })
public class SetupMoh740Report extends AbstractReportBuilder {
	
	private Moh740DatasetDefinition moh740DatasetDefinition;
	
	@Autowired
	public SetupMoh740Report(Moh740DatasetDefinition moh740DatasetDefinition) {
		this.moh740DatasetDefinition = moh740DatasetDefinition;
	}
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor,
	        ReportDefinition reportDefinition) {
		Program program = MetadataUtils.existing(Program.class, "8b4f6a38-4f5e-11ec-a4c2-a75a2e13cdaa");
		reportDefinition.setBaseCohortDefinition(ReportUtils.map(NcdappUtils.allDmHtnProgramPatientCohort(program
		        .getProgramId())));
		return Arrays.asList(map(moh740DatasetDefinition.getMoh740Data(), "startDate=${startDate},endDate=${endDate}"));
	}
}

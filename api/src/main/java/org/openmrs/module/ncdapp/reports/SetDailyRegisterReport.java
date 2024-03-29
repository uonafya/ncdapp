package org.openmrs.module.ncdapp.reports;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.report.HybridReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractHybridReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.calculation.library.hiv.BMIAtLastVisitCalculation;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.reporting.data.converter.CalculationResultConverter;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.ncdapp.NcdappUtils;
import org.openmrs.module.ncdapp.calculation.DmHtnDiagnosisCalculation;
import org.openmrs.module.ncdapp.reporting.data.converter.HtnDmDataConverter;
import org.openmrs.module.ncdapp.reporting.data.converter.ObjectCounterConverter;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.converter.ObsValueConverter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDatetimeDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.AgeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ConvertedPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ObsForPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonIdDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;
import org.openmrs.module.reporting.query.encounter.definition.SqlEncounterQuery;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.kenyaui.KenyaUiConstants.DATE_FORMAT;

@Component
@Builds({ "ncdapp.ncd.report.dailyRegister" })
public class SetDailyRegisterReport extends AbstractHybridReportBuilder {
	
	@Override
	protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor hybridReportDescriptor,
	        PatientDataSetDefinition patientDataSetDefinition) {
		return null;
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		Program program = MetadataUtils.existing(Program.class, "8b4f6a38-4f5e-11ec-a4c2-a75a2e13cdaa");
		report.setBaseCohortDefinition(ReportUtils.map(NcdappUtils.allDmHtnProgramPatientCohort(program.getProgramId())));
		return Arrays.asList(ReportUtils.map(dailyRegister(), "startDate=${startDate},endDate=${endDate+23h+59m}"));
	}
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	private DataSetDefinition dailyRegister() {
		EncounterDataSetDefinition dsd = new EncounterDataSetDefinition();
		String paramMapping = "startDate=${startDate},endDate=${endDate+23h+59m}";
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.setName("daily");
		PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class,
		    CommonMetadata._PatientIdentifierType.OPENMRS_ID);
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(
		        upn.getName(), upn), identifierFormatter);
		EncounterType screeningEncounter = MetadataUtils.existing(EncounterType.class,
		    "af5dbd36-18f9-11eb-ae6b-7f4c0920f004");
		EncounterType initialEncounter = MetadataUtils.existing(EncounterType.class, "cb5f27f0-18f8-11eb-88d7-fb1a7178f8ea");
		EncounterType followupEncounter = MetadataUtils
		        .existing(EncounterType.class, "f1573d1c-18f8-11eb-a453-63d51e56f5cb");
		
		dsd.addRowFilter(ReportUtils.map(
		    allDailyPatientsCohort(screeningEncounter.getEncounterTypeId(), initialEncounter.getEncounterTypeId(),
		        followupEncounter.getEncounterTypeId()), paramMapping));
		
		DataConverter formatter = new ObjectFormatter("{familyName}, {givenName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), formatter);
		dsd.addColumn("counnt", new PersonIdDataDefinition(), "", new ObjectCounterConverter());
		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		dsd.addColumn("Name", nameDef, "");
		dsd.addColumn("Identifier", identifierDef, "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "", null);
		dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
		dsd.addColumn("Age", new AgeDataDefinition(), "", null);
		dsd.addColumn("EncounterDate", new EncounterDatetimeDataDefinition(), "", new DateConverter(DATE_FORMAT));
		dsd.addColumn("weight",
		    getObservation(Context.getConceptService().getConceptByUuid("5089AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("height",
		    getObservation(Context.getConceptService().getConceptByUuid("5090AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("bmi", new CalculationDataDefinition("bmi", new BMIAtLastVisitCalculation()), "",
		    new CalculationResultConverter());
		dsd.addColumn("wc",
		    getObservation(Context.getConceptService().getConceptByUuid("163080AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("systolic",
		    getObservation(Context.getConceptService().getConceptByUuid("5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("diastolic",
		    getObservation(Context.getConceptService().getConceptByUuid("5086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("htn",
		    getObservation(Context.getConceptService().getConceptByUuid("74eb8e8d-d078-4fa3-8973-2d710d8f46df")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new HtnDmDataConverter(1));
		dsd.addColumn("dm",
		    getObservation(Context.getConceptService().getConceptByUuid("74eb8e8d-d078-4fa3-8973-2d710d8f46df")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new HtnDmDataConverter(2));
		dsd.addColumn("rbs",
		    getObservation(Context.getConceptService().getConceptByUuid("887AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("fbs",
		    getObservation(Context.getConceptService().getConceptByUuid("160912AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("hba1c",
		    getObservation(Context.getConceptService().getConceptByUuid("160912AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("diagnosis", new CalculationDataDefinition("diagnosis", new DmHtnDiagnosisCalculation()), "",
		    new CalculationResultConverter());
		
		return dsd;
	}
	
	private EncounterQuery allDailyPatientsCohort(int enc1, int enc2, int enc3) {
		SqlEncounterQuery cd = new SqlEncounterQuery();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setName("Active screened for DM and HTN Patients");
		cd.setQuery("SELECT e.encounter_id FROM encounter e  INNER JOIN patient p ON p.patient_id=e.patient_id  WHERE e.encounter_datetime BETWEEN :startDate AND :endDate AND e.encounter_type IN("
		        + enc1 + "," + enc2 + "," + enc3 + ")");
		return cd;
	}
	
	private DataDefinition getObservation(Concept question) {
		ObsForPersonDataDefinition obs = new ObsForPersonDataDefinition();
		obs.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		obs.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		obs.setWhich(TimeQualifier.LAST);
		obs.setQuestion(question);
		return obs;
		
	}
}

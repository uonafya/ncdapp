package org.openmrs.module.ncdapp.reports;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAttributeType;
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
import org.openmrs.module.ncdapp.calculation.VillageCalculation;
import org.openmrs.module.ncdapp.reporting.data.converter.ObjectCounterConverter;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.converter.ObsValueConverter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDatetimeDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ConvertedPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ObsForPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonAttributeDataDefinition;
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
@Builds({ "ncdapp.ncd.report.screening.Register" })
public class SetupScreeningRegisterReport extends AbstractHybridReportBuilder {
	
	@Override
	protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor hybridReportDescriptor,
	        PatientDataSetDefinition patientDataSetDefinition) {
		return null;
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		EncounterType screeningEncounter = MetadataUtils.existing(EncounterType.class,
		    "af5dbd36-18f9-11eb-ae6b-7f4c0920f004");
		report.setBaseCohortDefinition(ReportUtils.map(
		    NcdappUtils.allDmHtnForScreeningPatientCohort(screeningEncounter.getEncounterTypeId()),
		    "startDate=${startDate},endDate=${endDate}"));
		return Arrays.asList(ReportUtils.map(datasetColumns(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	private DataSetDefinition datasetColumns() {
		EncounterDataSetDefinition dsd = new EncounterDataSetDefinition();
		EncounterType screeningEncounter = MetadataUtils.existing(EncounterType.class,
		    "af5dbd36-18f9-11eb-ae6b-7f4c0920f004");
		String paramMapping = "startDate=${startDate},endDate=${endDate+1d}";
		dsd.setName("screening");
		dsd.setDescription("Visit Screening information");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addRowFilter(allScreenedPatientsCohort(screeningEncounter.getEncounterTypeId()), paramMapping);
		
		//Identifiers
		PatientIdentifierType nationalIdType = MetadataUtils.existing(PatientIdentifierType.class,
		    CommonMetadata._PatientIdentifierType.NATIONAL_ID);
		
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		DataDefinition nationalId = new ConvertedPatientDataDefinition("nationalId", new PatientIdentifierDataDefinition(
		        nationalIdType.getName(), nationalIdType), identifierFormatter);
		DataDefinition opdNumberId = new ConvertedPatientDataDefinition("opdId", new PatientIdentifierDataDefinition(
		        nationalIdType.getName(), nationalIdType), identifierFormatter);
		
		PersonAttributeType phoneNumber = MetadataUtils.existing(PersonAttributeType.class,
		    CommonMetadata._PersonAttributeType.TELEPHONE_CONTACT);
		
		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName} {middleName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
		
		dsd.addColumn("count", new PersonIdDataDefinition(), "", new ObjectCounterConverter());
		dsd.addColumn("id", new PatientIdDataDefinition(), "");
		dsd.addColumn("Name", nameDef, "");
		dsd.addColumn("NationalId", nationalId, "");
		dsd.addColumn("opdNumberId", opdNumberId, "");
		dsd.addColumn("phoneNumber", new PersonAttributeDataDefinition(phoneNumber), "");
		dsd.addColumn("village", new CalculationDataDefinition("village", new VillageCalculation()), "",
		    new CalculationResultConverter());
		dsd.addColumn("setting",
		    getObservation(Context.getConceptService().getConceptByUuid("a69f18da-54ae-4a14-b040-bc9e03f835fc")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("pregnant",
		    getObservation(Context.getConceptService().getConceptByUuid("5272AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("knownHypertensive",
		    getObservation(Context.getConceptService().getConceptByUuid("e08c9557-da37-49b9-832f-29b6d8a9dc45")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("ontreatment",
		    getObservation(Context.getConceptService().getConceptByUuid("45d9f615-3f8c-493f-b471-f12aee7e2dcf")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("bp1systollic",
		    getObservation(Context.getConceptService().getConceptByUuid("5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("bp1diastollic",
		    getObservation(Context.getConceptService().getConceptByUuid("5086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("bp2systollic",
		    getObservation(Context.getConceptService().getConceptByUuid("e411bc5a-3a31-43c7-a4ad-eb7ee432042d")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("bp2diastollic",
		    getObservation(Context.getConceptService().getConceptByUuid("3fd45f27-cbc3-4f5c-8305-780b381a00d2")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("knownDiabetic",
		    getObservation(Context.getConceptService().getConceptByUuid("3cd0edbb-f4ba-4fb5-ac60-7354efb078c5")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("Sex", new GenderDataDefinition(), "");
		dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
		dsd.addColumn("Screening Date", new EncounterDatetimeDataDefinition(), "", new DateConverter(DATE_FORMAT));
		dsd.addColumn("treatmentDiabetic",
		    getObservation(Context.getConceptService().getConceptByUuid("482c9f37-e7b3-41ef-91ea-ecc868ce4016")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("rbs",
		    getObservation(Context.getConceptService().getConceptByUuid("887AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("fbs",
		    getObservation(Context.getConceptService().getConceptByUuid("160912AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("weight",
		    getObservation(Context.getConceptService().getConceptByUuid("5089AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("height",
		    getObservation(Context.getConceptService().getConceptByUuid("5090AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("bmi", new CalculationDataDefinition("bmi", new BMIAtLastVisitCalculation()), "",
		    new CalculationResultConverter());
		dsd.addColumn("hivstatus",
		    getObservation(Context.getConceptService().getConceptByUuid("1169AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("alcohol",
		    getObservation(Context.getConceptService().getConceptByUuid("159449AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("tobbaco",
		    getObservation(Context.getConceptService().getConceptByUuid("163731AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		
		dsd.addColumn("reffered",
		    getObservation(Context.getConceptService().getConceptByUuid("1788AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("facility",
		    getObservation(Context.getConceptService().getConceptByUuid("161550AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		dsd.addColumn("vnumber",
		    getObservation(Context.getConceptService().getConceptByUuid("68bb4b1c-dc92-44bd-966e-832edc1f6033")),
		    "onOrAfter=${startDate},onOrBefore=${endDate+23h+59m}", new ObsValueConverter());
		
		return dsd;
	}
	
	private EncounterQuery allScreenedPatientsCohort(int enc1) {
		SqlEncounterQuery cd = new SqlEncounterQuery();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setName("Active screened for DM and HTN Patients");
		cd.setQuery("SELECT e.encounter_id FROM encounter e WHERE e.encounter_datetime BETWEEN :startDate AND :endDate AND e.encounter_type IN("
		        + enc1 + ")");
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

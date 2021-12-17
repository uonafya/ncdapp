package org.openmrs.module.ncdapp.reports;

import org.openmrs.Concept;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAttributeType;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.report.HybridReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractHybridReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.calculation.library.hiv.CountyAddressCalculation;
import org.openmrs.module.kenyaemr.calculation.library.hiv.SubCountyAddressCalculation;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.MflCodeCalculation;
import org.openmrs.module.kenyaemr.calculation.library.mchcs.PersonAddressCalculation;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.reporting.data.converter.CalculationResultConverter;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.ncdapp.NcdappUtils;
import org.openmrs.module.ncdapp.calculation.DmHtnEncounterDateCalculation;
import org.openmrs.module.ncdapp.calculation.VillageCalculation;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.converter.ObsValueConverter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.AgeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ConvertedPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ObsForPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonAttributeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonIdDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.kenyaui.KenyaUiConstants.DATE_FORMAT;

@Component
@Builds({ "ncdapp.ncd.report.permanent.Register" })
public class SetupPermanentRegisterReport extends AbstractHybridReportBuilder {
	
	@Override
	protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor hybridReportDescriptor,
	        PatientDataSetDefinition patientDataSetDefinition) {
		return null;
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		Program program = MetadataUtils.existing(Program.class, "8b4f6a38-4f5e-11ec-a4c2-a75a2e13cdaa");
		report.setBaseCohortDefinition(ReportUtils.map(NcdappUtils.allDmHtnProgramPatientCohort(program.getProgramId())));
		
		return Arrays.asList(ReportUtils.map(permanentRegister(), ""));
	}
	
	private DataSetDefinition permanentRegister() {
		PatientDataSetDefinition dsd = new PatientDataSetDefinition();
		dsd.setName("permanent");
		PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class,
		    CommonMetadata._PatientIdentifierType.PATIENT_CLINIC_NUMBER);
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(
		        upn.getName(), upn), identifierFormatter);
		
		PersonAttributeType phoneNumber = MetadataUtils.existing(PersonAttributeType.class,
		    CommonMetadata._PersonAttributeType.TELEPHONE_CONTACT);
		
		DataConverter formatter = new ObjectFormatter("{familyName}, {givenName} {middleName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), formatter);
		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		dsd.addColumn("Name", nameDef, "");
		dsd.addColumn("Identifier", identifierDef, "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "", null);
		dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
		dsd.addColumn("Date", encounterDate(), "onDate=${endDate}", new CalculationResultConverter());
		dsd.addColumn("phoneNumber", new PersonAttributeDataDefinition(phoneNumber), "");
		dsd.addColumn("village", new CalculationDataDefinition("village", new VillageCalculation()), "",
		    new CalculationResultConverter());
		dsd.addColumn("SubCounty", new CalculationDataDefinition("SubCounty", new SubCountyAddressCalculation()), "",
		    new CalculationResultConverter());
		dsd.addColumn("County", new CalculationDataDefinition("County", new CountyAddressCalculation()), "",
		    new CalculationResultConverter());
		dsd.addColumn("mflcode", new CalculationDataDefinition("mflcode", new MflCodeCalculation()), "",
		    new CalculationResultConverter());
		dsd.addColumn("landmark", new CalculationDataDefinition("landmark", new PersonAddressCalculation()), "",
		    new CalculationResultConverter());
		dsd.addColumn("nameTs",
		    getObservation(Context.getConceptService().getConceptByUuid("160638AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")), "",
		    new ObsValueConverter());
		dsd.addColumn("telTs",
		    getObservation(Context.getConceptService().getConceptByUuid("160642AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")), "",
		    new ObsValueConverter());
		dsd.addColumn("diaYear",
		    getObservation(Context.getConceptService().getConceptByUuid("159948AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")), "",
		    new ObsValueConverter());
		dsd.addColumn("nhif",
		    getObservation(Context.getConceptService().getConceptByUuid("6796075b-c55a-4306-8ed5-b60c8f10936d")), "",
		    new ObsValueConverter());
		
		return dsd;
	}
	
	private DataDefinition encounterDate() {
		CalculationDataDefinition cd = new CalculationDataDefinition("Date", new DmHtnEncounterDateCalculation());
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
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

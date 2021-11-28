package org.openmrs.module.ncdapp.calculation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.PatientProgram;
import org.openmrs.Program;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyaemr.calculation.BaseEmrCalculation;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.InitialArtStartDateCalculation;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NcdDiscontinuationVelocityCalculation extends AbstractPatientCalculation {
	
	protected static final Log log = LogFactory.getLog(NcdDiscontinuationVelocityCalculation.class);
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	        PatientCalculationContext context) {
		
		Program ncdProgram = MetadataUtils.existing(Program.class, "8b4f6a38-4f5e-11ec-a4c2-a75a2e13cdaa");
		CalculationResultMap ret = new CalculationResultMap();
		CalculationResultMap artStartDates = calculate(new InitialArtStartDateCalculation(), cohort, context);
		StringBuilder sb = new StringBuilder();
		for (Integer ptId : cohort) {
			Date artStartDat = EmrCalculationUtils.datetimeResultForPatient(artStartDates, ptId);
			
			Long artStartDate = null;
			Long enrollmentDate = null;
			
			ProgramWorkflowService service = Context.getProgramWorkflowService();
			List<PatientProgram> programs = service.getPatientPrograms(Context.getPatientService().getPatient(ptId),
			    ncdProgram, null, null, null, null, true);
			if (programs.size() > 0) {
				enrollmentDate = programs.get(0).getDateEnrolled().getTime();
			}
			if (artStartDat != null) {
				artStartDate = artStartDat.getTime();
			}
			
			sb.append("artStartDate:").append(artStartDate).append(",");
			sb.append("enrollmentDate:").append(enrollmentDate);
			
			ret.put(ptId, new SimpleResult(sb.toString(), this, context));
		}
		return ret;
	}
}

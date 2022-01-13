package org.openmrs.module.ncdapp.calculation;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DmHtnDiagnosisCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map,
	        PatientCalculationContext context) {
		
		CalculationResultMap ret = new CalculationResultMap();
		
		CalculationResultMap diseaseType = Calculations.allObs(
		    MetadataUtils.existing(Concept.class, "74eb8e8d-d078-4fa3-8973-2d710d8f46df"), cohort, context);
		CalculationResultMap diabeticType = Calculations.allObs(
		    MetadataUtils.existing(Concept.class, "119481AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), cohort, context);
		for (Integer ptId : cohort) {
			List<String> value = new ArrayList<String>();
			ListResult diseaseTypeListResults = (ListResult) diseaseType.get(ptId);
			ListResult diabeticTypeListResults = (ListResult) diabeticType.get(ptId);
			
			List<Obs> diseaseTypeListResultsList = CalculationUtils.extractResultValues(diseaseTypeListResults);
			List<Obs> diabeticTypeListResultsList = CalculationUtils.extractResultValues(diabeticTypeListResults);
			
			List<Obs> combinedList = new ArrayList<Obs>();
			combinedList.addAll(diseaseTypeListResultsList);
			combinedList.addAll(diabeticTypeListResultsList);
			
			for (Obs obs : combinedList) {
				if (obs.getValueCoded().equals(Dictionary.getConcept("142474AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))) {
					value.add("a");
				} else if (obs.getValueCoded().equals(Dictionary.getConcept("142473AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))) {
					value.add("b");
				} else if (obs.getValueCoded().equals(Dictionary.getConcept("1449AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))) {
					value.add("c");
				} else if (obs.getValueCoded().equals(Dictionary.getConcept("8d64fd71-8b49-4ecb-8cb8-033597fef7c1"))) {
					value.add("d");
				} else if (obs.getValueCoded().equals(Dictionary.getConcept("e744c6e9-14c6-4cfd-92ce-19387ae87b12"))) {
					value.add("d");
				} else {
					value.add("f");
				}
			}
			ret.put(ptId, new SimpleResult(StringUtils.join(value, ","), this));
		}
		return ret;
	}
}

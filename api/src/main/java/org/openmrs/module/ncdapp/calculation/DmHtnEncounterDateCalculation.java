package org.openmrs.module.ncdapp.calculation;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class DmHtnEncounterDateCalculation extends AbstractPatientCalculation {
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map,
	        PatientCalculationContext context) {
		CalculationResultMap ret = new CalculationResultMap();
		
		CalculationResultMap lastFollowUpEncounter = Calculations.lastEncounter(
		    MetadataUtils.existing(EncounterType.class, "f1573d1c-18f8-11eb-a453-63d51e56f5cb"), cohort, context);
		CalculationResultMap lastInitialEncounter = Calculations.lastEncounter(
		    MetadataUtils.existing(EncounterType.class, "cb5f27f0-18f8-11eb-88d7-fb1a7178f8ea"), cohort, context);
		for (Integer ptId : cohort) {
			Date encounterDate = null;
			Encounter initialEncounter = EmrCalculationUtils.encounterResultForPatient(lastInitialEncounter, ptId);
			Encounter followUpEncounter = EmrCalculationUtils.encounterResultForPatient(lastFollowUpEncounter, ptId);
			
			if (followUpEncounter != null) {
				encounterDate = followUpEncounter.getEncounterDatetime();
			} else if (initialEncounter != null) {
				
				encounterDate = initialEncounter.getEncounterDatetime();
			}
			
			ret.put(ptId, new SimpleResult(encounterDate, this));
		}
		return ret;
	}
}

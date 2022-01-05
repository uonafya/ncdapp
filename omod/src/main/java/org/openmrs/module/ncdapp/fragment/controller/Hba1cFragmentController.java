package org.openmrs.module.ncdapp.fragment.controller;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.ncdapp.NcdappUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hba1cFragmentController {

    public void controller(FragmentModel model) {

        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());
        //get a collection of all patients
        KenyaEmrService kenyaEmrService = Context.getService(KenyaEmrService.class);

        List<Integer> cohort = new ArrayList<Integer>();

        //loop through all and get their patient ids
        if (NcdappUtils.getBasePatientsToWorkWith(kenyaEmrService.getDefaultLocation()).size() > 0) {
            for (Encounter encounter : NcdappUtils.getBasePatientsToWorkWith(kenyaEmrService.getDefaultLocation())) {
                cohort.add(encounter.getPatient().getPatientId());
            }
        }

        //exclude dead patients
        Set<Integer> alivePatients = Filters.alive(cohort, context);
        Set<Integer> male = NcdappUtils.male(alivePatients, context);
        Set<Integer> female = Filters.female(alivePatients, context);
        //declare concepts
        Concept hba1c = Dictionary.getConcept("159644AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        model.addAttribute("hba1cM", getHba1cDone(hba1c, male, context));
        model.addAttribute("hba1cF", getHba1cDone(hba1c, male, context));
        model.addAttribute("hba1cMu7", getHba1c(hba1c, male, context, 7.0));
        model.addAttribute("hba1cMu7", getHba1c(hba1c, female, context, 7.0));
    }

    private Integer getHba1c(Concept q1, Set<Integer> cohort, PatientCalculationContext context, double minValue) {
        Set<Integer> allSet = new HashSet<Integer>();
        CalculationResultMap hbac1Map = Calculations.lastObs(q1, cohort, context);
        for (Integer pId : cohort) {
            Double hbac1 = EmrCalculationUtils.numericObsResultForPatient(hbac1Map, pId);
            Obs obs = EmrCalculationUtils.obsResultForPatient(hbac1Map, pId);
            if (hbac1 != null && obs != null) {
                if (hbac1 < minValue) {
                    allSet.add(pId);
                }
            }

        }
        return allSet.size();
    }

    private Integer getHba1cDone(Concept q1, Set<Integer> cohort, PatientCalculationContext context) {
        Set<Integer> allSet = new HashSet<Integer>();
        CalculationResultMap hbac1Map = Calculations.lastObs(q1, cohort, context);
        for (Integer pId : cohort) {
            Double hbac1 = EmrCalculationUtils.numericObsResultForPatient(hbac1Map, pId);
            if (hbac1 != null) {
                    allSet.add(pId);
                }

        }
        return allSet.size();
    }
}

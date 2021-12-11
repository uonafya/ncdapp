package org.openmrs.module.ncdapp.page.controller;

import org.openmrs.Patient;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.ncdapp.NcdappConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

@AppPage(NcdappConstants.APP_NCD_APP)
public class NcdClinicalActionsPageController {
	
	public void controller(@RequestParam("patientId") Patient patient, UiUtils ui, PageModel model) {
		model.put("patient", patient);
	}
}

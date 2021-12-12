<%
    ui.decorateWith("kenyaui", "panel", [ heading: "Diabetes Hypertention Care" ])


    def dataPoints = []
    def otherDataPoints = []
    def currentMadication = []
    def complications = []
    def labs = []

    if (calculations.diseaseType) {
        dataPoints << [ label: "Type", value: ui.format(calculations.diseaseType.value.valueCoded), extra: calculations.diseaseType.value.obsDatetime ]
    } else {
        dataPoints << [ label: "Disease type", value: "None" ]
    }

    if (calculations.systollicBp) {
        dataPoints << [ label: "Systollic BP", value: ui.format(calculations.systollicBp.value.valueNumeric)+"mmHg", extra: calculations.systollicBp.value.obsDatetime ]
    } else {
        dataPoints << [ label: "Systollic BP", value: "None" ]
    }

    if (calculations.diastollicBp) {
        dataPoints << [ label: "Systollic BP", value: ui.format(calculations.diastollicBp.value.valueNumeric)+"mmHg", extra: calculations.diastollicBp.value.obsDatetime ]
    } else {
        dataPoints << [ label: "Systollic BP", value: "None" ]
    }
    if (calculations.pulse) {
        dataPoints << [ label: "Pulse Rate", value: ui.format(calculations.pulse.value.valueNumeric)+"rate/min", extra: calculations.pulse.value.obsDatetime ]
    } else {
        dataPoints << [ label: "Pulse Rate", value: "None" ]
    }
    if (calculations.rbs) {
        otherDataPoints << [ label: "Blood glucose", value: ui.format(calculations.rbs.value.valueNumeric)+"mg/ml", extra: calculations.rbs.value.obsDatetime ]
    } else {
        otherDataPoints << [ label: "Blood glucose", value: "None" ]
    }
    if (calculations.fbs) {
        otherDataPoints << [ label: "Fasting blood glucose", value: ui.format(calculations.fbs.value.valueNumeric)+"mg/dL", extra: calculations.fbs.value.obsDatetime ]
    } else {
        otherDataPoints << [ label: "Fasting blood glucose", value: "None" ]
    }
    if (calculations.hba1c) {
        otherDataPoints << [ label: "HbA1c", value: ui.format(calculations.hba1c.value.valueNumeric)+"%", extra: calculations.hba1c.value.obsDatetime ]
    } else {
        otherDataPoints << [ label: "HbA1c", value: "None" ]
    }

    if (calculations.bmi) {
        otherDataPoints << [ label: "BMI", value: ui.format(calculations.bmi.value)]
    } else {
        otherDataPoints << [ label: "BMI", value: "None" ]
    }
    if (calculations.drugs) {
        currentMadication << [ label: "Current medications", value: ui.format(calculations.drugs.value)]
    } else {
        currentMadication << [ label: "Current medications", value: "None" ]
    }
    if (calculations.complications) {
        complications << [ label: "Current Complications", value: ui.format(calculations.complications.value)]
    } else {
        complications << [ label: "Current Complications", value: "None" ]
    }
    if (calculations.labs) {
        labs << [ label: "Current Lab Orders", value: ui.format(calculations.labs.value)]
    } else {
        labs << [ label: "Current Lab Orders", value: "None" ]
    }

%>
<div class="ke-stack-item">
    <table cellspacing="0" cellpadding="5">
        <tr>
            <td>
                <% dataPoints.each { print ui.includeFragment("kenyaui", "widget/dataPoint", it) } %>
            </td>
            <td>
                <% otherDataPoints.each { print ui.includeFragment("kenyaui", "widget/dataPoint", it) } %>
            </td>
        </tr>
        <br />
        <tr id="medication">
            <td colspan="2">
                <% currentMadication.each { print ui.includeFragment("kenyaui", "widget/dataPoint", it) } %>
            </td>
        </tr>
        <br />
        <tr id="complications">
            <td colspan="2">
                <% complications.each { print ui.includeFragment("kenyaui", "widget/dataPoint", it) } %>
            </td>
        </tr>
        <br />
        <tr id="labs">
            <td colspan="2">
                <% labs.each { print ui.includeFragment("kenyaui", "widget/dataPoint", it) } %>
            </td>
        </tr>
    </table>

</div>


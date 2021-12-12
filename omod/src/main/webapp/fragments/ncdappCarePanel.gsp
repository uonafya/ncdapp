<%
    ui.decorateWith("kenyaui", "panel", [ heading: "Diabetes Hypertention Care" ])


    def dataPoints = []
    def otherDataPoints = []

    if (calculations.diseaseType) {
        dataPoints << [ label: "Disease type", value: ui.format(calculations.diseaseType.value.valueCoded), extra: calculations.diseaseType.value.obsDatetime ]
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

%>
<div class="ke-stack-item">
    <table>
        <tr>
            <td>
                <% dataPoints.each { print ui.includeFragment("kenyaui", "widget/dataPoint", it) } %>
            </td>
            <td>
                <% otherDataPoints.each { print ui.includeFragment("kenyaui", "widget/dataPoint", it) } %>
            </td>
        </tr>
    </table>

</div>


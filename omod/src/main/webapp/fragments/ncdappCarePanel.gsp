<%
    ui.decorateWith("kenyaui", "panel", [ heading: "NCD Care" ])


    def dataPoints = []

    if (calculations.diseaseType) {
        dataPoints << [ label: "Disease type", value: ui.format(calculations.diseaseType.value.valueCoded), extra: calculations.diseaseType.value.obsDatetime ]
    } else {
        dataPoints << [ label: "Disease type", value: "None" ]
    }

    if (calculations.pointOfEntry) {
        dataPoints << [ label: "Patient entry point", value: ui.format(calculations.pointOfEntry.value.valueCoded), extra: calculations.pointOfEntry.value.obsDatetime ]
    } else {
        dataPoints << [ label: "Patient entry point", value: "None" ]
    }

%>
<div class="ke-stack-item">
    <% dataPoints.each { print ui.includeFragment("kenyaui", "widget/dataPoint", it) } %>
</div>


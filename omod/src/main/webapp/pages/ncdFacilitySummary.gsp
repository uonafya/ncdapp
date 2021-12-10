<%
    ui.decorateWith("kenyaemr", "standardPage")
    def menuItems = [
            [ label: "Find or create patient", iconProvider: "kenyaui", icon: "buttons/patient_search.png", href: ui.pageLink("ncdapp", "ncdappHome") ]
    ]
%>
<div class="ke-page-sidebar">
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>
<div class="ke-page-content">
    ${ ui.includeFragment("ncdapp", "ncdSummary") }
</div>
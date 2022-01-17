<%
    ui.decorateWith("kenyaemr", "standardPage")
    def menuItems = [
            [ label: "Find or create patient", iconProvider: "kenyaui", icon: "buttons/patient_search.png", href: ui.pageLink("ncdapp", "ncdappHome") ]
    ]
%>
<style>
table.initial {
    border-collapse: collapse;
    background-color: #F3F9FF;
}
table.initial > tbody > tr > td, table.initial > tbody > tr > th {
    border: 1px solid black;
    vertical-align: baseline;
    padding: 3.6px;
    text-align: left;
}
tr:nth-child(even) {background-color: #f2f2;}
</style>
<div class="ke-page-sidebar">
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>
<div class="ke-page-content">
    ${ ui.includeFragment("ncdapp", "facilitySummary") }
</div>
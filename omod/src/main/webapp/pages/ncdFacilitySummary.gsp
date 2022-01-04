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
    <div class="ke-panel-frame">
        <div class="ke-panel-heading">Diabetes and Hypertension Facility Dashboard</div>
        <table>
            <tr>
                <td colspan="3" width="100%">
                    ${ ui.includeFragment("ncdapp", "ncdSummary") }
                </td>
            </tr>
            <tr>
                <td width="34%">
                    ${ ui.includeFragment("ncdapp", "dtnHtnVisitRevisit") }
                </td>
                <td width="33%">
                    ${ ui.includeFragment("ncdapp", "dtnHtnWaistCircumference") }
                </td>
                <td width="33%">
                    ${ ui.includeFragment("ncdapp", "tbSummary") }
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    ${ ui.includeFragment("ncdapp", "newlyDiagnosed") }
                </td>
                <td>
                    ${ ui.includeFragment("ncdapp", "diabeticfoot") }
                </td>
            </tr>
        </table>
        <table>
            <td width="50%">
                ${ ui.includeFragment("ncdapp", "complications") }
            </td>
            <td width="50%">
                ${ ui.includeFragment("ncdapp", "treatment") }
            </td>
        </table>


    </div>
</div>
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
        <table width="80%" cellpadding="5" border="0" cellspacing="0">
            <tr>
                <td colspan="3" width="100%" valign="top">
                    ${ ui.includeFragment("ncdapp", "ncdSummary") }
                </td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td width="34%" valign="top">
                    ${ ui.includeFragment("ncdapp", "dtnHtnVisitRevisit") }
                </td>
                <td width="33%" valign="top">
                    ${ ui.includeFragment("ncdapp", "dtnHtnWaistCircumference") }
                </td>
                <td width="33%" valign="top">
                    ${ ui.includeFragment("ncdapp", "tbSummary") }
                </td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td width="34%" valign="top">
                    ${ ui.includeFragment("ncdapp", "newlyDiagnosed") }
                </td>
                <td width="33%" valign="top">
                    ${ ui.includeFragment("ncdapp", "diabeticfoot") }
                </td>
                <td width="33%" valign="top">
                    ${ ui.includeFragment("ncdapp", "hba1c") }
                </td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2" valign="top">
                    ${ ui.includeFragment("ncdapp", "complications") }
                </td>
                <td valign="top">
                    ${ ui.includeFragment("ncdapp", "treatment") }
                </td>
            </tr>
        </table>
</div>
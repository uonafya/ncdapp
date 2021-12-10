<%
    ui.decorateWith("kenyaemr", "standardPage")

    ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
    ui.includeCss("ehrconfigs", "onepcssgrid.css")
    ui.includeCss("ehrconfigs", "referenceapplication.css")

    ui.includeCss("ehrinventoryapp", "main.css")
    ui.includeCss("ehrconfigs", "custom.css")

    ui.includeJavascript("kenyaui", "pagebus/simple/pagebus.js")
    ui.includeJavascript("kenyaui", "kenyaui-tabs.js")
    ui.includeJavascript("kenyaui", "kenyaui-legacy.js")
    ui.includeJavascript("ehrconfigs", "moment.js")
    ui.includeJavascript("ehrconfigs", "jquery.dataTables.min.js")
    ui.includeJavascript("ehrconfigs", "jq.browser.select.js")

    ui.includeJavascript("ehrconfigs", "knockout-2.2.1.js")
    ui.includeJavascript("ehrconfigs", "emr.js")
    ui.includeJavascript("ehrconfigs", "jquery.simplemodal.1.4.4.min.js")

    def menuItems = [
            [ label: "Patient Summary", iconProvider: "kenyaui", icon: "buttons/back.png", label: "Patient Summary", href: ui.pageLink("ncdapp", "ncdappSummary") ]
    ]
%>

<div class="ke-page-sidebar">
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>
<div class="ke-page-content">
    ${ ui.includeFragment("ncdapp", "patientOrders") }
</div>
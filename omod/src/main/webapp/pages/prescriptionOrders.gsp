<%
    ui.decorateWith("appui", "standardEmrPage", [title: "Patient Treatment"])

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

%>
<script>
    var drugOrder = [];

    jq(function () {
        jq(".drug-name").on("focus.autocomplete", function () {
            var selectedInput = this;
            jq(this).autocomplete({
                source: function (request, response) {
                    jq.getJSON('${ ui.actionLink("ncdapp", "Orders", "getDrugs") }', {
                        q: request.term
                    }).success(function (data) {
                        var results = [];
                        for (var i in data) {
                            var result = {label: data[i].name, value: data[i].id};
                            results.push(result);
                        }
                        response(results);
                    });
                },
                minLength: 3,
                select: function (event, ui) {
                    event.preventDefault();
                    jq(selectedInput).val(ui.item.label);
                },
                change: function (event, ui) {
                    event.preventDefault();
                    jq(selectedInput).val(ui.item.label);

                    jq.getJSON('${ ui.actionLink("ncdapp", "Orders", "getFormulationByDrugName") }', {
                        "drugName": ui.item.label
                    }).success(function (data) {
                        var formulations = jq.map(data, function (formulation) {
                            jq('#formulationsSelect').append(jq('<option>').text(formulation.name + ":" + formulation.dozage).attr('value', formulation.id));
                        });
                    });

                    jq.getJSON('${ui.actionLink("ncdapp","Orders","getDrugUnit")}').success(function (data) {
                        var drugUnit = jq.map(data, function (drugUnit) {
                            jq('#drugUnitSelect').append(jq('<option>').text(drugUnit.label).attr('value', drugUnit.id));
                        });
                    });
                },
                open: function () {
                },
                close: function () {
                }
            });
        });

        var adddrugdialog = emr.setupConfirmationDialog({
            selector: '#addDrugDialog',
            actions: {
                confirm: function () {
                    if (jq('#drugName').val() === '') {
                        jq().toastmessage('showErrorToast', "Kindly ensure that the drug name has been Entered Correctly");
                        return false;
                    }
                    if (jq('#drugUnitSelect').val() === 'Select unit') {
                        jq().toastmessage('showErrorToast', "Kindly ensure that the dosage unit has been selected")
                    }

                    if (jq('#formulationsSelect').val() === 'Select Formulation') {
                        jq().toastmessage('showErrorToast', "Kindly ensure that the drug Formulation has been selected Correctly");
                        return false;
                    }

                    if (jq('#drugFrequency').val() === 'Select Frequency') {
                        jq().toastmessage('showErrorToast', "Kindly ensure that the drug Frequency has been selected Correctly");
                        return false;
                    }

                    if (!jq.isNumeric(jq('#drugDays').val())) {
                        jq().toastmessage('showErrorToast', "Kindly ensure that the number of Days has been filled correctly");
                        return false;
                    }

                    var tbody = jq('#drugsTable').children('tbody');
                    var table = tbody.length ? tbody : jq('#drugsTable');
                    table.append('<tr><td>' + jq("#drugName").val() + '</td><td>' + jq("#formulationsSelect option:selected").text() + '</td><td>' + jq("#drugFrequency option:selected").text() + '</td><td>' + jq("#drugDays").val() + '</td><td>' + jq("#drugComment").val() + '</td></tr>');
                    drugOrder.push(
                        {
                            name: jq("#drugName").val(),
                            formulation: jq("#formulationsSelect").val(),
                            frequency: jq("#drugFrequency").val(),
                            days: jq("#drugDays").val(),
                            comment: jq("#drugComment").val(),
                            dosage: jq("#drugDosage").val(),
                            drugUnit: jq("#drugUnitSelect").val()
                        }
                    );
                    jq('#prescription-set').val('SET');
                    adddrugdialog.close();
                },
                cancel: function () {
                    adddrugdialog.close();
                }
            }
        });

        jq("#addDrugsButton").on("click", function (e) {
            adddrugdialog.show();
        });
    });


</script>

<style>
.dialog textarea {
    resize: none;
}

.dialog li label span {
    color: #f00;
    float: right;
    margin-right: 10px;
}

.icon-remove {
    cursor: pointer !important;
}

.diagnosis-carrier-div {
    border-width: 1px 1px 1px 10px;
    border-style: solid;
    border-color: #404040;
    padding: 0px 10px 3px;
}

#diagnosis-carrier input[type="radio"] {
    -webkit-appearance: checkbox;
    -moz-appearance: checkbox;
    -ms-appearance: checkbox;
}

#prescriptionAlert {
    text-align: center;
    border: 1px #f00 solid;
    color: #f00;
    padding: 5px 0;
}

.alert {
    position: relative;
    padding: .75rem 1.25rem;
    margin-bottom: 1rem;
    border: 1px solid transparent;
    border-top-color: transparent;
    border-right-color: transparent;
    border-bottom-color: transparent;
    border-left-color: transparent;
    border-top-color: transparent;
    border-right-color: transparent;
    border-bottom-color: transparent;
    border-left-color: transparent;
    border-radius: .25rem;
    color: #721c24;
    background-color: #f8d7da;
    border-color: #f5c6cb;
}
</style>

<div class="content">
    <fieldset>
        <legend>Prescription</legend>
        <label class="label title-label" style="width: auto; padding-left: 5px;">
            PRESCRIBE DRUGS
            <span class="important"></span>
        </label>

        <p style="display: none">
            <input type="hidden" style="width: 450px" id="prescription-set" name="prescription-set"/>
        </p>

        <table id="drugsTable">
            <thead>
            <th style="width: auto;">Drug Name</th>
            <th>Formulation</th>
            <th>Frequency</th>
            <th>Number of Days</th>
            <th>Comment</th>
            </thead>
            <tbody>
            </tbody>
        </table>
        <input type="button" value="Add" class="button confirm" name="addDrugsButton" id="addDrugsButton">
    </fieldset>
</div>

<div id="addDrugDialog" class="dialog" style="display: none">
    <div class="dialog-header">
        <i class="icon-folder-open"></i>

        <h3>Prescription</h3>
    </div>

    <div class="dialog-content">
        <ul>
            <li>
                <span>Drug</span>
                <input class="drug-name" id="drugName" type="text">
            </li>
            <li>
                <label style="display: block; margin-top: 9px">Dosage<span>*</span></label>
                <input class="drug-dosage" id="drugDosage" type="text" style="width: 60px!important;">
                <select id="drugUnitSelect" class="drug-dosage-unit" style="width: 191px!important;">
                    <option>Select unit</option>
                </select>
            </li>
            <li>
                <span style="display: block; margin-top: 9px">Formulation</span>
                <select style="width: 100%;" id="formulationsSelect">
                    <option>Select Formulation</option>
                </select>
            </li>
            <li>
                <span style="display: block; margin-top: 9px">Frequency</span>

            </li>
            <li>
                <span style="display: block; margin-top: 9px">Number of Days</span>
                <input style="width: 100%;" id="drugDays" type="text">
            </li>
            <li>
                <span style="display: block; margin-top: 9px">Comment</span>
                <textarea style="width: 100%;" id="drugComment"></textarea>
            </li>
        </ul>

        <span class="button confirm right" style="margin-right: 1px">Confirm</span>
        <span class="button cancel">Cancel</span>
    </div>
</div>
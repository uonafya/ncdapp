<script>
    var drugOrder = [];
    var index = 0;
    var count = 0;

    var getJSON = function (dataToParse) {
        if (typeof dataToParse === "string") {
            return JSON.parse(dataToParse);
        }
        return dataToParse;
    }

    jq(function () {
        jq("#addDrugsButton").on("click", function (e) {
            e.preventDefault()
            adddrugdialog.show();
            resets();
            jq('#drugName').val('');
            jq('#formulationsSelect').val('');
            jq('#drugFrequency').val('');
            jq('#drugDays').val('');
            jq('#drugComment').val('');
            jq('#drugDosage').val('');
            jq('#drugUnitSelect').val('');
            jq('#drugName').change();
        });

            jq("#procedure").autocomplete({
                source: function (request, response) {
                    jq.getJSON('${ ui.actionLink("ncdapp", "Orders", "getProcedures") }', {
                        q: request.term
                    }).success(function (data) {
                     var results = [];
                        for (var i in data) {
                            var result = {label: data[i].label,
                                value: data[i].id};
                            results.push(result);
                        }
                        response(results);
                    });

                },
                minLength: 3,
                select: function (event, ui) {
                    var selectedProcedure = document.createElement('option');
                    selectedProcedure.value = ui.item.value;
                    selectedProcedure.text = ui.item.label;
                    selectedProcedure.id = ui.item.value;
                    var selectedProcedureList = document.getElementById("selectedProcedureList");

                    //adds the selected procedures to the div
                    var selectedProcedureP = document.createElement("div");
                    selectedProcedureP.className = "selectp";

                    var selectedProcedureT = document.createTextNode(ui.item.label);
                    selectedProcedureP.id = ui.item.value;
                    selectedProcedureP.appendChild(selectedProcedureT);

                    var btnselectedRemoveIcon = document.createElement("span");
                    btnselectedRemoveIcon.className = "icon-remove selecticon";
                    btnselectedRemoveIcon.id = "procedureRemoveIcon";

                    selectedProcedureP.appendChild(btnselectedRemoveIcon);

                    var selectedProcedureDiv = document.getElementById("selected-procedures");

                    //check if the item already exist before appending
                    var exists = false;
                    for (var i = 0; i < selectedProcedureList.length; i++) {
                        if (selectedProcedureList.options[i].value == ui.item.value) {
                            exists = true;
                        }
                    }

                    if (exists == false) {
                        selectedProcedureList.appendChild(selectedProcedure);
                        selectedProcedureDiv.appendChild(selectedProcedureP);
                    }

                    jq('#task-procedure').show();
                    jq('#procedure-set').val('SET');
                },
                open: function () {
                },
                close: function () {
                    jq(this).val('');
                }
            });
            jq("#selected-procedures").on("click", "#procedureRemoveIcon", function () {
                var procedureP = jq(this).parent("div");
                var procedureId = procedureP.attr("id");

                jq('#selectedProcedureList').find("#" + procedureId).remove();
                procedureP.remove();

                if (jq('#selectedProcedureList option').size() == 0) {
                    jq('#task-procedure').hide();
                    jq('#procedure-set').val('');
                }
            });


            jq("#investigation").autocomplete({
                source: function (request, response) {
                    jq.getJSON('${ ui.actionLink("ncdapp", "Orders", "getInvestigations") }',
                        {
                            q: request.term
                        }
                    ).success(function (data) {
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
                    var selectedInvestigation = document.createElement('option');
                    selectedInvestigation.value = ui.item.value;
                    selectedInvestigation.text = ui.item.label;
                    selectedInvestigation.id = ui.item.value;
                    var selectedInvestigationList = document.getElementById("selectedInvestigationList");

                    //adds the selected procedures to the div
                    var selectedInvestigationP = document.createElement("div");
                    selectedInvestigationP.className = "selectp";

                    var selectedInvestigationT = document.createTextNode(ui.item.label);
                    selectedInvestigationP.id = ui.item.value;
                    selectedInvestigationP.appendChild(selectedInvestigationT);

                    var btnselectedRemoveIcon = document.createElement("span");
                    btnselectedRemoveIcon.className = "icon-remove selecticon";
                    btnselectedRemoveIcon.id = "investigationRemoveIcon";

                    selectedInvestigationP.appendChild(btnselectedRemoveIcon);

                    var selectedInvestigationDiv = document.getElementById("selected-investigations");

                    //check if the item already exist before appending
                    var exists = false;
                    for (var i = 0; i < selectedInvestigationList.length; i++) {
                        if (selectedInvestigationList.options[i].value == ui.item.value) {
                            exists = true;
                        }
                    }

                    if (exists == false) {
                        selectedInvestigationList.appendChild(selectedInvestigation);
                        selectedInvestigationDiv.appendChild(selectedInvestigationP);
                    }

                    jq('#task-investigation').show();
                    jq('#investigation-set').val('SET');
                },
                open: function () {
                    jq(this).removeClass("ui-corner-all").addClass("ui-corner-top");
                },
                close: function () {
                    jq(this).removeClass("ui-corner-top").addClass("ui-corner-all");
                    jq(this).val('');
                }
            });

            jq("#selected-investigations").on("click", "#investigationRemoveIcon", function () {
                var investigationP = jq(this).parent("div");
                var investigationId = investigationP.attr("id");

                jq('#selectedInvestigationList').find("#" + investigationId).remove();
                investigationP.remove();

                if (jq('#selectedInvestigationList option').size() === 0) {
                    jq('#task-investigation').hide();
                    jq('#investigation-set').val('');
                }
            });

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

                        jq.getJSON('${ui.actionLink("patientdashboardapp","clinicalNotes","getDrugUnit")}').success(function (data) {
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

                        index = drugOrder.length
                        count = index + 1;

                        table.append('<tr id="' + index + '"><td>' + count + '</td><td>' + jq("#drugName").val() + '</td><td>' + jq("#drugDosage").val() + jq("#drugUnitSelect option:selected").text() + '</td><td>' + jq("#formulationsSelect option:selected").text() + '</td><td>' + jq("#drugFrequency option:selected").text() + '</td><td>' + jq("#drugDays").val() + '</td><td>' + jq("#drugComment").val() + '</td> <td><a onclick="removerFunction(' + index + ')" class="remover"><i class="icon-remove small" style="color:red"></i></td></tr>');
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

        function resets(){
            jq('form')[1].reset();
            jq('#drug-name').change();
        }



        jq("#treatmentSubmit").click(function (event) {
            jq().toastmessage({
                sticky: true
            });
            var savingMessage = jq().toastmessage('showSuccessToast', 'Please wait as Information is being Saved...');

            var selectedProc = new Array;
            jq("selectedProcedureList option").each(function () {
                selectedProc.push(jq(this).val());
            });

            var selectedInv = new Array;
            jq("#selectedInvestigationList option").each(function () {
                selectedInv.push(jq(this).val());
            });

            drugOrder = JSON.stringify(drugOrder);

            var treatmentFormData = {
                'patientId': ${patient.patientId},
                'selectedProcedureList': selectedProc,
                'selectedInvestigationList': selectedInv,
                'drugOrder':drugOrder
            };

            function successFn(successly_) {
                jq().toastmessage('removeToast', savingMessage);
                jq().toastmessage('showSuccessToast', "Patient Treatment has been updated Successfully");
                var summaryLink = ui.pageLink("ncdapp","ncdappSummary");
                window.location = summaryLink.substring(0, summaryLink.length - 1);
            }

            jq("#treatmentForm").submit(
                jq.ajax({
                    type: 'POST',
                    url: '${ ui.actionLink("ncdapp", "Orders", "orders") }',
                    data: treatmentFormData,
                    success: successFn,
                    dataType: "json"
                })
            );
        });

    });//end of doc ready

    function removerFunction(rowId){
        if (confirm("Are you sure about this?")){
            jq('#drugsTable > tbody > tr').remove();
            var tbody = jq('#drugsTable').children('tbody');
            var table = tbody.length ? tbody : jq('#drugsTable');
            drugOrder = jq.grep(drugOrder, function (item, index){
                return (rowId !== index);
            });

            jq.each(drugOrder, function (rowId){
                tbody.append('<tr id = "' + (rowId + 1) + '"><td>' + (rowId + 1) + ' </td><td>' + jq("#drugName").val() + '</td><td>' + jq("#drugDosage").val() + jq("#drugUnitSelect option:selected").text() + '</td><td>' + jq("#formulationsSelect option:selected").text() + '</td><td>' + jq("#drugFrequency option:selected").text() + '</td><td>' + jq("#drugDays").val() + '</td><td>' + jq("#drugComment").val() + '</td> <td><a onclick="removerFunction(' + index + ')" class="remover"><i class="icon-remove small" style="color:red"></i></td></tr>');
            });
        }else {
            return false;
        }
    }
</script>

<style>
.toast-item {
    background-color: #222;
}

#breadcrumbs a, #breadcrumbs a:link, #breadcrumbs a:visited {
    text-decoration: none;
}

.name {
    color: #f26522;
}

.new-patient-header .demographics .gender-age {
    font-size: 14px;
    margin-left: -55px;
    margin-top: 12px;
}

.new-patient-header .demographics .gender-age span {
    border-bottom: 1px none #ddd;
}

.new-patient-header .identifiers {
    margin-top: 5px;
}

.tag {
    padding: 2px 10px;
}

.tad {
    background: #666 none repeat scroll 0 0;
    border-radius: 1px;
    color: white;
    display: inline;
    font-size: 0.8em;
    padding: 2px 10px;
}

.status-container {
    padding: 5px 10px 5px 5px;
}

.catg {
    color: #363463;
    margin: 25px 10px 0 0;
}

form input:focus, form select:focus, form textarea:focus, form ul.select:focus, .form input:focus, .form select:focus, .form textarea:focus, .form ul.select:focus,
.simple-form-ui section fieldset select:focus, .simple-form-ui section fieldset input:focus, .simple-form-ui section #confirmationQuestion select:focus, .simple-form-ui section #confirmationQuestion input:focus,
.simple-form-ui #confirmation fieldset select:focus, .simple-form-ui #confirmation fieldset input:focus, .simple-form-ui #confirmation #confirmationQuestion select:focus,
.simple-form-ui #confirmation #confirmationQuestion input:focus, .simple-form-ui form section fieldset select:focus, .simple-form-ui form section fieldset input:focus,
.simple-form-ui form section #confirmationQuestion select:focus, .simple-form-ui form section #confirmationQuestion input:focus, .simple-form-ui form #confirmation fieldset select:focus,
.simple-form-ui form #confirmation fieldset input:focus, .simple-form-ui form #confirmation #confirmationQuestion select:focus, .simple-form-ui form #confirmation #confirmationQuestion input:focus {
    outline: 0px none #007fff;
    box-shadow: 0 0 0 0 #888;
}

#formBreadcrumb {
    background: #fff;
}

#ordersForm {
    background: #f9f9f9 none repeat scroll 0 0;
    margin-top: 3px;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
}

#charges-info {
    display: flex;
    flex-direction: column;
    max-width: 1024px;
    width: 100%;
}

#confirmation {
    min-height: 250px;
    width: 100%;
    max-width: 1024px;
}

.tasks {
    background: white none repeat scroll 0 0;
    border: 1px solid #cdd3d7;
    border-radius: 4px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
    color: #404040;
    font: 14px/20px "Lucida Grande", Verdana, sans-serif;
    margin: 10px 0 0 4px;
    width: 98.6%;
}

.tasks-header {
    background: #f0f1f2 linear-gradient(to bottom, #f5f7fd, #e6eaec) repeat scroll 0 0;
    border-bottom: 1px solid #d1d1d1;
    border-radius: 3px 3px 0 0;
    box-shadow: 0 1px rgba(255, 255, 255, 0.5) inset, 0 1px rgba(0, 0, 0, 0.03);
    color: #f26522;
    line-height: 24px;
    padding: 7px 15px;
    position: relative;
    text-shadow: 0 1px rgba(255, 255, 255, 0.7);
}

.tasks-title {
    color: inherit;
    font-size: 14px;
    font-weight: bold;
    line-height: inherit;
}

.tasks-lists {
    color: transparent;
    font: 0px/0 serif;
    height: 3px;
    margin-top: -11px;
    padding: 10px 4px;
    position: absolute;
    right: 10px;
    text-shadow: none;
    top: 50%;
    width: 19px;
}

.tasks-lists::before {
    background: #8c959d none repeat scroll 0 0;
    border-radius: 1px;
    box-shadow: 0 6px #8c959d, 0 -6px #8c959d;
    content: "";
    display: block;
    height: 3px;
}

.tasks-list {
    font: 13px/20px "Lucida Grande", Verdana, sans-serif;
}

.tasks-list-item {
    -moz-user-select: none;
    border-bottom: 1px solid #aaa;
    cursor: pointer;
    display: inline-block;
    line-height: 24px;
    margin-right: 20px;
    padding: 5px;
    width: 150px;
}

.tasks-list-cb {
    display: none;
}

.tasks-list-mark {
    border: 2px solid #c4cbd2;
    border-radius: 12px;
    display: inline-block;
    height: 20px;
    margin-right: 0;
    position: relative;
    vertical-align: top;
    width: 20px;
}

.tasks-list-mark::before {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    border-color: #39ca74;
    border-image: none;
    border-style: solid;
    border-width: 0 0 4px 4px;
    content: "";
    display: none;
    height: 4px;
    left: 50%;
    margin: -5px 0 0 -6px;
    position: absolute;
    top: 50%;
    transform: rotate(-45deg);
    width: 8px;
}

.tasks-list-cb:checked ~ .tasks-list-mark {
    border-color: #39ca74;
}

.tasks-list-cb:checked ~ .tasks-list-mark::before {
    display: block;
}

.tasks-list-desc {
    color: #555;
    font-weight: bold;
}

.tasks-list-cb:checked ~ .tasks-list-desc {
    color: #34bf6e;
}

.tasks-list input[type="radio"] {
    left: -9999px !important;
    position: absolute !important;
    top: -9999px !important;
}

.selectp {
    border-bottom: 1px solid darkgrey;
    margin: 7px 10px;
    padding-bottom: 3px;
    padding-left: 5px;
}

#investigationRemoveIcon,
#procedureRemoveIcon {
    float: right;
    color: #f00;
    cursor: pointer;
    margin: 2px 5px 0 0;
}

fieldset input[type="text"],
fieldset select {
    height: 45px
}

.title-label {
    color: #f26522;
    cursor: pointer;
    font-family: "OpenSansBold", Arial, sans-serif;
    font-size: 1.3em;
    padding-left: 5px;
}

.dialog-content ul li span {
    display: inline-block;
    width: 130px;
}

.dialog-content ul li input {
    width: 255px;
    padding: 5px 10px;
}

.dialog textarea {
    width: 255px;
}

.dialog select {
    display: inline-block;
    width: 255px;
}

.dialog select option {
    font-size: 1em;
}

.dialog .dialog-content li {
    margin-bottom: 3px;
}

.dialog select {
    margin: 0;
    padding: 5px;
}

#modal-overlay {
    background: #000 none repeat scroll 0 0;
    opacity: 0.4 !important;
}

#summaryTable tr:nth-child(2n), #summaryTable tr:nth-child(2n+1) {
    background: rgba(0, 0, 0, 0) none repeat scroll 0 0;
}

#summaryTable {
    margin: -5px 0 -6px;
}

#summaryTable tr, #summaryTable th, #summaryTable td {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    border-color: #eee;
    border-image: none;
    border-style: none none solid;
    border-width: 1px;
}

#summaryTable td:first-child {
    vertical-align: top;
    width: 180px;
}

.icon-remove {
    cursor: pointer !important;
}
</style>

<form class="zsimple-form-ui" id="ordersForm" method="post">
    <section>
        <fieldset>
            <legend>Procedure</legend>
            <input type="text" style="width:98.6%; margin-left:5px;" id="procedure" name="procedure"
                   placeholder="Enter Procedure"/>

            <p style="display: none">
                <input type="hidden" id="procedure-set" name="procedure-set"/>
            </p>

            <div class="tasks" id="task-procedure" style="display: none;">
                <header class="tasks-header">
                    <span id="title-symptom" class="tasks-title">PROCEDURES APPLIED</span>
                    <a class="tasks-lists"></a>
                </header>

                <div class="symptoms-qualifiers" data-bind="foreach: signs">
                    <select style="display: none" id="selectedProcedureList"></select>

                    <div class="symptom-container selectdiv" id="selected-procedures">

                    </div>
                </div>
            </div>
        </fieldset>
        <fieldset>
            <legend>Investigations</legend>
            <input type="text" style="width:98.6%; margin-left:5px;" id="investigation" name="investigation"
                   placeholder="Enter Investigations"/>

            <p style="display: none">
                <input type="hidden" id="investigation-set" name="investigation-set"/>
            </p>

            <div class="tasks" id="task-investigation" style="display: none;">
                <header class="tasks-header">
                    <span id="title-symptom" class="tasks-title">INVESTIGATIONS</span>
                    <a class="tasks-lists"></a>
                </header>

                <div class="symptoms-qualifiers" data-bind="foreach: signs">
                    <select style="display: none" id="selectedInvestigationList"></select>

                    <div class="symptom-container selectdiv" id="selected-investigations">

                    </div>
                </div>
            </div>
        </fieldset>

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
                <th>#</th>
                <th style="width: auto;">Drug Name</th>
                <th>Dosage</th>
                <th>Formulation</th>
                <th>Frequency</th>
                <th>Number of Days</th>
                <th>Comment</th>
                <th></th>
                </thead>
                <tbody>
                </tbody>
            </table>
            <input type="button" value="Add" class="button confirm" name="addDrugsButton" id="addDrugsButton">
        </fieldset>
    </section>

    <div id="confirmation" style="min-height: 250px;">
        <span id="confirmation_label" class="title">Confirmation</span>

        <div id="confirmationQuestion" class="focused" style="margin-top:0px">
            <p style="display: none">
                <button class="button submit confirm" style="display: none;"></button>
            </p>
            <span>
            <a value="Submit" type="submit" class="button confirm" id="treatmentSubmit" style="margin: 5px 10px;" href = ''>
                <i class="icon-save small"></i>
                Save
            </a>

            <a id="cancelButton" class="button cancel" style="margin: 5px">
                <i class="icon-remove small"></i>
                Cancel
            </a>
            </span>
        </div>
    </div>
</form>


<div id="addDrugDialog" class="dialog" style="display: none">
    <form>
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
                <select style="width: 100%;" id="drugFrequency">
                    <option>Select Frequency</option>
                    <% if (drugFrequencyList != null && drugFrequencyList != "") { %>
                    <% drugFrequencyList.each { dfl -> %>
                    <option value="${dfl.conceptId}">
                        ${dfl.name}
                    </option>
                    <% } %>
                    <% } %>
                </select>
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
    </form>
</div>

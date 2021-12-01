<%
    ui.decorateWith("appui", "standardEmrPage", [title: "Pharma"])
    ui.includeCss("ehrconfigs", "referenceapplication.css")

    ui.includeCss("uicommons", "datetimepicker.css")
    ui.includeCss("ehrconfigs", "onepcssgrid.css")
    ui.includeCss("ehrconfigs", "custom.css")


    ui.includeJavascript("ehrconfigs", "emr.js")
    ui.includeJavascript("ncdapp", "scripts.js")
%>

<style>
.dialog textarea{
    resize: none;
}

.dialog li label span {
    color: #f00;
    float: right;
    margin-right: 10px;
}
.icon-remove{
    cursor: pointer!important;
}
.diagnosis-carrier-div{
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
    border:     1px #f00 solid;
    color:      #f00;
    padding:    5px 0;
}
.alert{
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
<div class = "content">
<fieldset class="no-confirmation">
    <legend>Prescription</legend>
    <div>
        <div style="display:none">
            <p><input type="text"></p>
        </div>
        <h2>Prescribe Medicine</h2>
        <table id="addDrugsTable">
            <thead>
            <tr>
                <th>Drug Name</th>
                <th>Dosage</th>
                <th>Formulation</th>
                <th>Frequency</th>
                <th>Days</th>
                <th>Comments</th>
                <th></th>
            </tr>
            </thead>
            <tbody data-bind="foreach: drugs">
            <tr>
                <td data-bind="text: drugName"></td>
                <td data-bind="text: dosageAndUnit" ></td>
                <td data-bind="text: formulation().label"></td>
                <td data-bind="text: frequency().label"></td>
                <td data-bind="text: numberOfDays"></td>
                <td data-bind="text: comment"></td>
                <td>
                    <a href="#" title="Remove">
                        <i data-bind="click: \$root.removePrescription" class="icon-remove small" style="color: red" ></i>
                    </a>
                    <!-- <a href="#"><i class="icon-edit small"></i></a> -->
                </td>
            </tr>
            </tbody>
        </table>
        <br/>
        <button id="add-prescription">Add</button>
    </div>
    <p>
        <input type="hidden" id="drug-set" />
    </p>
</fieldset>
</div>

<div id="prescription-dialog" class="dialog" style="display:none;">
    <div class="dialog-header">
        <i class="icon-folder-open"></i>

        <h3>Prescription</h3>
    </div>

    <div class="dialog-content">
        <ul>
            <li id="prescriptionAlert">
                <div>No batches found in Pharmacy for the Selected Drug/Formulation combination</div>
            </li>

            <li>
                <label>Drug<span>*</span></label>
                <input class="drug-name" id="drugSearch" type="text" data-bind="value: prescription.drug().drugName, valueUpdate: 'blur'">
            </li>
            <li>
                <label>Dosage<span>*</span></label>
                <input type="text" class="drug-dosage" data-bind="value: prescription.drug().dosage" style="width: 60px!important;">
                <select id="dosage-unit" class="drug-dosage-unit" data-bind="options: prescription.drug().drugUnitsOptions, value: prescription.drug().drugUnit, optionsText: 'label',  optionsCaption: 'Select Unit'" style="width: 191px!important;"></select>
            </li>

            <li>
                <label>Formulation<span>*</span></label>
                <select id="drugFormulation" class="drug-formulation" data-bind="options: prescription.drug().formulationOpts, value: prescription.drug().formulation, optionsText: 'label',  optionsCaption: 'Select Formulation'"></select>
            </li>
            <li>
                <label>Frequency<span>*</span></label>
                <select class="drug-frequency" data-bind="options: prescription.drug().frequencyOpts, value: prescription.drug().frequency, optionsText: 'label',  optionsCaption: 'Select Frequency'"></select>
            </li>

            <li>
                <label>No. 0f Days<span>*</span></label>
                <input type="text" class="drug-number-of-days" data-bind="value: prescription.drug().numberOfDays">
            </li>
            <li>
                <label>Comment</label>
                <textarea class="drug-comment" data-bind="value: prescription.drug().comment"></textarea>
            </li>
        </ul>

        <label class="button confirm right">Confirm</label>
        <label class="button cancel">Cancel</label>
    </div>
</div>
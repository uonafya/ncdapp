var patientsInSystemTable;
var patientsInSystemResultData = [];
var recentPatientIds = [];
var dPatientsInSystemTable;

var getPatientsFromSystem = function() {
    jq.ajax({
        type: "POST",
        url: emr.fragmentActionLink('registration','revisitPatientRegistrationForm','searchPatient'),
        dataType: "json",
        data: ({
            gender: jq("#gender").val(),
            phrase: jq("#patient-search").val(),
            currentPage: 1,
            pageSize: 1000,
            age: jQuery("#age").val(),
            ageRange: jq("#ageRange").val(),
            patientMaritalStatus: jq("#patientMaritalStatus").val(),
            lastVisit: jq("#lastVisit").val(),
            phoneNumber: jq("#phoneNumber").val(),
            relativeName: jq("#relativeName").val(),
            nationalId: jq("#nationalId").val(),
            fileNumber: jq("#fileNumber").val(),
            lastDayOfVisit: jq('#lastDayOfVisit-field').val() && moment(jq('#lastDayOfVisit-field').val()).format('DD/MM/YYYY')
        }),
        success: function (data) {
            updateSearchResultsFromSystem(data);
            console.log(data);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log("error!");
            updateSearchResultsFromSystem([]);
        }
    });
}

var updateSearchResultsFromSystem = function (data) {
    patientsInSystemResultData = data || [];
    var dataRows = [];
    _.each(patientsInSystemResultData, function(result){
        if (result.dead) {
            return;
        }
        var patient_name = result.names.replace(/\snull|[\[\]]/gi, "");
        var last_visit = result.formartedVisitDate;

        var visit 	= moment(last_visit,'DD/MM/YYYY HH:mm:ss');
        var today 	= moment();
        var hours 	= Math.round(moment.duration(today - visit).asHours());

        console.log(hours);

        if (hours <= 24){
            recentPatientIds.push(result.wrapperIdentifier);
            patient_name += ' <span class="recent-lozenge">Within 24hrs</span>'
        }

        dataRows.push([result.wrapperIdentifier, patient_name, result.age, result.gender, visit.fromNow()]);
    });

    dPatientsInSystemTable.api().clear();

    if(dataRows.length > 0) {
        dPatientsInSystemTable.fnAddData(dataRows);
    }

    refreshInSystemTable();
}

var refreshInSystemTable = function(){
    var rowCount = patientsInSystemResultData.length;
    if(rowCount == 0){
        patientsInSystemTable.find('td.dataTables_empty').html("No patients found.");
    }
    dPatientsInSystemTable.fnPageChange(0);
}

var isInSystemTableEmpty = function(){
    if(patientsInSystemResultData.length > 0){
        return false
    }
    return !dPatientsInSystemTable || dPatientsInSystemTable.fnGetNodes().length == 0;
};

jq(function(){
    var $inputs = jq(':input', '#patient-search-form,.advanced-search').not("#search-in-db");
    console.log($inputs);
    var $selectInputs = jq('select');
    jq('#search-in-db').on("change", function (){
        if ( this.checked ) {
            $inputs.on("keyup change", function (e) {
                var key = e.keyCode || e.which;
                if (jq(this).is("select") || (key == 9 || key == 13)) {
                    getPatientsFromSystem();
                }
            });
        } else {
            $inputs.off();
        }
    });

    patientsInSystemTable = jq('#patients-in-system');

    dPatientsInSystemTable = patientsInSystemTable.dataTable({
        bFilter: true,
        bJQueryUI: true,
        bLengthChange: false,
        iDisplayLength: 15,
        sPaginationType: "full_numbers",
        bSort: false,
        sDom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg"ip>',
        oLanguage: {
            "sInfo": "Patients in System",
            "sInfoEmpty": " ",
            "sZeroRecords": "No patients found.",
            "oPaginate": {
                "sFirst": "First",
                "sPrevious": "Previous",
                "sNext": "Next",
                "sLast": "Last"
            }
        },

        fnDrawCallback : function(oSettings){
            if(isInSystemTableEmpty()){
                //this should ensure that nothing happens when the use clicks the
                //row that contain the text that says 'No data available in table'
                return;
            }

            if(highlightedKeyboardRowIndex != undefined && !isHighlightedRowOnVisiblePage()){
                unHighlightRow(dTable.fnGetNodes(highlightedKeyboardRowIndex));
            }

            //fnDrawCallback is called on each page redraw so we need to remove any previous handlers
            //otherwise there will multiple hence the logic getting executed multiples times as the
            //user the goes back and forth between pages
            patientsInSystemTable.find('tbody tr').unbind('click');
            patientsInSystemTable.find('tbody tr').unbind('hover');
            patientsInSystemTable.find('tbody tr').click(
                function(){
                    if (jq("#queue-choice").val() == 0) {
                        jq().toastmessage({sticky : true});
                        jq().toastmessage('showWarningToast', "Please select queue to send patient to");
                        jq("#queue-choice").focus();
                        return;
                    }
                    var selectedPatientId = dPatientsInSystemTable.api().row(this).data()[0];
                    var selectedPatient = _.find(patientsInSystemResultData, function(data) {
                        return data.wrapperIdentifier == selectedPatientId;
                    });

                    var opdId = jq('#queue-choice').val();
                    if(opdId == null){
                        opdId = 5704;
                    }

                    jq.getJSON(emr.fragmentActionLink("patientqueueapp", "patientQueue", "addPatientToQueue"),
                        {
                            'patientId': selectedPatient.patientId,
                            'opdId': opdId
                        })
                        .success(function(data) {
                            jq('#search-in-db').attr("checked", false);
                            jq("#patient-search-clear-button").click();
                            var opdId = jq('#queue-choice').val();
                            if(opdId == null){
                                window.location = emr.pageLink("mchapp", "main", { "patientId" : selectedPatient.patientId, "queueId" : data.queueId })
                            }else{
                                window.location = emr.pageLink("patientdashboardapp", "main", { "patientId" : selectedPatient.patientId, "opdId": opdId, "queueId" : data.queueId })
                            }
                        })
                        .fail(function(xhr, status, err) {
                            jq().toastmessage('showWarningToast', "An error occured while saving patient to queue please try again.");
                        });
                }
            );
        },

        fnRowCallback : function (nRow, aData, index){
            if (patientsInSystemResultData[index].wrapperIdentifier && jq.inArray(patientsInSystemResultData[index].wrapperIdentifier, recentPatientIds)>-1){
                nRow.className += " from-lab";
                return nRow;
            }
        }
    });
});
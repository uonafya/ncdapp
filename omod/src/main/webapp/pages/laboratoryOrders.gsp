<%
    ui.decorateWith("appui", "standardEmrPage", [title: "Lab"])
    ui.includeCss("ehrconfigs", "referenceapplication.css")

    ui.includeCss("uicommons", "datetimepicker.css")
    ui.includeCss("ehrconfigs", "onepcssgrid.css")
    ui.includeCss("ehrconfigs", "custom.css")


    ui.includeJavascript("uicommons", "datetimepicker/bootstrap-datetimepicker.min.js")
    ui.includeJavascript("uicommons", "handlebars/handlebars.min.js")
    ui.includeJavascript("uicommons", "navigator/validators.js")
    ui.includeJavascript("uicommons", "navigator/navigator.js")
    ui.includeJavascript("uicommons", "navigator/navigatorHandlers.js")
    ui.includeJavascript("uicommons", "navigator/navigatorModels.js")
    ui.includeJavascript("uicommons", "navigator/navigatorTemplates.js")
    ui.includeJavascript("uicommons", "navigator/exitHandlers.js")
%>

<script>
   var jq = jQuery

   jq(function (){
       jq("#investigation").autocomplete({
           source: function( request, response ) {
               jq.getJSON('${ ui.actionLink("ncdapp", "LaboratoryOrders", "getInvestigations") }',
                   {
                       q: request.term
                   }
               ).success(function(data) {
                   var results = [];
                   for (var i in data) {
                       var result = { label: data[i].name, value: data[i].id};
                       results.push(result);
                   }
                   response(results);
               });
           },
           minLength: 3,
           select: function( event, ui ) {
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
                   if(selectedInvestigationList.options[i].value==ui.item.value)
                   {
                       exists = true;
                   }
               }

               if(exists == false)
               {
                   selectedInvestigationList.appendChild(selectedInvestigation);
                   selectedInvestigationDiv.appendChild(selectedInvestigationP);
               }

               jq('#task-investigation').show();
               jq('#investigation-set').val('SET');
           },
           open: function() {
               jq( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
           },
           close: function() {
               jq( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
               jq(this).val('');
           }
       });

       jq("#selected-investigations").on("click", "#investigationRemoveIcon", function(){
           var investigationP = jq(this).parent("div");
           var investigationId = investigationP.attr("id");

           jq('#selectedInvestigationList').find("#" + investigationId).remove();
           investigationP.remove();

           if (jq('#selectedInvestigationList option').size() === 0){
               jq('#task-investigation').hide();
               jq('#investigation-set').val('');
           }
       });

   });//doc ready

</script>
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
.selectp{
    border-bottom: 1px solid darkgrey;
    margin: 7px 10px;
    padding-bottom: 3px;
    padding-left: 5px;
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

<fieldset>
    <legend>Investigation</legend>
    <input type="text" style="width:98.6%; margin-left:5px;" id="investigation" name="investigation" placeholder="Enter Investigations" />

    <p style="display: none">
        <input type="hidden" id="investigation-set" name="investigation-set" />
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
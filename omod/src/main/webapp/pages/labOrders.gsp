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
   var  pData;

   jq(function (){
       pData = getInvestigations();

       jq("#investigation").autocomplete({
           minLength: 3,
           source: function (request, response) {
               jq.getJSON('${ ui.actionLink("ncdapp", "laboratoryOrders", "getInvestigations") }',
                   {
                       name: request.term
                   }
               ).success(function (data) {

                   var results = [];
                   for (var i in data) {
                       var result = {label: data[i].name, value: data[i]};
                       results.push(result);
                   }
                   response(results);
               });
           },
           select: function(event, ui) {
               event.preventDefault();
               jq(this).val(ui.item.label);
               jq("#investigation-set").val("Investigation set");
               (new Investigation({
                   id: ui.item.value,
                   label: ui.item.label
               }));
               jq('#investigation').val('');
               jq('#task-investigation').show();
           }

       });
   });//doc ready

   function getInvestigations() {
       var toReturn;
       jQuery.ajax({
           type: "GET",
           url: "${ui.actionLink('ncdapp','laboratoryOrders','getInvestigations')}",
           dataType: "json",
           data: ({
               name: "ray"
           }),
           global: false,
           async: false,
           success: function (data) {
               toReturn = data;
           }
       });
       return toReturn;
   }

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


<fieldset class="no-confirmation">
    <legend>Investigations</legend>
    <div>
        <div class="input-position-class">
            <label class="label" for="investigation">Investigation:</label>
            <input type="text" id="investigation" name="investigation" />
        </div>

        <div id="task-investigation" class="tasks" style="display:none;">
            <header class="tasks-header">
                <span id="title-investigation" class="tasks-title">INVESTIGATION</span>
                <a class="tasks-lists"></a>
            </header>

            <div data-bind="foreach: investigations">
                <div class="investigation-container">
                    <span class="right pointer" data-bind="click: \$root.removeInvestigation"><i class="icon-remove small"></i></span>
                    <p data-bind="text: label"></p>
                </div>
            </div>
        </div>
        <div style="display:none">
            <p><input type="text" ></p>
        </div>
        <p>
            <input type="hidden" id="investigation-set" />
        </p>
    </div>
</fieldset>
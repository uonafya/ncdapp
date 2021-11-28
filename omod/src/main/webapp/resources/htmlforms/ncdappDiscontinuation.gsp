<!--
  ~ The contents of this file are subject to the OpenMRS Public License
  ~ Version 1.0 (the "License"); you may not use this file except in
  ~ compliance with the License. You may obtain a copy of the License at
  ~ http://license.openmrs.org
  ~
  ~ Software distributed under the License is distributed on an "AS IS"
  ~ basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
  ~ License for the specific language governing rights and limitations
  ~ under the License.
  ~
  ~ Copyright (C) OpenMRS, LLC.  All Rights Reserved.
-->
<htmlform>

    <script type="text/javascript">

        var Discontinuation_VELOCITY = "<lookup expression="ncdapp.NcdDiscontinuationVelocityCalculation()" />";
        var treatmentStartDate = Discontinuation_VELOCITY.split(",")[0].split(":")[1];
        var enrollmentDate = Discontinuation_VELOCITY.split(",")[1].split(":")[1];

        jq(document).ready(function(){
            if ((getValue('idReason.value')) == 160034) {
                jq('#pdied :input').prop('disabled', false);
                jq('#ptransferred :input').prop('disabled', true);
                jq('#ptrfVerification :input').prop('disabled', true);

            }
            else if((getValue('idReason.value')) == 159492) {
                jq('#pdied :input').prop('disabled', true);
                jq('#ptransferred :input').prop('disabled', false);
                jq('#ptrfVerification :input').prop('disabled', false);
            }
            else {
                jq('#pdied :input').prop('disabled', true);
                jq('#ptransferred :input').prop('disabled', true);
                jq('#ptrfVerification :input').prop('disabled', true);
            }

            jq("#idReason select").change(function() {
                if ((getValue('idReason.value')) == 160034) {
                    jq('#pdied :input').prop('disabled', false);
                    jq('#ptransferred :input').prop('disabled', true);
                    jq('#ptrfVerification :input').prop('disabled', true);
                    clearHiddenSections(jq('#ptransferred'));
                    clearHiddenSections(jq('#ptrfVerification'));
                }
                else if((getValue('idReason.value')) == 159492) {
                    jq('#pdied :input').prop('disabled', true);
                    jq('#ptransferred :input').prop('disabled', false);
                    jq('#ptrfVerification :input').prop('disabled', false);
                    clearHiddenSections(jq('#pdied'));
                }
                else {
                    jq('#pdied :input').prop('disabled', true);
                    jq('#ptransferred :input').prop('disabled', true);
                    jq('#ptrfVerification :input').prop('disabled', true);
                    clearHiddenSections(jq('#ptransferred'));
                    clearHiddenSections(jq('#ptrfVerification'));
                    clearHiddenSections(jq('#pdied'));
                }

            });
            jq("#tr-verified-obs").change(function(){
                if (getValue('tr-verified-obs.value') == 1066 || getValue('tr-verified-obs.value') == "") {
                    jq('#trf-verification-date :input').val('');
                    jq('#trf-verification-date :input').prop('disabled',true);
                }
                else
                {
                    jq('#trf-verification-date :input').prop('disabled',false);
                }
            });


            var pbirthdate = new Date("<lookup expression="patient.birthdate" />").getTime();
            var discontinueDate;
            var encounterDate;
            beforeSubmit.push(function() {
                encounterDate = new Date(getValue('encounter-date.value')).getTime();
                discontinueDate = new Date(getValue('discontinue-date.value')).getTime();
                var trfVerified = getValue('tr-verified-obs.value');
                /* var trfVerificationDate = new Date(getValue('trf-verification-date.value')).getTime();*/
                var trfVerificationDate = getValue('trf-verification-date.value');
                if(discontinueDate &lt; pbirthdate){
                    getField('discontinue-date.error').html('Discontinue date, should not be earlier than Birth date').show();
                    return false;
                }
                if(discontinueDate &lt; treatmentStartDate){
                    getField('discontinue-date.error').html('Discontinue date, should not be earlier than art start date').show();
                    return false;
                }
                if(discontinueDate &lt; enrollmentDate){
                    getField('discontinue-date.error').html('Discontinue date, should not be earlier than Enrollment date').show();
                    return false;
                }else {
                    getField('discontinue-date.error').html('Discontinue date, should not be earlier than Enrollment').hide();
                }
                if(encounterDate &lt; pbirthdate){
                    getField('encounter-date.error').html('Encounter date, should not be earlier than Birth date').show();
                    return false;
                }
                if(encounterDate &lt; treatmentStartDate){
                    getField('encounter-date.error').html('Encounter date, should not be earlier than art start date').show();
                    return false;
                }
                if(encounterDate &lt; enrollmentDate){
                    getField('encounter-date.error').html('Encounter date, should not be earlier than Enrollment date').show();
                    return false;
                }else {
                    getField('encounter-date.error').html('Encounter date, should not be earlier than Enrollment').hide();
                }
                if (trfVerified == 1065 &amp;&amp; trfVerificationDate == "") {
                    getField('trf-verification-date.error').html('Please enter date transfer was verified').show();
                    return false;
                }else{
                    getField('trf-verification-date.error').html('Please enter date transfer was verified').hide();
                }
                return true;
            });
        });
        clearHiddenSections = function(parentObj) {
            parentObj.find('input[type=radio]').each(function() {
                this.checked = false;
            });
            parentObj.find('input[type=checkbox]').each(function() {
                this.checked = false;
            });
            parentObj.find('input[type=text]').each(function() {
                jq(this).val("");
            });
            parentObj.find('select').each(function() {
                this.selectedIndex =0;
            });
        }
    </script>

    <div class="ke-form-content">
        <fieldset>
            <legend>Discontinue NCD Care</legend>
            <table >
                <tr>
                    <td>Encounter Date:</td>
                    <td><encounterDate id="encounter-date" showTime="true" /></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Effective discontinuation date</td>
                    <td><obs id= "discontinue-date" conceptId="164384AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" allowFutureDates="true" required="true" /></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Reason:</td>
                    <td><obs conceptId="161555AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" answerConceptIds="159492AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA,160034AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA,5240AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA,819AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA,5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA,1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" answerLabels="Transferred Out,Died,Lost to Follow,Cannot afford Treatment,Other,Unknown" id="idReason" /></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
            <div id="pdied">
                <table>
                    <tr>
                        <td>If Patient Died, please provide Date of Death if Known:</td>
                        <td><obs conceptId="1543AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" /></td>
                        <td></td>
                        <td></td>
                    </tr>
                </table>
            </div>

            <div id="ptransferred">
                <table>
                    <tr>
                        <td><i>(If transferred out)</i> <br/>Transfer to Facility:</td>
                        <td ><br/><obs conceptId="159495AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" labelText=" "/></td>
                        <td>Date Transferred Out<br /></td>
                        <td><br/><obs conceptId="160649AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" labelText=" " /></td>
                    </tr>
                </table>
            </div>
            <div id="ptrfVerification">
                <table>
                    <tr>
                        <td><br/>Transfer out verified?</td>
                        <td ><br/><obs conceptId="1285AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                                       answerConceptIds="1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA,1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                                       answerLabel="Yes,No"
                                       style="radio" id="tr-verified-obs"/> &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;</td>
                        <td>Date verified</td>
                        <td><br/><obs conceptId="164133AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" id="trf-verification-date" /></td>
                    </tr>
                </table>
            </div>
            <br/>
            <hr/>
            <br/>

            Signed  at <encounterLocation default="GlobalProperty:kenyaemr.defaultLocation" type="autocomplete"/>

            <br/>

        </fieldset>

        <completeProgram programId="8b4f6a38-4f5e-11ec-a4c2-a75a2e13cdaa" />
    </div>

    <div class="ke-form-footer">
        <submit />
    </div>

</htmlform>
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
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Diabetes and Hypertension Facility Dashboard</div>
    <div class="ke-panel-content">
        <br />
        <table border="0">
            <tr>
                <td>
                    <div class="ke-panel-heading">Disease Case Load</div>>
                    <table cellpadding="5" cellspacing="0" class="initial">
                        <tbody>
                        <tr>
                            <th></th>
                            <th colspan="2">Diabetes</th>
                            <th colspan="2">Hypertension</th>
                        </tr>
                        <tr>
                            <th>Age Group</th>
                            <th>Male</th>
                            <th>Female</th>
                            <th>Male</th>
                            <th>Female</th>
                        </tr>
                        <tr>
                            <td>0 to 5</td>
                            <td>${diabeticMaleZeroTo5}</td>
                            <td>${diabeticFemaleZeroTo5}</td>
                            <td>${hypertensionMaleZeroTo5}</td>
                            <td>${hypertensionFemaleZeroTo5}</td>
                        </tr>
                        <tr>
                            <td>6 to 18</td>
                            <td>${diabeticMale6To18}</td>
                            <td>${diabeticFemale6To18}</td>
                            <td>${hypertensionMale6To18}</td>
                            <td>${hypertensionFemale6To18}</td>
                        </tr>
                        <tr>
                            <td>19 to 35</td>
                            <td>${diabeticMale19To35}</td>
                            <td>${diabeticFemale19To35}</td>
                            <td>${hypertensionMale19To35}</td>
                            <td>${hypertensionFemale19To35}</td>
                        </tr>
                        <tr>
                            <td>36 to 60</td>
                            <td>${diabeticMale36To60}</td>
                            <td>${diabeticFemale36To60}</td>
                            <td>${hypertensionMale36To60}</td>
                            <td>${hypertensionFemale36To60}</td>
                        </tr>
                        <tr>
                            <td>&gt;60</td>
                            <td>${diabeticMale60To120}</td>
                            <td>${diabeticFemale60To120}</td>
                            <td>${hypertensionMale60To120}</td>
                            <td>${hypertensionFemale60To120}</td>
                        </tr>
                        <tr>
                            <td>Totals</td>
                            <td>${diabeticMaleTotals}</td>
                            <td>${diabeticFemaleTotals}</td>
                            <td>${hypertensionMaleTotals}</td>
                            <td>${hypertensionFemaleTotals}</td>
                        </tr>
                        </tbody>
                    </table>
                </td>
                <td>

                </td>
            </tr>
        </table>
    </div>
</div>
<%
    ui.includeCss("kenyaemrorderentry", "font-awesome.css")
    ui.includeCss("kenyaemrorderentry", "font-awesome.min.css")
    ui.includeCss("kenyaemrorderentry", "font-awesome.css.map")
    ui.includeCss("kenyaemrorderentry", "fontawesome-webfont.svg")
%>
<style>
.action-container {
    display: inline;
    float: left;
    width: 99.9%;
    margin: 0 1.04167%;
}
.action-section {
    margin-top: 2px;
    background: white;
    border: 1px solid #dddddd;
}
.float-left {
    float: left;
    clear: left;
    width: 97.91666%;
    color: white;
}

.action-section a:link {
    color: white;!important;
}

.action-section a:hover {
    color: white;
}

.action-section a:visited {
    color: white;
}
.action-section h3 {
    margin: 0;
    color: white;
    border-bottom: 1px solid white;
    margin-bottom: 5px;
    font-size: 1.5em;
    margin-top: 5px;
}
.action-section ul {
    background: #7f7b72;
    color: white;
    padding: 5px;
}

.action-section li {
    font-size: 1.1em;
}
.action-section i {
    font-size: 1.1em;
    margin-left: 8px;
}
</style>
<div class="action-container">
	<div class="action-section">


		<ul class="float-left">
			<h3>Doctors Actions</h3>
<li class="float-left" style="margin-top: 7px">
    <a href="${ ui.pageLink("ncdapp", "prescriptionOrders")}" class="float-left">
        <i class="fa fa-medkit fa-2x"></i>
        Prescribe Drugs
    </a>
</li>

<li class="float-left" style="margin-top: 7px">
    <a href="${ ui.pageLink("ncdapp", "laboratoryOrdersPage")}" class="float-left">
        <i class="fa fa-flask fa-2x"></i>
        Investigations Orders
    </a>
</li>

</ul>



</div>

</div>
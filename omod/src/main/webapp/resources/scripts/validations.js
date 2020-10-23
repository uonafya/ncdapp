/*$(document).ready(function () {

    $('#patientRegistrationForm').validate({
        rules: {
        	"patient\\.surName": {
                required: true
            },
            "patient\\.firstName": {
                required: true
            },
            "patient\\.birthdate": {
                required: true
            },
            
            "patient\\.gender": {
                required: true
            }
        },
        submitHandler: function (form) { // for demo
            alert('valid form');
           // form.submit();
            return false;
        }
    });

    // programmatically check any element using the `.valid()` method.
    $('#demographics').on('click', function () {
        $('input[name="patient.surName"]').valid();
        $('input[name="patient.firstName"]').valid();
        $('input[name="patient.birthdate"]').valid();
        $('input[name="patient.gender"]').valid();
    });

    // programmatically check the entire form using the `.valid()` method.

    $('#event2').on('click', function () {
        $('#patientRegistrationForm').valid();
    });

});*/
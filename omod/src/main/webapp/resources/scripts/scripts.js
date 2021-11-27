
var prescription = {};
    var prescriptionDialog = emr.setupConfirmationDialog({
    dialogOpts: {
    overlayClose: false,
    close: true
},
    selector: '#prescription-dialog',
    actions: {
    confirm: function() {
    if (!page_verified()){
    jq().toastmessage('showErrorToast', 'Ensure fields marked in red have been properly filled before you continue')
    return false;
}

    note.addPrescription(prescription.drug());
    prescription.drug(new Drug());
    prescriptionDialog.close();
},
    cancel: function() {
    prescription.drug(new Drug());
    prescriptionDialog.close();
}
}
});

    jq("#add-prescription").on("click", function(e) {
    e.preventDefault();

    jq('#prescriptionAlert').hide();
    prescriptionDialog.show();
});

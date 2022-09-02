function showModalDialog(title, message) {
    $("#modalTitle").html(title);
    $("#modalBody").html(message);
    $("#modalDialog").modal();
}

function showErrorModal(message) {
    showModalDialog("Error", message)
}

function showWarningModal(message) {
    showModalDialog("Warning", message)
}
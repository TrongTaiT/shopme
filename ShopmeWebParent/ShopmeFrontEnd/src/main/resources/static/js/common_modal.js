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

function showDeleteConfirmModal(link, entityName) {
    entityId = link.attr('entityId');

    $("#yesButton").attr('href', link.attr('href'));
    $("#confirmText").text('Are you sure you want to delete ' + entityName + ' with id ' + entityId + '?');

    $("#confirmModal").modal();
}
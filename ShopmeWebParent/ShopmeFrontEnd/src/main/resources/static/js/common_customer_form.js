var dropDownCountry;
var dataListState;
var fieldState;

$(document).ready(function () {
    dropDownCountry = $("#country");
    dataListState = $("#listStates");
    fieldState = $("#state");

    dropDownCountry.on("change", function () {
        loadStatesForCountry();
        fieldState.val("").focus();
    });
});

function loadStatesForCountry() {
    selectedCountry = $("#country option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "settings/list_states_by_country/" + countryId;

    $.get(url, function (responseJSON) {
        dataListState.empty();

        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.name).text(state.name).appendTo(dataListState);
        });
    });
}

function checkPasswordMatch(confirmPassword) {
    if (confirmPassword.value != $("#password").val()) {
        confirmPassword.setCustomValidity("Passwords do not match!");
    }
    else {
        confirmPassword.setCustomValidity("");
    }
}

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
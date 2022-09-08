let dropDownCountries;
let dropDownStates;

$(document).ready(function () {
    dropDownCountries = $("#country");
    dropDownStates = $("#listStates");

    dropDownCountries.on("change", function () {
        loadStatesForCountry();
        $("#state").val("").focus();
    });

    loadStatesForCountry();
});

function loadStatesForCountry() {
    selectedCountry = $("#country option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "states/list_by_country/" + countryId;

    $.get(url, function (responseJSON) {
        dropDownStates.empty();

        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.name).text(state.name).appendTo(dropDownStates);
        });
    }).fail(function () {
        showErrorModal("Error loading states/provinces for the selected country.");
    });
}
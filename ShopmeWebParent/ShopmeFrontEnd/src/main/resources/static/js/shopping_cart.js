$(document).ready(function () {
    $(".linkMinus").on("click", function (e) {
        e.preventDefault();
        decreaseQuantity($(this));
    });

    $(".linkPlus").on("click", function (e) {
        e.preventDefault();
        increaseQuantity($(this));
    });
});

function decreaseQuantity(link) {
    let productId = link.attr("pid");
    let quantityInput = $("#quantity" + productId);
    let newQuantity = parseInt(quantityInput.val()) - 1;

    if (newQuantity > 0) {
        quantityInput.val(newQuantity);
        updateQuantiy(productId, newQuantity);
    } else {
        showWarningModal('Minimun quantity is 1');
    }
}

function increaseQuantity(link) {
    let productId = link.attr("pid");
    let quantityInput = $("#quantity" + productId);
    let newQuantity = parseInt(quantityInput.val()) + 1;

    if (newQuantity <= 5) {
        quantityInput.val(newQuantity);
        updateQuantiy(productId, newQuantity);
    } else {
        showWarningModal('Maximum quantity is 5');
    }
}

function updateQuantiy(productId, quantity) {
    url = contextPath + "cart/update/" + productId + "/" + quantity;

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (updatedSubtotal) {
        updateSubtotal(updatedSubtotal, productId);
    }).fail(function () {
        showErrorModal("Error while updating product quantity.");
    });
}

function updateSubtotal(updatedSubtotal, productId) {
    formatedSubtotal = $.number(updatedSubtotal, 2);
    $("#subtotal" + productId).text(formatedSubtotal);
    updateTotal();
}

function updateTotal() {
    total = 0.0;

    $(".subtotal").each(function(index, element) {
        total += parseFloat(element.innerHTML.replaceAll(",", ""));
    });

    formatedTotal = $.number(total, 2);
    $("#total").text(formatedTotal);
}
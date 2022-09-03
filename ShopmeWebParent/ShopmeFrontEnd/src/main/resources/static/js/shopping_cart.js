decimalSeparator = decimalPointType == 'COMMA' ? ',' : '.';
thousandSeparator = thousandPointType == 'COMMA' ? ',' : '.';

$(document).ready(function () {
    $(".linkMinus").on("click", function (e) {
        e.preventDefault();
        decreaseQuantity($(this));
    });

    $(".linkPlus").on("click", function (e) {
        e.preventDefault();
        increaseQuantity($(this));
    });

    $(".linkRemove").on("click", function (e) {
        e.preventDefault();
        removeProduct($(this));
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
    formatedSubtotal = formatCurrency(updatedSubtotal);
    $("#subtotal" + productId).text(formatedSubtotal);
    updateTotal();
}

function updateTotal() {
    total = 0.0;
    productCount = 0;

    $(".subtotal").each(function (index, element) {
        productCount++;
        total += parseFloat(clearCurrencyFormat(element.innerHTML));
    });

    if (productCount < 1) {
        showEmptyShoppingCart();
    } else {
        formatedTotal = formatCurrency(total);
        $("#total").text(formatedTotal);
    }
}

function showEmptyShoppingCart() {
    $("#sectionTotal").hide();
    $("#sectionEmptyCartMessage").removeClass("d-none");
}

function removeProduct(link) {
    url = link.attr("href");

    $.ajax({
        type: "DELETE",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (response) {
        rowNumber = link.attr("rowNumber");
        removeProductHTML(rowNumber);
        updateTotal();
        updateCountNumber();

        showModalDialog("Shopping Cart", response);
    }).fail(function () {
        showErrorModal("Error while removing product.");
    });
}

function removeProductHTML(rowNumber) {
    $("#row" + rowNumber).remove();
}

function updateCountNumber() {
    $(".divCount").each(function (index, element) {
        element.innerHTML = "" + (index + 1);
    });
}

function formatCurrency(amount) {
    return $.number(amount, decimalDigits, decimalSeparator, thousandSeparator);
}

function clearCurrencyFormat(numberString) {
    result = numberString.replaceAll(thousandSeparator, "");
    return result.replaceAll(decimalSeparator, ".");
}
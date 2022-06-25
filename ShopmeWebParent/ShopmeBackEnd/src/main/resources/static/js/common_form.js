$(document).ready(function () {
    $("#buttonCancel").on("click", function () {
        window.location = moduelURL;
    });

    $("#fileImage").change(function () {
        if (checkFileSize(this)) {
            showImageThumbnail(this);
        }
    });

});

function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    }
    reader.readAsDataURL(file);
}

function checkFileSize(fileInput) {
    fileSize = fileInput.files[0].size;

    if (fileSize > MAX_FILE_SIZE * 1024) {
        fileInput.setCustomValidity("You must choose an image less than " + MAX_FILE_SIZE + "KB!");
        fileInput.reportValidity();
        return false;
    }
    else {
        fileInput.setCustomValidity("");
        return true;
    }
}
$(document).ready(function () {

    $("#buttonCancel").on("click",function () {
        window.location = moduleUrl;
    });
    $("#fileImage").change(function () {
        fileSize = this.files[0].size;
        //alert("File size : " + fileSize);
        if (fileSize > 1048576) {
            this.setCustomValidity("You must choose an image with less than 1 MB !");
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });
});

function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}

function showErrorDialog(message) {
    showModalDialog("Error",message);
}

function showWarningDialog(message) {
    showModalDialog("Warning",message);
}

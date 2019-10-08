$(document).ready(function () {
    $("#createform").submit(function (event) {
        event.preventDefault();
        create_submit();
    });
});

function create_submit() {
    var formData = {
            title : $("#title").val(),
            authors :  $("#authors").val(),
            genre :  $("#genre").val(),
          }
    if (formData.title == "" || formData.authors == "") {
        on_error ("Title and Authors must be filled");
    } else {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/create",
            data: JSON.stringify(formData),
            dataType: 'text',
            success: function () {
                location.replace("list.html");
            },
            error: function (e) {
                on_error (e.responseText);
            }
        });
    }
}


function on_error (e) {
    var json = "<h4>ERROR</h4><pre>"
                    + e + "</pre>";
                $('#feedback').html(json);
}
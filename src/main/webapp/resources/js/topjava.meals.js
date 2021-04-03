const mealsAjaxUrl = "meals";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl
};

$(function() {
    makeEditableMeal(
        $("#datatableMeals").DataTable({
            "paging" : false,
            "info" : true,
            "columns" : [
                {
                    "data" : "date and time"
                },
                {
                    "data" : "description"
                },
                {
                    "data" : "calories"
                },
                {
                    "defaultContent" : "Update",
                    "orderable" : false
                },
                {
                    "defaultContent" : "Delete",
                    "orderable" : false
                }
            ],
            "order" : [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function makeEditableMeal(datatableApi) {
    ctx.datatableApi = datatableApi;

    form = $('#detailsForm');
    $(".delete").click(function () {
        if (confirm('Are you sure?')) {
            deleteMealRow($(this).closest('tr').attr("id"));
        }
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function deleteMealRow(id) {
    $.ajax({
        url: ctx.ajaxUrl + "/delete?id=" + id,
        type: "GET"
    }).done(function () {
        updateTable();
        successNoty("Deleted");
    });
}

function addMeal() {
    form.find(":input").val("");
    $("#editMealRow").modal();
}

function saveMeal() {
    const form = $("#detailsForm");
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editMealRow").modal("hide");
        updateTable();
        successNoty("Saved");
    });
}
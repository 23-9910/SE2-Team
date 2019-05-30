$(document).ready(function() {

    var canSeeDate = 0;

    getCanSeeDayNum();
    getCinemaHalls();

    function getCinemaHalls() {
        var halls = [];
        getRequest(
            '/hall/all',
            function (res) {
                halls = res.content;
                renderHall(halls);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    /**
     * By yyf on 2019/05/30
     * 在每个影厅后添加了修改按键
     * @param halls
     */
    function renderHall(halls){
        $('#hall-card').empty();
        var hallDomStr = "";
        halls.forEach(function (hall) {
            var seat = "";
            for(var i =0;i<hall.row;i++){
                var temp = ""
                for(var j =0;j<hall.column;j++){
                    temp+="<div class='cinema-hall-seat'></div>";
                }
                seat+= "<div>"+temp+"</div>";
            }
            var hallDom =
                "<div class='cinema-hall'>" +
                "<div>" +
                "<span class='cinema-hall-name'>"+ hall.name +"</span>" + "<button type='button' class='btn btn-primary' data-backdrop='static' style='float:right' data-toggle='modal' data-target='#scheduleModalEdit' id="+ hall.id + " onclick='addId("+ hall.id + ")'>修改影厅 </button>" +
                "<span class='cinema-hall-size'>"+ hall.column +'*'+ hall.row +"</span>" +
                "</div>" +
                "<div class='cinema-seat'>" + seat +
                "</div>" +
                "</div>" ;
            hallDomStr+=hallDom;
        });
        $('#hall-card').append(hallDomStr);
    }

    function getCanSeeDayNum() {
        getRequest(
            '/schedule/view',
            function (res) {
                canSeeDate = res.content;
                $("#can-see-num").text(canSeeDate);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    $('#canview-modify-btn').click(function () {
       $("#canview-modify-btn").hide();
       $("#canview-set-input").val(canSeeDate);
       $("#canview-set-input").show();
       $("#canview-confirm-btn").show();
    });

    /**
     * By yyf on 2019/05/29
     */
    $('#hall-modify-btn').click(function () {
        $("#hall-modify-btn").hide();
        $("#hall-column-set-input").val(canSeeDate);
        $("#hall-column-set-input").show();
        $("#hall-row-set-input").val(canSeeDate);
        $("#hall-row-set-input").show();
        $("#hall-name-set-input").show();
        $("#hall-confirm-btn").show();
    });
    /**
     * By yyf on 2019/05/29
     */
    $('#hall-modify-edit-btn').click(function () {
        $("#hall-modify-edit-btn").hide();
        $("#hall-column-set-input").val(canSeeDate);
        $("#hall-column-set-input").show();
        $("#hall-row-set-input").val(canSeeDate);
        $("#hall-row-set-input").show();
        $("#hall-name-set-input").show();
        $("#hall-confirm-btn").show();
    });


    $('#canview-confirm-btn').click(function () {
        var dayNum = $("#canview-set-input").val();
        // 验证一下是否为数字
        postRequest(
            '/schedule/view/set',
            {day:dayNum},
            function (res) {
                if(res.success){
                    getCanSeeDayNum();
                    canSeeDate = dayNum;
                    $("#canview-modify-btn").show();
                    $("#canview-set-input").hide();
                    $("#canview-confirm-btn").hide();
                } else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    })

    /**
     * By yyf on 2019/05/29
     * 添加影厅
     */
    $('#schedule-form-btn').click( function(){
        var hallName = $("#hall-name-input").val();
        var row = $("#hall-row-input").val();
        var column = $("#hall-column-input").val();
        //这里需要做表单验证
        var form = {
            name:hallName,
            row : parseInt(row),
            column : parseInt(column),
            id : 0
        };
        postRequest(
            "/hall/add",
            form,
            function (res) {
                if(res.success){
                    getCinemaHalls();
                    $("#modal-content").modal('hide');
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        )

    })

    /**
     * By yang on 2019/05/29
     * 修改影厅信息
     */
    $('#schedule-form-btn-edit').click( function(){
        var hallName = $("#hall-name-edit-input").val();
        var row = $("#hall-row-edit-input").val();
        var column = $("#hall-column-edit-input").val();
        var id = $("#edit-film-id").text();
        //这里需要做表单验证
        var form = {
            name: hallName,
            row : parseInt(row),
            column : parseInt(column),
            id : parseInt(id)
        };
        postRequest(
            "/hall/update",
            form,
            function (res) {
                if(res.success){
                    getCinemaHalls();
                    $("#modal-content").modal('hide');
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        )
        $("#modal-content").modal('hide');

    })
    /**
     * By yyf 2019/05/30
     * 删除影厅
     */
    $("#schedule-edit-remove-btn").click( function(){
        var deleteId = $("#edit-film-id").text();

    })
});
/**
 * By yyf 2019/05/2
 * 根据不同的点击，填充ID
 */
function addId(id2){
    $("#edit-film-id").empty();
    $("#hall-name-edit-input").empty();
    $("#hall-row-edit-input").empty();
    $("#hall-column-edit-input").empty();
    $("#edit-film-id").append("<p style='padding:5px 5px 5px 5px'>" + id2 + "</p>");
}


var refundId;
$(document).ready(function () {
    getMovieList();
    findUser();
    function findUser() {
        $("#user-tag").text(window.sessionStorage.getItem('username'));
    }
    function getMovieList() {
        getRequest(
            '/ticket/get/' + sessionStorage.getItem('id'),
            function (res) {
                getRefundList(res.content);
            },
            function (error) {
                alert(error);
            });
    }

    function getRefundList(list) {
        var idList = [];
        for(var i=0;i<list.length;i++){
            idList.push(list[i].id);
        }
        postRequest(
            '/ticket/return',
            idList,
            function (res) {
                renderRefundTicket(res.content);
            },
            function (error) {
                alert(error);
            }
        )
    }

    function renderRefundTicket(list) {
        for(var i = 0;i < list.length;i++){
            var ticketStr = "";
            var ticketWithScheduleVO = list[i];
            var schedule = ticketWithScheduleVO.schedule;
            var movieName = schedule.movieName;
            var hallName = schedule.hallName;
            var startTime = schedule.startTime;
            var endTime = schedule.endTime;
            var row = ticketWithScheduleVO.rowIndex;
            var column = ticketWithScheduleVO.columnIndex;
            var stateStr = ticketWithScheduleVO.state;
            var ticketId = ticketWithScheduleVO.id;
            ticketStr += '<tr>'
                + '<th>' + movieName + '</th>'
                + '<th>' + hallName + '</th>'
                + '<th>' + (row + 1) + '排' + (column + 1) + '座' + '</th>'
                + '<th>' + startTime.slice(0,10) + " " + startTime.slice(11,19) + '</th>'
                + '<th>' + endTime.slice(0,10) + " " + endTime.slice(11,19) + '</th>'
                + '<th>' + stateStr + '</th>'
                +'<th>'+"<button type='button' class='btn btn-primary' data-backdrop='static' style='float:right' data-toggle='modal' data-target='#refundModal' id = " + ticketId + " onclick = addId("+ ticketId +")>退票</button>"
                + '</tr>';
            if(stateStr !== "未完成"){
                $('#refund-tickets-table-body').append(ticketStr);
            }
        }
    }

    $("#refund-confirm-btn").click(function () {
        $("#refundModal").hide();
        var ticketId = Number($("#ticket-id-input").innerHTML);
        var buyMethod = $("#buy-method-choose option:selected").val();
        if(buyMethod=="0"){
            postRequest(
                '/ticket/refund?ticketId='+refundId,
                {},
                function () {
                    alert("退票成功！");
                    window.location.reload();
                },
                function (error) {
                    alert(error);
                }
            )
        }
        else{
            postRequest(
                '/ticket/refund/vip?ticketId='+refundId,
                {},
                function () {
                    alert("退票成功！");
                    window.location.reload();
                },
                function (error) {
                    alert(error);
                }
            )
        }
    })

    addId = function (id) {
        $("#ticket-id-input").empty();
        $("#ticket-id-input").html(id);
        refundId = id;
    };
})
/**
 * By yyf on 2019/06/06
 *
 * 两个功能
 * 获取历史记录
 * 查看详情
 */
$(document).ready(function () {

    var userId = (window.sessionStorage.getItem("id"));
    renderHistory(userId);

    findUser();
    function findUser() {
        $("#user-tag").text(window.sessionStorage.getItem('username'));
    }
    /**
     * 获取历史列表查看
     * @param userId
     */
    function renderHistory(userId) {
    getRequest(
        "/ticket/get/user/record/"+userId,
        function (res) {
            var ticketStr = "";
            var historyList = res.content;
            for(var i = 0 ;i < historyList.length ;i++) {
                //从各个historyList中提取信息
                var consumingRecord = historyList[i];
                var recordId = consumingRecord.id;
                var payment = consumingRecord.payment;
                var payForm = consumingRecord.payForm;
                var payTime = consumingRecord.payTime;
               ticketStr += '<tr>'
                    + '<th>' + recordId +'</th>'
                    + '<th>' + payForm + '</th>'
                    + '<th>' + payment + '</th>'
                    + '<th>' + payTime.slice(0, 10) + " " + payTime.slice(11, 19) + '</th>'
                    + '<th><button id="'+ recordId + '" class="btn btn-primary" data-backdrop="static" style="float:right" data-toggle="modal" data-target="#detailFind" onclick="addDetail(' + recordId + ')">查看详情</button></th>'
                    + '</tr>'
            }
            //插入页面中即可
            $("#my-tickets-table-body").append(ticketStr);
        },
        function (error) {
            alert(error);

        }
    )


}


$("#schedule-form-btn-edit").onclick(
    function(){
        $("#detailFind").modal("hide")

})
})

/**
 * 根据记录ID获取详细内容
 * 弹窗中会显示不同内容根据
 * html中onclick调用
 * @param recordId
 */
function addDetail(recordId){
    $("#recordId").empty();
    $("#userId").empty();
    $("#payTime").empty();
    $("#payment").empty();
    $("#payForm").empty();
    $("#scheduleId").empty();
    $("#ticketAmount").empty();
    $("#couponId").empty();
    $("#seat").empty();
    $("#hallId").empty();
    $("#hallName").empty();


    getRequest(
        "/ticket/get/record/" + recordId,
        function(res){
            console.log(res)
            var historyItem = res.content;
            var id = historyItem.id;
            var userId = historyItem.userId
            var payment= historyItem.payment;
            var payForm = historyItem.payForm;
            var payTime = historyItem.payTime;
            var scheduleId = parseInt(historyItem.scheduleId);
            console.log(scheduleId)
            var ticketAmount = historyItem.ticketAmount;
            var couponId = historyItem.couponId;
            var payFormLine = "";
            var movieName = "";
            var hallId = 0;
            var hallName = "";
            var seatInfo = "";
            if(payForm == 0){
                payFormLine = "银行卡"
            }
            if(payForm == 1){
                payFormLine = "会员卡"
            }

            if(couponId == 0){
                couponId = "你没有用优惠券！"
            }


            getRequest(
                "/ticket/get/consumed/" + recordId,
                function(res2){
                    console.log(res2)
                    console.log(recordId)
                    var scheduleId1 = 0
                    var list = res2.content;
                    console.log(list)
                    for(var i=0;i<list.length;i++){
                        var tmp = list[i];
                        scheduleId1 = list[i].scheduleId;
                        seatInfo += "<div>" + (tmp.rowIndex+1)+ "排"  + (tmp.columnIndex+1) + "列" + "</div>"
                    }
                    $("#seat").append(seatInfo);
                    getRequest(
                        "/schedule/" + scheduleId,
                        function(res1){
                            console.log(res1)
                            movieName = res1.content.movieName;
                            hallId = res1.content.hallId;
                            hallName = res1.content.hallName;
                            $("#hallName").append(hallName);
                            $("#hallId").append(hallId);
                            $("#scheduleId").append(movieName);

                        },
                        function(error1){
                            alert(error1)
                        }
                    )



            $("#recordId").append(id);
            $("#userId").append(userId);
            $("#payTime").append(payTime.slice(0, 10) + " " + payTime.slice(11, 19) );
            $("#payment").append(payment + "元");
            $("#payForm").append(payFormLine);
            $("#ticketAmount").append(ticketAmount);
            $("#couponId").append(couponId);



        },
                function(error){
            alert(error)
                }

                );

},
        function(error) {
            alert(error)
        }
        )



}



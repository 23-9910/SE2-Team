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


    getRequest(
        "/ticket/get/record/" + recordId,
        function(res){
            var historyItem = res.content;
            var id = historyItem.id;
            var userId = historyItem.userId
            var payment= historyItem.payment;
            var payForm = historyItem.payForm;
            var payTime = historyItem.payTime;
            var scheduleId = historyItem.shceduleId;
            var ticketAmount = historyItem.ticketAmount;
            var couponId = historyItem.couponId;
            var payFormLine = ""
            if(payForm == 0){
                payFormLine = "银行卡"
            }
            if(payForm == 1){
                payFormLine = "会员卡"
            }

            if(couponId == 0){
                couponId = "你没有用优惠券！"
            }

            $("#recordId").append(id);
            $("#userId").append(userId);
            $("#payTime").append(payTime.slice(0, 10) + " " + payTime.slice(11, 19) );
            $("#payment").append(payment + "元");
            $("#payForm").append(payFormLine);
            $("#scheduleId").append(scheduleId);
            $("#ticketAmount").append(ticketAmount);
            $("#couponId").append(couponId);


        },
        function(error){
            alert(error)
        }
        );
}


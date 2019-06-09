/**
 * By yyf on 2019/06/06
 */
$(document).ready(function () {

    var userId = window.sessionStorage.getItem("id")
    renderHistory(userId)

})


function renderHistory(userid) {
    getRequest(
        "/get/user/record/"+userid,
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
                    + '<button id="'+ recordId + '" class="btn btn-primary " data-backdrop="static" style="float:right" data-toggle="modal" data-target="#detailFind" onclick="addDetail('+ recordId + '")>查看详情</button>'
                    + '</tr>';
            }
            //插入页面中即可
            $("#my-tickets-table-body").append(ticketStr);
        },
        function (error) {
            alert(error);

        }
    )


}

/**
 * 根据记录ID获取详细内容
 * @param recordId
 */
function addDetail(recordId){
    getRequest(
        "/get/record/" + recordId,
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

            var detailStr = ""
            detailStr += "2"


        },
        function(error){
            alert(error)
        }
        )
}
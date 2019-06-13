/**
 * By yyf on 2019/06/08
 * 功能有
 * 查看现有的退票策略
 * 修改现有的退票策略
 * 新增
 * 删除
 */
$(document).ready(function() {
    getInfo();

    /**
     * 获取当前策略
     */

function getInfo(){
    $("#refund-container").empty();
    getRequest(
        '/ticket/get/strategy',
        function (res) {
            var perCent = res.content;
            renderRefund(perCent);
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );

}

/**
 * 处理数据
 * @param activities
 */

function renderRefund(perCent) {
    $(".content-activity").empty();
    var activitiesDomStr = "";
        activitiesDomStr+=
            "<div class='activity-container'>" +
            "    <div class='activity-card card'>" +
            "       <div class='activity-line'>" +
            "           <span class='gray-text'></span>" +
            "       </div>" +
            "    </div>" +
            "    <div>" +
            "        <span class='gray-text'>退票手续费比率（按订单总价收取）:</span>" +
            "        <span class='gray-text'>"+ (perCent*100) + "%</span>" +
            "    </div>" +
            "</div>";

    $("#refund-container").append(activitiesDomStr);
}

/**
 * 提交新退票方案
 */
$("#activity-form-btn").click(
    function(){
        var rate = $("#activity-name-input").val();
        $("#activityModal").hide();

        getRequest(
            "/ticket/strategy/" + rate ,
            function(res){
                alert("成功！")
                window.location.reload()
            },
            function(error){
                alert(error)
            }
        )

    }
)

})

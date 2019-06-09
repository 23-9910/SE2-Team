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
})


function getInfo(){
    getRequest(
        '/activity/get',
        function (res) {
            var activities = res.content;
            renderRefund(activities);
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

function renderRefund(activities) {
    $(".content-activity").empty();
    var activitiesDomStr = "";

    activities.forEach(function (activity) {
        var movieDomStr = "";
        if(activity.movieList.length){
            activity.movieList.forEach(function (movie) {
                movieDomStr += "<li class='activity-movie primary-text'>"+movie.name+"</li>";
            });
        }else{
            movieDomStr = "<li class='activity-movie primary-text'>所有热映电影</li>";
        }

        activitiesDomStr+=
            "<div class='activity-container'>" +
            "    <div class='activity-card card'>" +
            "       <div class='activity-line'>" +
            "           <span class='title'>"+activity.name+"</span>" +
            "           <span class='gray-text'>"+activity.description+"</span>" +
            "       </div>" +
            "       <div class='activity-line'>" +
            "           <span>活动时间："+formatDate(new Date(activity.startTime))+"至"+formatDate(new Date(activity.endTime))+"</span>" +
            "       </div>" +
            "       <div class='activity-line'>" +
            "           <span>参与电影：</span>" +
            "               <ul>"+movieDomStr+"</ul>" +
            "       </div>" +
            "    </div>" +
            "    <div class='activity-coupon primary-bg'>" +
            "        <span class='title'>优惠券："+activity.coupon.name+"</span>" +
            "        <span class='title'>满"+activity.coupon.targetAmount+"减<span class='error-text title'>"+activity.coupon.discountAmount+"</span></span>" +
            "        <span class='gray-text'>"+activity.coupon.description+"</span>" +
            "    </div>" +
            "</div>";
    });
    $(".content-activity").append(activitiesDomStr);
}

/**
 * 提交新退票方案
 */
$("#activity-form-btn").click(
    function(){

    }
)



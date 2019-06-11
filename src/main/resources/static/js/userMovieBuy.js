var selectedSeats = [];
var scheduleId;
var order = {ticketId: [], couponId: 0};
var coupons = [];//用户能用的coupons
var isVIP = false;
var useVIP = true;
var userId = sessionStorage.getItem('id');

var orderInfo = {};
var fare = 0;
var scheduleItem = {};
var ticketFormItem = {};//“未完成”订单信息
var ticketWithCoupon = {};
var unCompletedTickets = [];

$(document).ready(function () {
    scheduleId = parseInt(window.location.href.split('?')[1].split('&')[1].split('=')[1]);

    getInfo();

    function getInfo() {
        getRequest(
            '/ticket/get/occupiedSeats?scheduleId=' + scheduleId,
            function (res) {
                if (res.success) {
                    /**得到该排片电影的单张票价**/
                    scheduleItem = res.content.scheduleItem;
                    fare = scheduleItem.fare;
                    renderSchedule(scheduleItem, res.content.seats);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }


});

function renderSchedule(schedule, seats) {
    $('#schedule-hall-name').text(schedule.hallName);
    $('#order-schedule-hall-name').text(schedule.hallName);
    $('#schedule-fare').text(schedule.fare.toFixed(2));
    $('#order-schedule-fare').text(schedule.fare.toFixed(2));
    $('#schedule-time').text(schedule.startTime.substring(5, 7) + "月" + schedule.startTime.substring(8, 10) + "日 " + schedule.startTime.substring(11, 16) + "场");
    $('#order-schedule-time').text(schedule.startTime.substring(5, 7) + "月" + schedule.startTime.substring(8, 10) + "日 " + schedule.startTime.substring(11, 16) + "场");

    var hallDomStr = "";
    var seat = "";
    for (var i = 0; i < seats.length; i++) {
        var temp = "";
        for (var j = 0; j < seats[i].length; j++) {
            var id = "seat" + i + j;

            if (seats[i][j] === 0) {
                // 未选
                temp += "<button class='cinema-hall-seat-choose' id='" + id + "' onclick='seatClick(\"" + id + "\"," + i + "," + j + ")'></button>";
            } else {
                // 已选中
                temp += "<button class='cinema-hall-seat-lock'></button>";
            }
        }
        seat += "<div>" + temp + "</div>";
    }
    var hallDom =
        "<div class='cinema-hall'>" +
        "<div>" +
        "<span class='cinema-hall-name'>" + schedule.hallName + "</span>" +
        "<span class='cinema-hall-size'>" + seats.length + '*' + seats[0].length + "</span>" +
        "</div>" +
        "<div class='cinema-seat'>" + seat +
        "</div>" +
        "</div>";
    hallDomStr += hallDom;

    $('#hall-card').html(hallDomStr);



}

function seatClick(id, i, j) {
    let seat = $('#' + id);
    if (seat.hasClass("cinema-hall-seat-choose")) {
        seat.removeClass("cinema-hall-seat-choose");
        seat.addClass("cinema-hall-seat");

        selectedSeats[selectedSeats.length] = [i, j]
    } else {
        seat.removeClass("cinema-hall-seat");
        seat.addClass("cinema-hall-seat-choose");

        selectedSeats = selectedSeats.filter(function (value) {
            return value[0] != i || value[1] != j;
        })
    }

    selectedSeats.sort(function (x, y) {
        var res = x[0] - y[0];
        return res === 0 ? x[1] - y[1] : res;
    });

    let seatDetailStr = "";
    if (selectedSeats.length == 0) {
        seatDetailStr += "还未选择座位";
        $('#order-confirm-btn').attr("disabled", "disabled")
    } else {
        for (let  seatLoc of selectedSeats) {
            seatDetailStr += "<span>" + (seatLoc[0] + 1) + "排" + (seatLoc[1] + 1) + "座</span>";
        }
        $('#order-confirm-btn').removeAttr("disabled");
    }
    $('#seat-detail').html(seatDetailStr);

}

/**
 * @Author syf
 * @Info   锁座方法，点击“确认下单”按钮，前端向后端提交未完成订单。
 */
function lockSeat(){
    ticketFormItem.scheduleId = scheduleId;
    ticketFormItem.userId = userId;
    var seatFormList = [];
    for(var i = 0;i < selectedSeats.length;i++){
        var seat = {
            rowIndex: selectedSeats[i][0],
            columnIndex: selectedSeats[i][1]
        };
        seatFormList.push(seat);
    }
    ticketFormItem.seats = seatFormList;

    postRequest(
        '/ticket/lockSeat',
        ticketFormItem,
        function (res) {
            if (res.success) {
                getTicketIdList();
            } else {
                alert(res.message);
            }
        },
        function (error) {
            alert(error);
        }
    )
}

function getTicketIdList(){
    getRequest(
        '/ticket/get/' + userId,
        function(res) {
            var ticketWithScheduleList = res.content;
            for(var i = 0;i < ticketWithScheduleList.length;i++){
                if(ticketWithScheduleList[i].state === "未完成") {
                    unCompletedTickets.push(ticketWithScheduleList[i].id);
                }
            }
            for(var k = unCompletedTickets.length - selectedSeats.length;k < unCompletedTickets.length;k++){
                order.ticketId[k - (unCompletedTickets.length - selectedSeats.length)] = unCompletedTickets[k];
            }
            getTicketsWithCoupons();
        },
        function(error){
            alert(error);
        }
    );

}

function getTicketsWithCoupons(){
    postRequest(
        '/ticket/get/withCoupon?userId=' + userId,
        order.ticketId,
        function(res) {
            ticketWithCoupon = res.content;
            coupons = ticketWithCoupon.coupons;
            orderConfirm();
        },
        function(error) {
            alert(error);
        }
    );

}

function orderConfirmClick() {
    /**
     * "确认下单"按钮的onclick事件函数
     * 先触发lockSeat函数，前端往后端postRequest，传递未完成订单信息
     * 如果onSuccess，则getRequest，得到TicketWithCouponVO，完成orderInfo
     * 此处加入时间延迟，等待lockSeat()数据请求完成
     *
     */
    lockSeat();
}

function orderConfirm(){
    $('#seat-state').css("display", "none");
    $('#order-state').css("display", "");
    /**
     * orderInfo是从后端传入的真数据
     */
    // console.log(order.ticketId);
    var ticketVOList = [];
    var seatsLen = selectedSeats.length;
    for (var i = 0;i < seatsLen;i++) {
        var ticketInfo = {};
        var selectedSeat = selectedSeats[i];
        ticketInfo.id = order.ticketId[i];
        ticketInfo.userId = userId;
        ticketInfo.scheduleId = scheduleId;
        ticketInfo.rowIndex = selectedSeat[0];
        ticketInfo.columnIndex = selectedSeat[1];
        ticketInfo.state = "未完成";
        ticketVOList.push(ticketInfo);
    }
    console.log(ticketVOList);
    orderInfo.ticketVOList = ticketVOList;
    orderInfo.total = ticketVOList.length * fare ;
    orderInfo.coupons = coupons;
    // console.log(coupons);
    orderInfo.activities = ticketWithCoupon.activities;
    renderOrder(orderInfo);

    // TODO:这里是假数据，需要连接后端获取真数据，数据格式可以自行修改，但如果改了格式，别忘了修改renderOrder方法
    /**var orderInfo = {

        "ticketVOList": [{
            "id": 63,
            "userId": 15,
            "scheduleId": 67,
            "columnIndex": 5,
            "rowIndex": 1,
            "state": "未完成"
        }, {
            "id": 64,
            "userId": 15,
            "scheduleId": 67,
             "columnIndex": 6,
              "rowIndex": 1,
               "state": "未完成"}
               ],
        "total": 41.0,
        "coupons": [{
            "id": 5,
            "description": "测试优惠券",
            "name": "品质联盟",
            "targetAmount": 30.0,
            "discountAmount": 4.0,
            "startTime": "2019-04-21T05:14:46.000+0800",
            "endTime": "2019-04-25T05:14:51.000+0800"
        }, {
            "id": 5,
            "description": "测试优惠券",
            "name": "品质联盟",
            "targetAmount": 30.0,
            "discountAmount": 4.0,
            "startTime": "2019-04-21T05:14:46.000+0800",
            "endTime": "2019-04-25T05:14:51.000+0800"
        }],
        "activities": [{
            "id": 4,
            "name": "测试活动",
            "description": "测试活动",
            "startTime": "2019-04-21T00:00:00.000+0800",
            "endTime": "2019-04-27T00:00:00.000+0800",
            "movieList": [{
                "id": 10,
                "name": "夏目友人帐",
                "posterUrl": "http://n.sinaimg.cn/translate/640/w600h840/20190312/ampL-hufnxfm4278816.jpg",
                "director": "大森贵弘 /伊藤秀樹",
                "screenWriter": "",
                "starring": "神谷浩史 /井上和彦 /高良健吾 /小林沙苗 /泽城美雪",
                "type": "动画",
                "country": null,
                "language": null,
                "startDate": "2019-04-14T22:54:31.000+0800",
                "length": 120,
                "description": "在人与妖怪之间过着忙碌日子的夏目，偶然与以前的同学结城重逢，由此回忆起了被妖怪缠身的苦涩记忆。此时，夏目认识了在归还名字的妖怪记忆中出现的女性·津村容莉枝。和玲子相识的她，现在和独子椋雄一同过着平稳的生活。夏目通过与他们的交流，心境也变得平和。但这对母子居住的城镇，却似乎潜伏着神秘的妖怪。在调查此事归来后，寄生于猫咪老师身体的“妖之种”，在藤原家的庭院中，一夜之间就长成树结出果实。而吃掉了与自己形状相似果实的猫咪老师，竟然分裂成了3个",
                "status": 0,
                "islike": null,
                "likeCount": null
            }],
            "coupon": {
                "id": 8,
                "description": "测试优惠券",
                "name": "123",
                "targetAmount": 100.0,
                "discountAmount": 99.0,
                "startTime": "2019-04-21T00:00:00.000+0800",
                "endTime": "2019-04-27T00:00:00.000+0800"
            }
        }]
    };
     */

    getRequest(
        '/vip/' + sessionStorage.getItem('id') + '/get',
        function (res) {
            isVIP = res.success;
            useVIP = res.success;
            if (isVIP) {
                $('#member-balance').html("<div><b>会员卡余额：</b>" + res.content.balance.toFixed(2) + "元</div>");
            } else {
                $("#member-pay").css("display", "none");
                $("#nonmember-pay").addClass("active");

                $("#modal-body-member").css("display", "none");
                $("#modal-body-nonmember").css("display", "");
            }
        },
        function (error) {
            alert(error);
        });
}

function switchPay(type) {
    useVIP = (type == 0);
    if (type == 0) {
        $("#member-pay").addClass("active");
        $("#nonmember-pay").removeClass("active");

        $("#modal-body-member").css("display", "");
        $("#modal-body-nonmember").css("display", "none");
    } else {
        $("#member-pay").removeClass("active");
        $("#nonmember-pay").addClass("active");

        $("#modal-body-member").css("display", "none");
        $("#modal-body-nonmember").css("display", "");
    }
}

/**
 * @Author syf
 * @Info   renderOrder：呈现订单方法。订单状态为未完成。
 */
function renderOrder(orderInfo) {
    var ticketStr = "<div>" + selectedSeats.length + "张</div>";
    var ticketVOList = orderInfo.ticketVOList;
    for (var i = 0;i < ticketVOList.length;i++){
        var ticketInfo = ticketVOList[i];
        ticketStr += "<div>" + (ticketInfo.rowIndex + 1) + "排" + (ticketInfo.columnIndex + 1) + "座</div>";
    }
    $('#order-tickets').html(ticketStr);

    var total = orderInfo.total.toFixed(2);
    $('#order-total').text(total);
    $('#order-footer-total').text("总金额： ¥" + total);

    var couponTicketStr = "";
    if (orderInfo.coupons.length == 0) {
        $('#order-discount').text("优惠金额：无");
        $('#order-actual-total').text(" ¥" + total);
        $('#pay-amount').html("<div><b>金额：</b>" + total + "元</div>");
    } else {
        for (var i = 0;i < coupons.length;i++) {
            var coupon = coupons[i];
            couponTicketStr += "<option>满" + coupon.targetAmount + "减" + coupon.discountAmount + "</option>"
        }
        $('#order-coupons').html(couponTicketStr);
        changeCoupon(0);
    }
}

function changeCoupon(couponIndex) {
    order.couponId = coupons[couponIndex].id;
    $('#order-discount').text("优惠金额： ¥" + coupons[couponIndex].discountAmount.toFixed(2));
    var actualTotal = (parseFloat($('#order-total').text()) - parseFloat(coupons[couponIndex].discountAmount)).toFixed(2);
    $('#order-actual-total').text(" ¥" + actualTotal);
    $('#pay-amount').html("<div><b>金额：</b>" + actualTotal + "元</div>");
}

function payConfirmClick() {
    if (useVIP) {
        postVIPPayRequest();
    } else {
        if (validateForm()) {
            if ($('#userBuy-cardNum').val() === "123123123" && $('#userBuy-cardPwd').val() === "123123") {
                postPayRequest();
            } else {
                alert("银行卡号或密码错误");
            }
        }
    }
}

// TODO:填空
function postVIPPayRequest() {
    //发送请求，样式变化，确认支付框消失
    var ticketId = order.ticketId;
    var couponId = order.couponId;
    console.log(ticketId)
    postRequest(
        "/ticket/vip/buy?couponId=" + couponId,
        ticketId,
        function(){
            $('#order-state').css("display", "none");
            $('#success-state').css("display", "");
            $('#buyModal').modal('hide');
            //addOneConsumingRecord
            //再往票内部插入记录Id  addRecordIdOnTicket
                },
        function(error){
            alert(error)
                }
             );

setTimeout(
    function(){
    addRecord(ticketId)
    }
    ,500
)
}


function addRecord(ticketId){
    postRequest(
        "/ticket/add/recordId",
        ticketId,
        function(res){
        },
        function(error){
            alert(error);
        }
    )
}
function postPayRequest() {
    //发送请求，样式变化，确认支付框消失
    var ticketId = order.ticketId;
    var couponId = order.couponId;

    console.log(couponId)
    postRequest(
        '/ticket/buy?couponId=' + couponId,
        ticketId,
        function () {
            $('#order-state').css("display", "none");
            $('#success-state').css("display", "");
            $('#buyModal').modal('hide');
        },
        function (error) {
            alert(error);
        }
    );

}

function validateForm() {
    var isValidate = true;
    if (!$('#userBuy-cardNum').val()) {
        isValidate = false;
        $('#userBuy-cardNum').parent('.form-group').addClass('has-error');
        $('#userBuy-cardNum-error').css("visibility", "visible");
    }
    if (!$('#userBuy-cardPwd').val()) {
        isValidate = false;
        $('#userBuy-cardPwd').parent('.form-group').addClass('has-error');
        $('#userBuy-cardPwd-error').css("visibility", "visible");
    }
    return isValidate;
}

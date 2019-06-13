$(document).ready(function () {
    
    getDescription();
    renderCoupons();
    renderAllVIP();
    findUser();
    function findUser() {
        $("#user-tag").html(window.sessionStorage.getItem('username'));
    }


    function getDescription() {
        getRequest(
            "/vip/get/description",
            function (res) {
                var des = res.content;
                renderDescription(des);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }
    
    function renderDescription(des) {
        $(".description-container").empty();
        var vipDomStr="";
        vipDomStr+= "<span class='title'>充值优惠：</span>" +des;
        $(".description-container").append(vipDomStr);
    }

    $("#vip-form-btn").click(function () {
        var price = $("#vip-price-input").val();
        var gift = $("#vip-gift-input").val();
        var description = "满"+price+"送"+gift;

        getRequest(
            "/vip/description/" + description,
            function (res) {
                if(res.success){
                    getDescription();
                    $("#VIPModal").modal('hide');
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    })

    $("#vip-change-btn").click(function(){
        var priceNew = $("#vip-new-price-input").val();
        var giftNew = $("#vip-new-gift-input").val();
        var descriptionNew = "满"+priceNew+"送"+giftNew;
        getRequest(
            "/vip/description/"+ descriptionNew,
            function (res) {
                if(res.success){
                    getDescription();
                    $("#VIPChangeModal").modal('hide');
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    })

    function renderCoupons(){
        getRequest(
            '/coupon/all',
            function (res) {
                let coupons = res.content;
                coupons.forEach(function(coupon){
                   $('#coupon-select').append("<option value="+coupon.id+">"+coupon.name+"</option>");
                });
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        )
    }
    //TODO
    function renderAllVIP(){
        let initialConsumption = "0";
        getRequest(
            '/vip/get/consumingSum/'+initialConsumption,
            function (res) {
                var allVips = res.content;
                $(".vips-table-container").empty();
                allVips.forEach(function (v) {
                    var vipDomStr = "<tr><th>No."+v.userId+"</th><th>"+v.userName+"</th><th>￥"+v.consumingSum+"</th><th><input type='checkbox' name='chooseVip' value = " + v.userId + " /></th></tr>";
                    $("#my-tickets-table-body").append(vipDomStr);
                })
            },
            function(error){
                alert(JSON.stringify(error));
            }
        )
    }
    //TODO
    /**
     * 获取符合条件的会员
     * 后端添加方法
     */
    $("#consumption-input-btn").click(function () {
        let consumption = $("#vip-history-input").val();
        getRequest(
            "/vip/get/consumingSum/"+consumption,
            function (res) {
                var vips = res.content;
                $(".vips-table-container").empty();
                vips.forEach(function (v) {
                    var vipDomStr = "<tr><th>No."+v.userId+"</th><th>"+v.userName+"</th><th>￥"+v.consumingSum+"</th><th><input type='checkbox' name='chooseVip' value = "+  v.userId + " /></th></tr>";
                    $("#my-tickets-table-body").append(vipDomStr);
                })
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        )
    })
    //TODO
    /**
     * 点击确认按钮，会将优惠卷赠送
     */

    $("#coupon-give-btn").click(function(){
        var checkedName = document.getElementsByName('chooseVip');
        var userIds = [];
        for(var i = 0; i<checkedName.length;i++){
            if(checkedName[i].checked)
                userIds.push(checkedName[i].value);
        }
        var couponId =$("#coupon-select option:selected").val();
        userIds.forEach(function (userId) {
            postRequest(
                '/coupon/issue?userId='+userId+'&couponId='+couponId,
                {},
                function () {
                    $("#couponGiveModal").hide();
                    window.location.reload();
                },
                function (error) {
                    JSON.stringify(error);
                }
            )
        })
    })

})
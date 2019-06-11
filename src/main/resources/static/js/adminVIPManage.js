$(document).ready(function () {
    
    getDescription();
    renderCoupons();
    renderAllVIP();



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
        console.log(des)
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
        var priceNew = $("#vip-price-input").val();
        var giftNew = $("#vip-gift-input").val();
        var descriptionNew = "满"+priceNew+"送"+giftNew;
        getRequest(
            "vip/description"+ descriptionNew,
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
            '/activity/get',
            function (res) {
                var coupons = res.content;
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
        getRequest(
            '/vip/',
            function (res) {

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
        var consumption = $("vip-history-input").val();
        getRequest(
            "/vip/get/consumingSum/{amount}"+consumption,
            function (res) {
                var vips = res.content;
                console.log(res);
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


})
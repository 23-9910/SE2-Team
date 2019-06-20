$(document).ready(function () {
    
    getDescription();
    renderCoupons();
    renderAllVIP();
    findUser();
    function findUser() {
        $("#user-tag").html(window.sessionStorage.getItem('username'));
    }


    function getDescription() {
        // res={
        //     content:"充100送1000";
        // };
        // renderDescription(res.content);

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
        if(isNaN(price)){
            alert("请填写正确信息！");
            return;
        }
        if(isNaN(gift)){
            alert("请填写正确信息！");
            return;
        }
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
        if(isNaN(priceNew)){
            alert("请填写正确信息！");
            return;
        }
        if(isNaN(giftNew)){
            alert("请填写正确信息！");
            return;
        }
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
        // res={content:[{id:10,name:"第一优惠券"},{id:20,name:"第二优惠券"}]};
        // coupons=res.content;
        // coupons.forEach(function(coupon){
        //     $('#coupon-select').append("<option value="+coupon.id+">"+coupon.name+"</option>");
        // });
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

    function renderAllVIP(){
        // res={content:[{userId:1,userName:"user1",consumingSum:100},{userId:2,userName:"user2",consumingSum:200}]};
        // var allVips = res.content;
        // $(".vips-table-container").empty();
        // allVips.forEach(function (v) {
        //     var vipDomStr = "<tr><th>No."+v.userId+"</th><th>"+v.userName+"</th><th>￥"+v.consumingSum+"</th><th><input type='checkbox' name='chooseVip' value = " + v.userId + " /></th></tr>";
        //     $("#my-tickets-table-body").append(vipDomStr);
        // })
        let initialConsumption = 0;
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

    /**
     * 获取符合条件的会员
     * 后端添加方法
     */
    $("#consumption-input-btn").click(function () {
        let consumption = $("#vip-history-input").val();
        if(isNaN(consumption)){
            alert("请正确输入信息！");
            return;
        }
        if(consumption<0){
            alert("消费额度应大于等于0！");
            return;
        }
        // res={content:[{userId:1,userName:"user1",consumingSum:100},{userId:2,userName:"user2",consumingSum:200}]};
        // var allVips = res.content;
        // $(".vips-table-container").empty();
        // allVips.forEach(function (v) {
        //     var vipDomStr = "<tr><th>No."+v.userId+"</th><th>"+v.userName+"</th><th>￥"+v.consumingSum+"</th><th><input type='checkbox' name='chooseVip' value = " + v.userId + " /></th></tr>";
        //     $("#my-tickets-table-body").append(vipDomStr);
        // })
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
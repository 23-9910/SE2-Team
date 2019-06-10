$(document).ready(function () {
    
    getVIPs();
    
    function getVIPs() {
        getRequest(
            "",
            function (res) {
                var vips = res.content;
                renderVIPs(vips);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }
    
    function renderVIPs(vips) {
        $(".content-vip").empty();
        var vipDomStr="";

        vips.forEach(function(vip) {
            vipDomStr+=
                "<span class='title'>充值优惠：</span>" +
                "<span class='title'>"+vip.description +"</span>"
        });
        $(".description-container").append(vipDomStr);
    }

    $("#vip-form-btn").click(function () {
        var price = $("#vip-price-input").val();
        var gift = $("#vip-gift-input").val();
        var description = "充"+price+"送"+gift;

        postRequest(
            "",
            description,
            function (res) {
                if(res.success){
                    getVIPs();
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
        var descriptionNew = "充"+priceNew+"送"+giftNew;
        postRequest(
            "",
            descriptionNew,
            function (res) {
                if(res.success){
                    getVIPs();
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
})
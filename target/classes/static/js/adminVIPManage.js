$(document).ready(function () {
    
    getVIPs();
    
    function getVIPs() {
        getRequest(
            '/get',
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
                "<div class='vip-container'>" +
                "    <div class='vip-card card'>" +
                "       <div class='vip-line'>" +
                "           <span class='title'>"+vip.name+"</span>" +
                "           <span class='gray-text'>"+vip.description+"</span>" +
                "       </div>" +
                "    </div>" +
                "    <div class='vip-coupon primary-bg'>" +
                "        <span class='title'>充值优惠："+vip.name+"</span>" +
                "        <span class='title'>买"+vip.price+"送<span class='error-text title'>"+vip.gift+"</span></span>" +
                "        <span class='gray-text'>"+vip.description+"</span>" +
                "    </div>" +
                "</div>";
        });
        $(".content-vip").append(vipDomStr);
    }

    $("#vip-form-btn").click(function () {
        var form = {
            name: $("#vip-name-input").val(),
            description: $("#vip-description-input").val(),
            price: $("#vip-price-input").val(),
            gift: $("#vip-gift-input").val()
        };

        postRequest(
            '/publish',
            form,
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
})
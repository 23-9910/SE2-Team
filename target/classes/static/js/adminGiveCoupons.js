
/**
 * By yyf on 2019/06/04
 * 赠送优惠券方法
 */
$(document).ready(function() {
    renderCoupons();
    renderAllVIP();
})

/**
 * By yyf on 2019/06/04
 * 获取所有优惠券
 */
function renderCoupons(){

}

/**
 * 初始化页面，显示所有会员卡信息
 */
function renderAllVIP(){
    getRequest(
        '/vip/getVIPInfo',
        function (res) {
            
        },
        function(error){

        }
    )
}

/**
 * 获取符合条件的会员
 */
function renderVIP(){

}

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
 * 后端新增方法，返回所有优惠券的内容
 */
function renderCoupons(){

}

/**
 * 初始化页面，显示所有会员卡信息
 * 后端需要添加方法
 */
function renderAllVIP(){
    getRequest(
        '/vip/',
        function (res) {
            
        },
        function(error){

        }
    )
}

/**
 * 获取符合条件的会员
 * 后端添加方法
 */
function renderVIP(){

}
/**
 * by yyf on 2019/06/04
 */

$(document).ready(function() {
    //加载时会向后端请求所有的用户内容
    //获取账户信息
    //每个账户后都添加一个修改buttonn
    var id = window.sessionStorage.getItem('id');
    var useId = {userId:parseInt(id)};
    postRequest("/search/all/manager",
        useId,
        function(res){
            if(res.success){
                getAllAccount(res.content);
            }
        },
        function (error) {
            alert(error);
        }
    );
});




function renderAccount(id){
    postRequest("/search/all/manager",
        {userId:id},
        function(res){
        if(res.success){
            getAllAccount(res.content);
        }
        },
        function (error) {
            alert(error);
        }
    );
};

function getAllAccount(list){
    for(var i = 0;i < list.length;i++){
        var ticketStr = "";
        var user = list[i];
        var id1 = parseInt(user.id);
        var username1 = user.username;
        var usertypeId = user.state;
        var userType = "";
        if( usertypeId == 0){
            userType = "经理";
        }
        if( usertypeId == 1){
            userType = "售票员";
        }
        if( usertypeId == 2){
            userType = "观众";
        }
        ticketStr = ticketStr += '<tr>'
            + '<th>' + id1 + '</th>'
            + '<th>' + username1 + '</th>'
            + '<th>' + userType + '</th>'
            +  "<button type='button' class='btn btn-primary' data-backdrop='static' style='float:right' data-toggle='modal' data-target='#editAccount' id="+ id1 + " onclick='addId("+ id1 + ")'>修改账户信息 </button>"
            + '</tr>';

        $('#my-tickets-table-body').append(ticketStr);
    }

}

$("#newAdd").click(function(){
    $("#account-input").clean();
    $("#password-input").clean();
})


/**
 * 提交添加请求
 */

$('#schedule-form-btn').click(function(){
    $("#addAccount").hide();
    var password = ("#password-input").val();
    var username = ("#account-input").val();
    var state = ("#type-input").val();
    var user = {
        id:0,
        username:username,
        password:password,
        state:parseInt(state)
    }
    postRequest(
        "/add/one/manager",
        user,
        function (res) {

        },
        function(error){
            alert(error)
        }
    )

});

/**
 * 修改
 */
$("#schedule-form-btn-edit").click(function(){
//根据ID传输修改内容
    var idEdit = $("#id-edit").val();
    var userName = $("#account-edit-input").val();
    var passWord = $("#password-edit-input").val();


});

/**
 * 点击不同位置的修改时自动填充ID
 */
function addId(id2){
    $("#account-edit-input").clean();
    $("#id-edit").append(id2);
}

/**
 * 删除改账户
 */
$("#account-edit-remove-btn").click(function() {
    //根据Id删除
    var idRemove = $("#id-edit").val();
});
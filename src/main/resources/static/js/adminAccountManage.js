/**
 * by yyf on 2019/06/04
 */

$(document).ready(function() {
    //加载时会向后端请求所有的用户内容
    //获取账户信息
    //每个账户后都添加一个修改buttonn
    var id = window.sessionStorage.getItem('id');
    console.log(id)
    var userID = parseInt(id);
    var useId = {userId:userID};
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


function getInfo(){
    var id = window.sessionStorage.getItem('id');
    console.log(id)
    var userID = parseInt(id);
    var useId = {userId:userID};
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
}

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

/**
 * 获取账号信息并插入页面
 * @param list
 */
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
    $("#addAccount").modal("hide");
    var password = $("#password-input").val();
    var username = $("#account-input").val();
    var state = $("#type-input").val();
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
            getInfo();

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
    $("#addAccount").modal("hide");
    var idEdit = $("#id-edit").val();
    var userName = $("#account-edit-input").val();
    var passWord = $("#password-edit-input").val();
var state = $("#type-edit-input").value();
var user = {
    id:parseInt(idEdit),
    username:userName,
    password:passWord,
    state:state
}
postRequest("/update/one/manager",
    user,
    function(res){
    alert("修改成功")
    getInfo()
    },
    function(error){
    alert(error)
    }
);

});

/**
 * 点击不同位置的修改时自动填充ID
 * 用于修改账户
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
    $("#addAccount").modal("hide");
    var idRemove = $("#id-edit").val();
    var userId = parseInt(idRemove);
    postRequest(
        "/delete/one/manager",
        {userId:userId},
        function(res){
            alert("删除成功！")
            getInfo()
        },
        function (error) {
            alert(error)

        }
    )
});
/**
 * by yyf on 2019/06/04
 */
var infoV = []
$(document).ready(function() {
    //加载时会向后端请求所有的用户内容
    //获取账户信息
    //每个账户后都添加一个修改buttonn
    findUser();
    function findUser() {
        $("#user-tag").html(window.sessionStorage.getItem('username'));
    }

    var id = window.sessionStorage.getItem('id');
    var z = window.sessionStorage.getItem('role');

    // res={content:[{id:1,username:"user1",state:1,password:"123456"},{id:2,username:"user2",state:2,password:"123456"}]};
    // getAllAccount(res.content);

    getRequest(
        "/search/all/manager/" + id,

        function(res){
            if(res.success){
                getAllAccount(res.content);
            }
        },
        function (error) {
            alert("你没有权限访问");
            window.location.href = "/admin/movie/manage";
        }
    );



function getInfo(){
    var id = window.sessionStorage.getItem('id');
    console.log(id);
    // res={content:[{id:1,username:"user1",state:1,password:"123456"},{id:2,username:"user2",state:2,password:"123456"}]};
    // getAllAccount(res.content);
    getRequest(
        "/search/all/manager/" + id,

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

// function renderAccount(id){
//     postRequest("/search/all/manager",
//         {userId:id},
//         function(res){
//         if(res.success){
//             getAllAccount(res.content);
//         }
//         },
//         function (error) {
//             alert(error);
//         }
//     );
// };

/**
 * 获取账号信息并插入页面
 * @param list
 */
function getAllAccount(list){
    infoV = list;
    $('#my-tickets-table-body').empty();

    for(var i = 0;i < list.length;i++){
        var ticketStr = "";
        var user = list[i];
        var id1 = parseInt(user.id);
        var username1 = user.username;
        var usertypeId = user.state;
        var password = user.password;
        var username0 = "'" + username1 + "'"
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
            + '<th>' + password + '</th>'
            +  "<th>"+"<button type='button' class='btn btn-primary' data-backdrop='static' style='float:right' data-toggle='modal' data-target='#editAccount' id="+ id1 + " onclick=addId("+ id1 +"," + usertypeId+ "," + username0 +  ")>修改账户信息 </button>";
        + '</th>'+ '</tr>'
        $('#my-tickets-table-body').append(ticketStr);
    }

}

    /**
     * 自动填充修改表单
     */
    $('#newAdd').click(function(){
    $('#account-input').empty();
    $('#password-input').empty();
})

    /**
     *判断输入是否为空
     */
    function isEmpty(obj){
        if(typeof obj == "undefined" || obj == null || obj == ""){
            return true;
        }else{
            return false;
        }
    }
/**
 * 提交添加请求
 */

$('#schedule-form-btn').click(function() {
    $("#addAccount").hide();

    var password = $("#password-input").val();
    var username = $("#account-input").val();
    var state = $("#type-input").val();

    if(isEmpty(username)){
        alert("请输入账户名！")
        return;
    }
    if(isEmpty(password)){
        alert("请输入账户密码！")
        return;
    }


    var user = {
        username:username,
        password:password,
        state:parseInt(state)
    }
    console.log(user)
    postRequest(
        "/add/one/manager",
        user,
        function (res) {
            window.location.reload()

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
    var password = "";

    $("#editAccount").hide();
    var idEdit = $("#id-edit").text();
    var userName = $("#account-edit-input").val();
    var password = $("#password-edit-input").val();
    console.log(userName)
    var state123 = $("#type-edit-input option:selected") .val();
    console.log(state123)
var user = {
    id:parseInt(idEdit),
    username:userName,
    password:password,
    state:parseInt(state123)
}
console.log(user)
postRequest("/update/one/manager",
    user,
    function(res){
    alert("修改成功");
        window.location.reload();
    },
    function(error){
    alert(error)
    }
);

});





/**
 * 删除改账户
 */
$("#account-edit-remove-btn").click(function() {
    //根据Id删除
    $("#editAccount").modal("hide");
    var idRemove = parseInt($("#id-edit").text());
    getRequest(
        "/delete/one/manager/"+idRemove,
        function(res){
            alert("删除成功！")
            getInfo()
        },
        function (error) {
            alert(error)

        }
    )
});

});

/**
 * 点击不同位置的修改时自动填充ID
 * 用于修改账户
 */
addId = function(id2,type1,name1){
    $("#id-edit").empty();
    $("#account-edit-input").empty();
    $("#account-edit-input").val(name1);
    $("#type-edit-input"). val(type1)
    $("#id-edit").append(id2);

    for(var i=0; i <infoV.length;i++){
        var tmp = infoV[i];
        if(tmp.id == id2){
            var password1= tmp.password;
            $("#password-edit-input").empty();
            $("#password-edit-input").val(password1);
            break;
        }
    }

};
$(document).ready(function(){

    var movieId = parseInt(window.location.href.split('?')[1].split('&')[0].split('=')[1]);
    var userId = sessionStorage.getItem('id');
    var isLike = false;

    getMovie();



if(window.sessionStorage.getItem('role') === 'user'){
    findUser1()
}else{
    getMovieLikeChart();
    findUser2()
}
    function findUser1() {
        $("#user-tag").text(window.sessionStorage.getItem('username'));
    }
    function findUser2() {
        $("#user-tag").html(window.sessionStorage.getItem('username'));
    }
    function getMovieLikeChart() {
       getRequest(
           '/movie/' + movieId + '/like/date',
           function(res){
               var data = res.content,
                    dateArray = [],
                    numberArray = [];
               data.forEach(function (item) {
                   dateArray.push(item.likeTime);
                   numberArray.push(item.likeNum);
               });

               var myChart = echarts.init($("#like-date-chart")[0]);

               // 指定图表的配置项和数据
               var option = {
                   title: {
                       text: '想看人数变化表'
                   },
                   xAxis: {
                       type: 'category',
                       data: dateArray
                   },
                   yAxis: {
                       type: 'value'
                   },
                   series: [{
                       data: numberArray,
                       type: 'line'
                   }]
               };

               // 使用刚指定的配置项和数据显示图表。
               myChart.setOption(option);
           },
           function (error) {
               alert(error);
           }
       );
    }

    function getMovie() {
        getRequest(
            '/movie/'+movieId + '/' + userId,
            function(res){
                var data = res.content;
                isLike = data.islike;
                repaintMovieDetail(data);
            },
            function (error) {
                alert(error);
            }
        );
    }

    function repaintMovieDetail(movie) {
        !isLike ? $('.icon-heart').removeClass('error-text') : $('.icon-heart').addClass('error-text');
        $('#like-btn span').text(isLike ? ' 已想看' : ' 想 看');
        $('#movie-img').attr('src',movie.posterUrl);
        $('#movie-name').text(movie.name);
        $('#movie-id').text(movie.id);
        $('#order-movie-name').text(movie.name);
        $('#movie-description').text(movie.description);
        $('#movie-startDate').text(new Date(movie.startDate).toLocaleDateString());
        $('#movie-type').text(movie.type);
        $('#movie-country').text(movie.country);
        $('#movie-language').text(movie.language);
        $('#movie-director').text(movie.director);
        $('#movie-starring').text(movie.starring);
        $('#movie-writer').text(movie.screenWriter);
        $('#movie-length').text(movie.length);

        $("#movie-name-input").value = movie.name;
        $("#movie-date-input").value = movie.startDate;
        $("#movie-img-input").value = movie.posterUrl;
        $("#movie-description-input").value = movie.description;
        $("#movie-type-input").value = movie.type;
        $("#movie-length-input").value = movie.length;
        $("#movie-country-input").value = movie.country;
        $("#movie-language-input").value = movie.language;
        $("#movie-director-input").value = movie.director;
        $("#movie-writer-input").value = movie.screenWriter;
    }

    // user界面才有
    $('#like-btn').click(function () {
        var url = isLike ?'/movie/'+ movieId +'/unlike?userId='+ userId :'/movie/'+ movieId +'/like?userId='+ userId;
        postRequest(
             url,
            null,
            function (res) {
                 isLike = !isLike;
                getMovie();
            },
            function (error) {
                alert(error);
            });
    });

    // admin界面才有
    //alert('交给你们啦，修改时需要在对应html文件添加表单然后获取用户输入，提交给后端，别忘记对用户输入进行验证。（可参照添加电影&添加排片&修改排片）');
    $("#modify-btn").click(function () {
        // $("#movie-name-input").clean();
        // $("#movie-date-input").clean();
        // $("#movie-img-input").clean();
        // $("#movie-description-input").clean();
        // $("#movie-type-input").clean();
        // $("#movie-length-input").clean();
        // $("#movie-country-input").clean();
        // $("#movie-language-input").clean();
        // $("#movie-director-input").clean();
        // $("#movie-star-input").clean();
        // $("#movie-writer-input").clean();


        // $("#movie-name-input").val();
        // $("#movie-date-input").val();
        // $("#movie-img-input").val();
        // $("#movie-description-input").val();
        // $("#movie-type-input").val();
        // $("#movie-length-input").val();
        // $("#movie-country-input").val();
        // $("#movie-language-input").val();
        // $("#movie-director-input").val();
        // $("#movie-star-input").val();
        // $("#movie-writer-input").val();



    });
    /**
     * 确认修改后提交
     */

    $("#movie-form-btn").click(function(){
        $("#scheduleModal").modal("hide")
        var id = $("#movie-id").text();
        var name = $("#movie-name-input").val();
        var date =  $("#movie-date-input").val();
        var img = $("#movie-img-input").val();
        var description =  $("#movie-description-input").val();
        var type1 =  $("#movie-type-input").val();
        var length =  $("#movie-length-input").val();
        var country =  $("#movie-country-input").val();
        var language =  $("#movie-language-input").val();
        var director =  $("#movie-director-input").val();
        var staring =  $("#movie-star-input").val();
        var screenWriter =  $("#movie-writer-input").val();
        var data = {
            id:parseInt(id),
            name:name,
            postUrl:img,
            description:description,
            language:language,
            country:country,
            type:type1,
            length:length,
            screenWriter:screenWriter,
            director:director,
            startDate:date,
            starring:staring,
        }

        postRequest(
            "/movie/update",
            data,
            function(res){
                getMovie();
                alert("修改成功！")

            },
            function (error) {
                alert(error)

            }
        )


    });
    /**
     * 确认删除后删除
     */
    $("#movie-delete-btn").click(function () {
        var filmToOff0 =$("#movie-id").text()
        console.log(filmToOff0)
        var filmToOff = parseInt(filmToOff0)
        var list123=[]
        list123.push(filmToOff)
        var batchOffMovie={movieIdList:list123}
        $("#deleteModal").modal("hide")
        console.log(batchOffMovie)
        postRequest(
            "/movie/off/batch",
            batchOffMovie,
            function(res) {
                alert("成功下架电影！")
            },
            function(error){
                alert(error)
            }
        )
        //alert('交给你们啦，下架别忘记需要一个确认提示框，也别忘记下架之后要对用户有所提示哦');
    });

});
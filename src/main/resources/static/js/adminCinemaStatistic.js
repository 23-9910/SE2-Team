$(document).ready(function() {
    var aList = [];

    getScheduleRate();
    
    getBoxOffice();

    getAudiencePrice();

    function getScheduleRate() {

        getRequest(
            '/statistics/scheduleRate',
            function (res) {
                var data = res.content||[];
                var tableData = data.map(function (item) {
                   return {
                       value: item.time,
                       name: item.name
                   };
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title : {
                        text: '今日排片率',
                        subtext: new Date().toLocaleDateString(),
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        x : 'center',
                        y : 'bottom',
                        data:nameList
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true},
                            dataView : {show: true, readOnly: false},
                            magicType : {
                                show: true,
                                type: ['pie', 'funnel']
                            },
                            restore : {show: true},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    series : [
                        {
                            name:'面积模式',
                            type:'pie',
                            radius : [30, 110],
                            center : ['50%', '50%'],
                            roseType : 'area',
                            data:tableData
                        }
                    ]
                };
                var scheduleRateChart = echarts.init($("#schedule-rate-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    function getBoxOffice() {

        getRequest(
            '/statistics/boxOffice/total',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.boxOffice;
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title : {
                        text: '所有电影票房',
                        subtext: '截止至'+new Date().toLocaleDateString(),
                        x:'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'bar'
                    }]
                };
                var scheduleRateChart = echarts.init($("#box-office-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }

    function getAudiencePrice() {
        getRequest(
            '/statistics/audience/price',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.price;
                });
                var nameList = data.map(function (item) {
                    return formatDate(new Date(item.date));
                });
                var option = {
                    title : {
                        text: '每日客单价',
                        x:'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'line'
                    }]
                };
                var scheduleRateChart = echarts.init($("#audience-price-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }

});

function getPlacingRate() {
    //     // todo
    //     // 传过来得是一个float数字
    //     var date = getDate();
    //     getRequest(
    //         'statistics/PlacingRate?date=' + date,
    //         function(res) {
    //             var data = res.content || 0;
    //             var option = {
    //                 title : {
    //                     text: '上座率',
    //                     subtext: date,
    //                     x:'center'
    //                 },
    //                 legend: {
    //                     orient : 'vertical',
    //                     x : 'left',
    //                     data:['上座率','未上座率']
    //                 },
    //                 calculable : true,
    //                 toolbox: {
    //                     show : true,
    //                     feature : {
    //                         mark : {show: true},
    //                         dataView : {show: true, readOnly: false},
    //                         magicType : {
    //                             show: true,
    //                             type: ['pie', 'funnel'],
    //                             option: {
    //                                 funnel: {
    //                                     x: '25%',
    //                                     width: '50%',
    //                                     funnelAlign: 'left',
    //                                     max: 1548
    //                                 }
    //                             }
    //                         },
    //                         restore : {show: true},
    //                         saveAsImage : {show: true}
    //                     }
    //                 },
    //                 series : [
    //                     {
    //                         name:'上座率',
    //                         type:'pie',
    //                         radius : '55%',
    //                         center: ['50%', '60%'],
    //                         data:[
    //                             {value:data, name:'上座率'},
    //                             {value:(1-data), name:'未上座率'},
    //                         ]
    //                     }
    //                 ]
    //
    //             };
    //             var placingRateChart = echarts.init($('#place-rate-container')[0]);
    //             placingRateChart.setOption(option);
    //         },
    //         function(error){
    //             alert(JSON.stringify(error));
    //         }
    //     );
    var date = getDate().toString();
    console.log(date);
    getRequest(
        '/statistics/placingRate?date=' + date,
        function (res) {
            var data = res.content||[];
            var tableData = data.map(function (item) {
                return item.placingRate;
            });
            var nameList = data.map(function (item) {
                return item.movieName;
            });
            var option= {
                title: {
                    text: '所有电影的上座率',
                    subtext: '日期:' + date,
                    x:'center'
                },
                xAxis:{
                    type:'category',
                    data:nameList
                },
                yAxis:{
                    type:'value'
                },
                series:[{
                    data: tableData,
                    type: 'bar'
                }]
            };
            var scheduleRateChart = echarts.init($("#place-rate-container")[0]);
            scheduleRateChart.setOption(option);
        }

    )
}

function getPopularMovie() {
    // // todo
    // var days = getDays();
    // var movieNum = getNums();
    // var rankBox = []; //传过来的是movieName的排名，我们要找出对应得box，来定量体现其排序方式。
    // getRequest(
    //     'statistics/popular/movie?days=' + days + '&movieNum=' + movieNum,
    //     function(res) {
    //         var movieNameList = res.content || [];
    //         for(var x = 1; x < movieNameList.length; x = x + 1){
    //             rankBox.push[x + 1];
    //         }
    //         var option = {
    //             title:{
    //                 text: days + '天内最受欢迎的' + movieNum + '部电影',
    //                 x: 'center'
    //             },
    //             xAxis:{
    //                 type: 'category',
    //                 data: movieNameList
    //             },
    //             yAxis:{
    //                 type: 'value',
    //
    //             },
    //             series:[{
    //                 data:rankBox,
    //                 type:'bar'
    //             }]
    //         };
    //         var popularMovieChart = echarts.init($('#popular-movie-container')[0]);
    //         popularMovieChart.setOption(option);
    //     },
    //     function(error){
    //         alert(JSON.stringify(error));
    //     }
    // );
    var days = parseInt(getDays());
    var movieNum = parseInt(getNums());
    getRequest(
        '/statistics/popular/movie?days=' + days + '&movieNum=' + movieNum,
        function (res) {
            var data = res.content||[];
            var tableData = data.map(function (item) {
                return item.box;
            });
            var nameList = data.map(function (item) {
                return item.movieName;
            });
            var option= {
                title: {
                    text: days + '天内最受欢迎的' + movieNum + '部电影及其票房',
                    x:'center'
                },
                xAxis:{
                    type:'category',
                    data:nameList
                },
                yAxis:{
                    type:'value'
                },
                series:[{
                    data: tableData,
                    type: 'bar'
                }]
            };
            var scheduleRateChart = echarts.init($("#popular-movie-container")[0]);
            scheduleRateChart.setOption(option);
        }
    )

}

function getDate() {
    return $('#placing-rate-date').val();
}

function getDays(){
    return $('#popular-movie-days').val();
}

function getNums(){
    return $('#popular-movie-num').val();
}



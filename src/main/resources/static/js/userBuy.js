$(document).ready(function () {
    getMovieList();

    function getMovieList() {
        getRequest(
            '/ticket/get/' + sessionStorage.getItem('id'),
            function (res) {
                renderTicketList(res.content);
            },
            function (error) {
                alert(error);
            });
    }


    // TODO:填空
    function renderTicketList(list) {
        //先获取一张票：TicketWithScheduleVO
        for(var i = 0;i < list.length;i++){
            var ticketStr = "";
            var ticketWithScheduleVO = list[i];
            var schedule = ticketWithScheduleVO.schedule;

            var movieName = schedule.movieName;
            var hallName = schedule.hallName;
            var startTime = schedule.startTime;
            var endTime = schedule.endTime;
            var row = ticketWithScheduleVO.rowIndex;
            var column = ticketWithScheduleVO.columnIndex;
            var stateStr = ticketWithScheduleVO.state;

            ticketStr += '<tr>'
                + '<th>' + movieName + '</th>'
                + '<th>' + hallName + '</th>'
                + '<th>' + (row + 1) + '排' + (column + 1) + '座' + '</th>'
                + '<th>' + startTime.slice(0,10) + " " + startTime.slice(11,19) + '</th>'
                + '<th>' + endTime.slice(0,10) + " " + endTime.slice(11,19) + '</th>'
                + '<th>' + stateStr + '</th>'
                + '</tr>';
            if(stateStr !== "未完成"){


                $('#my-tickets-table-body').append(ticketStr);
            }
        }
    }
});
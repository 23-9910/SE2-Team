$(document).ready(function () {
    getRefundList();

    function getRefundList() {
        getRequest(
            '/ticket/get/user/record/'+sessionStorage.getItem('id'),
            function(res){
                let consumingRecordList = res.content;
                console.log(consumingRecordList);
                let refundList = [];
                consumingRecordList.forEach(function (cr) {
                    getRequest(
                        '/ticket/get/consumed/'+cr.id,
                        function (newRes) {
                            let ticket = newRes.content;
                            console.log(ticket);
                            refundList.push(ticket);
                        },
                        function (newError) {
                            alert(newError);
                        }
                    )
                })
                console.log(refundList);
                renderRefundTicket(refundList);

            },
            function (error) {
                alert(error);
            }
        )
    }

    function renderRefundTicket(list) {
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


                $('#refund-tickets-table-body').append(ticketStr);
            }
        }
    }
})
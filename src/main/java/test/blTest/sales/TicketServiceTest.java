package test.blTest.sales;

import com.example.cinema.CinemaApplication;
import com.example.cinema.bl.management.ScheduleService;
import com.example.cinema.bl.promotion.CouponService;
import com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.po.ConsumingRecord;
import com.example.cinema.po.Ticket;
import com.example.cinema.po.VIPCard;
import com.example.cinema.vo.SeatForm;
import com.example.cinema.vo.TicketForm;
import com.example.cinema.vo.TicketWithScheduleVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import com.example.cinema.bl.sales.TicketService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun on 2019/06/21
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CinemaApplication.class)
public class TicketServiceTest {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private VIPCardMapper vipCardMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Test
    public void buyTicketByVIPCard(){
        //Test add tickets
        TicketForm ticketForm = new TicketForm();
        ticketForm.setUserId(12);
        ticketForm.setScheduleId(71);

        List<SeatForm> seatForms = new ArrayList<>();
        SeatForm seat1 = new SeatForm();
        seat1.setRowIndex(2);
        seat1.setColumnIndex(3);
        seatForms.add(seat1);

        ticketForm.setSeats(seatForms);
        ticketService.addTicket(ticketForm);
        List<TicketWithScheduleVO> tickets = (List<TicketWithScheduleVO>)
                ticketService.getTicketByUser(12).getContent();
        TicketWithScheduleVO ticketWithScheduleVO = tickets.get(tickets.size()-1);
        Assert.assertNotEquals(0,ticketWithScheduleVO.getId());

        //Test buy tickets
        List<Integer> ticketId = new ArrayList<>();
        ticketId.add(ticketWithScheduleVO.getId());
        Double payment = scheduleMapper.selectScheduleById(71).getFare() - couponMapper.selectById(9).getDiscountAmount();
        Double newBalance = vipCardMapper.selectCardByUserId(12).getBalance() - payment;

        ticketService.completeByVIPCard(ticketId,9);
        Assert.assertEquals(vipCardMapper.selectCardByUserId(12).getBalance(), newBalance,0.05);

        //Test add consumingRecord
        List<ConsumingRecord> records = (List<ConsumingRecord>)
                ticketService.getConsumingRecordByUserId(12).getContent();
        ConsumingRecord lastRecord = records.get(records.size()-1);
        Assert.assertNotNull(lastRecord.getId());
        Assert.assertEquals(1,lastRecord.getTicketAmount());
        Assert.assertEquals(12,lastRecord.getUserId());
        Assert.assertEquals(71,lastRecord.getScheduleId());

    }
}
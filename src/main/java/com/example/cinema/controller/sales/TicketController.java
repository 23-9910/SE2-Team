package com.example.cinema.controller.sales;

import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.TicketForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by liying on 2019/4/16.
 */
@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    TicketService ticketService;

    @PostMapping("/get/withCoupon")
    public ResponseVO getTicketWithCoupon(@RequestBody List<Integer> TicketId,@RequestParam int userId){
        return ticketService.getTicketWithCoupon(TicketId,userId);}

    @PostMapping("/vip/buy")
    public ResponseVO buyTicketByVIPCard(@RequestBody List<Integer> ticketId, @RequestParam int couponId){
        return ticketService.completeByVIPCard(ticketId,couponId);
    }

    @PostMapping("/lockSeat")
    public ResponseVO lockSeat(@RequestBody TicketForm ticketForm){
        return ticketService.addTicket(ticketForm);
    }

    @PostMapping("/buy")
    public ResponseVO buyTicket(@RequestBody List<Integer> ticketId,@RequestParam int couponId){
        return ticketService.completeTicket(ticketId,couponId);
    }
    @GetMapping("/get/{userId}")
    public ResponseVO getTicketByUserId(@PathVariable int userId){
        return ticketService.getTicketByUser(userId);
    }

    @GetMapping("/get/occupiedSeats")
    public ResponseVO getOccupiedSeats(@RequestParam int scheduleId){
        return ticketService.getBySchedule(scheduleId);
    }

    @PostMapping("/cancel")
    public ResponseVO cancelTicket(@RequestParam List<Integer> ticketId){
        return ticketService.cancelTicket(ticketId);
    }

    /**
     * Modified by sun on 2019/05/28
     * 2nd modified by sun on 2019/06/08: 购票方法 buyTicket & buyTicketByVIPTicket 自动添加消费记录
     */

    @PostMapping("add/recordId")
    public ResponseVO addRecordIdOnTicket(@RequestBody List<Integer> ticketId){
        return ticketService.addRecordIdOnTicket(ticketId);
    }

    @GetMapping("/get/user/record/{userId}")
    public ResponseVO getConsumingRecordByUserId(@PathVariable int userId){
        return ticketService.getConsumingRecordByUserId(userId);
    }

    @GetMapping("/get/record/{id}")
    public ResponseVO getConsumingRecordById(@PathVariable int id){
        return ticketService.getConsumingRecordById(id);
    }

    @PostMapping("/refund")
    public ResponseVO refundTicket(@RequestParam int ticketId){
        return ticketService.refundTicket(ticketId);
    }

    @PostMapping("/refund/vip")
    public ResponseVO refundTicketToVIPCard(@PathVariable int ticketId){
        return ticketService.refundTicketToVIPCard(ticketId);
    }

    @GetMapping("/get/consumed/{consumingRecordId}")
    public ResponseVO getTicketsByConsumingRecord(@PathVariable int consumingRecordId){
        return ticketService.getTicketByConsumingRecord(consumingRecordId);
    }
    /**
     * By jyt
     */
    @PostMapping("/return")
    public ResponseVO returnTickets(@RequestBody List<Integer> ticketId){
        return ticketService.returnTickets(ticketId);
    }
    @PostMapping("/discount")
    public ResponseVO changeDiscount(@RequestBody double discount){
        return ticketService.changeDiscount(discount);
    }

}

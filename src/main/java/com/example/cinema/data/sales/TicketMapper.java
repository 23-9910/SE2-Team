package com.example.cinema.data.sales;

import com.example.cinema.po.ConsumingRecord;
import com.example.cinema.po.RefundStrategy;
import com.example.cinema.po.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Created by liying on 2019/4/16.
 */
@Mapper
public interface TicketMapper {

    /**
     * 插入一张电影票
     * @param ticket
     * @return
     */
    int insertTicket(Ticket ticket);

    /**
     * 插入电影票列表
     * @param tickets
     * @return
     */
    int insertTickets(List<Ticket> tickets);

    /**
     * 删除一张票
     * @param ticketId
     */
    void deleteTicket(int ticketId);

    /**
     * 更新电影票状态
     * @param ticketId
     * @param state
     */
    void updateTicketState(@Param("ticketId") int ticketId, @Param("state") int state);

    /**
     * 通过排片id查询电影票
     * @param scheduleId
     * @return
     */
    List<Ticket> selectTicketsBySchedule(int scheduleId);

    /**
     * 通过排片id和座位信息查找电影票
     * @param scheduleId
     * @param columnIndex
     * @param rowIndex
     * @return
     */
    Ticket selectTicketByScheduleIdAndSeat(@Param("scheduleId") int scheduleId, @Param("column") int columnIndex, @Param("row") int rowIndex);

    /**
     * 通过id查找电影票
     * @param id
     * @return
     */
    Ticket selectTicketById(int id);

    /**
     * 通过userId查找电影票
     * @param userId
     * @return
     */
    List<Ticket> selectTicketByUser(int userId);

    /**
     * 清除无效电影票
     */
    @Scheduled(cron = "0/1 * * * * ?")
    void cleanExpiredTicket();

    /**
     * Modified by sun on 2019/05/29
     * 插入一个消费记录
     */
    int insertOneConsumingRecord(ConsumingRecord consumingRecord);

    /**
     * 通过userId查找消费记录
     * @param userId
     * @return
     */
    List<ConsumingRecord> selectConsumingRecordByUser(int userId);

    /**
     * 通过id查找消费记录
     * @param id
     * @return
     */
    ConsumingRecord selectConsumingRecordById(int id);

    /**
     * 通过消费记录查找电影票
     * @param consumingRecordId
     * @return
     */
    List<Ticket> selectTicketsByConsumingRecord(int consumingRecordId);

    /**
     * 更新电影票信息中的记录id
     * @param recordId
     * @param ticketId
     */
    void updateRecordId(@Param("recordId") int recordId, @Param("ticketId") int ticketId);

    /**
     * 更新退票策略
     */
    void updateRefundStrategy(int strategy);

    /**
     * 获得退票策略
     * @return
     */
    RefundStrategy selectRefundStrategy();

}


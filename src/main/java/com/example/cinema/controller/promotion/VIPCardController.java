package com.example.cinema.controller.promotion;

import com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.vo.VIPCardForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liying on 2019/4/14.
 */
@RestController()
@RequestMapping("/vip")
public class VIPCardController {
    @Autowired
    VIPService vipService;

    @PostMapping("/add")
    public ResponseVO addVIP(@RequestParam int userId){
        return vipService.addVIPCard(userId);
    }
    @GetMapping("{userId}/get")
    public ResponseVO getVIP(@PathVariable int userId){
        return vipService.getCardByUserId(userId);
    }

    @GetMapping("/getVIPInfo")
    public ResponseVO getVIPInfo(){
        return vipService.getVIPInfo();
    }

    @PostMapping("/charge")
    public ResponseVO charge(@RequestBody VIPCardForm vipCardForm){
        return vipService.charge(vipCardForm);
    }

    /**
     * Created by sun on 2019/05/28
     */
    @PostMapping("/add/record")
    public ResponseVO addOneChargeRecord(@RequestBody VIPCardForm vipCardForm){
        return vipService.addChargeRecord(vipCardForm);
    }

    @GetMapping("{userId}/get/record")
    public ResponseVO getChargeRecordByUserId(@PathVariable int userId){
        return vipService.getChargeRecordByUserId(userId);
    }

    @PostMapping("record")
    public ResponseVO getChargeRecordById(@RequestParam int id){
        return vipService.getChargeRecordById(id);
    }
    //。。。
    @PostMapping("/description")
    public ResponseVO addVIPDESCRIPTION(@RequestBody String description){
        return vipService.changeDescription(description);
    }
}

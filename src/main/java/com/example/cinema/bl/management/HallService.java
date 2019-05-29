package com.example.cinema.bl.management;

import com.example.cinema.vo.HallVO;
import com.example.cinema.vo.ResponseVO;

/**
 * @author fjj
 * @date 2019/4/12 2:01 PM
 */
public interface HallService {
    /**
     * 搜索所有影厅
     * @return
     */
    ResponseVO searchAllHall();

    /**
     * 添加影厅(影厅信息录入)
     * By sun on 2019/05/28
     */
    ResponseVO addHall(HallVO hallVO);

    /**
     * 修改影厅信息
     * By sun on 2019/05/28
     */
    ResponseVO updateHall(HallVO hallVO);
}

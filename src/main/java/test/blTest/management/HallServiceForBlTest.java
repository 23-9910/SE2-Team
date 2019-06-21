package test.blTest.management;

import com.example.cinema.CinemaApplication;
import com.example.cinema.bl.management.HallService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.po.Hall;
import com.example.cinema.vo.HallVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by sun on 2019/06/21
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CinemaApplication.class)
public class HallServiceForBlTest {
    @Autowired
    private HallServiceForBl hallService1;
    @Autowired
    private HallService hallService2;

    @Test
    public void getHallById(){
        //Get Hall
        Hall hall = hallService1.getHallById(3);
        Assert.assertNotNull(hall);
    }

    @Test
    public void insertOneHall(){
        //Insert Hall
        HallVO hall = new HallVO();
        hall.setName("sss");
        hall.setRow(3);
        hall.setColumn(4);
        hallService2.addHall(hall);
        Assert.assertNotNull(hall.getId());
    }

    @Test
    public void deleteOneHall(){
        //Delete Hall
        hallService2.deleteHall(3);
        Assert.assertNull(hallService1.getHallById(3).getId());
    }
}
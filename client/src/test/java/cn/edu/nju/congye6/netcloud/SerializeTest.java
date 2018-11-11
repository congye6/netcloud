package cn.edu.nju.congye6.netcloud;

import cn.edu.nju.congye6.netcloud.service.User;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by cong on 2018-11-11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SerializeTest {

    @Test
    public void decode(){
        User user=new User();
        user.setUserName("cc");
        byte[] data= JSONObject.toJSONBytes(user);
        user=JSONObject.parseObject(data,User.class);
        System.out.println(user.getUserName());
    }

}

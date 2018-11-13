package cn.edu.nju.congye6.netcloud.util;

import java.util.UUID;

/**
 * 生成全局唯一id
 * Created by cong on 2018-11-13.
 */
public class UuidUtil {

    public static String uuid(){
        return UUID.randomUUID().toString();
    }

}

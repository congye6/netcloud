package cn.edu.nju.congye6.netcloud.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cong on 2018-12-16.
 */
public class SleepUtil {

    private static final Logger LOGGER= LoggerFactory.getLogger(SleepUtil.class);

    /**
     * 使线程睡眠
     * @param millis 睡眠的毫秒数
     */
    public static void sleep(int millis){
        try {
            LOGGER.info("Thread sleeping,millis:"+millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("thread sleep error",e);
        }
    }

}

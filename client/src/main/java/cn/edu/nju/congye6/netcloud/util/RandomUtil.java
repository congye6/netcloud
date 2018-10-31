package cn.edu.nju.congye6.netcloud.util;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by cong on 2018-02-11.
 */
public class RandomUtil {

    private static Random randomGenerator=new Random();

    /**
     * 生成小于bound的随机数
     * @param bound
     * @return
     */
    public static int randomNumber(int bound){
        return randomGenerator.nextInt(bound);
    }

    public static void main(String[] args){
        for(int i=0;i<100;i++){
            System.out.println(RandomUtil.randomNumber(10));
        }
    }

}

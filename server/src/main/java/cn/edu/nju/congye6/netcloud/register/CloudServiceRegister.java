package cn.edu.nju.congye6.netcloud.register;

import javax.annotation.PostConstruct;

public class CloudServiceRegister{

    public void register(){
        Thread registerThread=new Thread(new RegisterTask());
        registerThread.start();
    }
}
package com.lana.cc.device.user.manager.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {
    public static void main(String[] args) {
        String json = "{\"meta\":{\"code\":1000,\"msg\":\"SUCCESS\"},\"data\":{\"uin\":20057,\"openId\":\"9a3b00bda891c4a2139bc123a9b5bc29\",\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJSQkNfVE9LRU4iLCJpc3MiOiJSQkNfU1ZSIiwidWluIjoiMjAwNTciLCJleHAiOjE1ODM1ODU2Mjl9.VqcXR3kX0OVhZYNKX6oLswLPCaXU-YYYRBM6kGdQ_Uo\",\"profileCompletion\":78,\"usrProfile\":{\"name\":\"Zhangyf\",\"avatar\":\"LeoWong@upuphub.com/1576747438277\",\"country\":\"中国\",\"province\":\"四川\",\"city\":\"成都\",\"district\":\"高新区\",\"street\":\"天府五街\",\"age\":24,\"birthday\":820429261000,\"profession\":\"土拨鼠\",\"gender\":\"男\",\"signature\":\"我啥子都没有，只\",\"level\":1,\"aboutMe\":\"哈哈哈\",\"school\":\"\",\"company\":\"\",\"degree\":\"\",\"language\":\"\",\"backgroundImage\":\"http://n.sinaimg.cn/news/1_img/dfic/6d34f853/243/w819h1024/20191006/eb32-ifmectm7430912.jpg\"},\"usrSetting\":{\"notification\":true,\"voiceNotification\":true,\"theme\":\"default\"},\"usrStatusFlag\":{\"emailVerifiedFlag\":true,\"disableFlag\":false,\"needMoreInfoFlag\":false},\"lastLoginInfo\":{\"device\":\"10.04(ertgertg)\",\"lastLoginTime\":\"2020-02-21 20:22:42\"}}}";

        Pattern patter = Pattern.compile("\"code\":(.*)\\,\"msg\":");
        Matcher m = patter.matcher(json);

    }
}

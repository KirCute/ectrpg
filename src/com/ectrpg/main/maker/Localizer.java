package com.ectrpg.main.maker;

import org.frice.util.data.Database;
import org.frice.util.data.Preference;

public final class Localizer {
    public static void main(String[] args) {
        String lang = "zh_CN";
        //String lang = "en_US";

        Database langFile = new Preference("resources/assets/lang/" + lang + ".lang");

        ///*
        langFile.insert("title.title", "ectrpg测试版本");
        langFile.insert("title.startGame", "开始游戏");
        langFile.insert("test.entity.prs", "改变Progress ID到0");
        langFile.insert("test.entity.faq", "你♂好");
        langFile.insert("test.entity.buff", "给我10s加速Buff");
        langFile.insert("test.entity.hello1", "我叫Van♂，我是一个艺术家。");
        langFile.insert("test.entity.hello2", "我是一个行♂为♂艺♂术家");
        langFile.insert("test.entity.title", "测试。。。。。。。。。。");
        //*/

        /*
        langFile.insert("title.title", "ectrpg Test");
        langFile.insert("title.startGame", "Play");
        langFile.insert("test.entity.prs", "Change the Progress ID as 0");
        langFile.insert("test.entity.faq", "Hello ♂");
        langFile.insert("test.entity.buff", "Give me 10s Speed Buff");
        langFile.insert("test.entity.hello1","My name is Van. I'm an artist." );
        langFile.insert("test.entity.hello2", "I'm a performance artist.");
        langFile.insert("test.entity.title", "Test.............");
        */
    }
}

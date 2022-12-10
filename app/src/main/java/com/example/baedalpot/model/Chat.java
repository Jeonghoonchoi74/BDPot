package com.example.baedalpot.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Chat {
    @Exclude

    public String massage;
    public String nickname;
    public String mg;
    public Chat(){

    }
    public Chat(String massage, String nickname, String mg){
        this.massage = massage;
        this.nickname = nickname;
        this.mg = mg;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMg() {
        return mg;
    }

    public void setMg(String mg) {
        this.mg = mg;
    }
}

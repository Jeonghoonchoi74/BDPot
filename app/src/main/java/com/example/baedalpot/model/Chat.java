package com.example.baedalpot.model;

public class Chat {
    public String massage;
    public String UserName;
    public String GroupKey;
    public Chat(){

    }
    public Chat(String massage, String userName, String groupKey){

    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getGroupKey() {
        return GroupKey;
    }

    public void setGroupKey(String groupKey) {
        GroupKey = groupKey;
    }
}

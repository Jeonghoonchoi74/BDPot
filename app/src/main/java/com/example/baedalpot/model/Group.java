package com.example.baedalpot.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Group {
    @Exclude

    // 작성자 UID
    public String writer;
    public String title;
    public String restaurant;
    public String category;
    public int maxPrice;
    public int maxPerson;
    public String destination;
    public long timestamp;
    public long timestampComplement;
    public String key;
    public ArrayList<String> userlist;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(int maxPerson) {
        this.maxPerson = maxPerson;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Group() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getUserlist() {
        return userlist;
    }

    public void setUserlist(ArrayList<String> userlist) {
        this.userlist = userlist;
    }

    public Group(String writer, String title, String restaurant, String category, int maxPrice, int maxPerson, String destination, String key, ArrayList<String> userlist) {
        this.writer = writer;
        this.title = title;
        this.restaurant = restaurant;
        this.category = category;
        this.maxPrice = maxPrice;
        this.maxPerson = maxPerson;
        this.destination = destination;
        this.timestamp = new Date().getTime();
        this.timestampComplement = timestamp * -1;
        this.userlist = userlist;
        this.key = key;
    }
}

package com.example.baedalpot.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Group {
    @Exclude
    public String key;

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

    public Group() {
    }

    public Group(String writer, String title, String restaurant, String category, int maxPrice, int maxPerson, String destination) {
        this.writer = writer;
        this.title = title;
        this.restaurant = restaurant;
        this.category = category;
        this.maxPrice = maxPrice;
        this.maxPerson = maxPerson;
        this.destination = destination;
        this.timestamp = new Date().getTime();
        this.timestampComplement = timestamp * -1;
    }
}

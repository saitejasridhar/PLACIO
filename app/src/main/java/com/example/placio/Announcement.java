package com.example.placio;

public class Announcement {

    String Name;
    String Description;

    Announcement(){}

    Announcement(String Name, String Description){
        this.Name=Name;
       this.Description= Description;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }
}

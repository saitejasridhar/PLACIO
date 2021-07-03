package com.example.placio;

import java.util.List;

public class Announcement {

    String Name;
    String Description;
    private List<String> Branch;
    String DateTime;


    Announcement(){}

    Announcement(String Name, String Description,List<String> Branch,  String DateTime){
        this.Name=Name;
       this.Description= Description;
       this.Branch=Branch;
       this.DateTime=DateTime;
    }

    public List<String> getBranch() {
        return Branch;
    }

    public String getDateTime() {
        return DateTime;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }
}

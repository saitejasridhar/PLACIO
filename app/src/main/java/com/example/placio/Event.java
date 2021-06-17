package com.example.placio;

public class Event {

    String description;
    String type;
    String date;
    String companyname;
    String companyid;
    String time;

    Event(){}

    Event( String type, String date, String description,String companyid,String companyname,String time){
        this.date=date;
        this.type=type;
        this.description=description;
        this.companyid=companyid;
        this.companyname=companyname;
        this.time=time;
    }

    public String getDescription() {
        return description;
    }

    public String getCompanyname() {
        return companyname;
    }

    public String getCompanyid() {
        return companyid;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}

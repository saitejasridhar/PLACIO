package com.example.placio;

public class Ticket {

    private String Subject;
    private String Description;
    private String Date;
    private String closedon;
    private String status;
    private String UID;

    Ticket(){}

    Ticket( String Subject,String Description,String Date, String closedon, String status,String UID){
        this.Subject=Subject;
        this.Date=Date;
        this.Description=Description;
        this.closedon=closedon;
        this.status=status;
        this.UID=UID;
    }

    public String getSubject() {
        return Subject;
    }

    public String getDescription() {
        return Description;
    }

    public String getDate() {
        return Date;
    }

    public String getClosedon() {
        return closedon;
    }

    public String getUID() {
        return UID;
    }

    public String getStatus() {
        return status;
    }
}

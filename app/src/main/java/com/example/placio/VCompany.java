package com.example.placio;

import android.location.LocationListener;

import java.util.List;
import java.util.Map;

public class VCompany {
    private List<String> AppliedStudents;
    private String Name;
    private Long Ctc;
    private List<String> Roles;
    private List<String> Branch;
    private int CLBacklog;
    private int Backlog;
    private List<String> Batches;
    private String Breakdown;
    private int Cgpa;
    private String Date;
    private String Description;
    private String Location;
    private String Offer;
    private List<String> Skills;
    private int Tenth;
    private int Twelfth;
    private String Tier;
    private List<String> Placed;
    private String isHistory;
    private String AllowEdit;
    private List<String> Rejected;
    private List<String> InProgress;


    public VCompany(){}

    public VCompany(String Name,Long Ctc,List<String> Roles,List<String> Branch,int Backlog,int CLBacklog,List<String> Batches,String Breakdown,int Cgpa,String Date
            ,String Description,String Location,String Offer,List<String> Skills,int Twelfth,int Tenth,String TierList,List<String> AppliedStudents,List<String> Placed
              ,String isHistory,List<String> Rejected ,List<String> InProgress){
        this.Name=Name;
        this.Ctc=Ctc;
        this.Backlog=Backlog;
        this.CLBacklog=CLBacklog;
        this.Roles=Roles;
        this.Branch=Branch;
        this.Batches=Batches;
        this.Tenth=Tenth;
        this.Twelfth=Twelfth;
        this.Skills=Skills;
        this.Description=Description;
        this.Skills=Skills;
        this.Location= Location;
        this.Offer=Offer;
        this.Breakdown=Breakdown;
        this.Tier=Tier;
        this.Cgpa=Cgpa;
        this.Date=Date;
        this.AppliedStudents=AppliedStudents;
        this.Placed=Placed;
        this.isHistory=isHistory;
        this.AllowEdit=AllowEdit;
        this.Rejected=Rejected;
        this.InProgress=InProgress;
    }

    public String getName() {
        return Name;
    }

    public Long getCtc() {
        return Ctc;
    }

    public List<String> getRoles() {
        return Roles;
    }

    public List<String> getBranch() {
        return Branch;
    }

    public int getBacklog() {
        return Backlog;
    }

    public int getCLBacklog() {
        return CLBacklog;
    }

    public List<String> getAppliedStudents() {
        return AppliedStudents;
    }

    public List<String> getBatches() {
        return Batches;
    }

    public String getBreakdown() {
        return Breakdown;
    }

    public int getCgpa() {
        return Cgpa;
    }

    public String getDate() {
        return Date;
    }

    public String getDescription() {
        return Description;
    }

    public String getLocation() {
        return Location;
    }

    public String getOffer() {
        return Offer;
    }

    public List<String> getSkills() {
        return Skills;
    }

    public int getTenth() {
        return Tenth;
    }
//
    public int getTwelfth() {
        return Twelfth;
    }

    public String getTier() {
        return Tier;
    }

    public List<String> getPlaced() {
        return Placed;
    }

    public String getIsHistory() {
        return isHistory;
    }

    public String getAllowEdit() {
        return AllowEdit;
    }

    public List<String> getRejected() {
        return Rejected;
    }

    public List<String> getInProgress() {
        return InProgress;
    }
}

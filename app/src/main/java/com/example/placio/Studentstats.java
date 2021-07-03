package com.example.placio;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.internal.Util;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Studentstats extends AppCompatActivity {


    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    int totalcompanies=0;
    int urcompanies=0;

    int totalstudents=0;
    int urbranchplacedstudents=0;

    PieChart pieChart1;
    PieData pieData1;
    PieDataSet pieDataSet1;
    ArrayList pieEntries1;

    FirebaseFirestore firestore;
    int coreappsint=0,dreamappsint=0,totalappsint=0,massappsint=0;
    Button back;
    TextView count1,count2,count3,count4,count5,skill1,skill2,skill3,skill4,skill5;
    TextView comp1,comp2,comp3,comp4,comp5,pack5,pack1,pack2,pack3,pack4;
    int dream=0,mass=0,core=0,urdream=0,urmass=0,urcore=0;
    TextView massper,masseli,masstot,coreper,coretot,coreeli,dreamper,dreamtot,dreameli;
    TextView urcgpa,dreamcgpa,corecgpa,masscgpa,dreamavg,coreavg,massavg;
    TextView avgpack,placedper,urbranch,allbranchcomp;
    TextView dreamaccper,coreaccper,massaccper;

    ProgressBar progbar;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentstats);
        progbar = (ProgressBar) findViewById(R.id.progbar);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        progbar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);


        final LinearLayout outLL = findViewById(R.id.outLL);
        final LinearLayout chartLL = findViewById(R.id.chartLL);
//        ((ViewGroup) chartLL.getParent()).removeView(chartLL);

        final LinearLayout outLL1 = findViewById(R.id.outLL1);
        final LinearLayout chartLL1 = findViewById(R.id.chartLL1);
        ((ViewGroup) chartLL1.getParent()).removeView(chartLL1);
        getSupportActionBar().hide();

        dreamaccper=findViewById(R.id.dreamaccper);
        coreaccper=findViewById(R.id.coreaccper);
        massaccper=findViewById(R.id.massaccper);

        avgpack=findViewById(R.id.avgpack);
        placedper=findViewById(R.id.placedper);
        urbranch=findViewById(R.id.urbranch);
        allbranchcomp=findViewById(R.id.allbranchcomps);

        skill1=findViewById(R.id.skill1);
        skill2=findViewById(R.id.skill2);
        skill3=findViewById(R.id.skill3);
        skill4=findViewById(R.id.skill4);
        skill5=findViewById(R.id.skill5);

        comp1=findViewById(R.id.comp1);
        comp2=findViewById(R.id.comp2);
        comp3=findViewById(R.id.comp3);
        comp4=findViewById(R.id.comp4);
        comp5=findViewById(R.id.comp5);

        pack1=findViewById(R.id.pack1);
        pack2=findViewById(R.id.pack2);
        pack3=findViewById(R.id.pack3);
        pack4=findViewById(R.id.pack4);
        pack5=findViewById(R.id.pack5);

        count1=findViewById(R.id.count1);
        count3=findViewById(R.id.count3);
        count2=findViewById(R.id.count2);
        count4=findViewById(R.id.count4);
        count5=findViewById(R.id.count5);

        masseli=findViewById(R.id.masseli);
        massper=findViewById(R.id.massper);
        masstot=findViewById(R.id.masstot);
        dreameli=findViewById(R.id.dreameli);
        dreamper=findViewById(R.id.dreamper);
        dreamtot=findViewById(R.id.dreamtot);
        coreeli=findViewById(R.id.coreeli);
        coreper=findViewById(R.id.coreper);
        coretot=findViewById(R.id.coretot);

        urcgpa=findViewById(R.id.urcgpa);
        dreamcgpa=findViewById(R.id.dreamcgpa);
        corecgpa=findViewById(R.id.corecgpa);
        masscgpa=findViewById(R.id.masscgpa);
        dreamavg=findViewById(R.id.dreamavg);
        coreavg=findViewById(R.id.coreavg);
        massavg=findViewById(R.id.massavg);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(Profile.class);
            }
        });

        TextView totalapps,dreamapps,coreapps,massapps;

        totalapps=findViewById(R.id.totalapps);
        coreapps=findViewById(R.id.coreapps);
        dreamapps=findViewById(R.id.dreamapps);
        massapps=findViewById(R.id.massapps);


        firestore = FirebaseFirestore.getInstance();
        String currentuser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DocumentReference docIdRef1 = firestore.collection("students").document(currentuser).collection("Details").document(currentuser);
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String app= document.get("Applied").toString();
                        String[] myArray = app.split(",");
                        urcgpa.setText(document.get("CGPA").toString());

                        String appstring= document.get("Applied").toString();
                        appstring= appstring.substring(1, appstring.length() - 1);
                        List<String> apc = Arrays.asList(appstring.split("\\s*,\\s*"));
                        if(appstring.equals("[]")){
                            totalappsint=0;
                        }
                        else {
                            totalappsint=apc.size();
                        }
                        Log.d("size",String.valueOf(apc));
                        if(!String.valueOf(apc).equals("[]")) {

                            for (String i : apc) {
                                DocumentReference docIdRef2 = firestore.collection("Companys").document(i);
                                docIdRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String tier = document.get("Tier").toString();

                                                if (tier.equals("Dream")) {
                                                    dreamappsint++;
                                                } else if (tier.equals("Core")) {
                                                    coreappsint++;
                                                } else {
                                                    massappsint++;
                                                }


                                                totalapps.setText(String.valueOf(totalappsint));
                                                dreamapps.setText(String.valueOf(dreamappsint));
                                                coreapps.setText(String.valueOf(coreappsint));
                                                massapps.setText(String.valueOf(massappsint));


                                                pieEntries = new ArrayList<>();
                                            pieEntries.add(new PieEntry(massappsint, 0));
                                            pieEntries.add(new PieEntry(coreappsint, 1));
                                            pieEntries.add(new PieEntry(dreamappsint, 2));
//
//                                                pieEntries.add(new PieEntry(12, 0));
//                                                pieEntries.add(new PieEntry(20, 1));
//                                                pieEntries.add(new PieEntry(45, 2));


//                                                outLL.addView(chartLL, 0);


                                                pieChart = findViewById(R.id.pieChart);
                                                pieDataSet = new PieDataSet(pieEntries, "");
                                                pieData = new PieData(pieDataSet);
                                                pieChart.setData(pieData);
                                                pieDataSet.setColors(Color.parseColor("#EC6B56"),
                                                        Color.parseColor("#47B39C"), Color.parseColor("#FFC154")
                                                );
                                                pieDataSet.setValueFormatter(new ValueFormatter() {
                                                    @Override
                                                    public String getFormattedValue(float value) {
                                                        return String.valueOf((int) Math.floor(value));
                                                    }
                                                });

                                                pieChart.setHoleRadius(65f);
                                                pieChart.setTransparentCircleRadius(65f);
                                                pieChart.getDescription().setEnabled(false);
                                                pieChart.getLegend().setEnabled(false);
                                                pieDataSet.setSliceSpace(0f);
                                                pieDataSet.setValueTextColor(Color.BLACK);
                                                pieDataSet.setValueTextSize(122);
                                                pieDataSet.setValueTextSize(10f);
                                                pieChart.setDrawSliceText(false);
                                                pieDataSet.setSliceSpace(5f);

                                            } else {
                                                Log.d("please", "help");
                                            }
                                        } else {
                                            Log.d("TAG", "Failed with: ", task.getException());
                                        }
                                    }
                                });
                            }
                        }


                        firestore.collection("Companys").orderBy("Ctc", Query.Direction.DESCENDING)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            List<String> allskills = new ArrayList<String>();
                                            List<String> avgpackage = new ArrayList<String>();
                                            List<String> dreamcgpaarray = new ArrayList<String>();
                                            List<String> masscgpaarray = new ArrayList<String>();
                                            List<String> corecgpaarray = new ArrayList<String>();
                                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                            List<String> packcom = new ArrayList<>();

                                            for(int i=0;i<myListOfDocuments.size();i++) {
                                                totalcompanies++;
                                                if (myListOfDocuments.get(i).get("Branch").toString().contains(document.get("Branch").toString())
                                                        && myListOfDocuments.get(i).get("Batches").toString().contains(document.get("Batch").toString())) {
                                                    allskills.addAll((Collection<? extends String>) myListOfDocuments.get(i).get("Skills"));
                                                    urcompanies++;

                                                    if(myListOfDocuments.get(i).get("Tier").toString().equals("Dream")){
                                                        dreamcgpaarray.add((myListOfDocuments.get(i).get("Cgpa")).toString());
                                                    }
                                                    else if(myListOfDocuments.get(i).get("Tier").toString().equals("Core")){
                                                        corecgpaarray.add((myListOfDocuments.get(i).get("Cgpa")).toString());
                                                    }
                                                    else {
                                                        masscgpaarray.add((myListOfDocuments.get(i).get("Cgpa")).toString());
                                                    }

                                                }
                                            }

                                            pieEntries1 = new ArrayList<>();
                                            pieEntries1.add(new PieEntry(urcompanies, "Your branch"));
                                            pieEntries1.add(new PieEntry(totalcompanies-urcompanies, "All branches"));
//
                                            urbranch.setText(String.valueOf(urcompanies));
                                            allbranchcomp.setText(String.valueOf(totalcompanies));
//
//                                            pieEntries1.add(new PieEntry(12, 0));
//                                            pieEntries1.add(new PieEntry(20, 1));
//                                            pieEntries1.add(new PieEntry(45, 2));


                                            outLL1.addView(chartLL1, 0);

                                            pieChart1 = findViewById(R.id.pieChart1);
                                            pieChart1.getLegend().setEnabled(true);
                                            pieDataSet1 = new PieDataSet(pieEntries1, "asda");
                                            pieData1 = new PieData(pieDataSet1);
                                            pieChart1.setData(pieData1);
                                            pieDataSet1.setColors(Color.parseColor("#EC6B56"),
                                                    Color.parseColor("#47B39C"), Color.parseColor("#FFC154")
                                            );
                                            pieDataSet1.setValueFormatter(new ValueFormatter() {
                                                @Override
                                                public String getFormattedValue(float value) {
                                                    return String.valueOf((int) Math.floor(value));
                                                }
                                            });



                                            pieChart1.setHoleRadius(5f);
                                            pieChart1.setTransparentCircleRadius(5f);
                                            pieChart1.getDescription().setEnabled(false);
                                            pieChart1.getLegend().setEnabled(false);
                                            pieDataSet1.setSliceSpace(0f);
                                            pieDataSet1.setValueTextColor(Color.BLACK);
                                            pieDataSet1.setValueTextSize(122);
                                            pieDataSet1.setValueTextSize(10f);
                                            pieChart1.setDrawSliceText(false);
                                            pieDataSet1.setSliceSpace(5f);

                                            progbar.setVisibility(View.INVISIBLE);
                                            scrollView.setVisibility(View.VISIBLE);



                                            if(dreamcgpaarray.size()>0){
                                                dreamcgpa.setText(findWord(dreamcgpaarray));
                                                dreamavg.setText("Avg. Cutoff "+avg(dreamcgpaarray));
                                            }

                                            else{
                                                dreamcgpa.setText("-");
                                                dreamavg.setText("Avg. Cutoff -");
                                            }

                                            if(masscgpaarray.size()>0){
                                                masscgpa.setText(findWord(masscgpaarray));
                                                massavg.setText("Avg. Cutoff "+avg(masscgpaarray));
                                            }

                                            else{
                                                masscgpa.setText("-");
                                                massavg.setText("Avg. Cutoff -");
                                            }

                                            if(corecgpaarray.size()>0){
                                                corecgpa.setText(findWord(corecgpaarray));
                                                coreavg.setText("Avg. Cutoff "+avg(corecgpaarray));
                                            }

                                            else{
                                                corecgpa.setText("-");
                                                coreavg.setText("Avg. Cutoff -");
                                            }


                                            for(int i=0;i<myListOfDocuments.size();i++) {
                                                if (myListOfDocuments.get(i).get("Branch").toString().contains(document.get("Branch").toString())
                                                        && myListOfDocuments.get(i).get("Batches").toString().contains(document.get("Batch").toString())) {
                                                    packcom.add(String.valueOf(myListOfDocuments.get(i).get("Name")));
                                                    packcom.add(String.valueOf(myListOfDocuments.get(i).get("Ctc")));
                                                }
                                            }

                                            try {
                                                comp1.setText(packcom.get(0));
                                            } catch(IndexOutOfBoundsException e) {
                                                comp1.setText("-");
                                            }

                                            try {
                                                comp2.setText(packcom.get(2));
                                            } catch(IndexOutOfBoundsException e) {
                                                comp2.setText("-");
                                            }

                                            try {
                                                comp3.setText(packcom.get(4));
                                            } catch(IndexOutOfBoundsException e) {
                                                comp3.setText("-");
                                            }

                                            try {
                                                comp4.setText(packcom.get(6));
                                            } catch(IndexOutOfBoundsException e) {
                                                comp4.setText("-");
                                            }

                                            try {
                                                comp5.setText(packcom.get(8));
                                            } catch(IndexOutOfBoundsException e) {
                                                comp5.setText("-");
                                            }



                                            try {
                                                pack1.setText(packcom.get(1));
                                            } catch(IndexOutOfBoundsException e) {
                                                pack1.setText("-");
                                            }
                                            try {
                                                pack2.setText(packcom.get(3));
                                            } catch(IndexOutOfBoundsException e) {
                                                pack2.setText("-");
                                            }
                                            try {
                                                pack3.setText(packcom.get(5));
                                            } catch(IndexOutOfBoundsException e) {
                                                pack3.setText("-");
                                            }
                                            try {
                                                pack4.setText(packcom.get(7));
                                            } catch(IndexOutOfBoundsException e) {
                                                pack4.setText("-");
                                            }
                                            try {
                                                pack5.setText(packcom.get(9));
                                            } catch(IndexOutOfBoundsException e) {
                                                pack5.setText("-");
                                            }


                                            String[] skillsarray = new String[allskills.size()];
                                            skillsarray = allskills.toArray(skillsarray);
                                            List<String> skillsarrayret = topKFrequent(skillsarray);

                                            try {
                                                skill1.setText(skillsarrayret.get(0));
                                            } catch(IndexOutOfBoundsException e) {
                                                skill1.setText("-");
                                            }
                                            try {
                                                skill2.setText(skillsarrayret.get(2));
                                            } catch(IndexOutOfBoundsException e) {
                                                skill2.setText("-");
                                            }
                                            try {
                                                skill3.setText(skillsarrayret.get(4));
                                            } catch(IndexOutOfBoundsException e) {
                                                skill3.setText("-");
                                            }
                                            try {
                                                skill4.setText(skillsarrayret.get(6));
                                            } catch(IndexOutOfBoundsException e) {
                                                skill4.setText("-");
                                            }
                                            try {
                                                skill5.setText(skillsarrayret.get(8));
                                            } catch(IndexOutOfBoundsException e) {
                                                skill5.setText("-");
                                            }

                                            try {
                                                count1.setText(skillsarrayret.get(1));
                                            } catch(IndexOutOfBoundsException e) {
                                                count1.setText("-");
                                            }
                                            try {
                                                count2.setText(skillsarrayret.get(3));
                                            } catch(IndexOutOfBoundsException e) {
                                                count2.setText("-");
                                            }
                                            try {
                                                count3.setText(skillsarrayret.get(5));
                                            } catch(IndexOutOfBoundsException e) {
                                                count3.setText("-");
                                            }
                                            try {
                                                count4.setText(skillsarrayret.get(7));
                                            } catch(IndexOutOfBoundsException e) {
                                                count4.setText("-");
                                            }
                                            try {
                                                count5.setText(skillsarrayret.get(9));
                                            } catch(IndexOutOfBoundsException e) {
                                                count5.setText("-");
                                            }
                                        }
                                    }
                                });

                        firestore.collection("Companys").orderBy("Ctc", Query.Direction.DESCENDING)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            List<String> allskills = new ArrayList<String>();

                                            List<String> appdream = new ArrayList<String>();
                                            List<String> appcore = new ArrayList<String>();
                                            List<String> appmass = new ArrayList<String>();
                                            List<String> pladream = new ArrayList<String>();
                                            List<String> placore = new ArrayList<String>();
                                            List<String> plamass = new ArrayList<String>();



                                            List<String> packages = new ArrayList<String>();
                                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                            List<String> packcom = new ArrayList<>();
                                            for(int i=0;i<myListOfDocuments.size();i++){
                                                if(myListOfDocuments.get(i).get("Branch").toString().contains(document.get("Branch").toString())
                                                && myListOfDocuments.get(i).get("Batches").toString().contains(document.get("Batch").toString())){

                                                    packages.add(myListOfDocuments.get(i).get("Ctc").toString());


                                                    if(myListOfDocuments.get(i).get("Tier").toString().equals("Mass")){


                                                            appmass.addAll((Collection<? extends String>) myListOfDocuments.get(i).get("AppliedStudents"));
                                                            plamass.addAll((Collection<? extends String>) myListOfDocuments.get(i).get("Placed"));


                                                        mass++;
                                                        if(Float.parseFloat(myListOfDocuments.get(i).get("Tenth").toString())<=Float.parseFloat(document.get("TenthMarks").toString())
                                                                && Float.parseFloat(myListOfDocuments.get(i).get("Twelfth").toString())<=Float.parseFloat(document.get("PreUniMarks").toString())
                                                                && Integer.parseInt(myListOfDocuments.get(i).get("ClBacklog").toString())>=Integer.parseInt(document.get("ClearArr").toString())
                                                                && Integer.parseInt(myListOfDocuments.get(i).get("Backlog").toString())>=Integer.parseInt(document.get("CurArr").toString()) &&
                                                                Float.parseFloat(myListOfDocuments.get(i).get("Cgpa").toString())<=Float.parseFloat(document.get("CGPA").toString())){
                                                            urmass++;
                                                        }
                                                    }
                                                    else if(myListOfDocuments.get(i).get("Tier").toString().equals("Core")){


                                                            appcore.addAll((Collection<? extends String>) myListOfDocuments.get(i).get("AppliedStudents"));
                                                            placore.addAll((Collection<? extends String>) myListOfDocuments.get(i).get("Placed"));

                                                        core++;
                                                        if(Float.parseFloat(myListOfDocuments.get(i).get("Tenth").toString())<=Float.parseFloat(document.get("TenthMarks").toString())
                                                                && Float.parseFloat(myListOfDocuments.get(i).get("Twelfth").toString())<=Float.parseFloat(document.get("PreUniMarks").toString())
                                                                && Integer.parseInt(myListOfDocuments.get(i).get("ClBacklog").toString())>=Integer.parseInt(document.get("ClearArr").toString())
                                                                && Integer.parseInt(myListOfDocuments.get(i).get("Backlog").toString())>=Integer.parseInt(document.get("CurArr").toString()) &&
                                                                Float.parseFloat(myListOfDocuments.get(i).get("Cgpa").toString())<=Float.parseFloat(document.get("CGPA").toString())){
                                                            urcore++;
                                                        }
                                                    }
                                                    else if(myListOfDocuments.get(i).get("Tier").toString().equals("Dream")){

                                                            appdream.addAll((Collection<? extends String>) myListOfDocuments.get(i).get("AppliedStudents"));
                                                            pladream.addAll((Collection<? extends String>) myListOfDocuments.get(i).get("Placed"));

                                                        dream++;
                                                        if(Float.parseFloat(myListOfDocuments.get(i).get("Tenth").toString())<=Float.parseFloat(document.get("TenthMarks").toString())
                                                                && Float.parseFloat(myListOfDocuments.get(i).get("Twelfth").toString())<=Float.parseFloat(document.get("PreUniMarks").toString())
                                                                && Integer.parseInt(myListOfDocuments.get(i).get("ClBacklog").toString())>=Integer.parseInt(document.get("ClearArr").toString())
                                                                && Integer.parseInt(myListOfDocuments.get(i).get("Backlog").toString())>=Integer.parseInt(document.get("CurArr").toString()) &&
                                                                Float.parseFloat(myListOfDocuments.get(i).get("Cgpa").toString())<=Float.parseFloat(document.get("CGPA").toString())){
                                                            urdream++;
                                                        }
                                                    }

                                                }

                                            }

                                            if(appdream.size()==0){
                                                dreamaccper.setText("0.0%");
                                            }
                                            else
                                            {
                                                dreamaccper.setText(String.valueOf((pladream.size()/(double)appdream.size())*100)+"%");
                                                Log.d("placed",String.valueOf(pladream.size()));
                                                Log.d("applied",String.valueOf(appdream.size()));
                                            }

                                            if(appcore.size()==0){
                                                coreaccper.setText("0.0%");
                                            }
                                            else
                                            coreaccper.setText(String.valueOf((placore.size()/(double)appcore.size())*100)+"%");

                                            if(appmass.size()==0){
                                                massaccper.setText("0.0%");
                                            }
                                            else
                                            massaccper.setText(String.valueOf((plamass.size()/(double)appmass.size())*100)+"%");



                                            if(packages.size()>0){
                                              avgpack.setText(avg(packages).substring(0,3));
                                            }
                                            else
                                                avgpack.setText("-");

                                            masseli.setText("Eligible companies: "+urmass);
                                            masstot.setText("Total companies: "+mass);
                                            if(mass==0)
                                                massper.setText("0%");
                                            else
                                            massper.setText((int)((urmass/(double)mass)*100)+"%");

                                            coreeli.setText("Eligible companies: "+urcore);
                                            coretot.setText("Total companies: "+core);
                                            if(core==0){
                                                coreper.setText("0%");
                                            }
                                            else
                                            coreper.setText((int)((urcore/(double)core)*100)+"%");

                                            dreameli.setText("Eligible companies: "+urdream);
                                            dreamtot.setText("Total companies: "+dream);
                                            if(dream==0){
                                                dreamper.setText("0%");
                                            }
                                            else
                                            dreamper.setText((int)((urdream/(double)dream)*100)+"%");
                                        }
                                    }
                                });


                        firestore.collectionGroup("Details")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            List<DocumentSnapshot> myListOfDocuments1 = task.getResult().getDocuments();
                                            for(int i=0;i<myListOfDocuments1.size();i++) {

                                                if (myListOfDocuments1.get(i).get("Branch").toString().equals(document.getString("Branch"))
                                                        && myListOfDocuments1.get(i).get("Batch").toString().equals(document.get("Batch"))) {
                                                    totalstudents++;

                                                    if(!myListOfDocuments1.get(i).get("PlacedAt").toString().equals("[]")){
                                                        urbranchplacedstudents++;
                                                    }
                                                }
                                            }

                                            if(totalstudents!=0)
                                            placedper.setText(String.valueOf(Math.floor((urbranchplacedstudents/(double)totalstudents)*100)).substring(0, String.valueOf(Math.floor((urbranchplacedstudents/(double)totalstudents)*100)).length() - 2)+"%");
                                            else
                                                placedper.setText("-");

                                        }
                                    }
                                });



                    } else {
                        Log.d("please", "help");
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }

        });



    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<String> topKFrequent(String[] nums) {
        Map<String, Integer> map = new HashMap<>();
        for(String n: nums){
            map.put(n, map.getOrDefault(n,0)+1);
        }

        PriorityQueue<Map.Entry<String, Integer>> maxHeap =
                new PriorityQueue<>((a,b)->(b.getValue()-a.getValue()));
        for(Map.Entry<String,Integer> entry: map.entrySet()){
            maxHeap.add(entry);
        }

        List<String> res = new ArrayList<>();
        while(res.size()<10){
            Map.Entry<String, Integer> entry = maxHeap.poll();
            res.add(entry.getKey());
            res.add(String.valueOf(entry.getValue()));
        }
        return res;
    }


    static  String avg(List<String> sum){
        double total=0;
        double avg=0;
        for(int i = 0; i<sum.size(); i++)
            total = total+Double.parseDouble(sum.get(i));
        avg = total / sum.size();
        avg= Math.floor(avg * 100) / 100;
        return String.valueOf(avg);
    }

    static String findWord(List<String> arr)
    {
        HashMap<String, Integer> hs = new HashMap<String, Integer>();

        for (int i = 0; i < arr.size(); i++) {
            if (hs.containsKey(arr.get(i))) {
                hs.put(arr.get(i), hs.get(arr.get(i)) + 1);
            }
            else {
                hs.put(arr.get(i), 1);
            }
        }

        Set<Map.Entry<String, Integer> > set = hs.entrySet();
        String key = "";
        int value = 0;

        for (Map.Entry<String, Integer> me : set) {
            if (me.getValue() > value) {
                value = me.getValue();
                key = me.getKey();
            }
        }

        return key;
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }

}
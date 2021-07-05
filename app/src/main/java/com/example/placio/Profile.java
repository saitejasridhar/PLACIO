package com.example.placio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationView bottomNavigationView;
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    FirebaseFirestore firestore;
    TextView fname,sname,email,phone,usn,cgpa,backlogs,sem,section,batch,pemail,branch,applied,
            inprogtext,rejected,accepted;
    int rejectedcompanies,placedcompanies,inprogcompanies,appliedcompanies;
    TextView totapps,placed,unplaced,comps,highpack,avgpack,corecomp,dreamcomp;
    int placedstuds=0;
    int unplacedstuds=0;
    int companies=0,dreamcompanies=0,corecompanies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final LinearLayout outLL = findViewById(R.id.outLL);
        final LinearLayout chartLL = findViewById(R.id.chartLL);
        ((ViewGroup) chartLL.getParent()).removeView(chartLL);
        fname=findViewById(R.id.fname);
        sname=findViewById(R.id.sname);
        email=findViewById(R.id.gmail);
        phone=findViewById(R.id.phone);
        usn=findViewById(R.id.usn);
        cgpa=findViewById(R.id.cgpa);
        backlogs=findViewById(R.id.backlog);
        sem=findViewById(R.id.sem);
        section=findViewById(R.id.section);
        batch=findViewById(R.id.batch);
        pemail=findViewById(R.id.pemail);
        branch=findViewById(R.id.branch);
        applied=findViewById(R.id.applied);
        inprogtext=findViewById(R.id.inprogtext);
        accepted=findViewById(R.id.accepted);
        rejected=findViewById(R.id.rejected);

        totapps=findViewById(R.id.totalapplications);
        placed=findViewById(R.id.placedstudents);
        unplaced=findViewById(R.id.unplaced);
        comps=findViewById(R.id.visited);
        highpack=findViewById(R.id.highpack);
        avgpack=findViewById(R.id.avgpack);
        dreamcomp=findViewById(R.id.dreamcom);
        corecomp=findViewById(R.id.corecomp);


        getSupportActionBar().hide();
        Button logout_button = (Button) findViewById(R.id.logout);
        final ProgressBar progbar = (ProgressBar) findViewById(R.id.progbar);
        final ConstraintLayout scrollView = (ConstraintLayout) findViewById(R.id.scrollView);
        progbar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                finish();
                openNewActivity(MainActivity.class);
            }
        });



        findViewById(R.id.compro).setOnClickListener(this);
        findViewById(R.id.completestats).setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();

        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docIdRef1 = firestore.collection("students").document(currentuser).collection("Details").document(currentuser);
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                       String app= document.get("Applied").toString();
                        String[] myArray = app.split(",");

                        String appstring= document.get("Applied").toString();
                        List<String>  apc = Arrays.asList(appstring.split("\\s*,\\s*"));
                        if(appstring.equals("[]")){
                            appliedcompanies=0;
                        }
                        else {
                            appliedcompanies=apc.size();
                        }

                        String rejstring= document.get("Rejected").toString();
                        List<String>  rej = Arrays.asList(rejstring.split("\\s*,\\s*"));
                        if(rejstring.equals("[]")){
                             rejectedcompanies=0;
                        }
                        else {
                            rejectedcompanies=rej.size();
                        }

                        String plastring= document.get("PlacedAt").toString();
                        List<String>  pla = Arrays.asList(plastring.split("\\s*,\\s*"));
                        if(plastring.equals("[]")){
                            placedcompanies=0;
                        }
                        else {
                            placedcompanies=pla.size();
                        }


                        String instring= document.get("InProgress").toString();
                        List<String>  inprog = Arrays.asList(instring.split("\\s*,\\s*"));
                        if(instring.equals("[]")){
                            inprogcompanies=0;
                        }
                        else {
                            inprogcompanies=inprog.size();
                        }

                        fname.setText(document.get("FName").toString());
                        sname.setText(document.get("Sname").toString());
                        email.setText(document.get("PEmail").toString());
                        phone.setText(document.get("PPhone").toString());
                        pemail.setText(document.get("PEmail").toString());
                        backlogs.setText("Active BackLogs: "+document.get("CurArr").toString());
                        batch.setText(document.get("Batch").toString()+ " Batch");
                        section.setText(document.get("Section").toString());
                        sem.setText("Current Semester: "+document.get("CurSem").toString());
                        branch.setText(document.get("Branch").toString());
                        cgpa.setText("CGPA: "+document.get("CGPA").toString());
                        usn.setText(document.get("USN").toString());
                        applied.setText(String.valueOf(appliedcompanies));
                        accepted.setText(String.valueOf(placedcompanies));
                        rejected.setText(String.valueOf(rejectedcompanies));
                        inprogtext.setText(String.valueOf(inprogcompanies));


                        pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(rejectedcompanies, 0));
                        pieEntries.add(new PieEntry(placedcompanies, 1));
                        pieEntries.add(new PieEntry(inprogcompanies, 2));


                        outLL.addView(chartLL,0);

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



                        firestore.collection("Companys").orderBy("Ctc", Query.Direction.DESCENDING)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            List<DocumentSnapshot> myListOfDocuments1 = task.getResult().getDocuments();
                                            String name=myListOfDocuments1.get(0).get("Name").toString();
                                            String pack=myListOfDocuments1.get(0).get("Ctc").toString();
                                            highpack.setText(pack);
                                            List<String> packs = new ArrayList<String>();
                                            for(int i=0;i<myListOfDocuments1.size();i++) {
                                                companies++;
                                                packs.add(String.valueOf(myListOfDocuments1.get(i).get("Ctc")));
                                                if(myListOfDocuments1.get(i).get("Tier").equals("Dream"))
                                                    dreamcompanies++;
                                                if(myListOfDocuments1.get(i).get("Tier").equals("Core"))
                                                    corecompanies++;
                                            }
                                            dreamcomp.setText(String.valueOf(dreamcompanies));
                                            corecomp.setText(String.valueOf(corecompanies));
                                            avgpack.setText(String.valueOf(avg(packs)));
                                            comps.setText(String.valueOf(companies));
                                            firestore.collectionGroup("Details")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                List<String> allskills = new ArrayList<String>();
                                                                List<DocumentSnapshot> myListOfDocuments1 = task.getResult().getDocuments();
                                                                for(int i=0;i<myListOfDocuments1.size();i++) {
                                                                    allskills.addAll((Collection<? extends String>) myListOfDocuments1.get(i).get("PlacedAt"));

                                                                    if (!myListOfDocuments1.get(i).get("PlacedAt").toString().equals("[]"))
                                                                    {
                                                                        placedstuds++;

                                                                    }
                                                                    else{
                                                                        unplacedstuds++;
                                                                    }
                                                                }
                                                                placed.setText(String.valueOf(placedstuds));
                                                                unplaced.setText(String.valueOf(unplacedstuds));
                                                                totapps.setText(String.valueOf(allskills.size()));
                                                                progbar.setVisibility(View.INVISIBLE);
                                                                scrollView.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                    });
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

        bottomNavigationView=(BottomNavigationView) findViewById(R.id.navbar);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.home:
                        openNewActivity(MainHome.class);
                        break;
                    case R.id.Announcemtns:
                        openNewActivity(Announcements.class);
                        break;
                    case R.id.events:
                        openNewActivity(Events.class);
                        break;
                    case R.id.Tickets:
                        openNewActivity(ViewTickets.class);
                        break;
                }
                return  true;
            }
        });
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.compro:
                startActivity(new Intent(this, CopleteProfile.class));
                break;
            case R.id.completestats:
                startActivity(new Intent(this, Studentstats.class));
                break;
        }
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
}


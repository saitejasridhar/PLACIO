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
import com.google.protobuf.StringValue;

import java.util.ArrayList;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationView bottomNavigationView;
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    FirebaseFirestore firestore;
    TextView fname,sname,email,phone,usn,cgpa,backlogs,sem,section,batch,pemail,branch,applied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
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
                       int appliedcompanies=myArray.length;
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

                        pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(appliedcompanies, 0));
                        pieEntries.add(new PieEntry(2, 1));
                        pieEntries.add(new PieEntry(3, 2));


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

                        progbar.setVisibility(View.INVISIBLE);
                        scrollView.setVisibility(View.VISIBLE);

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
        }
    }

}


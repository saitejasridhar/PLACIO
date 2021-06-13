package com.example.placio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CopleteProfile extends AppCompatActivity {
    TextView fname,sname,email,phone,usn,sem,section,batch,pemail,branch,address;
    FirebaseFirestore firestore;
    TextView cgpa,curback,clback;
    TextView iname10th,marks10,bname10th,qyear10;
    TextView iname12th,marks12,bname12th,qyear12;
    String resume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_coplete_profile);
        Button openresume=findViewById(R.id.openres);
        final ProgressBar progbar = (ProgressBar) findViewById(R.id.progbar);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        progbar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
        fname=findViewById(R.id.fname);
        sname=findViewById(R.id.sname);
        email=findViewById(R.id.gmail);
        phone=findViewById(R.id.phone);
        usn=findViewById(R.id.usn);
        sem=findViewById(R.id.cursem);
        section=findViewById(R.id.section);
        batch=findViewById(R.id.batch);
        pemail=findViewById(R.id.pemail);
        branch=findViewById(R.id.branch);
        address=findViewById(R.id.address);

        cgpa=findViewById(R.id.cgpa);
        curback=findViewById(R.id.curback);
        clback=findViewById(R.id.clback);

        iname10th=findViewById(R.id.name10);
        marks10=findViewById(R.id.marks10);
        bname10th=findViewById(R.id.board10);
        qyear10=findViewById(R.id.qyear10);

        iname12th=findViewById(R.id.name12);
        marks12=findViewById(R.id.marks12);
        bname12th=findViewById(R.id.board12);
        qyear12=findViewById(R.id.qyear12);

        Button edit =  findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity(EditProfile.class);
            }
        });

        Button  cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(Profile.class);
            }
        });


        firestore = FirebaseFirestore.getInstance();

        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docIdRef1 = firestore.collection("students").document(currentuser).collection("Details").document(currentuser);
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        fname.setText(document.get("FName").toString());
                        sname.setText(document.get("Sname").toString());
                        email.setText(document.get("PEmail").toString());
                        phone.setText(document.get("PPhone").toString());
                        pemail.setText(document.get("PEmail").toString());
                        batch.setText(document.get("Batch").toString()+ " Batch");
                        section.setText(document.get("Section").toString());
                        branch.setText(document.get("Branch").toString());
                        usn.setText(document.get("USN").toString());
                        address.setText("Address: "+document.get("CurAddress").toString());
                        sem.setText(document.get("CurSem").toString()+" Semester");

                        cgpa.setText("CGPA: "+document.get("CGPA").toString());
                        curback.setText("Current BackLogs: "+document.get("CurArr").toString());
                        clback.setText("Cleared BackLogs: "+document.get("ClearArr").toString());

                        iname10th.setText(document.get("10thInstitute").toString());
                        marks10.setText("Percentage: "+document.get("10thMarks").toString());
                        bname10th.setText(document.get("10thBoard").toString());
                        qyear10.setText("Qualification Year: "+document.get("10thQyear").toString());

                        iname12th.setText(document.get("PreUniInstitute").toString());
                        marks12.setText("Percentage: "+document.get("PreUniMarks").toString());
                        bname12th.setText(document.get("PreUniBoard").toString());
                        qyear12.setText("Qualification Year: "+document.get("PreUniQyear").toString());

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

        DocumentReference docIdRef2 = firestore.collection("students").document(currentuser).collection("Resume").document(currentuser);
        docIdRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        resume = document.get("resume").toString();
                        try {
                            resume= URLEncoder.encode(resume,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("please", "help");
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }
        });
        openresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Resume.class);
                intent.putExtra("url",resume);
                startActivity(intent);
            }
        });
    }



    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }
}
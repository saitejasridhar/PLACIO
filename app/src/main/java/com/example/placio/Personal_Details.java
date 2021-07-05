package com.example.placio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Personal_Details extends AppCompatActivity {
    Button confrim,cancel;
    TextView name;
    String Name;
    TextView fname,sname,email,phone,usn,sem,section,batch,pemail,branch,address;
    FirebaseFirestore firestore;
    TextView cgpa,curback,clback;
    TextView iname10th,marks10,bname10th,qyear10;
    TextView iname12th,marks12,bname12th,qyear12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_personal_details);
        Intent intent = getIntent();
        String value = intent.getExtras().getString("company");
        String compname = intent.getExtras().getString("compname");
        cancel=findViewById(R.id.cancel);
        confrim=findViewById(R.id.confrim);
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
                        Name = document.get("resume").toString();
                        try {
                            Name= URLEncoder.encode(Name,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        cgpa.setText("CGPA: "+document.get("CGPA").toString());
                        curback.setText("Current BackLogs: "+document.get("CurArr").toString());
                        clback.setText("Cleared BackLogs: "+document.get("ClearArr").toString());

                        iname10th.setText(document.get("TenthInstitute").toString());
                        marks10.setText("Percentage: "+document.get("TenthMarks").toString());
                        bname10th.setText(document.get("TenthBoard").toString());
                        qyear10.setText("Qualification Year: "+document.get("TenthQyear").toString());

                        iname12th.setText(document.get("PreUniInstitute").toString());
                        marks12.setText("Percentage: "+document.get("PreUniMarks").toString());
                        bname12th.setText(document.get("PreUniBoard").toString());
                        qyear12.setText("Qualification Year: "+document.get("PreUniQyear").toString());

                    } else {
                        Log.d("please", "help");
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }
        });


        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResumeReview.class);
                intent.putExtra("url",Name);
                intent.putExtra("company",value);
                intent.putExtra("compname",compname);

                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }
}
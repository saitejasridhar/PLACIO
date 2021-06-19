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
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaiseTicket extends AppCompatActivity {
    EditText sub, desc;
    Button back, submit;
    boolean isAllFieldsChecked;
    FirebaseFirestore firestore;
    CollectionReference reference;
    String Name, Branch,USN, Section;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_ticket);

        sub=findViewById(R.id.subject);
        desc=findViewById(R.id.desc);
        back=findViewById(R.id.back);
        submit=findViewById(R.id.submit);
        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("Tickets");

        firestore = FirebaseFirestore.getInstance();

        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docIdRef1 = firestore.collection("students").document(currentuser).collection("Details").document(currentuser);
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Name=document.get("FName").toString()+" "+document.get("Sname").toString();
                        USN=document.get("USN").toString();
                        Section=document.get("Section").toString();
                        Branch=document.get("Branch").toString();
                    } else {
                        Log.d("please", "help");
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked){

                    final Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("Subject",sub.getText().toString());
                    dataMap.put("Description",desc.getText().toString());
                    dataMap.put("Name",Name);
                    dataMap.put("Section",Section);
                    dataMap.put("Branch",Branch);
                    dataMap.put("USN",USN);
                    dataMap.put("Date", java.text.DateFormat.getDateTimeInstance().format(new Date()).toString());

                    reference.add(dataMap)
                            .addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                                    openNewActivity(Profile.class);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else
                    Toast.makeText(getApplicationContext(),"Some Fields are invalid",Toast.LENGTH_LONG).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean CheckAllFields() {
        int ret=0;
        if (sub.length() == 0) {
            sub.setError("This field is required");
            ret++;
        }

        if (desc.length() == 0) {
            desc.setError("This field is required");
            ret++;
        }
        return ret == 0;
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }
}
package com.example.placio;
import androidx.annotation.NonNull;
import android.content.Intent;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.Calendar;


public class Form extends AppCompatActivity {
    Spinner section,isdiploma;
    Button register;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    CollectionReference reference;
    FirebaseAuth firebaseAuth;
    boolean isAllFieldsChecked;
    boolean is12th=true;
    EditText fname, sname, usn, pphone,pemail,gphone,board10,institutename10,marks10,qyear10,qyear12,marks12,institutename12,
            board12,qyeardiploma,marksdiploma,institutenamediploma,boarddiploma,permanentaddress,currentaddress,batch,
            clarrears,carrears,cgpa,currentsem;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String value = getIntent().getExtras().getString("branch");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isFirst","True");
        editor.putString("isHome","False");
        editor.putString("isReg","True");
        editor.apply();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_form);
        section = findViewById(R.id.section);
        isdiploma = findViewById(R.id.isdiploma);
        final LinearLayout LinearL = findViewById(R.id.LinearL);
        final LinearLayout Linear12 = findViewById(R.id.Linear12);
        final LinearLayout Lineardiploma = findViewById(R.id.Lineardiploma);
        ((ViewGroup) Linear12.getParent()).removeView(Linear12);
        ((ViewGroup) Lineardiploma.getParent()).removeView(Lineardiploma);
        firebaseAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.register);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = firestore.collection("students");
        fname = findViewById(R.id.fname);
        sname = findViewById(R.id.sname);
        usn = findViewById(R.id.usn);
        pphone = findViewById(R.id.pphone);
        pemail = findViewById(R.id.pemail);
        gphone = findViewById(R.id.gphone);
        board10 = findViewById(R.id.board10);
        institutename10 = findViewById(R.id.institutename10);
        marks10 = findViewById(R.id.marks10);
        qyear10=findViewById(R.id.qyear10);
        permanentaddress = findViewById(R.id.permanentaddress);
        currentaddress = findViewById(R.id.currentaddress);
        clarrears = findViewById(R.id.clarrears);
        carrears = findViewById(R.id.carrears);
        cgpa = findViewById(R.id.cgpa);
        currentsem = findViewById(R.id.currentsem);
        batch =findViewById(R.id.batch);

        final String[] items = new String[]{"A", "B", "C","D"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        section.setAdapter(adapter);


        final String[] preuni = new String[]{"12th", "Diploma"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, preuni);
        isdiploma.setAdapter(adapter1);
        isdiploma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(preuni[position]=="Diploma"){
                    LinearL.removeView(Linear12);
                    LinearL.addView(Lineardiploma);
                    qyeardiploma = findViewById(R.id.qyeardiploma);
                    marksdiploma = findViewById(R.id.marksdiploma);
                    institutenamediploma = findViewById(R.id.institutenamediploma);
                    boarddiploma = findViewById(R.id.boarddiploma);
                    is12th=false;
                }
                if(preuni[position]=="12th") {
                    is12th=true;
                    LinearL.removeView(Lineardiploma);
                    LinearL.addView(Linear12);
                    board12 = findViewById(R.id.board12);
                    qyear12 = findViewById(R.id.qyear12);
                    marks12=findViewById(R.id.marks12);
                    institutename12 = findViewById(R.id.institutename12);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked){
                    final Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("Branch",value);
                    dataMap.put("FName",fname.getText().toString());
                    dataMap.put("Sname",sname.getText().toString());
                    dataMap.put("USN",usn.getText().toString());
                    dataMap.put("PPhone",pphone.getText().toString());
                    dataMap.put("PEmail",pemail.getText().toString());
                    dataMap.put("GPhone",gphone.getText().toString());
                    dataMap.put("TenthBoard",board10.getText().toString());
                    dataMap.put("TenthInstitute",institutename10.getText().toString());
                    dataMap.put("TenthMarks",marks10.getText().toString());
                    dataMap.put("TenthQyear",qyear10.getText().toString());
                    dataMap.put("PerAddress",permanentaddress.getText().toString());
                    dataMap.put("CurAddress",currentaddress.getText().toString());
                    dataMap.put("ClearArr",clarrears.getText().toString());
                    dataMap.put("CurArr",carrears.getText().toString());
                    dataMap.put("CGPA",cgpa.getText().toString());
                    dataMap.put("CurSem",currentsem.getText().toString());
                    dataMap.put("PreUni",isdiploma.getSelectedItem().toString());
                    dataMap.put("Section",section.getSelectedItem().toString());
                    dataMap.put("Batch",batch.getText().toString());


                    if(is12th){
                        dataMap.put("PreUniBoard",board12.getText().toString());
                        dataMap.put("PreUniQyear",qyear12.getText().toString());
                        dataMap.put("PreUniMarks",marks12.getText().toString());
                        dataMap.put("PreUniInstitute",institutename12.getText().toString());
                    }
                    else {
                        dataMap.put("PreUniQyear",qyeardiploma.getText().toString());
                        dataMap.put("PreUniMarks",marksdiploma.getText().toString());
                        dataMap.put("PreUniBoard",boarddiploma.getText().toString());
                        dataMap.put("PreUniInstitute",institutenamediploma.getText().toString());
                    }

                    reference.document(auth.getUid().toString()).collection("Details").document(auth.getUid().toString())
                            .set(dataMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                                    openNewActivity(Register.class);                                }
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


    }

    private boolean CheckAllFields() {
        int ret=0;
        if (fname.length() == 0) {
            fname.setError("This field is required");
            ret++;
        }

        if (sname.length() == 0) {
            sname.setError("This field is required");
            ret++;
        }

        if (usn.length() == 0) {
            usn.setError("This field is required");
            ret++;
        } else if (usn.length() != 10 ) {
            usn.setError("USN must be 10 characters");
            ret++;
        }

        if (pphone.length() == 0) {
            pphone.setError("This field is required");
            ret++;
        } else if (pphone.length() != 10 ) {
            pphone.setError("Phone number must be 10 characters");
            ret++;
        }

        if (pemail.length() == 0) {
            pemail.setError("This field is required");
            ret++;
        }
        if (gphone.length() == 0) {
            gphone.setError("This field is required");
            ret++;
        } else if (gphone.length() != 10 ) {
            gphone.setError("Phone number must be 10 characters");
            ret++;
        }


        if (board10.length() == 0) {
            board10.setError("This field is required");
            ret++;
        }

        if (institutename10.length() == 0) {
            institutename10.setError("This field is required");
            ret++;
        }

        if (marks10.length() == 0) {
            marks10.setError("10th percentage is required");
            ret++;
        }else {
            String input=marks10.getText().toString();
            float value=Float.parseFloat(input);
            if(value>100){
                marks10.setError("Enter valid percentage");
                ret++;
            }
        }

        if (qyear10.length() == 0) {
            qyear10.setError("This field is required");
            ret++;
        } else if (qyear10.length() > 4  ) {
            qyear10.setError("Enter a valid year");
            ret++;
        }

        if (batch.length() == 0) {
            batch.setError("This field is required");
            ret++;
        } else if (batch.length() > 4) {
            batch.setError("Enter a valid year");
            ret++;
        }

        if(is12th) {
            if (qyear12.length() == 0) {
                qyear12.setError("This field is required");
                ret++;
            } else if (qyear12.length() > 4) {
                qyear10.setError("Enter a valid year");
                ret++;
            }
            if (marks12.length() == 0) {
                marks12.setError("Password is required");
                ret++;
            } else {
                String input = marks12.getText().toString();
                float value=Float.parseFloat(input);
                if (value > 100) {
                    marks12.setError("Enter valid percentage");
                    ret++;
                }
            }
            if (institutename12.length() == 0) {
                institutename12.setError("This field is required");
                ret++;
            }
            if (board12.length() == 0) {
                board12.setError("This field is required");
                ret++;
            }
        }
        if(!is12th) {
            if (qyeardiploma.length() == 0) {
                qyeardiploma.setError("This field is required");
                ret++;
            } else if (qyeardiploma.length() > 4) {
                qyeardiploma.setError("Enter a valid year");
                ret++;
            }
            if (marksdiploma.length() == 0) {
                marksdiploma.setError("PThis field is required");
                ret++;
            } else {
                String input = marksdiploma.getText().toString();
                float value=Float.parseFloat(input);
                if (value > 100) {
                    marksdiploma.setError("Enter valid percentage");
                    ret++;
                }
            }
            if (institutenamediploma.length() == 0) {
                institutenamediploma.setError("This field is required");
                ret++;
            }
            if (boarddiploma.length() == 0) {
                boarddiploma.setError("This field is required");
                ret++;
            }
        }
        if (permanentaddress.length() == 0) {
            permanentaddress.setError("This field is required");
            ret++;
        }

        if (currentaddress.length() == 0) {
            currentaddress.setError("This field is required");
            ret++;
        }

        if (clarrears.length() == 0) {
            clarrears.setError("This field is required");
            ret++;
        }
        if (carrears.length() == 0) {
            carrears.setError("This field is required");
            ret++;
        }
        if (cgpa.length() == 0) {
            cgpa.setError("This field is required");
            ret++;
        } else {
            String input=cgpa.getText().toString();
            float value=Float.parseFloat(input);
            if(value>10){
                cgpa.setError("Enter valid CGPA below 10");
                ret++;
            }
        }

        if (currentsem.length() == 0) {
            currentsem.setError("This field is required");
            ret++;
        } else{
            String input=currentsem.getText().toString();
            int value= Integer.parseInt(input);
            if(value>8){
                currentsem.setError("Enter semester between 1-8");
                ret++;
            }
        }
        // after all validation return true.
        return ret == 0;
    }


    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }
}
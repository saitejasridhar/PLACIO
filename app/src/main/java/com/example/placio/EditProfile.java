package com.example.placio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    EditText fname, sname, usn, pphone,pemail,gphone,board10,institutename10,marks10,qyear10,qyear12,marks12,institutename12,
            board12,qyeardiploma,marksdiploma,institutenamediploma,boarddiploma,permanentaddress,currentaddress,batch,
            clarrears,carrears,cgpa,currentsem;
    FirebaseFirestore firestore;
    Spinner section,isdiploma;
    boolean is12th,isAllFieldsChecked;
    String value;
    LinearLayout Linear12,LinearL,Lineardiploma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();
         LinearL = findViewById(R.id.LinearL);
         Linear12 = findViewById(R.id.Linear12);
         Lineardiploma = findViewById(R.id.Lineardiploma);
//        ((ViewGroup) Linear12.getParent()).removeView(Linear12);
//        ((ViewGroup) Lineardiploma.getParent()).removeView(Lineardiploma);

        Lineardiploma.setVisibility(View.INVISIBLE);
        Linear12.setVisibility(View.INVISIBLE);
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
        section = findViewById(R.id.section);
        isdiploma = findViewById(R.id.isdiploma);
        firestore = FirebaseFirestore.getInstance();
        CollectionReference reference = firestore.collection("students");
        Button  done = findViewById(R.id.register);
        Button  cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(CopleteProfile.class);
            }
        });


        final String[] items = new String[]{"A", "B", "C","D"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        section.setAdapter(adapter);

        isdiploma = findViewById(R.id.isdiploma);
        final String[] preuni = new String[]{"12th", "Diploma"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, preuni);
        isdiploma.setAdapter(adapter1);
        isdiploma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(preuni[position].equals("Diploma")){
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) Linear12.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    Linear12.setLayoutParams(params);

                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) Lineardiploma.getLayoutParams();
                    params1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    Lineardiploma.setLayoutParams(params1);
//                    LinearL.removeView(Linear12);
//                    LinearL.addView(Lineardiploma);
                    Lineardiploma.setVisibility(View.VISIBLE);
                    Linear12.setVisibility(View.INVISIBLE);
                    qyeardiploma = findViewById(R.id.qyeardiploma);
                    marksdiploma = findViewById(R.id.marksdiploma);
                    institutenamediploma = findViewById(R.id.institutenamediploma);
                    boarddiploma = findViewById(R.id.boarddiploma);
                    is12th=false;
                }
                if(preuni[position].equals("12th")) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) Lineardiploma.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    Lineardiploma.setLayoutParams(params);
                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) Linear12.getLayoutParams();
                    params1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    Linear12.setLayoutParams(params1);
                    is12th=true;
                    Linear12.setVisibility(View.VISIBLE);
                    Lineardiploma.setVisibility(View.INVISIBLE);
                    board12 = findViewById(R.id.board12);
                    qyear12 = findViewById(R.id.qyear12);
                    marks12=findViewById(R.id.marks12);
                    institutename12 = findViewById(R.id.institutename12);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docIdRef1 = firestore.collection("students").document(currentuser).collection("Details").document(currentuser);
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        value=document.get("Branch").toString();
                        fname.setText(document.get("FName").toString());
                        sname.setText(document.get("Sname").toString());
                        pphone.setText(document.get("PPhone").toString());
                        gphone.setText(document.get("GPhone").toString());
                        pemail.setText(document.get("PEmail").toString());
                        batch.setText(document.get("Batch").toString());
                        SetSpinnerSelection(section,items,document.get("Section").toString());
                        SetSpinnerSelection12th(isdiploma,preuni,document.get("PreUni").toString(),document);
                        if(document.get("PreUni").equals("12th")){
//                            LinearL.removeView(Lineardiploma);
//                            LinearL.addView(Linear12);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) Lineardiploma.getLayoutParams();
                            params.height = 0;
                            params.width = 0;
                            Lineardiploma.setLayoutParams(params);

                            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) Linear12.getLayoutParams();
                            params1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            Linear12.setLayoutParams(params1);

                            Linear12.setVisibility(View.VISIBLE);
                            Lineardiploma.setVisibility(View.INVISIBLE);
                            board12 = findViewById(R.id.board12);
                            qyear12 = findViewById(R.id.qyear12);
                            marks12=findViewById(R.id.marks12);
                            institutename12 = findViewById(R.id.institutename12);
                            board12.setText(document.get("PreUniBoard").toString());
                            qyear12.setText(document.get("PreUniQyear").toString());
                            marks12.setText(document.get("PreUniMarks").toString());
                            institutename12.setText(document.get("PreUniInstitute").toString());

                        }
                        else {
//                            LinearL.removeView(Linear12);
//                            LinearL.addView(Lineardiploma);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) Linear12.getLayoutParams();
                            params.height = 0;
                            params.width = 0;
                            Linear12.setLayoutParams(params);

                            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) Lineardiploma.getLayoutParams();
                            params1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            Lineardiploma.setLayoutParams(params1);

                            Lineardiploma.setVisibility(View.VISIBLE);
                            Linear12.setVisibility(View.INVISIBLE);
                            qyeardiploma = findViewById(R.id.qyeardiploma);
                            marksdiploma = findViewById(R.id.marksdiploma);
                            institutenamediploma = findViewById(R.id.institutenamediploma);
                            boarddiploma = findViewById(R.id.boarddiploma);
                            boarddiploma.setText(document.get("PreUniBoard").toString());
                            qyeardiploma.setText(document.get("PreUniQyear").toString());
                            marksdiploma.setText(document.get("PreUniMarks").toString());
                            institutenamediploma.setText(document.get("PreUniInstitute").toString());
                        }
                        usn.setText(document.get("USN").toString());
                        currentaddress.setText(document.get("CurAddress").toString());
                        currentsem.setText(document.get("CurSem").toString());

                        institutename10.setText(document.get("TenthInstitute").toString());
                        marks10.setText(document.get("TenthMarks").toString());
                        board10.setText(document.get("TenthBoard").toString());
                        qyear10.setText(document.get("TenthQyear").toString());

                        cgpa.setText(document.get("CGPA").toString());
                        carrears.setText(document.get("CurArr").toString());
                        clarrears.setText(document.get("ClearArr").toString());
                        permanentaddress.setText(document.get("PerAddress").toString());

                    } else {
                        Log.d("please", "help");
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
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

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    reference.document(auth.getUid().toString()).collection("Details").document(auth.getUid().toString())
                            .update(dataMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                                    openNewActivity(CopleteProfile.class);                                }
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

    public void SetSpinnerSelection(Spinner spinner,String[] array,String text) {
        for(int i=0;i<array.length;i++) {
            if(array[i].equals(text)) {
                spinner.setSelection(i);
            }
        }
    }

    public void SetSpinnerSelection12th(Spinner spinner,String[] array,String text,DocumentSnapshot document) {
        for(int i=0;i<array.length;i++) {
            if(array[i].equals(text)) {
                spinner.setSelection(i);
            }
        }
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
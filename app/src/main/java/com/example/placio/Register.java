package com.example.placio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {


    Button upload,selectFile,next;
    TextView notification;
    Uri pdfUri;

    FirebaseStorage storage;
    DocumentReference firestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        next = findViewById(R.id.next);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity(MainHome.class);
            }
        });


        String Uid = FirebaseAuth.getInstance().getUid();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isFirst","True");
        editor.putString("isHome","True");
        editor.putString("isReg","False");
        editor.apply();

     storage=FirebaseStorage.getInstance();
     firestore=FirebaseFirestore.getInstance().collection("students").document(Uid);


     selectFile=findViewById(R.id.selectFile);
     upload=findViewById(R.id.upload);
     notification=findViewById(R.id.notification);


    selectFile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(ContextCompat.checkSelfPermission(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            {
              selectPdf();
            }
            else
            {
                ActivityCompat.requestPermissions(Register.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
            }
        }
    });
    upload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(pdfUri!=null)
                uploadFile(pdfUri);
            else
                Toast.makeText(Register.this,"Select a File",Toast.LENGTH_SHORT).show();
        }
    });

    }

    private void uploadFile(Uri pdfUri) {
        final Button next = (Button)findViewById(R.id.next);
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();
        final String fileName=System.currentTimeMillis()+"";
        final StorageReference storageReference=storage.getReference();

       final StorageReference ref=storageReference.child("Resumes").child(fileName);

       ref.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        String url=downloadUrl.toString();
                        Map<String, Object> data = new HashMap<>();
                        data.put("resume", url);
                        String Uid = FirebaseAuth.getInstance().getUid();
                        firestore.collection("Details").document(Uid).update(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register.this,"Upload successfull",Toast.LENGTH_SHORT).show();
                                        next.setVisibility(View.VISIBLE);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register.this, "Upload not successfull", Toast.LENGTH_SHORT).show();                                    }
                                });
//                        firestore.collection("Resume").document(Uid).set(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                progressDialog.dismiss();
//                                Toast.makeText(Register.this,"Upload successfull",Toast.LENGTH_SHORT).show();
//                                next.setVisibility(View.VISIBLE);
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                progressDialog.dismiss();
//                                Toast.makeText(Register.this, "Upload not successfull", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this,"Upload not successfull",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
        int currentProgress=(int) (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
        progressDialog.setProgress(currentProgress);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
        }
        else
        {
            Toast.makeText(Register.this,"Please grant permission",Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPdf(){

        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            notification.setText("File selected");
        }
        else {
            Toast.makeText(Register.this, "Please select file", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {

    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }


}
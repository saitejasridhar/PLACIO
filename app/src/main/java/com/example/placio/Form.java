package com.example.placio;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    Spinner section,ccm,isdiploma;
    Button register;
    List<String> savedImagesUri;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    CollectionReference reference;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_form);
        section = findViewById(R.id.section);
        section.setPrompt("Title");
        final LinearLayout LinearL = findViewById(R.id.LinearL);
        final LinearLayout Linear12 = findViewById(R.id.Linear12);
        final LinearLayout Lineardiploma = findViewById(R.id.Lineardiploma);
        ((ViewGroup) Linear12.getParent()).removeView(Linear12);
        ((ViewGroup) Lineardiploma.getParent()).removeView(Lineardiploma);


        firebaseAuth = FirebaseAuth.getInstance();

        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity(Register.class);
            }
        });

        firestore = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        reference = firestore.collection("tickets");

        savedImagesUri = new ArrayList<>();

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
                Toast.makeText(getApplicationContext(),preuni[position],Toast.LENGTH_SHORT).show();
                if(preuni[position]=="Diploma"){
                    LinearL.removeView(Linear12);
                    LinearL.addView(Lineardiploma);
                }

                if(preuni[position]=="12th") {
                    LinearL.removeView(Lineardiploma);
                    LinearL.addView(Linear12);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }




    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }
}
package com.example.placio;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.placio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.List;

public class MainHome extends AppCompatActivity implements tab1.OnDataPass,tab2.OnDataPass {

    @Override
    public void onDataPass(String data,String activity) {
        if (activity.equals("companydetails")) {
            Intent intent = new Intent(this, CompanyDetails.class);
            intent.putExtra("Name", data);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, CompanyEvents.class);
            intent.putExtra("Companyid", data);
            startActivity(intent);
        }
    }
    BottomNavigationView bottomNavigationView;
    TabLayout tabs;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test","inMain");

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isFirst","False");
        editor.putString("isHome","True");
        editor.putString("isReg","True");
        editor.apply();

        setContentView(R.layout.activity_main_home);
        final ProgressBar progbar = (ProgressBar) findViewById(R.id.progbar);
        final ConstraintLayout scrollView = (ConstraintLayout) findViewById(R.id.scrollView);
        progbar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
        firestore = FirebaseFirestore.getInstance();


        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docIdRef1= firestore.collection("students").document(currentuser).collection("Details").document(currentuser);
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                            String cgpa = document.get("CGPA").toString();
                            String m10th = document.get("TenthMarks").toString();
                            String m12th = document.get("PreUniMarks").toString();
                            String clarr = document.get("ClearArr").toString();
                            String curarr = document.get("CurArr").toString();
                            String bran = document.get("Branch").toString();
                            String bat = document.get("Batch").toString();
                            List<String> appliedcompanies = (List<String>) document.get("InProgress");
                            List<String> appliedcompanies2 = (List<String>) document.get("Applied");
                            List<String> rejected = (List<String>) document.get("InProgress");
                            List<String> placed = (List<String>) document.get("InProgress");
                            String app = appliedcompanies.toString();

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("CGPA", cgpa);
                            editor.putString("TenthMarks", m10th);
                            editor.putString("PreUniMarks", m12th);
                            editor.putString("ClearArr", clarr);
                            editor.putString("CurArr", curarr);
                            editor.putString("Branch", bran);
                            editor.putString("Batch", bat);
                            editor.putString("Applied", app);
                            editor.putString("Applied2", appliedcompanies2.toString());
                            editor.putString("Rejected", rejected.toString());
                            editor.putString("Placed", placed.toString());

                            editor.putString("Tiers", document.get("Tiers").toString());
                            editor.apply();

                            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getBaseContext(), getSupportFragmentManager());
                            ViewPager viewPager = findViewById(R.id.view_pager);
                            viewPager.setAdapter(sectionsPagerAdapter);
                            tabs = findViewById(R.id.tabs);
                            tabs.setupWithViewPager(viewPager);

                            progbar.setVisibility(View.INVISIBLE);
                            scrollView.setVisibility(View.VISIBLE);

                    } else {
                        Log.d("please","help");
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }
        });

        Button profile = (Button) findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(Profile.class);
            }
        });


        bottomNavigationView=(BottomNavigationView) findViewById(R.id.navbar);
        bottomNavigationView.getMenu().getItem(0).setEnabled(false);
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

    @Override
    public void onBackPressed() {

    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }
}

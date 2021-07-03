package com.example.placio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.List;

public class CompanyDetails extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView name,location,Ctc,breakdown,date,offer,tenth,twefth,cur,clear,cgpa,batches,tier,desc;
    FirebaseFirestore firestore;
    Button confrim,cancel;
    LinearLayout branLL,skillsLL,rolesLL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String value = intent.getExtras().getString("Name");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_company_details);
        final ProgressBar progbar = (ProgressBar) findViewById(R.id.progbar);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        progbar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        Ctc = findViewById(R.id.ctc);
        breakdown = findViewById(R.id.breakdown);
        date = findViewById(R.id.date);
        offer = findViewById(R.id.offering);
        tenth = findViewById(R.id.marks10);
        twefth = findViewById(R.id.marks12);
        cur = findViewById(R.id.curback);
        clear = findViewById(R.id.clback);
        cgpa = findViewById(R.id.cgpa);
//        batches = findViewById(R.id.batches);
        tier = findViewById(R.id.tier);
        desc = findViewById(R.id.desc);



        firestore = FirebaseFirestore.getInstance();
        branLL=findViewById(R.id.BranchLL);
        rolesLL=findViewById(R.id.PositionsLL);
        skillsLL=findViewById(R.id.SkillsLL);


        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docIdRef1 = firestore.collection("Companys").document(value);
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        List<String> branch = (List<String>) document.get("Branch");
                        List<String> batch = (List<String>) document.get("Batches");
                        List<String> skills = (List<String>) document.get("Skills");
                        List<String> role = (List<String>) document.get("Roles");


//                        batches.setText(batch.toString());
                        name.setText(document.get("Name").toString());
                        desc.setText(document.get("Description").toString());
                        location.setText(document.get("Location").toString());
                        Ctc.setText(document.get("Ctc").toString()+" LPA");
                        breakdown.setText(document.get("Breakdown").toString());
                        date.setText("Please apply before "+document.get("Date").toString()+" "+document.get("Time").toString());
                        offer.setText(document.get("Offer").toString());
                        tenth.setText(document.get("Tenth").toString()+"%");
                        twefth.setText(document.get("Twelfth").toString()+"%");
                        cur.setText(document.get("Backlog").toString());
                        clear.setText(document.get("ClBacklog").toString());
                        cgpa.setText(document.get("Cgpa").toString());
                        tier.setText(document.get("Tier").toString());


                        final TextView[] branchitemView = new TextView[branch.size()];
                        final TextView[] skillsitemView = new TextView[skills.size()];
                        final TextView[] rolesitemView = new TextView[role.size()];


                        for (int i = 0; i < branch.size(); i++) {
                            final TextView branchitem = new TextView(getApplicationContext());
                            branchitem.setText(branch.get(i));
                            branchitem.setTextSize(11);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(10,0,0,0);
                            branchitem.setLayoutParams(params);
                            branchitem.setWidth(100);
                            branchitem.setGravity(Gravity.CENTER);
                            branchitem.setBackgroundDrawable(getResources().getDrawable(R.drawable.details_items));
                            branLL.addView(branchitem);
                            branchitemView[i] = branchitem;
                        }
                        for (int i = 0; i < skills.size(); i++) {
                            final TextView skillsitem = new TextView(getApplicationContext());
                            skillsitem.setText(skills.get(i));
                            skillsitem.setTextSize(11);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(13,0,0,0);
                            skillsitem.setLayoutParams(params);
                            skillsitem.setPadding(8,8,8,8);
                            skillsitem.setGravity(Gravity.CENTER);
                            skillsitem.setBackgroundDrawable(getResources().getDrawable(R.drawable.details_items));
                            skillsLL.addView(skillsitem);
                            skillsitemView[i] = skillsitem;
                        }
                        for (int i = 0; i < role.size(); i++) {
                            final TextView rolesitem= new TextView(getApplicationContext());
                            rolesitem.setText(role.get(i));
                            rolesitem.setTextSize(11);
                            rolesitem.setPadding(8,8,8,8);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(10,0,0,0);
                            rolesitem.setLayoutParams(params);
                            rolesitem.setGravity(Gravity.CENTER);
                            rolesitem.setBackgroundDrawable(getResources().getDrawable(R.drawable.details_items));
                            rolesLL.addView(rolesitem);
                            rolesitemView[i] = rolesitem;
                            progbar.setVisibility(View.INVISIBLE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("please", "help");
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }
        });

        cancel = findViewById(R.id.cancel);
        confrim = findViewById(R.id.confrim);

        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Personal_Details.class);
                intent.putExtra("company",value);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(MainHome.class);
            }
        });
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }
}
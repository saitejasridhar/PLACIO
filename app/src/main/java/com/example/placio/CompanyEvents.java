package com.example.placio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CompanyEvents extends AppCompatActivity {

    String usid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CompanyEventsAdapter adapter;
    FirebaseFirestore firestore;
    TextView Name,offering,tier,positions,location,ctc,breakdown,roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_events);
        getSupportActionBar().hide();
        Name=findViewById(R.id.companyName);
        offering=findViewById(R.id.offering);
        tier=findViewById(R.id.category);
        positions=findViewById(R.id.positions);
        location=findViewById(R.id.location);
        ctc=findViewById(R.id.ctc);
        breakdown=findViewById(R.id.breakdown);

        final ProgressBar progbar = (ProgressBar) findViewById(R.id.progbar);
        final ConstraintLayout scrollView = (ConstraintLayout) findViewById(R.id.scrollView);
        progbar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);

        final String value = getIntent().getExtras().getString("Companyid");
         CollectionReference companyRef = db.collection("Companys").document(value).collection("events");
        Button profile = (Button) findViewById(R.id.profile);

        firestore = FirebaseFirestore.getInstance();

        DocumentReference docIdRef1 = firestore.collection("Companys").document(value);
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Name.setText(document.getString("Name"));
                        offering.setText(document.getString("Offer"));
                        tier.setText(document.getString("Tier"));
                        location.setText(document.getString("Location"));
                        positions.setText(document.get("Roles").toString().substring(1, document.get("Roles").toString().length() - 1));
                        ctc.setText(document.get("Ctc").toString());
                        breakdown.setText(document.getString("Breakdown"));

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


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(Profile.class);
            }
        });

        Button back = (Button) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(MainHome.class);
            }
        });



        Query query = companyRef.orderBy("type", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class)
                .build();
        adapter = new CompanyEventsAdapter(options);
        RecyclerView recyclerView =findViewById(R.id.companyevents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CompanyEventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Date date6 = null;
                SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String sDate6 = documentSnapshot.getString("date")+" "+ documentSnapshot.getString("time");
                try {
                    date6=formatter6.parse(sDate6);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(date6);
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", false);
                intent.putExtra("rrule", "FREQ=ONCE");
                intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
                intent.putExtra("title", documentSnapshot.getString("companyname")+" "+documentSnapshot.getString("type"));
                startActivity(intent);            }
        });
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
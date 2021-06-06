package com.example.placio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

public class CompanyDetails extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) { Intent intent=getIntent();
        String value = intent.getExtras().getString("Name");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        name=findViewById(R.id.name);
        name.setText(value.toString());

        bottomNavigationView=(BottomNavigationView) findViewById(R.id.navbar);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
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
                }
                return  true;
            }
        });
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }

}
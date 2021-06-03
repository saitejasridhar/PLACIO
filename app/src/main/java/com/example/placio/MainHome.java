package com.example.placio;

import android.app.Activity;
        import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

        import com.example.placio.R;
        import com.google.android.material.bottomnavigation.BottomNavigationView;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.android.material.snackbar.Snackbar;
        import com.google.android.material.tabs.TabLayout;

        import androidx.annotation.NonNull;
        import androidx.viewpager.widget.ViewPager;
        import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainHome extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button editpro;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isFirst","False");
        editor.apply();


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
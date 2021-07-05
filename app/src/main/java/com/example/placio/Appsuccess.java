package com.example.placio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Appsuccess extends AppCompatActivity {
TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String companyname = intent.getExtras().getString("compname");
        setContentView(R.layout.activity_appsuccess);
        final ProgressBar progbar = (ProgressBar) findViewById(R.id.progbar);
        final ConstraintLayout scrollView = (ConstraintLayout) findViewById(R.id.scrollView);
        progbar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);

        name=findViewById(R.id.name);
        name.setText("Your application was succesfully submitted for "+companyname);

        progbar.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);

    }

    public void myMethod(View view) {
        openNewActivity(MainHome.class);
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }
}
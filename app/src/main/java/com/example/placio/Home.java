package com.example.placio;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;


public class Home extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isFirst","True");
        editor.putString("isHome","False");
        editor.putString("isReg","True");
        editor.apply();
    }

    public void cse(View view) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("branch","CSE");
        startActivity(intent);
    }
    public void mca(View view) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("branch","MCA");
        startActivity(intent);
    }

    public void ise(View view) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("branch","ISE");
        startActivity(intent);
    }
    public void ec(View view) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("branch","EC");
        startActivity(intent);
    }  public void mech(View view) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("branch","MECH");
        startActivity(intent);
    }
    public void ipe(View view) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("branch","IPE");
        startActivity(intent);
    }
    public void civil(View view) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("branch","CIVIL");
        startActivity(intent);
    }
    public void eee(View view) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("branch","EEE");
        startActivity(intent);
    }


    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
package com.example.placio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

//        Button logout_button = (Button) findViewById(R.id.logout);
//        logout_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                auth.signOut();
//                finish();
//                openNewActivity(MainActivity.class);
//            }
//        });

    }

    public void cse(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("key","CSE");
        startActivity(intent);
    }
    public void mca(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("key","MCA");
        startActivity(intent);
    }

    public void ise(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("key","ISE");
        startActivity(intent);
    }
    public void ec(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("key","EC");
        startActivity(intent);
    }  public void mech(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("key","MECH");
        startActivity(intent);
    }
    public void ipe(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("key","IPE");
        startActivity(intent);
    }
    public void civil(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("key","CIVIL");
        startActivity(intent);
    }
    public void eee(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("key","EEE");
        startActivity(intent);
    }




    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
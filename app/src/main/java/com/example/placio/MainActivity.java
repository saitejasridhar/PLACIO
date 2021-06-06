package com.example.placio;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        Button login_button =  findViewById(R.id.login);
        Button signup_button= findViewById(R.id.signup);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String str =preferences.getString("isFirst", "");
        String str1 =preferences.getString("isForm", "");
        String str2 =preferences.getString("isHome", "");
        String str3 =preferences.getString("isReg", "");


        if (auth.getCurrentUser() != null) {
            if(str.equals("False"))
            openNewActivity(MainHome.class);
            if(str2.equals("False"))
                openNewActivity(Home.class);
            if(str3.equals("False"))
                openNewActivity(Register.class);
        }
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(Signup.class);
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity(Login.class);
            }
        });
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

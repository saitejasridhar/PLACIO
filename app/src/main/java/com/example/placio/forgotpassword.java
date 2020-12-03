package com.example.placio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {
    private Button restbutton;
    private EditText email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        restbutton=(Button) findViewById(R.id.sendmail);
        email=(EditText) findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().hide();

        restbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail=email.getText().toString();
                if(TextUtils.isEmpty(userEmail))
                {
                    Toast.makeText(forgotpassword.this,"Please Enter Email",Toast.LENGTH_LONG).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Check your mail", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            }
                            else
                            {
                                String mes=task.getException().getMessage();
                                Toast.makeText(getApplicationContext(),"error"+mes,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
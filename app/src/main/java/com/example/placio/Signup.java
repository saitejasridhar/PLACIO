package com.example.placio;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Signup extends AppCompatActivity implements View.OnClickListener {


    EditText editTextEmail, editTextPassword,editTextConfirmPassword;
    private FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;

    public static final String mypreference = "mypref";
    public static final String isFirst = "false";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();



        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);



        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Registration Successful Please Verify mail and log in",Toast.LENGTH_LONG).show();
                                auth.signOut();
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("isFirst", "True");
                                editor.apply();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public int SetValidation() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String cpassword=editTextConfirmPassword.getText().toString().trim();
        boolean isEmailValid,isPasswordValid,isCPasswordValid;


        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            isEmailValid = false;
        }
        else if(!email.endsWith("@nie.ac.in")){
            editTextEmail.setError("Enter college email");
            editTextEmail.requestFocus();
            isEmailValid = false;
        }
        else  {
            isEmailValid = true;
        }


        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            isPasswordValid = false;
        } else if (password.length() < 6) {
            editTextPassword.setError("Password is not secure");
            editTextPassword.requestFocus();
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
        }
        if (cpassword.isEmpty()) {
            editTextConfirmPassword.setError("Password Confirmation is required");
            editTextConfirmPassword.requestFocus();
            isCPasswordValid = false;
        } else if (!cpassword.equals(password)) {
            editTextConfirmPassword.setError("Passwords don't match");
            editTextConfirmPassword.requestFocus();
            isCPasswordValid = false;
        } else  {
            isCPasswordValid = true;
        }

        if (isEmailValid && isPasswordValid && isCPasswordValid)
            return 1;
            else
                return 0;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(SetValidation()==1) {
                            registerUser();
                            finish();
                            startActivity(new Intent(Signup.this, Login.class));
                        }
                    }
                },3000);

                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}
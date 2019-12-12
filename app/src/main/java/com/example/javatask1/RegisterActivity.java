package com.example.javatask1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dynamicyield.dyapi.DYApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity{

    EditText tvName, tvEmail, tvPassword, tvBirthday;
    Button btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);
        tvBirthday = findViewById(R.id.tvBirthday);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void registerUser() {
        final String name = tvName.getText().toString();
        final String email = tvEmail.getText().toString();
        String password = tvPassword.getText().toString();
        final String birthday = tvBirthday.getText().toString();

        if (email.isEmpty()) {
            tvEmail.setError("email required");
            tvEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            tvPassword.setError("password needed");
            tvPassword.requestFocus();
            return;
        }

        if (birthday.isEmpty()) {
            tvBirthday.setError("birthday needed");
            tvBirthday.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final User user = new User(name, email, birthday);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                                String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(email);

                                try {
                                    JSONObject Signup = new JSONObject();
                                    Signup.put("dyType", "signup-v1");
                                    Signup.put("hashedEmail", sha256hex);

                                    DYApi.getInstance().trackEvent("Signup", Signup);
                                    Log.d("signupWDY", "onComplete: tracking DY Event");
                                } catch (Exception e) {
                                    Log.d("signUpException", "DY signup event failed ");
                                }
                                //Populating the Prop parameter

//Sending the Event

                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("email", email);
                                setResult(RESULT_OK, returnIntent);
                                finish();

                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void doRegistrationForFirebase(View v) {
        registerUser();
//        Intent intent = new Intent();
//
//        intent.putExtra("firstName", "hello");
//        setResult(RESULT_OK, intent);
//        finish();
    }
}

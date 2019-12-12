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
import com.dynamicyield.engine.DYPageContext;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnRegister, btnLogin;
    EditText tvEmail, tvPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);

        mAuth = FirebaseAuth.getInstance();

    }

    private void loginUser() {
        final String email = tvEmail.getText().toString();
        String password = tvPassword.getText().toString();

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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("email success", "login successfully");
                            FirebaseUser user = mAuth.getCurrentUser();

                            JSONObject login = new JSONObject();
                            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(email);

                            try {
                                login.put("hashedEmail", sha256hex);
                                login.put("dyType", "login-v1");
//Sending the Event
                                DYApi.getInstance().trackEvent("login", login);
                                DYApi.getInstance().trackPageView("javaTest1",new DYPageContext("en_US",DYPageContext.OTHER,null));

                                DYLib.getInstance(MainActivity.this).setDyExperimentsStateListener(new DYLib.DyExperimentsStateListener() {
                                    @Override
                                    public void experimentsReady() {

                                    }

                                });

                            } catch (Exception e) {
                                Log.d("signInDYException", "failed to track login.");
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void doLogin(View v) {
        loginUser();
    }

    public void doRegister(View v) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 1);
    }

}

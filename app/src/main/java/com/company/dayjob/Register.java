package com.company.dayjob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

class User {
    private String username;
    private String email;

    public User() {

    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

public class Register extends AppCompatActivity {

    private Button btnLogin;

    private EditText edtUsername, edtEmail, edtPassword, edtPassword2;
    private Button btnSubmit;

    private ImageButton btnEye, btnEye2;
    boolean f1 = false, f2 = false;

    private Button btnGoogle, btnFacebook, btnApple;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private boolean validateData(String email, String p1, String p2) {
        if(email.trim().equals("") || p1.trim().equals("") || p2.trim().equals("")) {
            return false;
        }

        if(!p1.trim().equals(p2.trim())) {
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        edtUsername = findViewById(R.id.edt_username);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtPassword2 = findViewById(R.id.edt_password2);

        btnEye = findViewById(R.id.btn_eye);
        btnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType;

                if(f1) {
                    inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                }
                else {
                    inputType = InputType.TYPE_CLASS_TEXT;
                }

                edtPassword.setInputType(inputType);
                f1 = !f1;
            }
        });

        btnEye2 = findViewById(R.id.btn_eye2);
        btnEye2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType;

                if(f2) {
                    inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                }
                else {
                    inputType = InputType.TYPE_CLASS_TEXT;
                }

                edtPassword2.setInputType(inputType);
                f2 = !f2;
            }
        });

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String password2 = edtPassword2.getText().toString().trim();

                if(!validateData(email, password, password2)) {
                    Toast.makeText(Register.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                    edtEmail.setText("");
                    edtPassword.setText("");
                    edtPassword2.setText("");
                    shakeItBaby();

                    return;
                }

                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("**INFO**", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    mDatabase = FirebaseDatabase.getInstance().getReference();

                                    String username = edtUsername.getText().toString().trim();
                                    String email = user.getEmail();

                                    User newUser = new User(username, email);
                                    mDatabase.child("Users").child(user.getUid()).setValue(newUser);

                                    Intent intent = new Intent(Register.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("**INFO**", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    shakeItBaby();
                                }
                            }
                        });
            }
        });

        btnGoogle = findViewById(R.id.btn_google_login);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add Register with Google
            }
        });

        btnFacebook = findViewById(R.id.btn_facebook_login);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add Register with Facebook
            }
        });

        btnApple = findViewById(R.id.btn_apple_login);
        btnApple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add Register with Apple
            }
        });
    }

    private void shakeItBaby() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
    }
}
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

public class Login extends AppCompatActivity {

    private Button btnSignUp;
    private EditText edtEmail, edtPassword;

    private ImageButton btnEye;
    private boolean f = false;

    private Button btnSubmit;
    private Button btnGoogle, btnFacebook, btnApple;

    private FirebaseAuth mAuth;

    private boolean validateData(String email, String p1) {
        if(email.trim().equals("") || p1.trim().equals("")) {
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignUp = findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        btnEye = findViewById(R.id.btn_eye);
        btnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType;

                if(f) {
                    inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                }
                else {
                    inputType = InputType.TYPE_CLASS_TEXT;
                }

                edtPassword.setInputType(inputType);
                f = !f;
            }
        });

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if(!validateData(email, password)) {
                    Toast.makeText(Login.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                    edtEmail.setText("");
                    edtPassword.setText("");
                    shakeItBaby();

                    return;
                }

                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("**INFO**", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Intent intent = new Intent(Login.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("**INFO**", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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
                //TODO: Add Login with Google
            }
        });

        btnFacebook = findViewById(R.id.btn_facebook_login);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add Login with Facebook
            }
        });

        btnApple = findViewById(R.id.btn_apple_login);
        btnApple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add Login with Apple
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
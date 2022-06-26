package com.company.dayjob;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class login_register extends AppCompatActivity {
    Button login, register;
    private View animView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        login = findViewById(R.id.signin);
        register = findViewById(R.id.signup);

        animView = findViewById(R.id.anim_view);

        ObjectAnimator animatorSpin = ObjectAnimator.ofFloat(animView, "rotation", 1000f);
        ObjectAnimator animatorMove = ObjectAnimator.ofFloat(animView, "translationX", 1000f);

        animatorSpin.setDuration(4000);
        animatorMove.setDuration(4000);

        animatorSpin.start();
        animatorMove.start();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });
    }
}
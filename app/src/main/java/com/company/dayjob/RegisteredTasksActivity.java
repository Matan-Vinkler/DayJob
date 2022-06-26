package com.company.dayjob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisteredTasksActivity extends AppCompatActivity {
    private Button btnBack;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private LinearLayout tasksLayout;
    private FrameLayout frameLayout;
    private FrameLayout.LayoutParams params;
    private FragmentManager ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_tasks);

        tasksLayout = findViewById(R.id.tasks_layout);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisteredTasksActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        mDatabase.child("RegisteredUsers").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    String id = snapshot1.getKey();

                    mDatabase.child("Tasks").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Task task = snapshot.getValue(Task.class);
                            generateFragment(task, id);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void generateFragment(Task task, String taskID) {
        frameLayout = new FrameLayout(RegisteredTasksActivity.this);
        frameLayout.setId(View.generateViewId());

        params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(params);

        tasksLayout.addView(frameLayout, params);

        ft = getSupportFragmentManager();

        if(!ft.isDestroyed()) {
            ft.beginTransaction().add(frameLayout.getId(), TaskFragment.newInstance(task, taskID, "Reg")).commit();
        }
    }
}
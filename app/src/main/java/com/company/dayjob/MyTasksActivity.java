package com.company.dayjob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
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

public class MyTasksActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_my_tasks);

        tasksLayout = findViewById(R.id.tasks_layout);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTasksActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        mDatabase.child("Tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    Task task = child.getValue(Task.class);

                    if(task.getOwnerID().equals(currentUser.getUid())) {
                        generateFragment(task, child.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void generateFragment(Task task, String taskID) {
        frameLayout = new FrameLayout(MyTasksActivity.this);
        frameLayout.setId(View.generateViewId());

        params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(params);

        tasksLayout.addView(frameLayout, params);

        ft = getSupportFragmentManager();

        if(!ft.isDestroyed()) {
            ft.beginTransaction().add(frameLayout.getId(), TaskFragment.newInstance(task, taskID, "My")).commit();
        }
    }
}
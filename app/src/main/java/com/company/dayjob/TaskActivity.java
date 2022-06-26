package com.company.dayjob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TaskActivity extends AppCompatActivity {

    private TextView txtTitle, txtSalary, txtDescription, txtLocation, txtDate;
    private Button btnBack, btnAccept, btnDecline;

    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;

    private String ownerID, ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        ID = getIntent().getStringExtra("ID");
        String backActivity = getIntent().getStringExtra("BackActivity");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        txtTitle = findViewById(R.id.txt_title);
        txtSalary = findViewById(R.id.txt_salary);
        txtDescription = findViewById(R.id.txt_description);
        txtLocation = findViewById(R.id.txt_location);
        txtDate = findViewById(R.id.txt_date);

        btnBack = findViewById(R.id.btn_back);
        btnAccept = findViewById(R.id.btn_accept);
        btnDecline = findViewById(R.id.btn_decline);

        View.OnClickListener goBack = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                switch (backActivity) {
                    case "Home":
                        intent = new Intent(TaskActivity.this, HomeActivity.class);
                        break;
                    case "My":
                        intent = new Intent(TaskActivity.this, MyTasksActivity.class);
                        break;
                    case "Reg":
                        intent = new Intent(TaskActivity.this, RegisteredTasksActivity.class);
                        break;
                    default:
                        intent = null;
                        break;
                }

                startActivity(intent);
                finish();
            }
        };

        btnBack.setOnClickListener(goBack);
        btnDecline.setOnClickListener(goBack);

        mDatabase.child("Tasks").child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Task task = snapshot.getValue(Task.class);

                txtTitle.setText(task.getName());
                txtSalary.setText(task.getSalary() + "$");
                txtDescription.setText(task.getDescription());
                txtLocation.setText(task.getLocation());
                txtDate.setText(task.getDateTime().split(", ")[0]);

                ownerID = task.getOwnerID();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("RegisteredUsers").child(firebaseUser.getUid()).child(ID).setValue(true);

                Intent intent = new Intent(TaskActivity.this, TaskSelectedActivity.class);
                intent.putExtra("UID", ownerID);
                startActivity(intent);
                finish();
            }
        });
    }
}
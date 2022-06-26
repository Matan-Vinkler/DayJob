package com.company.dayjob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TaskSelectedActivity extends AppCompatActivity {

    private Button btnContact, btnBack;
    private TextView txtMessage;

    private String username, email;
    private String firstName;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_selected);

        txtMessage = findViewById(R.id.txt_message);

        String uid = getIntent().getStringExtra("UID");

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskSelectedActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnContact = findViewById(R.id.btn_contact);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                username = user.getUsername();
                email = user.getEmail();

                if(username.contains(" ")) {
                    firstName = username.split(" ")[0];
                }
                else {
                    firstName = username;
                }

                txtMessage.setText("You're about to help " + firstName + "!");
                btnContact.setText("Contact " + firstName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Job Contact");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "I'm interested in your job! What is the information?");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("message/rfc822");

                startActivity(Intent.createChooser(emailIntent, "Choose an Email client:"));
            }
        });
    }
}
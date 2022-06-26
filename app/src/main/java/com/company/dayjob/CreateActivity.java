package com.company.dayjob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateActivity extends AppCompatActivity {

    private EditText edtName, edtDate, edtTime, edtLocation, edtHelpers, edtSalary, edtDescription;
    private Button btnBack, btnSubmit;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        edtName = findViewById(R.id.edt_name);
        edtDate = findViewById(R.id.edt_date);
        edtTime = findViewById(R.id.edt_time);
        edtLocation = findViewById(R.id.edt_location);
        edtHelpers = findViewById(R.id.edt_helpers);
        edtSalary = findViewById(R.id.edt_salary);
        edtDescription = findViewById(R.id.edt_description);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextWatcher dateWatcher = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    edtDate.setText(current);
                    edtDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        edtDate.addTextChangedListener(dateWatcher);

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edtTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Task task = new Task();

                    String name = edtName.getText().toString();
                    String dateTime = edtDate.getText().toString() + ", " + edtTime.getText().toString();
                    String location = edtLocation.getText().toString();
                    String description = edtDescription.getText().toString();
                    String ownerID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    int helpers = Integer.parseInt(edtHelpers.getText().toString());
                    int salary = Integer.parseInt(edtSalary.getText().toString());

                    validateData(name);
                    validateData(dateTime);
                    validateData(location);
                    validateData(description);

                    task.setName(name);
                    task.setDateTime(dateTime);
                    task.setLocation(location);
                    task.setDescription(description);
                    task.setHelpers(helpers);
                    task.setSalary(salary);
                    task.setOwnerID(ownerID);

                    mDatabase.child("Tasks").push().setValue(task);

                    Intent intent = new Intent(CreateActivity.this, TaskCreatedActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void validateData(String data) throws Exception {
        if(data.trim().equals("")) {
            throw new Exception("Invalid Data");
        }
    }
}
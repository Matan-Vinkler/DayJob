package com.company.dayjob;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

class Task {
    private String Name;
    private String DateTime;
    private String Description;
    private int Helpers;
    private int Salary;
    private String Location;
    private String OwnerID;

    public Task() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        this.DateTime = dateTime;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public int getHelpers() {
        return Helpers;
    }

    public void setHelpers(int helpers) {
        this.Helpers = helpers;
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int salary) {
        this.Salary = salary;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        this.OwnerID = ownerID;
    }
}

public class HomeActivity extends AppCompatActivity {

    private EditText edtSearch;
    private ImageButton btnAdd, btnMic, btnMenu;
    private SeekBar seekBar;
    private Switch btnSwitch;
    private NavigationView navigationView;

    private DatabaseReference mDatabase;

    private LinearLayout tasksLayout;
    private FrameLayout frameLayout;
    private FrameLayout.LayoutParams params;
    private FragmentManager ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tasksLayout = findViewById(R.id.tasks_layout);

        edtSearch = findViewById(R.id.edt_search);
        btnAdd = findViewById(R.id.btn_add);
        btnMic = findViewById(R.id.btn_mic);
        btnMenu = findViewById(R.id.btn_menu);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                int id = item.getItemId();

                if(id == R.id.menu_about) {
                    Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.menu_signout) {
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(HomeActivity.this, login_register.class);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.menu_create) {
                    Intent intent = new Intent(HomeActivity.this, CreateActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.menu_mytask) {
                    Intent intent = new Intent(HomeActivity.this, MyTasksActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.menu_regtask) {
                    Intent intent = new Intent(HomeActivity.this, RegisteredTasksActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            }
        });

        View headerLayout = navigationView.getHeaderView(0);
        seekBar = headerLayout.findViewById(R.id.seek_bar);
        btnSwitch = headerLayout.findViewById(R.id.switch_btn);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        BroadcastReceiver receiver = new BatteryBroadcastReciever();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    Task task = child.getValue(Task.class);
                    generateFragment(task, child.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                tasksLayout.removeAllViews();

                mDatabase.child("Tasks").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child: snapshot.getChildren()) {
                            String filter = edtSearch.getText().toString().trim().toLowerCase();
                            Task task = child.getValue(Task.class);

                            if(task.getName().toLowerCase().startsWith(filter)) {
                                generateFragment(task, child.getKey());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to Text");
                startActivityForResult(speechIntent, 1);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CreateActivity.class);
                startActivity(intent);
                finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                btnSwitch.setChecked(progress != 0);
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private int prog = manager.getStreamVolume(AudioManager.STREAM_MUSIC);

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!btnSwitch.isChecked()) {
                    prog = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    manager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
                    seekBar.setProgress(0);
                }
                else {
                    manager.setStreamVolume(AudioManager.STREAM_MUSIC, prog, AudioManager.FLAG_SHOW_UI);
                    seekBar.setProgress(prog);
                }
            }
        });
    }

    private void generateFragment(Task task, String taskID) {
        frameLayout = new FrameLayout(HomeActivity.this);
        frameLayout.setId(View.generateViewId());

        params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(params);

        tasksLayout.addView(frameLayout, params);

        ft = getSupportFragmentManager();

        if(!ft.isDestroyed()) {
            ft.beginTransaction().add(frameLayout.getId(), TaskFragment.newInstance(task, taskID, "Home")).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            edtSearch.setText(matches.get(0).toString());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
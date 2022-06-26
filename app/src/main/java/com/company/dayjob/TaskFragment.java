package com.company.dayjob;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM0 = "param0";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";

    private String mTaskID;      //param0
    private String mTaskName;    //param1
    private String mDateTime;    //param2
    private String mDescription; //param3
    private String mHelpers;     //param4
    private String mSalary;      //param5
    private String mBackActivity;

    private TextView txtName, txtDateTime, txtDescription, txtHelpers, txtSalary;
    private CardView cardTask;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(Task task, String taskID, String backActivity) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM0, taskID);
        args.putString(ARG_PARAM1, task.getName());
        args.putString(ARG_PARAM2, task.getDateTime());
        args.putString(ARG_PARAM3, task.getDescription());
        args.putString(ARG_PARAM4, Integer.toString(task.getHelpers()));
        args.putString(ARG_PARAM5, Integer.toString(task.getSalary()));
        args.putString(ARG_PARAM6, backActivity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTaskID = getArguments().getString(ARG_PARAM0);
            mTaskName = getArguments().getString(ARG_PARAM1);
            mDateTime = getArguments().getString(ARG_PARAM2);
            mDescription = getArguments().getString(ARG_PARAM3);
            mHelpers = getArguments().getString(ARG_PARAM4);
            mSalary = getArguments().getString(ARG_PARAM5);
            mBackActivity = getArguments().getString(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtName = view.findViewById(R.id.txt_name);
        txtDateTime = view.findViewById(R.id.txt_datetime);
        txtDescription = view.findViewById(R.id.txt_description);
        txtHelpers = view.findViewById(R.id.txt_helpers);
        txtSalary = view.findViewById(R.id.txt_salary);

        txtDateTime.setText(mDateTime);
        txtHelpers.setText(mHelpers + " Helpers");
        txtSalary.setText(mSalary + "$");

        if(mTaskName.length() >= 15) {
            txtName.setText(mTaskName.substring(0, 11) + "...");
        }
        else {
            txtName.setText(mTaskName);
        }

        if(mDescription.length() >= 45) {
            txtDescription.setText(mDescription.substring(0, 42) + "...");
        }
        else {
            txtDescription.setText(mDescription);
        }

        cardTask = view.findViewById(R.id.card_task);
        cardTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TaskActivity.class);
                intent.putExtra("ID", mTaskID);
                intent.putExtra("BackActivity", mBackActivity);

                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
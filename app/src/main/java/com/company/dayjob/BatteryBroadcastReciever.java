package com.company.dayjob;

import android.app.AlertDialog;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Handler;

public class BatteryBroadcastReciever extends BroadcastReceiver {
    private int level = -1;
    private boolean flag = true;


    @Override
    public void onReceive(Context context, Intent intent) {
        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        if(level < 20 && flag) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Your battery level is under 20%. Connect your device to a charger.");
            builder.setTitle("Battery Low");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            flag = false;
        }

        if(level >= 20) {
            flag = true;
        }
    }
}

package com.company.dayjob;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.company.dayjob.R;

public class MusicService extends Service {
    private MediaPlayer player;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.player = MediaPlayer.create(this, R.raw.audio);
        this.player.setLooping(true);
        this.player.setVolume(100, 100);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.player.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.player.stop();
        this.player.release();
    }
}
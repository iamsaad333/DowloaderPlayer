package com.mp4andmp3.superextremeplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import java.io.IOException;

public class AudioService extends Service {
    MediaPlayer mediaPlayer;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        this.mediaPlayer = new MediaPlayer();
        try {
            this.mediaPlayer.setDataSource(intent.getStringExtra("audioPath"));
            this.mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mediaPlayer.seekTo(intent.getIntExtra("audioTime", 0));
        this.mediaPlayer.start();
        return START_STICKY;
    }

    public void onDestroy() {
        this.mediaPlayer.stop();
    }
}

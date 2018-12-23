package com.wuchangi.dailymood.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.wuchangi.dailymood.R;

public class BGMService extends Service {

    private MediaPlayer mBGMPlayer = null;

    public BGMService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startBGM();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBGM();
    }


    private void startBGM() {
        if(mBGMPlayer == null){
            mBGMPlayer = MediaPlayer.create(this, R.raw.bgm);
            mBGMPlayer.start();

            mBGMPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mBGMPlayer.start();
                    //设置循环播放
                    mBGMPlayer.setLooping(true);
                }
            });
        }
    }

    private void stopBGM() {
        if(mBGMPlayer != null && mBGMPlayer.isPlaying()){
            mBGMPlayer.stop();
            mBGMPlayer.release();
            mBGMPlayer = null;
        }
    }
}

package com.dsl.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button playBtn, forwardBtn, backwardBtn, nextBtn, prevBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;
    Runnable runnable;
    Handler handler;
    private Handler hdlr = new Handler();
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        playBtn = (Button) findViewById(R.id.playBtn);
        forwardBtn = (Button) findViewById(R.id.forwardBtn);
        backwardBtn = (Button) findViewById(R.id.backwardBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        prevBtn = (Button) findViewById(R.id.prevBtn);
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        handler = new Handler();
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();


        forwardBtn.setOnClickListener(this);
        backwardBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

      volumeBar = (SeekBar) findViewById(R.id.volumeBar);

        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                positionBar.setMax(mp.getDuration());
                mp.start();
                changePositionBar();
            }
        });

        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mp.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void changePositionBar() {
        positionBar.setProgress(mp.getCurrentPosition());
        if (mp.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    changePositionBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }



    public void playBtnClick(View view) {
        if (!mp.isPlaying()) {
                  mp.start();
                  playBtn.setBackgroundResource(R.drawable.stop);
                  eTime = mp.getDuration();
                  sTime = mp.getCurrentPosition();
                  if(oTime == 0){
                      positionBar.setMax(eTime);
                      oTime =1;
                  }

                  remainingTimeLabel.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(eTime),
                          TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                  elapsedTimeLabel.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(sTime),
                          TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
                  positionBar.setProgress(sTime);
                  //handler.postDelayed(UpdateSongTime, 100);
                  changePositionBar();
              } else {
                  mp.pause();
                  playBtn.setBackgroundResource(R.drawable.play);
              }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.forwardBtn:
                mp.seekTo(mp.getCurrentPosition() + 5000);
                break;
            case R.id.backwardBtn:
                mp.seekTo(mp.getCurrentPosition() - 50000);
                break;
        }

    }

    /*private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = mp.getCurrentPosition();
            elapsedTimeLabel.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );
            positionBar.setProgress(sTime);
            hdlr.postDelayed(this, 100);
        }
    }; Not really working for now
    */

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) { }





}

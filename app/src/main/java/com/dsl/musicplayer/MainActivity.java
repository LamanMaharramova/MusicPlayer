package com.dsl.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button playBtn, forwardBtn, backwardBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;
    Runnable runnable;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (Button) findViewById(R.id.playBtn);
        forwardBtn = (Button) findViewById(R.id.forwardBtn);
        backwardBtn = (Button) findViewById(R.id.backwardBtn);
        positionBar = findViewById(R.id.positionBar);
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

//      volumeBar = (SeekBar) findViewById(R.id.volumeBar);
//        volumeBar.setOnSeekBarChangeListener(
//                new SeekBar.OnSeekBarChangeListener() {
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        float volumeNum = progress / 100f;
//                        mp.setVolume(volumeNum, volumeNum);
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//
//                    }
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//
//                    }
//                }
//        );

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

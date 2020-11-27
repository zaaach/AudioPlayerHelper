package com.zaaach.audioplayer;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zaaach.audioplayerhelper.AudioPlayerHelper;
import com.zaaach.audioplayerhelper.OnAudioPlayStateChangeListener;

public class MainActivity extends AppCompatActivity implements OnAudioPlayStateChangeListener {
    private static final String MUSIC_URL = "http://music.163.com/song/media/outer/url?id=574633384.mp3";
    private static final String MUSIC_URL_2 = "http://music.163.com/song/media/outer/url?id=1498342485.mp3";

    private AudioPlayerHelper playerHelper;
    private Button playBtn;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
        seekBar = findViewById(R.id.seek_bar);
        playBtn = findViewById(R.id.btn_play_or_pause);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerHelper.setDataSource(MUSIC_URL_2);
                playerHelper.playOrPause();
            }
        });
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerHelper.stop();
            }
        });

        playerHelper = new AudioPlayerHelper(this)
                .attachSeekBar(seekBar)
                .setLooping(true)
                .setDebug(true)
                .setOnAudioPlayStateChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerHelper.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playerHelper.restore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerHelper.destroy();
    }

    //=================================
    // 播放器回调
    //=================================
    @Override
    public void onPreparing(MediaPlayer player) {
    }

    @Override
    public void onPrepared(MediaPlayer player, long duration) {
        seekBar.setMax((int) duration);
    }

    @Override
    public void onPlaying(MediaPlayer player) {
        playBtn.setText("暂停");
    }

    @Override
    public void onProgress(MediaPlayer player, @Nullable SeekBar seekBar, boolean isDragging, long position, long duration) {
        if (seekBar != null && !isDragging) {
            seekBar.setProgress((int) position);
        }
    }

    @Override
    public void onPlayPaused(MediaPlayer player) {
        playBtn.setText("继续");
    }

    @Override
    public void onPlayStop(MediaPlayer player) {
        playBtn.setText("播放");
    }

    @Override
    public void onPlayComplete(MediaPlayer player) {

    }

    @Override
    public void onPlayError(String msg) {

    }
}
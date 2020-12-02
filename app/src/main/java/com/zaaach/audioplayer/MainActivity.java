package com.zaaach.audioplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zaaach.audioplayerhelper.AudioPlayerHelper;
import com.zaaach.audioplayerhelper.OnAudioPlayStateChangeListener;

import java.util.Locale;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity implements OnAudioPlayStateChangeListener {
    private static final String MUSIC_URL = "http://music.163.com/song/media/outer/url?id=574633384.mp3";

    private AudioPlayerHelper playerHelper;
    private Button playBtn;
    private SeekBar seekBar;
    private TextView tvTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTimer = findViewById(R.id.tv_timer);
        seekBar = findViewById(R.id.seek_bar);
        playBtn = findViewById(R.id.btn_play_or_pause);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                .setDataSource(MUSIC_URL)
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

    public String formatTime(long mills){
        String standardTime;
        long seconds = mills / 1000;
        if (seconds <= 0){
            standardTime = "00:00";
        } else if (seconds < 60) {
            standardTime = String.format(Locale.getDefault(), "00:%02d", seconds % 60);
        } else if (seconds < 3600) {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
        } else {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 60);
        }
        return standardTime;
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
        tvTimer.setText("00:00 / " + formatTime(duration));
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
        tvTimer.setText(formatTime(position) + " / " + formatTime(duration));
    }

    @Override
    public void onPlayPaused(MediaPlayer player) {
        playBtn.setText("继续");
    }

    @Override
    public void onPlayStop(MediaPlayer player) {
        playBtn.setText("播放");
        seekBar.setProgress(0);
        tvTimer.setText("00:00 / " + formatTime(player.getDuration()));
    }

    @Override
    public void onPlayComplete(MediaPlayer player) {
        Log.e("Audio", "onPlayComplete");
    }

    @Override
    public void onPlayError(String msg) {
        Log.e("Audio", msg);
        seekBar.setProgress(0);
        tvTimer.setText("00:00 / 00:00");
    }
}
package com.zaaach.audioplayerhelper;

import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.SeekBar;

import java.io.IOException;

/**
 * @Author: Zaaach
 * @Date: 2020/11/26
 * @Email: zaaach@aliyun.com
 * @Description: 音频播放辅助类
 */
public class AudioPlayerHelper implements IAudioPlayer, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {
    public static final String TAG = "AudioPlayerHelper";

    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int PROGRESS_INTERVAL = 1000;//进度更新间隔时间ms

    private Context context;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private Handler mHandler;
    private int state = STATE_IDLE;

    private AudioFocusRequest audioFocusRequest;
    private OnAudioPlayStateChangeListener onAudioPlayStateChangeListener;
    private SeekBar mSeekBar;

    private String audioPath;
    private boolean isDragging;//是否正在拖动seekbar
    private boolean looping;
    private int interval = PROGRESS_INTERVAL;//进度更新间隔时间

    /**
     * 初始化
     * @param context
     */
    public AudioPlayerHelper(Context context) {
        this.context = context;
        mHandler = new Handler(Looper.getMainLooper());
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        initMediaPlayer();
    }

    private void initMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public AudioPlayerHelper setOnAudioPlayStateChangeListener(OnAudioPlayStateChangeListener listener) {
        this.onAudioPlayStateChangeListener = listener;
        return this;
    }

    /**
     * 关联SeekBar之后，seekBar的拖动事件无需再处理，进度只需要在回调里更新即可
     * @param seekBar
     * @return
     */
    public AudioPlayerHelper attachSeekBar(SeekBar seekBar){
        this.mSeekBar = seekBar;
        setupSeekBar();
        return this;
    }

    public AudioPlayerHelper setLooping(boolean looping){
        this.looping = looping;
        return this;
    }

    public AudioPlayerHelper setInterval(int millis){
        this.interval = millis;
        return this;
    }

    public AudioPlayerHelper setDebug(boolean debug){
        ALog.DEBUG = debug;
        return this;
    }

    public AudioPlayerHelper setDataSource(String path){
        this.audioPath = path;
        return this;
    }

    private void setupSeekBar() {
        if (mSeekBar != null){
            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    isDragging = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    isDragging = false;
                    if (isPlaying() || isPaused()){
                        mediaPlayer.seekTo(seekBar.getProgress());
                    }else {
                        seekBar.setProgress(0);
                    }
                }
            });
        }
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying()){
                if (onAudioPlayStateChangeListener != null){
                    onAudioPlayStateChangeListener.onProgress(mediaPlayer, mSeekBar, isDragging, mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                }
                mHandler.postDelayed(timerRunnable, interval);
            }
        }
    };

    @Override
    public void onPrepared(MediaPlayer mp) {
        ALog.d(TAG, "======================onPrepared()");
        int duration = mp.getDuration();
        state = STATE_PREPARED;
        if (onAudioPlayStateChangeListener != null){
            onAudioPlayStateChangeListener.onPrepared(mp, duration);
        }
        requestAudioFocus();
    }

    private void startPlayer(){
        if (mediaPlayer == null) return;
        if (isPlaying()) return;
        ALog.d(TAG, "======================startPlayer()");
        mediaPlayer.start();
        state = STATE_PLAYING;
        mHandler.postDelayed(timerRunnable, interval);
        if (onAudioPlayStateChangeListener != null) {
            onAudioPlayStateChangeListener.onPlaying(mediaPlayer);
        }
    }

    private void requestAudioFocus() {
        if (isPreparing() || isPlaying()) return;
        ALog.d(TAG, "======================requestAudioFocus()");
        int focusResult;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(this)
                    .build();
            audioFocusRequest.acceptsDelayedFocusGain();
            focusResult = audioManager.requestAudioFocus(audioFocusRequest);
        }else {
            focusResult = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
        if (focusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            startPlayer();
        }else {
            if (onAudioPlayStateChangeListener != null) {
                onAudioPlayStateChangeListener.onPlayError("播放失败,音频服务可能已被其他程序占用!");
            }
        }
    }

    private void abandonAudioFocus(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if (audioFocusRequest != null) {
                audioManager.abandonAudioFocusRequest(audioFocusRequest);
            }
        }else {
            audioManager.abandonAudioFocus(this);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        state = STATE_IDLE;
        if (onAudioPlayStateChangeListener != null){
            onAudioPlayStateChangeListener.onPlayComplete(mp);
        }
        if (mSeekBar != null && !mediaPlayer.isLooping()){
            mSeekBar.setProgress(0);
        }
        mHandler.removeCallbacks(timerRunnable);
        if (mediaPlayer.isLooping()){
            startPlayer();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (onAudioPlayStateChangeListener != null){
            onAudioPlayStateChangeListener.onPlayError("error: " + what);
        }
        if (mSeekBar != null){
            mSeekBar.setProgress(0);
        }
        return false;
    }

    @Override
    public boolean isIdle() {
        return state == STATE_IDLE;
    }

    @Override
    public boolean isPreparing() {
        return state == STATE_PREPARING;
    }

    @Override
    public boolean isPrepared() {
        return state == STATE_PREPARED;
    }

    @Override
    public boolean isPlaying() {
        return state == STATE_PLAYING;
    }

    @Override
    public boolean isPaused() {
        return state == STATE_PAUSED;
    }

    @Override
    public void playOrPause() {
        if (isPreparing()){
            stop();
        }else if (isPlaying()){
            pause();
        }else if (isPaused()){
            restore();
        }else {
            doPlay();
        }
    }

    @Override
    public void play(String path) {
        //如果已处于播放，先停止再重新播放
        if (mediaPlayer != null && !isIdle()){
            mediaPlayer.stop();
        }
        doPlay(path);
    }

    private void doPlay(){
        doPlay(audioPath);
    }

    private void doPlay(String path){
        this.audioPath = path;
        if (TextUtils.isEmpty(audioPath)) {
            throw new RuntimeException(TAG + "======================audio path must not be null");
        }
        try {
            if (mediaPlayer == null){
                initMediaPlayer();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.setLooping(looping);
            mediaPlayer.prepareAsync();
            state = STATE_PREPARING;
            if (onAudioPlayStateChangeListener != null){
                onAudioPlayStateChangeListener.onPreparing(mediaPlayer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (onAudioPlayStateChangeListener != null){
                onAudioPlayStateChangeListener.onPlayError("error:" + e.getMessage());
            }
        }
    }

    @Override
    public void seekTo(int msec) {
        if (isPlaying() || isPaused()){
            mediaPlayer.seekTo(msec);
        }
    }

    @Override
    public void pause() {
        if (!isPlaying()) return;
        ALog.d(TAG, "======================pause()");
        if (mediaPlayer != null){
            mediaPlayer.pause();
        }
        state = STATE_PAUSED;
        abandonAudioFocus();
        mHandler.removeCallbacks(timerRunnable);
        if (onAudioPlayStateChangeListener != null){
            onAudioPlayStateChangeListener.onPlayPaused(mediaPlayer);
        }
    }

    @Override
    public void restore() {
        if (isIdle()) return;
        ALog.d(TAG, "======================restore()");
        if (isPaused()){
            requestAudioFocus();
        }
    }

    @Override
    public void stop() {
        abandonAudioFocus();
        if (isIdle()) return;
        ALog.d(TAG, "======================stop()");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        state = STATE_IDLE;
        if (onAudioPlayStateChangeListener != null){
            onAudioPlayStateChangeListener.onPlayStop(mediaPlayer);
        }
        if (mSeekBar != null){
            mSeekBar.setProgress(0);
        }
    }

    @Override
    public void destroy() {
        onAudioPlayStateChangeListener = null;
        stop();
        if (mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        context = null;
        ALog.d(TAG, "======================destroy()");
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange){
            case AudioManager.AUDIOFOCUS_GAIN:
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE:
                ALog.d(TAG, "======================onAudioFocusChange()：获取焦点");
                startPlayer();
                break;
            case AudioManager.AUDIOFOCUS_LOSS://长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时
                ALog.d(TAG, "======================onAudioFocusChange()：失去焦点");
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://短暂性丢失焦点并作降音处理
                break;
        }
    }
}

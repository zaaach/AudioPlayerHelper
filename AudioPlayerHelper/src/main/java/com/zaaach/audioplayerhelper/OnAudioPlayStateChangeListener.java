package com.zaaach.audioplayerhelper;

import android.media.MediaPlayer;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

/**
 * @Author: Zaaach
 * @Date: 2020/11/26
 * @Email: zaaach@aliyun.com
 * @Description: 音频播放状态变化监听器
 */
public interface OnAudioPlayStateChangeListener {
    void onPreparing(MediaPlayer player);
    void onPrepared(MediaPlayer player, long duration);
    void onPlaying(MediaPlayer player);
    void onProgress(MediaPlayer player, @Nullable SeekBar seekBar, boolean isDragging, long position, long duration);
    void onPlayPaused(MediaPlayer player);
    void onPlayStop(MediaPlayer player);
    void onPlayComplete(MediaPlayer player);
    void onPlayError(String msg);
}

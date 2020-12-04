package com.zaaach.audioplayerhelper;

/**
 * @Author: Zaaach
 * @Date: 2020/11/26
 * @Email: zaaach@aliyun.com
 * @Description: 音频播放器
 */
public interface IAudioPlayer {
    boolean isIdle();
    boolean isPreparing();
    boolean isPrepared();
    boolean isPlaying();
    boolean isPaused();

    /**
     * 播放指定音频地址
     */
    void play(String path);

    /**
     * 播放or暂停播放，需要先设置音频地址
     */
    void playOrPause();

    /**
     * 跳转进度
     * @param msec
     */
    void seekTo(int msec);

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 恢复播放
     */
    void restore();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 销毁播放器
     */
    void destroy();
}

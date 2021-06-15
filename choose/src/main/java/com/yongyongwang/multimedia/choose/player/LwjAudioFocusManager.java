package com.yongyongwang.multimedia.choose.player;

import android.content.Context;
import android.media.AudioManager;

import androidx.annotation.NonNull;

/**
 * @author myselyhero
 *
 * desc:音频焦点管理
 *
 * @// TODO: 2021/6/7
 */
public final class LwjAudioFocusManager implements AudioManager.OnAudioFocusChangeListener {

    /**  */
    private AudioManager mAudioManager;

    /** 是否持有 */
    private boolean isHold;

    /** 当前音频焦点的状态 */
    private int mCurrentStatus = 0;

    private LwjAudioFocusListener mFocusListener;

    public LwjAudioFocusManager(@NonNull Context context, @NonNull LwjAudioFocusListener listener) {
        mFocusListener = listener;
        mAudioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onAudioFocusChange(final int focusChange) {
        if (mCurrentStatus == focusChange) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (focusChange) {
                    /** 获得焦点 */
                    case AudioManager.AUDIOFOCUS_GAIN:
                        /** 暂时获得焦点 */
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                        if (mFocusListener != null)
                            mFocusListener.onAcquire();
                        isHold = true;
                        break;
                        /** 焦点丢失(找不回来了) */
                    case AudioManager.AUDIOFOCUS_LOSS:
                        /** 暂时丢失 */
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        if (mFocusListener != null)
                            mFocusListener.onLose();
                        isHold = false;
                        break;
                        /** 降低音量 */
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        if (mFocusListener != null)
                            mFocusListener.onFlat();
                        break;
                }
            }
        }).start();

        mCurrentStatus = focusChange;
    }

    /**
     *
     * @return
     */
    public int getVolume(){
        return mAudioManager != null ? mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) : -1;
    }

    /**
     * 请求获得焦点
     */
    public void requestFocus() {
        if (isHold || mAudioManager == null) {
            return;
        }

        int status = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status) {
            mCurrentStatus = AudioManager.AUDIOFOCUS_GAIN;
            isHold = true;
        }
    }

    /**
     * 释放音频焦点
     */
    public void abandonFocus() {
        if (mAudioManager == null) {
            return;
        }
        isHold = false;
        mAudioManager.abandonAudioFocus(this);
    }

    /**
     * 音频焦点的监听
     */
    public interface LwjAudioFocusListener {

        /** 获得 */
        void onAcquire();

        /** 丢失 */
        void onLose();

        /** 降音 */
        void onFlat();
    }
}
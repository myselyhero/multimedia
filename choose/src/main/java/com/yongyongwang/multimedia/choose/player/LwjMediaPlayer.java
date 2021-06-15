package com.yongyongwang.multimedia.choose.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import java.io.IOException;

/**
 * @author myselyhero
 * 
 * @desc:系统播放器
 * 
 * @// TODO: 2021/6/7
 */
public class LwjMediaPlayer {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 播放器实例
     */
    private MediaPlayer mMediaPlayer;

    /**
     * 监听
     */
    private LwjPlayerListener mPlayerListener;

    public void init(Context context) {
        mContext = context;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        /** 播放时屏幕常亮 */
        //mMediaPlayer.setScreenOnWhilePlaying(true);

        mMediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
        mMediaPlayer.setOnCompletionListener(completionListener);
        mMediaPlayer.setOnErrorListener(errorListener);
        mMediaPlayer.setOnInfoListener(infoListener);
        mMediaPlayer.setOnPreparedListener(preparedListener);
        mMediaPlayer.setOnVideoSizeChangedListener(videoSizeChangedListener);
    }

    public void setDataSource(String url) {
        if (mMediaPlayer == null || TextUtils.isEmpty(url))
            return;
        try {
            mMediaPlayer.setDataSource(mContext, Uri.parse(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepare() {
        if (mMediaPlayer == null)
            return;
        mMediaPlayer.prepareAsync();
    }

    public void onStart() {
        if (mMediaPlayer == null || mMediaPlayer.isPlaying())
            return;
        mMediaPlayer.start();
    }

    public void onPause() {
        if (mMediaPlayer == null || !mMediaPlayer.isPlaying())
            return;
        mMediaPlayer.pause();
    }

    public void onStop() {
        if (mMediaPlayer == null || !mMediaPlayer.isPlaying())
            return;
        onPause();
        mMediaPlayer.stop();
    }

    public void onReset() {
        if (mMediaPlayer == null)
            return;
        if (mMediaPlayer.isPlaying()){
            onStop();
        }
        mMediaPlayer.reset();
        mMediaPlayer.setSurface(null);
        mMediaPlayer.setDisplay(null);
    }

    public void onRelease() {
        if (mMediaPlayer == null)
            return;
        if (mMediaPlayer.isPlaying()){
            onReset();
        }
        mMediaPlayer.setOnErrorListener(null);
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnInfoListener(null);
        mMediaPlayer.setOnBufferingUpdateListener(null);
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.setOnVideoSizeChangedListener(null);
        new Thread() {
            @Override
            public void run() {
                try {
                    mMediaPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public void seekTo(long time) {
        if (mMediaPlayer == null)
            return;
        mMediaPlayer.seekTo((int) time);
    }

    public long getCurrentPosition() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }

    public long getDuration() {
        return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
    }

    public void setSurface(Surface surface) {
        if (mMediaPlayer != null)
            mMediaPlayer.setSurface(surface);
    }

    public void setDisplay(SurfaceHolder holder) {
        if (mMediaPlayer != null)
            mMediaPlayer.setDisplay(holder);
    }

    public void setVolume(float v1, float v2) {
        if (mMediaPlayer != null)
            mMediaPlayer.setVolume(v1,v2);
    }

    public void setLooping(boolean isLooping) {
        if (mMediaPlayer != null)
            mMediaPlayer.setLooping(isLooping);
    }

    public boolean isLooping() {
        return mMediaPlayer != null && mMediaPlayer.isLooping();
    }

    public void setSpeed(LwjSpeedLevelEnum speed) {
        if (mMediaPlayer == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParams params = mMediaPlayer.getPlaybackParams();
            params.setSpeed(speed.getSpeed());
            mMediaPlayer.setPlaybackParams(params);
        }
    }

    public int[] getVideoSize() {
        if (mMediaPlayer == null)
            return new int[]{0,0};
        return new int[]{mMediaPlayer.getVideoWidth(),mMediaPlayer.getVideoHeight()};
    }

    public void addPlayerListener(@NonNull LwjPlayerListener listener) {
        mPlayerListener = listener;
    }

    /* 播放器监听 */

    /**
     * 准备监听
     */
    private MediaPlayer.OnPreparedListener preparedListener = mp -> {
        if (mPlayerListener != null)
            mPlayerListener.onPreparedEnd();
    };

    /**
     * 缓存监听
     */
    private MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = (mp, percent) -> {
        if (mPlayerListener != null)
            mPlayerListener.onBuffering(percent,-1);
    };

    /**
     * 加载、渲染监听
     */
    private MediaPlayer.OnInfoListener infoListener = (mp, what, extra) -> {
        if (mPlayerListener != null)
            mPlayerListener.onInfo(what,extra);
        return false;
    };

    /**
     * 错误监听
     */
    private MediaPlayer.OnErrorListener errorListener = (mp, what, extra) -> {
        if (what != -38) {
            if (mPlayerListener != null)
                mPlayerListener.onError();
            return false;
        }else {
            return true;
        }
    };

    /**
     * 播放结束
     */
    private MediaPlayer.OnCompletionListener completionListener = mp -> {
        if (mPlayerListener != null)
            mPlayerListener.onCompletion();
    };

    /**
     * 视频大小监听
     */
    private MediaPlayer.OnVideoSizeChangedListener videoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            if (mPlayerListener != null)
                mPlayerListener.onSizeChanged(mp.getVideoWidth(),mp.getVideoHeight());
        }
    };
}

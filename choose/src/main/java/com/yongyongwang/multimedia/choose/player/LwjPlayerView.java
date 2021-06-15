package com.yongyongwang.multimedia.choose.player;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author myselyhero
 *
 * desc:自定义播放器
 * 
 * @// TODO: 2021/6/7
 */
public class LwjPlayerView extends FrameLayout  {

    private static final String TAG = "LwjPlayerView";

    /** 开始渲染视频画面 */
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    /** 缓冲开始 */
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    /** 缓冲结束 */
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    /** 视频旋转信息 */
    public static final int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;

    /**  */
    private Activity mActivity;

    /** 播放器实例 */
    private LwjMediaPlayer mPlayer;

    /** 控制器 */
    private LwjControllerBaseView mControllerView;

    /** 渲染 */
    private LwjTextureView mDrawingInterface;

    /** 容器 */
    private FrameLayout mFrameLayout;

    /** 播放进度 */
    private long mCurrentPosition;

    /** 是否循环播放 */
    private boolean isLooping = true;

    /** 是否禁音 */
    private boolean isVoice;

    /** 数据源 */
    private String mDataSource;

    /** 状态 */
    private LwjStatusEnum mStatusEnum = LwjStatusEnum.STATUS_IDLE;

    /** 尺寸 */
    private LwjRatioEnum mRatioEnum = LwjRatioEnum.RATIO_DEFAULT;

    /** 音频焦点 */
    private boolean isFocus = true;
    private LwjAudioFocusManager mFocusManager;

    /**  */
    private LwjStatusChangeListener statusChangeListener;

    public LwjPlayerView(@NonNull Context context) {
        super(context);
        init();
    }

    public LwjPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LwjPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        mActivity = (Activity) getContext();
        /** 初始化容器 */
        mFrameLayout = new FrameLayout(getContext());
        mFrameLayout.setBackgroundColor(Color.TRANSPARENT);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mFrameLayout, params);
    }

    /**
     *
     * @param dataSource
     */
    public void setDataSource(String dataSource) {
        if (TextUtils.isEmpty(dataSource))
            return;
        mDataSource = dataSource;
    }

    /**
     *
     * @param ratioEnum
     */
    public void setRatio(LwjRatioEnum ratioEnum) {
        mRatioEnum = ratioEnum;
        if (mDrawingInterface != null) {
            mDrawingInterface.setRatio(ratioEnum);
        }
    }

    /**
     *
     * @param l
     * @param r
     */
    public void setVolume(float l, float r) {
        if (!isUsable()){
            return;
        }
        mPlayer.setVolume(l,r);
    }

    /**
     *
     * @param to
     */
    public void seekTo(long to) {
        if (!isUsable())
            return;
        mPlayer.seekTo(to);
    }

    /**
     *
     * @return
     */
    public long getDuration() {
        return isUsable() ? mPlayer.getDuration() : 0;
    }

    /**
     *
     * @return
     */
    public long getCurrentPosition() {
        return isUsable() ? mPlayer.getCurrentPosition() : 0;
    }

    /**
     *
     * @param focus
     */
    public void setFocus(boolean focus) {
        isFocus = focus;
        /** 设置了取消并且如果以获取则释放 */
        if (!isFocus() && mFocusManager != null){
            mFocusManager.abandonFocus();
        }
    }

    /**
     *
     * @return
     */
    public boolean isFocus() {
        return isFocus;
    }

    /**
     *
     * @param looping
     */
    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    /**
     *
     * @return
     */
    public boolean isLooping() {
        return isLooping;
    }

    /**
     *
     * @param voice
     */
    public void setVoice(boolean voice) {
        isVoice = voice;
        float volume = isVoice ? 0.0f : 1.0f;
        setVolume(volume,volume);
    }

    /**
     *
     * @return
     */
    public boolean isVoice() {
        return isVoice;
    }

    /**
     *
     * @param controllerBaseView
     */
    public void setControllerView(@NonNull LwjControllerBaseView controllerBaseView) {
        removeControllerView();
        mControllerView = controllerBaseView;
        if (mControllerView != null){
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mFrameLayout.addView(mControllerView, params);
            mControllerView.bindPlayer(this);
        }
    }

    /**
     *
     * @param listener
     */
    public void addStatusChangeListener(@NonNull LwjStatusChangeListener listener) {
        statusChangeListener = listener;
    }

    /**
     *
     * @return
     */
    public Bitmap screenCapture() {
        return mDrawingInterface == null ? null : mDrawingInterface.screenCapture();
    }

    /**
     *
     * @return
     */
    public boolean isPlayer() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    /**
     *
     */
    public void onStart() {

        if (TextUtils.isEmpty(mDataSource)){
            Log.e(TAG, "onStart: url not null!!!");
            return;
        }

        switch (mStatusEnum){
            case STATUS_IDLE:
                initPlayer();
                mFrameLayout.setKeepScreenOn(true);
                changeStatus(LwjStatusEnum.STATUS_PREPARING);
                break;
            case STATUS_PAUSED:
            case STATUS_PLAYING:
            case STATUS_PREPARING:
                initAudioManager();
                if (mCurrentPosition > 0)
                    mPlayer.seekTo(mCurrentPosition);
                mPlayer.onStart();
                changeStatus(LwjStatusEnum.STATUS_PLAYING);
                mFrameLayout.setKeepScreenOn(true);
                break;
        }
    }

    /**
     *
     */
    public void onPause() {
        if (!isUsable())
            return;

        if (mStatusEnum != LwjStatusEnum.STATUS_PLAYING && !isPlayer())
            return;

        mCurrentPosition = mPlayer.getCurrentPosition();
        mPlayer.onPause();
        changeStatus(LwjStatusEnum.STATUS_PAUSED);
        releaseAudioManager();
        mFrameLayout.setKeepScreenOn(false);
    }

    /**
     *
     */
    public void onRelease() {
        if (mStatusEnum == LwjStatusEnum.STATUS_IDLE)
            return;
        /** 先暂停播放器 */
        onPause();
        /** 释放播放器 */
        if (mPlayer != null) {
            mPlayer.onStop();
            mPlayer.onRelease();
            mPlayer = null;
        }
        /** 释放TextureView */
        removeTextureView();
        /** 关闭AudioFocus监听 */
        releaseAudioManager();
        /** 关闭屏幕常亮 */
        mFrameLayout.setKeepScreenOn(false);
        /** 重置播放进度 */
        mCurrentPosition = 0;
        /** 切换转态 */
        changeStatus(LwjStatusEnum.STATUS_IDLE);
    }

    /* ---- 内部方法 ---- */

    /**
     * 播放器是否可用
     * @return
     */
    private boolean isUsable(){
        return mPlayer != null;
    }

    /**
     * 初始化播放器核心
     */
    private void initPlayer(){
        mPlayer = new LwjMediaPlayer();
        mPlayer.addPlayerListener(mPlayerListener);
        mPlayer.init(getContext());
        mPlayer.setLooping(isLooping);
        mPlayer.setDataSource(mDataSource);
        setVoice(isVoice);
        mPlayer.prepare();
    }

    /**
     *
     */
    private void initTextureView(){

        removeTextureView();
        mDrawingInterface = new LwjTextureView(getContext());

        mDrawingInterface.attach(mPlayer);
        mDrawingInterface.setRatio(mRatioEnum);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        mFrameLayout.addView(mDrawingInterface, 0, params);
    }

    /**
     *
     */
    private void removeTextureView(){
        if (mDrawingInterface != null) {
            mFrameLayout.removeView(mDrawingInterface);
            mDrawingInterface = null;
        }
    }

    /**
     * 删除已添加的控制器
     */
    private void removeControllerView(){
        if (mControllerView != null) {
            mFrameLayout.removeView(mControllerView);
            mControllerView = null;
        }
    }

    /**
     * 播放器状态改变了
     * @param statusEnum
     */
    private void changeStatus(@NonNull LwjStatusEnum statusEnum){
        mStatusEnum = statusEnum;
        if (mControllerView != null)
            mControllerView.changeStatus(mStatusEnum);
        if (statusChangeListener != null)
            statusChangeListener.onChangeStatus(mStatusEnum);
    }

    /**
     * 初始化音频焦点
     */
    private void initAudioManager(){
        /** 已禁用或已静音则不获取 */
        if (!isFocus() || !isVoice)
            return;
        if (mFocusManager == null) {
            mFocusManager = new LwjAudioFocusManager(getContext(), mFocusListener);
        }
        mFocusManager.requestFocus();
    }

    /**
     * 释放音频焦点
     */
    private void releaseAudioManager(){
        if (mFocusManager != null){
            mFocusManager.abandonFocus();
            mFocusManager = null;
        }
    }

    /* ---- 回调 ---- */

    /**
     * 音频焦点回调
     */
    private LwjAudioFocusManager.LwjAudioFocusListener mFocusListener = new LwjAudioFocusManager.LwjAudioFocusListener() {
        @Override
        public void onAcquire() {
            post(new Runnable() {
                @Override
                public void run() {
                    onStart();
                }
            });
            /**
             * 已禁音则不恢复音量
             */
            if (isVoice){
                setVolume(1,1);
            }
        }

        @Override
        public void onLose() {
            post(new Runnable() {
                @Override
                public void run() {
                    onPause();
                }
            });
        }

        @Override
        public void onFlat() {
            setVolume(0.1f, 0.1f);
        }
    };

    /**
     * 播放器回调
     */
    private LwjPlayerListener mPlayerListener = new LwjPlayerListener() {
        @Override
        public void onPreparedEnd() {
            initTextureView();
            initAudioManager();
            onStart();
            changeStatus(LwjStatusEnum.STATUS_PLAYING);
        }

        @Override
        public void onBuffering(int buffer, long speed) {
            if (mControllerView != null)
                mControllerView.bufferUpdate(buffer);
        }

        @Override
        public void onInfo(int what, int extra) {
            switch (what) {
                case MEDIA_INFO_BUFFERING_START://缓冲
                    changeStatus(LwjStatusEnum.STATUS_BUFFERING);
                    break;
                case MEDIA_INFO_BUFFERING_END://缓冲结束
                    changeStatus(LwjStatusEnum.STATUS_BUFFEEND);
                    break;
                case MEDIA_INFO_VIDEO_RENDERING_START: // 视频开始渲染
                    changeStatus(LwjStatusEnum.STATUS_PLAYING);
                    if (getWindowVisibility() != VISIBLE)
                        onPause();
                    break;
                case MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    if (mDrawingInterface != null)
                        mDrawingInterface.setRotationDegree(extra);
                    break;
            }
        }

        @Override
        public void onSizeChanged(int width, int height) {
            if (mDrawingInterface != null) {
                mDrawingInterface.setVideoSize(width, height);
            }
        }

        @Override
        public void onError() {
            changeStatus(LwjStatusEnum.STATUS_ERROR);
            setKeepScreenOn(false);
        }

        @Override
        public void onCompletion() {
            mCurrentPosition = 0;
            setKeepScreenOn(false);
            changeStatus(LwjStatusEnum.STATUS_COMPLETED);
        }
    };
}
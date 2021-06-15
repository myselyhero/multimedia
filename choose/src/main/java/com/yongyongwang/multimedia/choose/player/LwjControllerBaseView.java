package com.yongyongwang.multimedia.choose.player;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author myselyhero 
 *
 * desc:控制器父类
 *
 * @// TODO: 2021/6/7
 */
public abstract class LwjControllerBaseView extends FrameLayout implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
        View.OnTouchListener {

    private String TAG = "LwjControllerBaseView";

    /** 是否锁定 */
    protected boolean isLock;

    /** 当前音量 */
    protected int mVolume;

    /** 多长时间后隐藏控制器 */
    protected int mControllerTime = 3000;

    /** 音频 */
    private AudioManager mAudioManager;

    /** 播放器实例 */
    private LwjPlayerView mLwjPlayer;

    /**  */
    private GestureDetector mGestureDetector;
    /** 是否关闭手势 */
    protected boolean isGesture;

    /** 控制器隐藏线程 */
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    protected Runnable mRunnable;

    /** 刷新进度 */
    protected int progressDefault = 1000;
    protected Timer progressTimer;

    public LwjControllerBaseView(@NonNull Context context) {
        super(context);
        init();
    }

    public LwjControllerBaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LwjControllerBaseView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mGestureDetector = new GestureDetector(getContext(), this);
        setOnTouchListener(this);

        if (getLayoutId() != 0){
            LayoutInflater.from(getContext()).inflate(getLayoutId(),this);
            initView();
        }
    }



    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        onClick(e);
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        onDblClick(e);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (isGesture)
            return false;
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mGestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {

        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取布局ID
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化视图
     */
    protected abstract void initView();


    /**
     * 绑定播放器
     * @param playerView
     */
    public void bindPlayer(@NonNull LwjPlayerView playerView){
        mLwjPlayer = playerView;
    }

    /**
     * 单击
     * @param event
     */
    protected abstract void onClick(MotionEvent event);

    /**
     * 双击
     * @param event
     */
    protected abstract void onDblClick(MotionEvent event);

    /**
     * 更新缓冲值
     * @param buffering
     */
    public abstract void bufferUpdate(int buffering);

    /**
     * 刷新进度
     * @param position
     */
    public abstract void currencyPosition(long position);

    /**
     * 改变播放器状态
     * @param statusEnum
     */
    public void changeStatus(LwjStatusEnum statusEnum){

        /** 停止或启动进度条的刷新 */
        switch (statusEnum){
            case STATUS_PLAYING:
            case STATUS_BUFFEEND:
                startProgress();
                break;
            case STATUS_BUFFERING:
            case STATUS_PAUSED:
            case STATUS_IDLE:
            case STATUS_COMPLETED:
            case STATUS_PREPARING:
            case STATUS_ERROR:
                stopProgress();
                break;
        }

        statusListener(statusEnum);
    }

    /**
     *
     * @param statusEnum
     */
    public abstract void statusListener(LwjStatusEnum statusEnum);

    /**
     * 开始计时刷新进度s
     */
    private void startProgress(){
        stopProgress();

        /** 如果是拉流不进行刷新 */
        if (mLwjPlayer == null)
            return;

        progressTimer = new Timer();
        progressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        currencyPosition(mLwjPlayer.getCurrentPosition());
                    }
                });
            }
        },0,progressDefault);
    }

    /**
     * 停止刷新进度的线程
     */
    private void stopProgress(){
        if (progressTimer != null){
            progressTimer.cancel();
            progressTimer = null;
        }
    }

    /**
     *
     */
    protected void startAndStop(){
        if (mLwjPlayer == null)
            return;

        if (mLwjPlayer.isPlayer()){
            mLwjPlayer.onPause();
        }else {
            mLwjPlayer.onStart();
        }
    }

    /**
     *
     * @param position
     */
    protected void seekTo(long position){
        if (mLwjPlayer != null && position > 0)
            mLwjPlayer.seekTo(position);
    }

    /**
     *
     * @return
     */
    protected long getDuration(){
        return mLwjPlayer == null ? -1 : mLwjPlayer.getDuration();
    }

    /**
     *
     * @return
     */
    protected long getCurrentPosition(){
        return mLwjPlayer == null ? -1 : mLwjPlayer.getCurrentPosition();
    }

    /**
     *
     * @param voice
     */
    protected boolean setVoice(boolean voice){
        if (mLwjPlayer != null)
            mLwjPlayer.setVoice(voice);

        return isVoice();
    }

    /**
     *
     * @return
     */
    protected boolean isVoice(){
        return mLwjPlayer != null && mLwjPlayer.isVoice();
    }

    /**
     * 转换毫秒数成“分、秒”，如“01:53”。若超过60分钟则显示“时、分、秒”，如“01:01:30
     * @param time
     * @return
     */
    protected String longTimeToString(long time) {
        int second = 1000;
        int minute = second * 60;
        int hour = minute * 60;

        long hourTime = (time) / hour;
        long minuteTime = (time - hourTime * hour) / minute;
        long secondTime = (time - hourTime * hour - minuteTime * minute) / second;

        String strHour = hourTime < 10 ? "0" + hourTime : "" + hourTime;
        String strMinute = minuteTime < 10 ? "0" + minuteTime : "" + minuteTime;
        String strSecond = secondTime < 10 ? "0" + secondTime : "" + secondTime;
        if (hourTime > 0) {
            return strHour + ":" + strMinute + ":" + strSecond;
        } else {
            return strMinute + ":" + strSecond;
        }
    }

    /**
     *
     */
    protected void showControllerAnim(View view){
        if (view.getVisibility() == View.VISIBLE)
            return;

        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);
    }

    /**
     *
     * @param view
     */
    protected void hideControllerAnim(View view){
        if (view.getVisibility() == View.GONE)
            return;

        view.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
    }
}

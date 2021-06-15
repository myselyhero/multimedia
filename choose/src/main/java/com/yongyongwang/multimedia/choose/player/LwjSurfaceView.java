package com.yongyongwang.multimedia.choose.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author myselyhero
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/7
 */
@SuppressLint("ViewConstructor")
public class LwjSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "LwjSurfaceView";
    
    /** 播放器 */
    @Nullable
    private LwjMediaPlayer mPlayer;

    public LwjSurfaceView(Context context) {
        super(context);
        init();
    }

    public LwjSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LwjSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measuredSize = LwjMeasureHelper.getInstance().onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measuredSize[0], measuredSize[1]);
    }

    /**
     *
     */
    private void init(){
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if (mPlayer != null) {
            mPlayer.setDisplay(holder);
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    /**
     *
     * @param player
     */
    public void attach(@NonNull LwjMediaPlayer player) {
        mPlayer = player;
    }

    /**
     *
     * @param videoWidth
     * @param videoHeight
     */
    public void setVideoSize(int videoWidth, int videoHeight) {
        LwjMeasureHelper.getInstance().setVideoSize(videoWidth,videoHeight);
        requestLayout();
    }

    /**
     *
     * @param degree
     */
    public void setRotationDegree(int degree) {
        LwjMeasureHelper.getInstance().setRotationDegree(degree);
        requestLayout();
    }

    /**
     *
     * @param ratioEnum
     */
    public void setRatio(LwjRatioEnum ratioEnum) {
        LwjMeasureHelper.getInstance().setRatioEnum(ratioEnum);
        requestLayout();
    }
}

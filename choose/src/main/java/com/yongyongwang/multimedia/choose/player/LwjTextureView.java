package com.yongyongwang.multimedia.choose.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author myselyhero 
 *
 * desc:渲染视图
 *
 * @// TODO: 2021/6/7  
 */
@SuppressLint("ViewConstructor")
public class LwjTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    /**  */
    private SurfaceTexture mSurfaceTexture;

    /**  */
    @Nullable
    private LwjMediaPlayer mPlayer;
    private Surface mSurface;

    public LwjTextureView(Context context) {
        super(context);
        setSurfaceTextureListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measuredSize = LwjMeasureHelper.getInstance().onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measuredSize[0], measuredSize[1]);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (mSurfaceTexture != null) {
            setSurfaceTexture(mSurfaceTexture);
        } else {
            mSurfaceTexture = surfaceTexture;
            mSurface = new Surface(surfaceTexture);
            if (mPlayer != null) {
                mPlayer.setSurface(mSurface);
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

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
        if (videoWidth > 0 && videoHeight > 0) {
            LwjMeasureHelper.getInstance().setVideoSize(videoWidth,videoHeight);
            requestLayout();
        }
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

    /**
     * 截图
     * @return
     */
    public Bitmap screenCapture() {
        return getBitmap();
    }


    /**
     *
     */
    public void release() {
        if (mSurface != null)
            mSurface.release();
        if (mSurfaceTexture != null)
            mSurfaceTexture.release();
    }
}
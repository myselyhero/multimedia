package com.yongyongwang.multimedia.choose.player;

import android.view.View;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/7
 */
public class LwjMeasureHelper {

    private static final String TAG = "LwjMeasureHelper";

    private static LwjMeasureHelper instance;

    /** 视频旋转 */
    private int mRotationDegree;

    /** 宽高 */
    private int mVideoWidth,mVideoHeight;

    /** 屏幕尺寸 */
    private LwjRatioEnum mRatioEnum = LwjRatioEnum.RATIO_DEFAULT;

    public static LwjMeasureHelper getInstance() {
        if (instance == null){
            synchronized (LwjMeasureHelper.class){
                instance = new LwjMeasureHelper();
            }
        }
        return instance;
    }

    /**
     *
     * @param mRotationDegree
     */
    public void setRotationDegree(int mRotationDegree) {
        this.mRotationDegree = mRotationDegree;
    }

    /**
     *
     * @param width
     * @param height
     */
    public void setVideoSize(int width,int height){
        mVideoWidth = width;
        mVideoHeight = height;
    }

    /**
     *
     * @param mRatioEnum
     */
    public void setRatioEnum(LwjRatioEnum mRatioEnum) {
        this.mRatioEnum = mRatioEnum;
    }

    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     * @return
     */
    public int[] onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /** 软解码时处理旋转信息，交换宽高 */
        if (mRotationDegree == 90 || mRotationDegree == 270) {
            widthMeasureSpec = widthMeasureSpec + heightMeasureSpec;
            heightMeasureSpec = widthMeasureSpec - heightMeasureSpec;
            widthMeasureSpec = widthMeasureSpec - heightMeasureSpec;
        }

        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        if (mVideoHeight == 0 || mVideoWidth == 0) {
            return new int[]{width, height};
        }

        /** 设置了宽高比例 */
        switch (mRatioEnum){
            case RATIO_DEFAULT://默认使用原视频高宽
            default:
                if (mVideoWidth > 0 && mVideoHeight > 0) {

                    int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
                    int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
                    int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
                    int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);

                    if (widthSpecMode == View.MeasureSpec.EXACTLY && heightSpecMode == View.MeasureSpec.EXACTLY) {
                        // the size is fixed
                        width = widthSpecSize;
                        height = heightSpecSize;

                        // for compatibility, we adjust size based on aspect ratio
                        if (mVideoWidth * height < width * mVideoHeight) {
                            //Log.i("@@@", "image too wide, correcting");
                            width = height * mVideoWidth / mVideoHeight;
                        } else if (mVideoWidth * height > width * mVideoHeight) {
                            //Log.i("@@@", "image too tall, correcting");
                            height = width * mVideoHeight / mVideoWidth;
                        }
                    } else if (widthSpecMode == View.MeasureSpec.EXACTLY) {
                        // only the width is fixed, adjust the height to match aspect ratio if possible
                        width = widthSpecSize;
                        height = width * mVideoHeight / mVideoWidth;
                        if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                            // couldn't match aspect ratio within the constraints
                            height = heightSpecSize;
                        }
                    } else if (heightSpecMode == View.MeasureSpec.EXACTLY) {
                        // only the height is fixed, adjust the width to match aspect ratio if possible
                        height = heightSpecSize;
                        width = height * mVideoWidth / mVideoHeight;
                        if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                            // couldn't match aspect ratio within the constraints
                            width = widthSpecSize;
                        }
                    } else {
                        // neither the width nor the height are fixed, try to use actual video size
                        width = mVideoWidth;
                        height = mVideoHeight;
                        if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                            // too tall, decrease both width and height
                            height = heightSpecSize;
                            width = height * mVideoWidth / mVideoHeight;
                        }
                        if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                            // too wide, decrease both width and height
                            width = widthSpecSize;
                            height = width * mVideoHeight / mVideoWidth;
                        }
                    }
                } else {
                    // no size yet, just adopt the given spec sizes
                }
                break;
            case RATIO_SHRINK://缩小居中与屏幕
                if (mVideoWidth * height < width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    height = width * mVideoHeight / mVideoWidth;
                }
                break;
            case RATIO_CROP://居中
                if (mVideoWidth * height > width * mVideoHeight) {
                    height = width * mVideoHeight / mVideoWidth;
                }
                break;
            case RATIO_FULL://全屏
                width = widthMeasureSpec;
                height = heightMeasureSpec;
                break;
            case RATIO_16_9://
                if (height > width / 16 * 9) {
                    height = width / 16 * 9;
                } else {
                    width = height / 9 * 16;
                }
                break;
            case RATIO_4_3://
                if (height > width / 4 * 3) {
                    height = width / 4 * 3;
                } else {
                    width = height / 3 * 4;
                }
                break;
        }
        return new int[]{width, height};
    }
}

package com.yongyongwang.multimedia.choose.edit.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;


/**
 * @author myselyhero 
 * 
 * @desc: 背景层
 * 
 * @// TODO: 2021/6/8
 */
public class BackGroundLayer extends AppCompatImageView {

    private static final int DISPLAY_PADDING_LEFT = 30;
    private static final int DISPLAY_PADDING_TOP = 24;
    private static final int DISPLAY_PADDING_RIGHT = 30;
    private static final int DISPLAY_PADDING_BOTTOM = 24;

    private Bitmap mBitmap;

    private final Rect contentRect = new Rect();
    private Rect boundRect;

    private OnUpdateListener listener;

    public BackGroundLayer(Context context) {
        super(context);
        init();
    }

    public BackGroundLayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     *
     */
    private void init(){
        //setScaleType(ScaleType.CENTER);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        /*boundRect = new Rect(left + dip2px(DISPLAY_PADDING_LEFT),
                top + dip2px(DISPLAY_PADDING_TOP),
                right - dip2px(DISPLAY_PADDING_RIGHT),
                bottom - dip2px(DISPLAY_PADDING_BOTTOM));*/
        boundRect = new Rect(left, top, right, bottom);

        updateViewport();
        invalidate();
    }

    /**
     *
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return;
        mBitmap = bitmap;
        setImageBitmap(mBitmap);
        updateViewport();
        invalidate();
    }

    /**
     *
     * @return
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }

    /**
     *
     */
    private void updateViewport() {

        if (mBitmap == null || boundRect == null) {
            return;
        }
        float scaleFactor = fitSrcImageToBounds(mBitmap, boundRect, contentRect);
        if (listener != null)
            listener.onUpdate(contentRect, scaleFactor);
    }

    /**
     *
     * @param boundRect
     * @param contentRect
     * @return
     */
    private float fitSrcImageToBounds(Bitmap bitmap,Rect boundRect, Rect contentRect) {
        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();
        int boundWidth = boundRect.width();
        int boundHeight = boundRect.height();

        contentRect.set(boundRect);

        float imgRatio = (float) imgWidth / (float) imgHeight;
        float boundRatio = (float) boundWidth / (float) boundHeight;
        boolean scaleByWidth = imgRatio >= boundRatio;
        if (scaleByWidth) {
            return scaleContentRectByWidth(contentRect, imgWidth, imgHeight, boundWidth);
        } else {
            return scaleContentRectByHeight(contentRect, imgWidth, imgHeight, boundHeight);
        }
    }

    /**
     *
     * @param contentRect
     * @param imgWidth
     * @param imgHeight
     * @param boundHeight
     * @return
     */
    private float scaleContentRectByHeight(Rect contentRect, int imgWidth, int imgHeight, int boundHeight) {
        int contentWidth = boundHeight * imgWidth / imgHeight;
        updateContentRect(contentRect, contentWidth, boundHeight);
        return (float) boundHeight / (float) imgHeight;
    }

    /**
     *
     * @param contentRect
     * @param imgWidth
     * @param imgHeight
     * @param boundWidth
     * @return
     */
    private float scaleContentRectByWidth(Rect contentRect, int imgWidth, int imgHeight, int boundWidth) {
        int contentHeight = boundWidth * imgHeight / imgWidth;
        updateContentRect(contentRect, boundWidth, contentHeight);
        return (float) boundWidth / (float) imgWidth;
    }

    /**
     *
     * @param contentRect
     * @param width
     * @param height
     */
    private void updateContentRect(Rect contentRect, int width, int height) {
        int centerX = contentRect.centerX();
        int centerY = contentRect.centerY();
        int halfW = width >> 1;
        int halfH = height >> 1;
        contentRect.set(centerX - halfW, centerY - halfH, centerX + halfW, centerY + halfH);
    }

    /**
     *
     * @param dipValue
     * @return
     */
    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     *
     * @param listener
     */
    public void setListener(OnUpdateListener listener) {
        this.listener = listener;
    }

    /**
     *
     */
    public interface OnUpdateListener {
        /**
         * This method will be called when BackgroundLayer update
         *
         * @param baselineRect Content Rect
         * @param scaleFactor  Viewport ScaleFactor
         */
        void onUpdate(Rect baselineRect, float scaleFactor);
    }
}
package com.yongyongwang.multimedia.choose.crop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.yongyongwang.multimedia.choose.crop.util.ImageEditUtils;

/**
 * @author myselyhero 
 * 
 * @desc: 背景层
 * 
 * @// TODO: 2021/8/16
 */
public class BackGroundLayer extends AppCompatImageView {

    private static final int DISPLAY_PADDING_LEFT = 30;
    private static final int DISPLAY_PADDING_TOP = 24;
    private static final int DISPLAY_PADDING_RIGHT = 30;
    private static final int DISPLAY_PADDING_BOTTOM = 24;

    private final Rect contentRect = new Rect();
    private Bitmap previewImg;
    private Rect boundRect;

    private OnUpdateListener listener;

    public BackGroundLayer(@NonNull Context context) {
        super(context);
    }

    public BackGroundLayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BackGroundLayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        /*boundRect = new Rect(left + ScreenUtil.dp2px(DISPLAY_PADDING_LEFT),
                top + ScreenUtil.dp2px(DISPLAY_PADDING_TOP),
                right - ScreenUtil.dp2px(DISPLAY_PADDING_RIGHT),
                bottom - ScreenUtil.dp2px(DISPLAY_PADDING_BOTTOM));*/
        boundRect = new Rect(left, top, right, bottom);

        updateViewport();
        invalidate();
    }

    /**
     *
     */
    private void updateViewport() {
        if (previewImg == null || boundRect == null) {
            return;
        }
        if (listener != null){
            float scaleFactor = ImageEditUtils.fitSrcImageToBounds(previewImg, boundRect, contentRect);
            listener.onUpdate(contentRect,scaleFactor);
        }
    }

    /**
     *
     * @return
     */
    public Rect getContentRect() {
        return contentRect;
    }

    /**
     *
     * @return
     */
    public Bitmap getPreviewImage() {
        return previewImg;
    }

    /**
     *
     * @param pixelMap
     */
    public void setPreviewImage(Bitmap pixelMap) {
        if (pixelMap != null) {
            previewImg = pixelMap;
            setImageBitmap(previewImg);
            updateViewport();
        }
    }

    /**
     *
     * @param listener
     */
    public void setOnUpdateListener(OnUpdateListener listener) {
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

package com.yongyongwang.multimedia.choose.edit.view;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Image Picker
 */
public abstract class ImagePicker extends View {

    /**
     * BaseLine Rect
     */
    RectF baseLineRect = new RectF();

    /**
     * OnPick Action Listener
     */
    OnPickActionListener onPickActionListener;

    /**
     * Image Picker Constructor
     *
     * @param context context
     */
    public ImagePicker(Context context) {
        super(context);
    }

    /**
     * Image Picker Constructor
     * @param context
     * @param attrs
     */
    public ImagePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Image Picker Constructor
     *
     * @param context context
     * @param attrSet attribute set
     * @param resId   Resource ID
     */
    public ImagePicker(Context context, AttributeSet attrSet, int resId) {
        super(context, attrSet, resId);
    }

    /**
     * Obtains BaselineRect
     *
     * @return BaselineRect
     */
    public RectF getBaselineRect() {
        return baseLineRect;
    }

    /**
     * Obtains PickRatioRect
     *
     * @return PickRatioRect
     */
    public abstract RectF getPickRatioRect();

    /**
     * Set Aspect Ratio
     *
     * @param ratio      Aspect Ratio
     * @param boundRatio BoundRatio, can be null
     */
    public abstract void setAspectRatio(float ratio, RectF boundRatio);

    /**
     * Set OnPickActionListener
     *
     * @param listener OnPickActionListener
     */
    public void setOnPickActionListener(OnPickActionListener listener) {
        onPickActionListener = listener;
    }

    /**
     * CropPicker Update
     *
     * @param baseLineRect baseLineRect
     */
    public abstract void update(Rect baseLineRect);

    /**
     * OnPick Action Listener Interface
     */
    public interface OnPickActionListener {

        void onPickUpdate(RectF cropRect, Path closedPath);
    }
}

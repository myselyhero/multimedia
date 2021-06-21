package com.yongyongwang.multimedia.choose.edit.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.util.FileUtils;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/7
 */
public class CropView extends FrameLayout {

    private static final String TAG = "CropView";

    private BackGroundLayer backgroundView;
    private CropPicker cropPicker;

    public CropView(@NonNull Context context) {
        super(context);
        init();
    }

    public CropView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CropView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        backgroundView = new BackGroundLayer(getContext());
        cropPicker = new CropPicker(getContext());
        backgroundView.setListener((baselineRect, scaleFactor) -> {
            cropPicker.update(baselineRect);
        });

        addView(backgroundView);
        addView(cropPicker);
    }

    /**
     *
     * @return
     */
    public Rect getEditRect() {
        RectF cropRatioRect = cropPicker.getPickRatioRect();
        Bitmap srcBmp = backgroundView.getBitmap();
        float width = srcBmp.getWidth();
        float height = srcBmp.getHeight();

        Rect rect = new Rect(Math.round(cropRatioRect.left * width),
                Math.round(cropRatioRect.top * height),
                Math.round(cropRatioRect.right * width),
                Math.round(cropRatioRect.bottom * height));

        try {
            Bitmap outBitmap = Bitmap.createBitmap(srcBmp, rect.left, rect.top, rect.right, rect.bottom);
            backgroundView.setBitmap(outBitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rect;
    }

    /**
     *
     * @param ratio
     */
    public void setRatio(float ratio){
        cropPicker.setAspectRatio(ratio,null);
    }

    /**
     *
     * @param url
     */
    public void setImage(String url){
        if (TextUtils.isEmpty(url))
            return;
        backgroundView.setBitmap(BitmapFactory.decodeFile(url));
    }
}

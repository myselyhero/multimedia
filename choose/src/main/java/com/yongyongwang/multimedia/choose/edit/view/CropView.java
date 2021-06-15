package com.yongyongwang.multimedia.choose.edit.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.R;

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
     * @param url
     */
    public void setImage(String url){
        if (TextUtils.isEmpty(url))
            return;
        backgroundView.setBitmap(BitmapFactory.decodeFile(url));
    }
}

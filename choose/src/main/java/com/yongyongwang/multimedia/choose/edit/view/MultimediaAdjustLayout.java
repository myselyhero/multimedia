package com.yongyongwang.multimedia.choose.edit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.entity.MultimediaAdjustEntity;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/16
 */
public class MultimediaAdjustLayout extends LinearLayout {

    private TwoLineSeekBar lineSeekBar;

    private MultimediaAdjustList adjustList;

    private MultimediaAdjustEntity mEntity;

    public MultimediaAdjustLayout(Context context) {
        super(context);
        init();
    }

    public MultimediaAdjustLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultimediaAdjustLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.multimedia_edit_adjust_layout,this);
        lineSeekBar = findViewById(R.id.multimedia_edit_adjust_bar);
        adjustList = findViewById(R.id.multimedia_edit_adjust_list);

        adjustList.setClickListener(entity -> {
            mEntity = entity;
            lineSeekBar.setValue(entity.getValue());
        });
    }

    /**
     *
     * @param listener
     */
    public void addItemClickListener(MultimediaAdjustList.OnAdjustClickListener listener){
        adjustList.setClickListener(listener);
    }
}

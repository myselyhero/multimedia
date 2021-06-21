package com.yongyongwang.multimedia.choose.edit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.entity.MultimediaBeautifyEntity;


/**
 *
 */
public class MultimediaBeautifyLayout extends LinearLayout {

    private MultimediaBeautifyEstate beautifyEstate;
    private MultimediaBeautifyList beautifyList;

    private MultimediaBeautifyEntity mEntity;

    public MultimediaBeautifyLayout(Context context) {
        super(context);
        init();
    }

    public MultimediaBeautifyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultimediaBeautifyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.multimedia_edit_beautify_layout,this);

        beautifyEstate = findViewById(R.id.multimedia_beautify_estate);
        beautifyList = findViewById(R.id.multimedia_beautify_list);
        beautifyList.setListener(entity -> {
            mEntity = entity;
            beautifyEstate.setValue(mEntity.getValue());
        });
        beautifyEstate.setEstateListener(value -> {
            if (mEntity != null)
                mEntity.setValue(value);
        });
    }
}

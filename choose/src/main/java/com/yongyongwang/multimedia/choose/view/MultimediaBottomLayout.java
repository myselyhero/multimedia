package com.yongyongwang.multimedia.choose.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.MultimediaConfig;
import com.yongyongwang.multimedia.choose.R;

/**
 * @author myselyhero
 *
 * @desc:
 *
 * @// TODO: 2021/6/6
 */
public class MultimediaBottomLayout extends LinearLayout {

    private TextView editTextView;

    private LinearLayout previewBackground;
    private TextView previewTextView;

    public MultimediaBottomLayout(Context context) {
        super(context);
        initView();
    }

    public MultimediaBottomLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MultimediaBottomLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     *
     */
    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.multimedia_bottom_layout,this);

        previewBackground = findViewById(R.id.multimedia_bottom_preview);
        previewTextView = findViewById(R.id.multimedia_bottom_preview_tv);

        editTextView = findViewById(R.id.multimedia_bottom_edit);
    }

    /**
     *
     * @param num
     */
    public void setPreviewNum(int num){
        if (num > 0){
            previewTextView.setText(String.format(getContext().getString(R.string.multimedia_choose_preview_n),num));
        }else {
            previewTextView.setText(getContext().getString(R.string.multimedia_choose_preview));
        }
    }

    /**
     *
     * @param multimediaConfig
     */
    public void setConfig(MultimediaConfig multimediaConfig){
        if (multimediaConfig == null)
            return;
        editTextView.setVisibility(multimediaConfig.isCrop() ? View.VISIBLE : View.GONE);
    }

    /**
     *
     * @param listener
     */
    public void addEditClickListener(View.OnClickListener listener){
        editTextView.setOnClickListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void addPreviewClickListener(OnClickListener listener){
        previewBackground.setOnClickListener(listener);
    }
}

package com.yongyongwang.multimedia.choose.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.MultimediaConfig;
import com.yongyongwang.multimedia.choose.R;

/**
 * @author yongyong
 *
 * desc: 预览的顶部布局
 * 
 * @// TODO: 2021/6/6
 */
public class MultimediaPreviewTopLayout extends LinearLayout implements View.OnClickListener {

    private Activity mActivity;

    private ImageView goBackBackground;
    private TextView positionTextView;
    private TextView button;

    /**
     *
     */
    private MultimediaConfig mConfig = MultimediaConfig.getInstance();

    public MultimediaPreviewTopLayout(Context context) {
        super(context);
        initView();
    }

    public MultimediaPreviewTopLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MultimediaPreviewTopLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     *
     */
    private void initView(){
        mActivity = (Activity) getContext();
        LayoutInflater.from(getContext()).inflate(R.layout.multimedia_preview_top_layout,this);
        goBackBackground = findViewById(R.id.multimedia_preview_top_layout_close);
        positionTextView = findViewById(R.id.multimedia_preview_top_layout_position);
        button = findViewById(R.id.multimedia_preview_top_layout_btn);

        button.setEnabled(false);
        goBackBackground.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.multimedia_preview_top_layout_close) {
            mActivity.finish();
        }
    }

    /**
     *
     * @param mConfig
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void setConfig(MultimediaConfig mConfig) {
        this.mConfig = mConfig;
        setBackgroundColor(mConfig.isDarkTheme() ? getResources().getColor(R.color.multimedia_theme) : getResources().getColor(R.color.multimedia_white_theme));
        goBackBackground.setImageResource(mConfig.isDarkTheme() ? R.drawable.multimedia_back_white : R.drawable.multimedia_back_black);
        positionTextView.setTextColor(mConfig.isDarkTheme() ? getResources().getColor(R.color.white) : getResources().getColor(R.color.multimedia_white_black));
        if (!TextUtils.isEmpty(mConfig.getConfirmText()))
            button.setText(mConfig.getConfirmText());
        if (mConfig.getConfirmDrawable() > 0)
            button.setBackground(getResources().getDrawable(mConfig.getConfirmDrawable()));
        if (mConfig.getConfirmTextColor() > 0)
            button.setTextColor(getResources().getColor(mConfig.getConfirmTextColor()));
    }

    /**
     *
     * @param position
     * @param total
     */
    public void setPosition(int position,int total){
        positionTextView.setText(position+"/"+total);
    }

    /**
     *
     * @param num
     */
    public void setCheckedNum(int num){

        if (!mConfig.isOnly() && num > 0)
            button.setText(TextUtils.isEmpty(mConfig.getConfirmText()) ? String.format(getContext().getString(R.string.multimedia_choose_confirm_n),num) : String.format(mConfig.getConfirmText() + "(%d)",num));
        else
            button.setText(TextUtils.isEmpty(mConfig.getConfirmText()) ? getContext().getString(R.string.multimedia_choose_confirm) : mConfig.getConfirmText());

        if (mConfig.isOnly()){
            button.setEnabled(num > 0);
        }else {
            if (mConfig.getMinNum() > 0){
                button.setEnabled(num >= mConfig.getMinNum());
            }else {
                button.setEnabled(num > 0);
            }
        }
    }

    /**
     *
     * @param listener
     */
    public void setButtonOnClickListener(@NonNull OnClickListener listener){
        button.setOnClickListener(listener);
    }
}

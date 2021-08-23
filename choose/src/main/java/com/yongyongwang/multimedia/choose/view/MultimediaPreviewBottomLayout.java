package com.yongyongwang.multimedia.choose.view;

import android.content.Context;
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
public class MultimediaPreviewBottomLayout extends LinearLayout {

    private LinearLayout boxBackground;
    private ImageView boxImageView;
    private TextView textView;
    private TextView chooseTextView;
    private MultimediaConfig mConfig;

    public MultimediaPreviewBottomLayout(Context context) {
        super(context);
        initView();
    }

    public MultimediaPreviewBottomLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MultimediaPreviewBottomLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     *
     */
    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.multimedia_preview_bottom_layout,this);
        boxBackground = findViewById(R.id.multimedia_preview_bottom_box);
        boxImageView = findViewById(R.id.multimedia_preview_bottom_iv);
        textView = findViewById(R.id.multimedia_bottom_preview_edit);
        chooseTextView = findViewById(R.id.multimedia_preview_bottom_tv);
    }

    /**
     *
     * @param multimediaConfig
     */
    public void setConfig(MultimediaConfig multimediaConfig){
        if (multimediaConfig == null)
            mConfig = MultimediaConfig.getInstance();
        else
            mConfig = multimediaConfig;
        setBackgroundColor(mConfig.isDarkTheme() ? getResources().getColor(R.color.multimedia_theme) : getResources().getColor(R.color.multimedia_white_theme));
        //textView.setTextColor(mConfig.isDarkTheme() ? getResources().getColor(R.color.white) : getResources().getColor(R.color.multimedia_white_black));
        chooseTextView.setTextColor(mConfig.isDarkTheme() ? getResources().getColor(R.color.white) : getResources().getColor(R.color.multimedia_white_black));
        //textView.setVisibility(mConfig.isCrop() ? View.VISIBLE : View.GONE);
    }

    /**
     *
     * @param check
     * @return
     */
    public void setChecked(boolean check){
        if (check){
            boxImageView.setImageResource(R.drawable.multimedia_choose_sel);
        }else {
            boxImageView.setImageResource(mConfig.isDarkTheme() ? R.drawable.multimedia_choose_un : R.drawable.multimedia_choose_un_black);
        }
    }

    /**
     *
     * @param clickListener
     */
    public void addEditClickListener(View.OnClickListener clickListener){
        textView.setOnClickListener(clickListener);
    }

    /**
     *
     * @param listener
     */
    public void addCheckListener(@NonNull OnClickListener listener){
        boxBackground.setOnClickListener(listener);
    }
}

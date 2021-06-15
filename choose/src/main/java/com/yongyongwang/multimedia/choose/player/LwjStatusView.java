package com.yongyongwang.multimedia.choose.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.util.NetWorkUtils;

/**
 * @author yongyong
 *
 * @desc:状态视图
 * 
 * @// TODO: 2020/12/8
 */
public class LwjStatusView extends LinearLayout {

    private ProgressBar mProgressBar;
    private ImageView imageView;
    private TextView textView;

    /**  */
    private LwjStatusEnum mStatusEnum;

    public LwjStatusView(Context context) {
        super(context);
        initView();
    }

    public LwjStatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LwjStatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     *
     */
    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.lwj_status_view,this);

        mProgressBar = findViewById(R.id.lwj_status_view_progress);
        imageView = findViewById(R.id.lwj_status_view_image);
        textView = findViewById(R.id.lwj_status_view_tv);
    }

    /**
     *
     * @return
     */
    public LwjStatusEnum getStatusEnum() {
        return mStatusEnum;
    }

    /**
     *
     */
    public void onIdle(){
        mStatusEnum = LwjStatusEnum.IDLE;
        onStatusChange();
    }

    /**
     *
     */
    public void onLoader(){
        mStatusEnum = LwjStatusEnum.LOADER;
        onStatusChange();
    }

    /**
     *
     */
    public void onFail(){
        mStatusEnum = LwjStatusEnum.FAIL;
        onStatusChange();
    }

    /**
     *
     */
    public void onStop(){
        mStatusEnum = LwjStatusEnum.STOP;
        onStatusChange();
    }

    /**
     *
     */
    private void onStatusChange(){
        switch (mStatusEnum){
            case IDLE:
                setVisibility(View.GONE);
                break;
            case LOADER:
                setVisibility(View.VISIBLE);
                setBackground(getResources().getDrawable(R.drawable.lwj_status_background));
                imageView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(getContext().getText(R.string.lwj_status_loader));
                break;
            case FAIL:
                setVisibility(View.VISIBLE);
                setBackground(getResources().getDrawable(R.drawable.lwj_status_background));
                mProgressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                if (!NetWorkUtils.IsNetWorkEnable(getContext())){
                    imageView.setImageResource(R.drawable.lwj_default_not_network);
                    textView.setText(getContext().getText(R.string.lwj_status_fail_network));
                }else {
                    imageView.setImageResource(R.drawable.lwj_default_fail);
                    textView.setText(getContext().getText(R.string.lwj_status_fail));
                }
                break;
            case STOP:
                setVisibility(View.VISIBLE);
                setBackground(null);
                mProgressBar.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.multimedia_preview_player);
                break;
        }
    }

    /**
     *
     * @param speed
     */
    public void setTcpSpeed(String speed){
        if (textView.getVisibility() == View.GONE || mStatusEnum != LwjStatusEnum.LOADER)
            return;
        textView.setText(speed);
    }

    /**
     *
     */
    public enum LwjStatusEnum {

        /** 空闲 */
        IDLE,
        /** 加载中 */
        LOADER,
        /** 错误 */
        FAIL,
        /** 停止播放 */
        STOP,
    }
}

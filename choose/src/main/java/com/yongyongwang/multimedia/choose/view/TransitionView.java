package com.yongyongwang.multimedia.choose.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;

/**
 * @author yongyongwang
 * 
 * @desc:
 * 
 * @// TODO: 2021/4/19
 */
public class TransitionView extends LinearLayout {

    private LottieAnimationView animationView;
    private TextView textView;

    /** 当前状态 */
    private SmileTransitionStatus mStatus = SmileTransitionStatus.LOADER;

    /**  */
    private String defaultLoadingFile = "lottie/loading.json";
    private String defaultEmptyFile = "lottie/empty.json";
    /**  */
    private String loadText = "请稍后...";
    private int loadTextColor = -1;
    /**  */
    private String emptyText = "暂无数据";
    private int emptyTextColor = -1;

    public TransitionView(Context context) {
        super(context);
        initView();
    }

    public TransitionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TransitionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     *
     */
    private void initView(){
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        animationView = new LottieAnimationView(getContext());
        textView = new TextView(getContext());

        animationView.loop(true);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#ffffff"));
        addView(animationView);
        addView(textView);

        onLoader();
    }

    /**
     *
     */
    private void execute(){
        switch (mStatus){
            case LOADER:
                setVisibility(View.VISIBLE);
                animate()
                        .alpha(1f)
                        .setDuration(1000)
                        .setListener(null);
                animationView.pauseAnimation();

                textView.setText(loadText);
                if (loadTextColor != -1){

                    textView.setTextColor(loadTextColor);
                }

                if (!TextUtils.isEmpty(defaultLoadingFile)){
                    animationView.setAnimation(defaultLoadingFile);
                    animationView.playAnimation();
                }
                break;
            case EMPTY:
                setVisibility(View.VISIBLE);
                animate()
                        .alpha(1f)
                        .setDuration(1000)
                        .setListener(null);
                animationView.pauseAnimation();

                textView.setText(emptyText);
                if (emptyTextColor != -1){
                    textView.setTextColor(emptyTextColor);
                }
                if (!TextUtils.isEmpty(defaultEmptyFile)){
                    animationView.setAnimation(defaultEmptyFile);
                    animationView.playAnimation();
                }
                break;
            case SUCCESS:
                if (getVisibility() == View.VISIBLE){
                    animate()
                            .alpha(0f)
                            .setDuration(1000)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    setVisibility(View.GONE);
                                }
                            });
                }
                break;
        }
    }

    /* 对外API */

    /**
     *
     */
    public void onLoader(){
        mStatus = SmileTransitionStatus.LOADER;
        execute();
    }

    /**
     *
     */
    public void onEmpty(){
        mStatus = SmileTransitionStatus.EMPTY;
        execute();
    }

    /**
     *
     */
    public void onSuccess(){
        mStatus = SmileTransitionStatus.SUCCESS;
        execute();
    }

    /**
     *
     * @return
     */
    public boolean isVisibility(){
        return getVisibility() == View.VISIBLE;
    }

    /**
     *
     * @return
     */
    public boolean isLoading(){
        return mStatus == SmileTransitionStatus.LOADER;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty(){
        return mStatus == SmileTransitionStatus.EMPTY;
    }

    /**
     *
     */
    enum SmileTransitionStatus {
        /** 正在加载 */
        LOADER,
        /** 加载成功 */
        SUCCESS,
        /** 没有数据 */
        EMPTY
    }
}

package com.yongyongwang.multimedia.choose.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.MultimediaConfig;
import com.yongyongwang.multimedia.choose.MultimediaEnum;
import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.entity.MultimediaFolderEntity;
import com.yongyongwang.multimedia.choose.window.MultimediaFolderWindow;

import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/6
 */
public class MultimediaTopLayout extends LinearLayout implements View.OnClickListener {

    private String TAG = MultimediaTopLayout.class.getSimpleName();

    private ImageView backImageView;

    private LinearLayout folderBackground;
    private TextView folderTextView;
    private ImageView folderImageView;

    private TextView button;

    private Activity mActivity;

    /**
     *
     */
    private MultimediaConfig mConfig = MultimediaConfig.getInstance();

    private MultimediaFolderWindow folderWindow;
    private ObjectAnimator rotationAnimator;
    private ObjectAnimator recoverAnimator;

    private OnMultimediaTopLayoutListener layoutListener;

    public MultimediaTopLayout(Context context) {
        super(context);
        initView();
    }

    public MultimediaTopLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MultimediaTopLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     *
     */
    private void initView(){
        mActivity = (Activity) getContext();
        LayoutInflater.from(getContext()).inflate(R.layout.multimedia_top_layout,this);

        backImageView = findViewById(R.id.multimedia_top_layout_close);

        folderBackground = findViewById(R.id.multimedia_top_layout_folder);
        folderTextView = findViewById(R.id.multimedia_top_layout_folder_tv);
        folderImageView = findViewById(R.id.multimedia_top_layout_folder_iv);

        button = findViewById(R.id.multimedia_top_layout_button);

        rotationAnimator = ObjectAnimator.ofFloat(folderImageView,"rotation",0,180f);
        rotationAnimator.setDuration(500);

        recoverAnimator = ObjectAnimator.ofFloat(folderImageView,"rotation",180f,0);
        recoverAnimator.setDuration(500);

        String type = getContext().getString(R.string.multimedia_choose_folder_all);
        if (mConfig.getMultimediaType() == MultimediaEnum.IMAGE){
            type = getContext().getString(R.string.multimedia_choose_folder_image);
        }else if (mConfig.getMultimediaType() == MultimediaEnum.VIDEO){
            type = getContext().getString(R.string.multimedia_choose_folder_video);
        }
        folderTextView.setText(type);

        button.setEnabled(false);
        backImageView.setOnClickListener(this);
        folderBackground.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    /**
     *
     * @param mConfig
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void setConfig(MultimediaConfig mConfig) {
        this.mConfig = mConfig;
        if (!TextUtils.isEmpty(mConfig.getConfirmText()))
            button.setText(mConfig.getConfirmText());
        if (mConfig.getConfirmDrawable() > 0)
            button.setBackground(getResources().getDrawable(mConfig.getConfirmDrawable()));
        if (mConfig.getConfirmTextColor() > 0)
            button.setTextColor(getResources().getColor(mConfig.getConfirmTextColor()));
    }

    /**
     *
     * @param data
     */
    public void init(List<MultimediaFolderEntity> data){
        if (data == null || data.size() == 0)
            return;
        MultimediaFolderEntity entity = data.get(0);
        folderTextView.setText(entity.getFolder());

        folderWindow = new MultimediaFolderWindow(getContext(),data,new MultimediaFolderWindow.OnMultimediaFolderWindowListener(){
            @Override
            public void onDismiss() {
                folderImageView.clearAnimation();
                recoverAnimator.start();
            }

            @Override
            public void onListener(MultimediaFolderEntity entity) {
                folderTextView.setText(entity.getFolder());
                if (layoutListener != null)
                    layoutListener.onFolderChoose(entity);
            }
        });
    }

    /**
     *
     * @param num
     */
    public void setCheckedNum(int num){

        /*if (num > 0){
            button.setText(String.format(getContext().getString(R.string.multimedia_choose_confirm_n),num));
        }else {
            button.setText(getContext().getString(R.string.multimedia_choose_confirm));
        }*/

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
     * @param layoutListener
     */
    public void setLayoutListener(OnMultimediaTopLayoutListener layoutListener) {
        this.layoutListener = layoutListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.multimedia_top_layout_close) {
            mActivity.finish();
        } else if (id == R.id.multimedia_top_layout_folder) {
            if (folderWindow == null)
                return;
            folderImageView.clearAnimation();
            folderWindow.showAsDropDown(this);
            rotationAnimator.start();
        } else if (id == R.id.multimedia_top_layout_button) {
            if (layoutListener != null)
                layoutListener.onConfirm();
        }
    }

    /**
     *
     */
    public interface OnMultimediaTopLayoutListener {

        /**
         *
         */
        void onConfirm();

        /**
         *
         * @param entity
         */
        void onFolderChoose(MultimediaFolderEntity entity);
    }
}

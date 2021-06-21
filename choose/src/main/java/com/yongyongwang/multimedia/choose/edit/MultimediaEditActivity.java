package com.yongyongwang.multimedia.choose.edit;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.edit.view.CropPicker;
import com.yongyongwang.multimedia.choose.edit.view.CropView;
import com.yongyongwang.multimedia.choose.edit.view.MultimediaAdjustLayout;
import com.yongyongwang.multimedia.choose.edit.view.MultimediaBeautifyLayout;
import com.yongyongwang.multimedia.choose.edit.view.MultimediaEditButtonLayout;
import com.yongyongwang.multimedia.choose.edit.view.MultimediaFilterLayout;
import com.yongyongwang.multimedia.choose.edit.view.MultimediaRatioLayout;
import com.yongyongwang.multimedia.choose.edit.view.TwoLineSeekBar;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.magic.utils.MagicParams;
import com.yongyongwang.multimedia.choose.util.FileUtils;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/7
 */
public class MultimediaEditActivity extends MultimediaBaseActivity implements View.OnClickListener {

    private ImageView backImageView;
    private ImageView saveImageView;

    private CropView cropView;

    private MultimediaEditButtonLayout buttonView;
    private MultimediaRatioLayout ratioView;
    private MultimediaFilterLayout filterLayout;
    private MultimediaBeautifyLayout beautifyLayout;
    private MultimediaAdjustLayout adjustLayout;

    private MultimediaEntity mEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.multimedia_edit_activity;
    }

    @Override
    protected void initView() {
        backImageView = findViewById(R.id.multimedia_edit_back);
        saveImageView = findViewById(R.id.multimedia_edit_save);
        cropView = findViewById(R.id.multimedia_edit_crop);
        buttonView = findViewById(R.id.multimedia_edit_button);
        ratioView = findViewById(R.id.multimedia_edit_ration);
        filterLayout = findViewById(R.id.multimedia_edit_filter);
        adjustLayout = findViewById(R.id.multimedia_edit_adjust);
        beautifyLayout = findViewById(R.id.multimedia_edit_beautify);

        MagicParams.context = this;
        FileUtils.initPath(this);

        backImageView.setOnClickListener(this);
        saveImageView.setOnClickListener(this);

        buttonView.setClickListener(arg -> {
            if (!TextUtils.equals(arg,getString(R.string.multimedia_edit_crop))){
                cropView.getEditRect();
            }
            if (TextUtils.equals(arg,getString(R.string.multimedia_edit_crop))){
                filterLayout.setVisibility(View.GONE);
                beautifyLayout.setVisibility(View.GONE);
                adjustLayout.setVisibility(View.GONE);
                ratioView.setVisibility(View.VISIBLE);
            }else if (TextUtils.equals(arg,getString(R.string.multimedia_edit_filter))){
                ratioView.setVisibility(View.GONE);
                beautifyLayout.setVisibility(View.GONE);
                adjustLayout.setVisibility(View.GONE);
                filterLayout.setVisibility(View.VISIBLE);
            }else if (TextUtils.equals(arg,getString(R.string.multimedia_edit_beauty))){
                ratioView.setVisibility(View.GONE);
                adjustLayout.setVisibility(View.GONE);
                filterLayout.setVisibility(View.GONE);
                beautifyLayout.setVisibility(View.VISIBLE);
            }else {
                ratioView.setVisibility(View.GONE);
                filterLayout.setVisibility(View.GONE);
                beautifyLayout.setVisibility(View.GONE);
                adjustLayout.setVisibility(View.VISIBLE);
            }
        });
        filterLayout.setClickListener(type -> {

        });
        ratioView.setClickListener(ration -> {
            if (TextUtils.equals(ration,getString(R.string.multimedia_ration_1))){
                cropView.setRatio(CropPicker.SQUARE);
            }else if (TextUtils.equals(ration,getString(R.string.multimedia_ration_4))){
                cropView.setRatio(CropPicker.FOUR_ONE);
            }else if (TextUtils.equals(ration,getString(R.string.multimedia_ration_16))){
                cropView.setRatio(CropPicker.SIXTEEN_NINE);
            }else {
                cropView.setRatio(CropPicker.NINE_SIXTEEN);
            }
        });

        mEntity = (MultimediaEntity) getIntent().getSerializableExtra(MULTIMEDIA_REQUEST_DATA);
        if (mEntity != null){
            cropView.setImage(mEntity.getPath());
        }
    }

    @Override
    protected void onUpdatePreview(int num) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.multimedia_edit_back){
            finish();
        }else if (v.getId() == R.id.multimedia_edit_save){

        }
    }
}

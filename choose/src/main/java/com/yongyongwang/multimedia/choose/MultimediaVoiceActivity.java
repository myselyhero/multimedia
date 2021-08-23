package com.yongyongwang.multimedia.choose;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.entity.MultimediaVoiceEntity;
import com.yongyongwang.multimedia.choose.util.MultimediaContentResolver;
import com.yongyongwang.multimedia.choose.util.ToastUtil;
import com.yongyongwang.multimedia.choose.view.MultimediaVoiceView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/8/1
 */
public class MultimediaVoiceActivity extends MultimediaBaseActivity implements View.OnClickListener {

    private static final String TAG = "MultimediaMusicActivity";

    private LinearLayout background;
    private RelativeLayout statusBarLayout;
    private ImageView backImageView;
    private TextView button;
    private MultimediaVoiceView voiceView;

    private MultimediaContentResolver contentResolver;

    private final List<MultimediaVoiceEntity> chooseDataSource = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.multimedia_voice_activity;
    }

    @Override
    protected void initView() {
        background = findViewById(R.id.multimedia_voice_bg);
        statusBarLayout = findViewById(R.id.multimedia_voice_bar);
        backImageView = findViewById(R.id.multimedia_voice_back);
        button = findViewById(R.id.multimedia_voice_btn);
        voiceView = findViewById(R.id.multimedia_voice_item);

        statusBarLayout.setBackgroundColor(mChooseConfig.isDarkTheme() ? getResources().getColor(R.color.multimedia_theme) : getResources().getColor(R.color.multimedia_white_theme));
        backImageView.setImageResource(mChooseConfig.isDarkTheme() ? R.drawable.multimedia_back_white : R.drawable.multimedia_back_black);
        background.setBackgroundColor(mChooseConfig.isDarkTheme() ? getResources().getColor(R.color.multimedia_theme_background) : getResources().getColor(R.color.multimedia_white_background));
        if (!TextUtils.isEmpty(mChooseConfig.getConfirmText()))
            button.setText(mChooseConfig.getConfirmText());
        if (mChooseConfig.getConfirmDrawable() > 0)
            button.setBackground(getResources().getDrawable(mChooseConfig.getConfirmDrawable()));
        if (mChooseConfig.getConfirmTextColor() > 0)
            button.setTextColor(getResources().getColor(mChooseConfig.getConfirmTextColor()));

        voiceView.setDarkTheme(mChooseConfig.isDarkTheme());
        voiceView.setClickListener((position, entity) -> {
            if (entity.isChoose()){
                entity.setChoose(false);
                chooseDataSource.remove(entity);
            }else {
                if (mChooseConfig.isOnly()){
                    voiceView.clearChoose();
                }else {
                    if (chooseDataSource.size() >= mChooseConfig.getMaxNum()){
                        ToastUtil.showShort(this,String.format(getString(R.string.multimedia_choose_max_voice),mChooseConfig.getMaxNum()));
                        return;
                    }
                }
                entity.setChoose(true);
                chooseDataSource.add(entity);
            }
            voiceView.notifyData(position);
            choose();
        });

        backImageView.setOnClickListener(this);
        button.setOnClickListener(this);

        if (checkReadAndWrite(this)){
            loadData();
        }
    }

    @Override
    protected void onUpdatePreview(int num) {

    }

    /**
     *
     */
    private void choose(){
        if (mChooseConfig.isOnly()){
            button.setEnabled(chooseDataSource.size() > 0);
        }else {
            if (mChooseConfig.getMinNum() > 0){
                button.setEnabled(chooseDataSource.size() >= mChooseConfig.getMinNum());
            }else {
                button.setEnabled(chooseDataSource.size() > 0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.multimedia_voice_back){
            finish();
        }else if (v.getId() == R.id.multimedia_voice_btn){
            if (chooseDataSource.size() == 0)
                return;
            if (MultimediaConfig.voiceResultListener != null){
                MultimediaConfig.voiceResultListener.onListener(chooseDataSource);
            }else {
                Intent intent = new Intent();
                intent.putExtra(RESULT_DATA, (Serializable) chooseDataSource);
                setResult(RESULT_OK,intent);
            }
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE){
            int num = 0;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    num ++;
                }
            }
            if (num == 0){
                loadData();
            }
        }
    }

    /**
     *
     */
    private void loadData(){
        if (contentResolver == null)
            contentResolver = new MultimediaContentResolver(this,mChooseConfig);
        contentResolver.getMusic(list -> {
            voiceView.setDataSource(list);
        });
    }
}
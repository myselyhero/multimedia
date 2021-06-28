package com.yongyongwang.multimedia.choose.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.MultimediaConfig;
import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.camera.listener.ErrorListener;
import com.yongyongwang.multimedia.choose.camera.listener.JCameraListener;
import com.yongyongwang.multimedia.choose.camera.view.JCameraView;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.util.FileUtils;
import com.yongyongwang.multimedia.choose.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc: 拍照录像
 * 
 * @// TODO: 2021/6/15
 */
public class MultimediaCameraActivity extends MultimediaBaseActivity {

    private String TAG = "MultimediaCameraActivity";

    private JCameraView jCameraView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.multimedia_camera_activity;
    }

    @Override
    protected void initView() {
        jCameraView = findViewById(R.id.jcameraview);

        if (mChooseConfig.getCameraType() > 0 && mChooseConfig.getCameraType() == JCameraView.BUTTON_STATE_ONLY_CAPTURE ||
                mChooseConfig.getCameraType() == JCameraView.BUTTON_STATE_ONLY_RECORDER){
            jCameraView.setFeatures(mChooseConfig.getCameraType());
        }else {
            jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        }
        if (mChooseConfig.getRecordMaxDuration() > 3000)
            jCameraView.setDuration(mChooseConfig.getRecordMaxDuration());
        if (mChooseConfig.getRecordMinDuration() > 1000)
            jCameraView.setMinDuration(mChooseConfig.getRecordMinDuration());
        if (TextUtils.isEmpty(mChooseConfig.getDir()))
            FileUtils.initPath(this);
        jCameraView.setSaveVideoPath(TextUtils.isEmpty(mChooseConfig.getDir()) ? FileUtils.DIR : mChooseConfig.getDir());
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                ToastUtil.showShort(MultimediaCameraActivity.this,getString(R.string.multimedia_camera_error));
            }

            @Override
            public void AudioPermissionError() {
                ToastUtil.showShort(MultimediaCameraActivity.this,getString(R.string.multimedia_camera_no_permission));
            }
        });
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                String path = FileUtils.saveBitmap(MultimediaCameraActivity.this,TextUtils.isEmpty(mChooseConfig.getDir()) ? FileUtils.DIR : mChooseConfig.getDir(), bitmap);
                if (TextUtils.isEmpty(path)){
                    ToastUtil.showShort(MultimediaCameraActivity.this,getString(R.string.multimedia_camera_save_error));
                    finish();
                    return;
                }
                MultimediaEntity entity = new MultimediaEntity();
                entity.setPath(path);
                entity.setMimeType("image/jpeg");
                int[] size = FileUtils.getImageSize(path);
                entity.setWidth(size[0]);
                entity.setHeight(size[1]);

                complete(entity);
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame,long duration) {
                /** 通知更新系统相册 */
                Intent notify = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                notify.setData(Uri.fromFile(new File(url)));
                MultimediaCameraActivity.this.sendBroadcast(notify);

                MultimediaEntity entity = new MultimediaEntity();
                entity.setPath(url);
                entity.setMimeType("video/mp4");
                int[] size = FileUtils.getVideoWidthAndHeight(url);
                entity.setWidth(size[0]);
                entity.setHeight(size[1]);
                entity.setDuration(duration);

                complete(entity);
            }
        });

        if (mChooseConfig.getLeftIcon() > 0)
            jCameraView.setLeftIcon(mChooseConfig.getLeftIcon());
        if (mChooseConfig.getRightIcon() > 0)
            jCameraView.setRightIcon(mChooseConfig.getRightIcon());

        if (MultimediaConfig.leftIconListener != null){
            jCameraView.setLeftClickListener(MultimediaConfig.leftIconListener);
        }else {
            jCameraView.setLeftClickListener(v -> {
                finish();
            });
        }
        if (MultimediaConfig.rightIconListener != null)
            jCameraView.setRightClickListener(MultimediaConfig.rightIconListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    @Override
    protected void onUpdatePreview(int num) {

    }

    /**
     *
     * @param entity
     */
    protected void complete(MultimediaEntity entity){
        if (MultimediaConfig.cameraListener != null){
            List<MultimediaEntity> list = new ArrayList<>();
            list.add(entity);
            MultimediaConfig.cameraListener.onResult(list);
        }else {
            Intent intent = new Intent();
            intent.putExtra(RESULT_DATA, entity);
            setResult(RESULT_OK,intent);
        }
        finish();
    }
}

package com.yongyongwang.multimedia.choose;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.camera.JCameraView;
import com.yongyongwang.multimedia.choose.camera.listener.ErrorListener;
import com.yongyongwang.multimedia.choose.camera.listener.JCameraListener;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.util.FileUtils;
import com.yongyongwang.multimedia.choose.util.ToastUtil;

import java.io.File;

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
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        FileUtils.initPath(this);
        jCameraView.setSaveVideoPath(FileUtils.DIR);
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                ToastUtil.showShort(MultimediaCameraActivity.this,"摄像头打开失败！");
            }

            @Override
            public void AudioPermissionError() {
                ToastUtil.showShort(MultimediaCameraActivity.this,"没有录音权限！");
            }
        });
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                String path = FileUtils.saveBitmap(MultimediaCameraActivity.this,FileUtils.DIR, bitmap);
                if (TextUtils.isEmpty(path)){
                    ToastUtil.showShort(MultimediaCameraActivity.this,"图片保存失败！");
                    finish();
                    return;
                }
                MultimediaEntity entity = new MultimediaEntity();
                entity.setPath(path);
                entity.setMimeType("image/jpeg");
                int[] size = FileUtils.getImageSize(path);
                entity.setWidth(size[0]);
                entity.setHeight(size[1]);
                Intent intent = new Intent();
                intent.putExtra(MULTIMEDIA_RESULT_DATA,entity);
                setResult(RESULT_OK,intent);
                finish();
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
                Intent intent = new Intent();
                intent.putExtra(MULTIMEDIA_RESULT_DATA,entity);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        jCameraView.setLeftClickListener(v -> {
            finish();
        });
        jCameraView.setRightClickListener(v -> {
            Log.e(TAG, "initView: 点击右边按钮");
        });
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
}

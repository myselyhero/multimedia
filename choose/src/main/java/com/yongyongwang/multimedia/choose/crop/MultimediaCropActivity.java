package com.yongyongwang.multimedia.choose.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.yongyongwang.multimedia.choose.MultimediaConfig;
import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.crop.view.CropView;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.util.FileUtils;
import com.yongyongwang.multimedia.choose.util.StatusBarUtil;
import com.yongyongwang.multimedia.choose.util.ToastUtil;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/8/17
 */
public class MultimediaCropActivity extends MultimediaBaseActivity implements View.OnClickListener {

    private static final String TAG = "MultimediaCropActivity";

    private ImageView backImageView;
    private ImageView saveImageView;

    private CropView cropView;

    private String mPath;

    @Override
    protected int getLayoutId() {
        return R.layout.multimedia_crop_activity;
    }

    @Override
    protected void initView() {
        backImageView = findViewById(R.id.multimedia_crop_back);
        saveImageView = findViewById(R.id.multimedia_crop_save);
        cropView = findViewById(R.id.multimedia_crop_cv);

        StatusBarUtil.setStatusBarColor(this,R.color.multimedia_theme);
        StatusBarUtil.setStatusBarDarkTheme(this,false);

        FileUtils.initPath(this);

        backImageView.setOnClickListener(this);
        saveImageView.setOnClickListener(this);

        mPath = getIntent().getStringExtra(REQUEST_DATA);
        if (TextUtils.isEmpty(mPath))
            return;
        cropView.setDataSource(mPath);
    }

    @Override
    protected void onUpdatePreview(int num) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.multimedia_crop_back){
            finish();
        }else if (v.getId() == R.id.multimedia_crop_save){
            if (mPath == null)
                return;

            cropView.crop();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = cropView.getBitmap();
                    if (bitmap == null)
                        return;
                    String str = FileUtils.saveBitmap(MultimediaCropActivity.this,FileUtils.DIR,bitmap);
                    if (!TextUtils.isEmpty(str)){
                        Rect rect = cropView.getCropRect();
                        MultimediaEntity entity = new MultimediaEntity();
                        entity.setPath(mPath);
                        entity.setCropPath(str);
                        entity.setMimeType("image/png");
                        entity.setHeight(rect.height());
                        entity.setWidth(rect.width());
                        completeCrop(entity);
                    }
                }
            },500);
        }
    }

    /**
     *
     * @param entity
     */
    private void completeCrop(MultimediaEntity entity){
        if (MultimediaConfig.cropResultListener != null){
            MultimediaConfig.cropResultListener.onResult(entity);
        }else {
            Intent intent = new Intent();
            intent.putExtra(RESULT_DATA,entity);
            setResult(RESULT_OK,intent);
        }
        finish();
    }
}
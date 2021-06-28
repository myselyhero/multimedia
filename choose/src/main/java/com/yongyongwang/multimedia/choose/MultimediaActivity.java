package com.yongyongwang.multimedia.choose;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.camera.MultimediaCameraActivity;
import com.yongyongwang.multimedia.choose.edit.MultimediaEditActivity;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.entity.MultimediaFolderEntity;
import com.yongyongwang.multimedia.choose.model.MultimediaContentFolderListener;
import com.yongyongwang.multimedia.choose.model.MultimediaContentListener;
import com.yongyongwang.multimedia.choose.util.FileUtils;
import com.yongyongwang.multimedia.choose.util.MultimediaCompressUtil;
import com.yongyongwang.multimedia.choose.util.MultimediaContentResolver;
import com.yongyongwang.multimedia.choose.util.ProgressManagerKit;
import com.yongyongwang.multimedia.choose.view.MultimediaBottomLayout;
import com.yongyongwang.multimedia.choose.view.MultimediaRecyclerView;
import com.yongyongwang.multimedia.choose.view.MultimediaTopLayout;
import com.yongyongwang.multimedia.choose.view.TransitionView;

import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/5
 */
public class MultimediaActivity extends MultimediaBaseActivity implements MultimediaContentFolderListener,MultimediaContentListener {

    private static final String TAG = MultimediaActivity.class.getSimpleName();

    private TransitionView transitionView;
    private MultimediaTopLayout mTopLayout;
    private MultimediaBottomLayout mBottomLayout;
    private MultimediaRecyclerView mRecyclerView;

    private MultimediaContentResolver contentResolver;

    @Override
    protected void onResume() {
        super.onResume();
        //预览页面数据改变后在这里刷新一下
        if (mRecyclerView != null)
            mRecyclerView.update();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.multimedia_activity;
    }

    @Override
    protected void initView() {
        mTopLayout = findViewById(R.id.multimedia_top);
        mBottomLayout = findViewById(R.id.multimedia_bottom);
        mRecyclerView = findViewById(R.id.multimedia_item);
        transitionView = findViewById(R.id.multimedia_transition);

        mRecyclerView.setCamera(mChooseConfig.isCamera());
        mRecyclerView.setShade(mChooseConfig.isShade());
        mTopLayout.setConfig(mChooseConfig);
        mBottomLayout.setConfig(mChooseConfig);
        if (mChooseConfig.getSpanCount() > 0){
            mRecyclerView.setSpanCount(mChooseConfig.getSpanCount());
        }

        mRecyclerView.setAdapterListener(new MultimediaRecyclerView.OnMultimediaAdapterListener() {
            @Override
            public void onItemClick(int position, MultimediaEntity entity) {
                Intent intent = new Intent(MultimediaActivity.this,MultimediaPreviewActivity.class);
                intent.putExtra(MultimediaPreviewActivity.PREVIEW_POSITION,position);
                startActivityForResult(intent,COMMON_CODE);
            }

            @Override
            public void onCheckClick(int position, MultimediaEntity entity) {
                chooseItem(entity);
            }

            @Override
            public void onCamera() {
                if (checkCameraPermission(MultimediaActivity.this)){
                    Intent intent = new Intent(MultimediaActivity.this, MultimediaCameraActivity.class);
                    startActivityForResult(intent,COMMON_CAMERA_CODE);
                }
            }
        });
        mTopLayout.setLayoutListener(new MultimediaTopLayout.OnMultimediaTopLayoutListener() {
            @Override
            public void onConfirm() {
                if (mChooseConfig.isCompress()){
                    compress();
                }else {
                    complete();
                    finish();
                }
            }

            @Override
            public void onFolderChoose(MultimediaFolderEntity entity) {
                if (entity.getData() == null || entity.getData().size() == 0){
                    if (contentResolver != null)
                        contentResolver.loadMultimedia( entity.getBucketId(), MultimediaActivity.this);
                }else {
                    mRecyclerView.setDataSource(entity.getData());
                }
            }
        });
        mBottomLayout.addEditClickListener(v -> {
            if (mChooseDataSource.size() == 0)
                return;
            MultimediaEntity entity = mChooseDataSource.get(mChooseDataSource.size()-1);
            if (entity == null || FileUtils.isGif(entity.getPath()) || FileUtils.isVideo(entity.getMimeType()))
                return;
            Intent intent = new Intent(this, MultimediaEditActivity.class);
            intent.putExtra(REQUEST_DATA,entity);
            startActivity(intent);
        });
        mBottomLayout.addPreviewClickListener(v -> {
            if (mChooseDataSource.size() == 0)
                return;
            Intent intent = new Intent(MultimediaActivity.this,MultimediaPreviewActivity.class);
            intent.putExtra(MultimediaPreviewActivity.PREVIEW_MODEL,false);
            startActivityForResult(intent,COMMON_CODE);
        });

        transitionView.onLoader();
        if (checkReadAndWrite(this)){
            loadFolder();
        }
    }

    @Override
    protected void onUpdatePreview(int num) {
        mRecyclerView.update();
        mTopLayout.setCheckedNum(num);
        mBottomLayout.setPreviewNum(num);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearFolder();
        clearChoose();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int num = 0;
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                num ++;
            }
        }

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if (num == 0){
                    loadFolder();
                }else {
                    transitionView.onEmpty();
                    Log.e(TAG, "onRequestPermissionsResult: 读写权限未授予！");
                }
                break;
            case PERMISSION_REQUEST_CAMERA:
                if (num > 0){
                    Log.e(TAG, "onRequestPermissionsResult: 拍照、录音权限未授予！");
                }else {
                    Intent intent = new Intent(MultimediaActivity.this,MultimediaCameraActivity.class);
                    startActivityForResult(intent,COMMON_CAMERA_CODE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case COMMON_CODE:
                    if (mChooseConfig.isCompress()){
                        compress();
                    }else {
                        complete();
                        finish();
                    }
                    break;
                case COMMON_CAMERA_CODE:
                    MultimediaEntity entity = (MultimediaEntity) data.getSerializableExtra(RESULT_DATA);
                    if (entity == null)
                        return;
                    clearChoose();
                    chooseItem(entity);
                    for (MultimediaFolderEntity folderEntity : mFolderDataSource) {
                        if (folderEntity.getBucketId() == -1 || folderEntity.isChecked()){
                            folderEntity.add(0,entity);
                        }
                        if (folderEntity.isChecked())
                            mRecyclerView.setDataSource(folderEntity.getData());
                    }
                    break;
            }
        }
    }

    /**
     *
     */
    protected void compress(){
        ProgressManagerKit.getInstance().start(this);
        MultimediaCompressUtil.getInstance().compress(this,mChooseConfig.getDir(),mChooseDataSource,data -> {
            ProgressManagerKit.getInstance().close();
            complete();
            finish();
        });
    }

    /**
     *
     */
    private void loadFolder(){

        if (contentResolver == null)
            contentResolver = new MultimediaContentResolver(this,mChooseConfig);
        contentResolver.loadFolder(this);
    }

    @Override
    public void onResult(List<MultimediaFolderEntity> data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data == null || data.size() == 0){
                    transitionView.onEmpty();
                }else {
                    transitionView.onSuccess();
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.animate()
                            .alpha(1f)
                            .setDuration(1000)
                            .setListener(null);
                    mFolderDataSource.addAll(data);
                    mTopLayout.init(mFolderDataSource);
                    MultimediaFolderEntity folderEntity = mFolderDataSource.get(0);
                    folderEntity.setChecked(true);
                    contentResolver.loadMultimedia(folderEntity.getBucketId(), MultimediaActivity.this);
                }
            }
        });
    }

    @Override
    public void onMultimedia(long bucketId, @NonNull List<MultimediaEntity> data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data == null || data.size() == 0){
                    mRecyclerView.setVisibility(View.GONE);
                }else {
                    if (mRecyclerView.getVisibility() == View.GONE){
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                    //判断已选
                    for (MultimediaEntity entity : data) {
                        for (MultimediaEntity e: mChooseDataSource) {
                            if (TextUtils.equals(entity.getPath(),e.getPath())){
                                entity.setChoose(true);
                            }
                        }
                    }
                    for (MultimediaFolderEntity entity : mFolderDataSource) {
                        if (bucketId == entity.getBucketId()){
                            entity.setData(data);
                            if (entity.isChecked()){
                                mRecyclerView.setDataSource(entity.getData());
                            }
                            break;
                        }
                    }
                }
            }
        });
    }
}

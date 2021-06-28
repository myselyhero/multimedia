package com.yongyongwang.multimedia.choose.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yongyongwang.multimedia.choose.MultimediaConfig;
import com.yongyongwang.multimedia.choose.MultimediaEnum;
import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.entity.MultimediaFolderEntity;
import com.yongyongwang.multimedia.choose.util.FileUtils;
import com.yongyongwang.multimedia.choose.util.SystemBarTintManager;
import com.yongyongwang.multimedia.choose.util.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/5
 */
public abstract class MultimediaBaseActivity extends AppCompatActivity {

    public static final int COMMON_CODE = 1;
    public static final int COMMON_CAMERA_CODE = 2;

    public static final int PERMISSION_REQUEST_CODE = 10;
    public static final int PERMISSION_REQUEST_CAMERA = 11;

    /**
     * 配置参数
     */
    public static final String MULTIMEDIA_CONFIG = "multimedia_config";
    protected MultimediaConfig mChooseConfig;

    /**
     *
     */
    public static final String REQUEST_DATA = "multimedia_request";
    public static final String RESULT_DATA = "multimedia_result";

    /**
     * 文件夹数据源
     */
    protected static List<MultimediaFolderEntity> mFolderDataSource = new ArrayList<>();

    /**
     * 已选择的数据源
     */
    protected static List<MultimediaEntity> mChooseDataSource = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** 设置状态栏颜色 */
        SystemBarTintManager.setStatusBarColor(this,R.color.multimedia_theme);

        mChooseConfig = (MultimediaConfig) getIntent().getSerializableExtra(MULTIMEDIA_CONFIG);
        if (mChooseConfig == null)
            mChooseConfig = MultimediaConfig.getInstance();

        if (getLayoutId() > 0){
            setContentView(getLayoutId());
        }
        initView();
    }

    /**
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     *
     */
    protected abstract void  initView();

    /**
     *
     * @param num
     */
    protected abstract void onUpdatePreview(int num);

    /**
     * 点击确定 优先使用resultListener，如果resultListener为空空则使用setResult()
     *
     */
    protected void complete(){
        if (MultimediaConfig.resultListener != null){
            MultimediaConfig.resultListener.onResult(mChooseDataSource);
        }else {
            Intent intent = new Intent();
            intent.putExtra(RESULT_DATA, (Serializable) mChooseDataSource);
            setResult(RESULT_OK,intent);
        }
    }

    /**
     *
     */
    protected void clearFolder(){
        if (mFolderDataSource.size() != 0){
            mFolderDataSource.clear();
        }
    }

    /**
     *
     */
    protected void clearChoose(){
        if (mChooseDataSource.size() != 0){
            mChooseDataSource.clear();
        }
    }

    /**
     * 全部文件夹和当前文件选中转状态统一
     *
     * @param entity
     */
    private void isNotifyFolder(MultimediaEntity entity){
        for (MultimediaFolderEntity folderEntity : mFolderDataSource) {
            if (folderEntity.getData() != null && folderEntity.getData().size() != 0){
                for (MultimediaEntity e : folderEntity.getData()) {
                    if (TextUtils.equals(e.getPath(),entity.getPath())){
                        e.setChoose(entity.isChoose());
                        break;
                    }
                }
            }
        }
    }

    /**
     * 选中操作
     * @param entity
     */
    protected void chooseItem(MultimediaEntity entity){

        /**
         * 已选
         */
        if (entity.isChoose()){
            entity.setChoose(false);
            mChooseDataSource.remove(entity);
        }else {
            /**
             * 未选择
             * 单选时先将已选择清空
             */
            if (mChooseConfig.isOnly()){
                if (entity.isChoose()){
                    entity.setChoose(false);
                    mChooseDataSource.remove(entity);
                }else {
                    for (MultimediaFolderEntity folderEntity : mFolderDataSource) {
                        if (folderEntity.getData() != null && folderEntity.getData().size() != 0){
                            for (MultimediaEntity multimediaEntity : folderEntity.getData()) {
                                multimediaEntity.setChoose(false);
                            }
                        }
                    }
                    mChooseDataSource.clear();
                    entity.setChoose(true);
                    mChooseDataSource.add(entity);
                }
            }else {
                /**
                 * 未到最大数量
                 */
                if (mChooseDataSource.size() < mChooseConfig.getMaxNum()){
                    //图片视频混合选择
                    if (mChooseConfig.isMixture()){
                        entity.setChoose(true);
                        mChooseDataSource.add(entity);
                    }else {
                        //不支持混合选择如果已选的为视频后续添加类型也必须相同
                        if (mChooseDataSource.size() == 0 || lastIsVideo() && FileUtils.isVideo(entity.getMimeType()) || !lastIsVideo() && !FileUtils.isVideo(entity.getMimeType())){
                            entity.setChoose(true);
                            mChooseDataSource.add(entity);
                        }else {
                            ToastUtil.showShort(this,getString(R.string.multimedia_choose_mixture));
                        }
                    }
                }else {
                    if (mChooseConfig.isMixture()){
                        ToastUtil.showShort(this,String.format(getString(R.string.multimedia_choose_max_mixture),mChooseConfig.getMaxNum()));
                    }else if (mChooseConfig.getMultimediaType() == MultimediaEnum.VIDEO){
                        ToastUtil.showShort(this,String.format(getString(R.string.multimedia_choose_max_video),mChooseConfig.getMaxNum()));
                    }else {
                        ToastUtil.showShort(this,String.format(getString(R.string.multimedia_choose_max_image),mChooseConfig.getMaxNum()));
                    }
                }
            }
        }
        onUpdatePreview(mChooseDataSource.size());
        isNotifyFolder(entity);
    }

    /**
     * 用于在预览界面进行操作时，isAll(false)不删除,treu删除、在不删除的情况下退出预览时需将mChooseDataSource未选中的删除
     * @param entity
     * @param sel 是否已选预览
     * @return
     */
    protected boolean previewChooseItem(MultimediaEntity entity, boolean sel){

        /**
         * 已选
         */
        if (entity.isChoose()){
            entity.setChoose(false);
            if (sel){
                mChooseDataSource.remove(entity);
            }
        }else {
            /**
             * 未选择
             * 单选时先将已选择清空
             */
            if (mChooseConfig.isOnly()){
                for (MultimediaFolderEntity folderEntity : mFolderDataSource) {
                    if (folderEntity.getData() != null && folderEntity.getData().size() != 0){
                        for (MultimediaEntity multimediaEntity : folderEntity.getData()) {
                            multimediaEntity.setChoose(false);
                        }
                    }
                }
                entity.setChoose(true);
                if (sel){
                    mChooseDataSource.clear();
                    mChooseDataSource.add(entity);
                }
            }else {
                /**
                 * 未到最大数量
                 */
                if (mChooseDataSource.size() < mChooseConfig.getMaxNum()){
                    if (mChooseConfig.isMixture()){
                        entity.setChoose(true);
                        if (sel){//这里判断一下 sel等于false时更新状态但是不添加
                            mChooseDataSource.add(entity);
                        }
                    }else {
                        //不支持混合后续选择的类型必须和前一个一致
                        if (mChooseDataSource.size() == 0 || lastIsVideo() && FileUtils.isVideo(entity.getMimeType()) || !lastIsVideo() && !FileUtils.isVideo(entity.getMimeType())){
                            entity.setChoose(true);
                            if (sel){
                                mChooseDataSource.add(entity);
                            }
                        }else {
                            ToastUtil.showShort(this,getString(R.string.multimedia_choose_mixture));
                        }
                    }
                }else {
                    if (mChooseConfig.isMixture()){
                        ToastUtil.showShort(this,String.format(getString(R.string.multimedia_choose_max_mixture),mChooseConfig.getMaxNum()));
                    }else if (mChooseConfig.getMultimediaType() == MultimediaEnum.VIDEO){
                        ToastUtil.showShort(this,String.format(getString(R.string.multimedia_choose_max_video),mChooseConfig.getMaxNum()));
                    }else {
                        ToastUtil.showShort(this,String.format(getString(R.string.multimedia_choose_max_image),mChooseConfig.getMaxNum()));
                    }
                }
            }
        }

        if (sel){
            onUpdatePreview(mChooseDataSource.size());
        }else {//虽然不删除，但是选中数量要更新
            int i = 0;
            for (MultimediaEntity multimediaEntity : mChooseDataSource) {
                if (multimediaEntity.isChoose())
                    i++;
            }
            onUpdatePreview(i);
        }
        isNotifyFolder(entity);
        return entity.isChoose();
    }

    /**
     * 判断已选列表最后一个是否为视频
     * @return
     */
    protected boolean lastIsVideo(){

        if (mChooseDataSource.size() == 0){
           return false;
        }

        MultimediaEntity entity = mChooseDataSource.get(mChooseDataSource.size() -1);
        return FileUtils.isVideo(entity.getMimeType());
    }

    /* 权限 */

    /**
     * 读写权限
     * @param mContexts
     * @return
     */
    protected boolean checkReadAndWrite(Context mContexts) {
        boolean flag = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContexts, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(mContexts, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 检查拍照和录音权限
     * @param mContexts
     * @return
     */
    protected boolean checkCameraPermission(Context mContexts) {
        boolean flag = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContexts, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(mContexts, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CAMERA);
                flag = false;
            }
        }
        return flag;
    }
}

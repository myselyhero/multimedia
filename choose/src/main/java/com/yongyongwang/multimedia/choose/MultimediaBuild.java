package com.yongyongwang.multimedia.choose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;


import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.camera.MultimediaCameraActivity;
import com.yongyongwang.multimedia.choose.model.MultimediaVoiceResultListener;
import com.yongyongwang.multimedia.choose.model.MultimediaResultListener;

import java.lang.ref.WeakReference;

/**
 * @author yongyong
 * 
 * @// TODO: 2021/6/5
 */
public class MultimediaBuild {

    private WeakReference<Context> mContext;
    private WeakReference<Activity> mActivity;

    private MultimediaConfig mChooseConfig = MultimediaConfig.getInstance();

    private MultimediaBuild(Context context) {
        mContext = new WeakReference<>(context);
        mActivity = new WeakReference<>((Activity) context);
    }

    /**
     *
     * @param context
     * @return
     */
    public static MultimediaBuild create(Context context) {
        return new MultimediaBuild(context);
    }

    /**
     * 选择类型
     * @param type
     * @return
     */
    public MultimediaBuild setMultimediaType (MultimediaEnum type){
        mChooseConfig.setMultimediaType(type);
        return this;
    }

    /**
     * 单选
     * @param only
     * @return
     */
    public MultimediaBuild isOnly(boolean only){
        mChooseConfig.setOnly(only);
        return this;
    }

    /**
     * 单选模式下预览页面是否需要已选列表展示
     * @param preview
     * @return
     */
    public MultimediaBuild isOnlyPreview(boolean preview){
        mChooseConfig.setOnlyPreview(preview);
        return this;
    }

    /**
     * 选中后是否需要阴影效果
     * @param shade
     * @return
     */
    public MultimediaBuild isShade(boolean shade){
        mChooseConfig.setShade(shade);
        return this;
    }

    /**
     * 最大可选数量
     * @param maxNum
     * @return
     */
    public MultimediaBuild maxNum(int maxNum){
        mChooseConfig.setMaxNum(maxNum);
        return this;
    }

    /**
     * 设置图片的最小选择数量
     * @param minNum
     * @return
     */
    public MultimediaBuild minNum(int minNum){
        mChooseConfig.setMinNum(minNum);
        return this;
    }

    /**
     * 混合选择(图片/视频)
     * @param mixture
     * @return
     */
    public MultimediaBuild mixture(boolean mixture){
        mChooseConfig.setMixture(mixture);
        return this;
    }

    /**
     * 设置显示列数
     * @param spanCount
     * @return
     */
    public MultimediaBuild spanCount(int spanCount){
        mChooseConfig.setSpanCount(spanCount);
        return this;
    }

    /**
     * 过滤图片的最大大小
     * @param size MB
     * @return
     */
    public MultimediaBuild maxSize(int size){
        mChooseConfig.setMaxSize(size);
        return this;
    }

    /**
     * 是否剪切
     * @param crop
     * @return
     */
    public MultimediaBuild isCrop(boolean crop){
        mChooseConfig.setCrop(crop);
        return this;
    }

    /**
     * 是否压缩
     * @param compress
     * @return
     */
    public MultimediaBuild isCompress(boolean compress){
        mChooseConfig.setCompress(compress);
        return this;
    }

    /**
     * 是否可拍照
     * @param camera
     * @return
     */
    public MultimediaBuild isCamera(boolean camera){
        mChooseConfig.setCamera(camera);
        return this;
    }

    /**
     * 过滤小于该值视频
     * @param duration
     * @return
     */
    public MultimediaBuild minDuration(long duration){
        mChooseConfig.setMinDuration(duration);
        return this;
    }

    /**
     * 过滤大于该值视频
     * @param duration
     * @return
     */
    public MultimediaBuild maxDuration(long duration){
        mChooseConfig.setMaxDuration(duration);
        return this;
    }

    /**
     *  是否显示gif
     * @param gif
     * @return
     */
    public MultimediaBuild isGif(boolean gif){
        mChooseConfig.setGif(gif);
        return this;
    }

    /**
     * 产生的图片/视频自定义保存地址
     * @param dir
     * @return
     */
    public MultimediaBuild dir(String dir){
        mChooseConfig.setDir(dir);
        return this;
    }

    /**
     * 暗色主题
     * @param dark
     * @return
     */
    public MultimediaBuild darkTheme(boolean dark){
        mChooseConfig.setDarkTheme(dark);
        return this;
    }

    /**
     * 确认按钮
     * @param text
     * @return
     */
    public MultimediaBuild confirmText(String text){
        mChooseConfig.setConfirmText(text);
        return this;
    }

    /**
     *
     * @param color
     * @return
     */
    public MultimediaBuild confirmTextColor(int color){
        mChooseConfig.setConfirmTextColor(color);
        return this;
    }

    /**
     *
     * @param drawable
     * @return
     */
    public MultimediaBuild confirmDrawable(int drawable){
        mChooseConfig.setConfirmDrawable(drawable);
        return this;
    }

    /**
     * 静音
     * @param mute
     * @return
     */
    public MultimediaBuild mute(boolean mute){
        mChooseConfig.setMute(mute);
        return this;
    }

    /**
     * 播放器是否循环
     * @param loop
     * @return
     */
    public MultimediaBuild isLoop(boolean loop){
        mChooseConfig.setLoop(loop);
        return this;
    }

    /**
     * 是否自动播放
     * @param auto
     * @return
     */
    public MultimediaBuild autoPlayer(boolean auto){
        mChooseConfig.setAutoPlayer(auto);
        return this;
    }

    /**
     * {@link com.yongyongwang.multimedia.choose.camera.view.JCameraView}
     * @param type BUTTON_STATE_BOTH 拍照/录像 BUTTON_STATE_ONLY_CAPTURE 拍照
     *             BUTTON_STATE_ONLY_RECORDER 录像
     * @return
     */
    public MultimediaBuild cameraType(int type){
        mChooseConfig.setCameraType(type);
        return this;
    }

    /**
     * 视频最大录制时长
     * @param duration
     * @return
     */
    public MultimediaBuild recordMaxDuration(int duration){
        if (duration < 0)
            return this;
        mChooseConfig.setRecordMaxDuration(duration);
        return this;
    }

    /**
     * 视频最小录制时长
     * @param duration
     * @return
     */
    public MultimediaBuild recordMinDuration(int duration){
        if (duration < 0)
            return this;
        mChooseConfig.setRecordMinDuration(duration);
        return this;
    }

    /**
     *
     * @param leftIcon
     * @return
     */
    public MultimediaBuild leftIcon(int leftIcon){
        mChooseConfig.setLeftIcon(leftIcon);
        return this;
    }

    /**
     *
     * @param listener
     * @return
     */
    public MultimediaBuild leftIconClickListener(View.OnClickListener listener){
        MultimediaConfig.leftIconListener = new WeakReference<>(listener).get();
        return this;
    }

    /**
     *
     * @param rightIcon
     * @return
     */
    public MultimediaBuild rightIcon(int rightIcon){
        mChooseConfig.setRightIcon(rightIcon);
        return this;
    }

    /**
     *
     * @param listener
     * @return
     */
    public MultimediaBuild rightIconClickListener(View.OnClickListener listener){
        MultimediaConfig.rightIconListener = new WeakReference<>(listener).get();
        return this;
    }

    /**
     *
     * @param resultListener
     */
    public void start(MultimediaResultListener resultListener){
        if (resultListener == null)
            return;
        MultimediaConfig.resultListener = new WeakReference<>(resultListener).get();

        Intent intent = new Intent(mActivity.get(), MultimediaActivity.class);
        intent.putExtra(MultimediaBaseActivity.MULTIMEDIA_CONFIG,mChooseConfig);
        mActivity.get().startActivity(intent);
    }

    /**
     *
     * @param requestCode
     */
    public void start(int requestCode){
        Intent intent = new Intent(mActivity.get(), MultimediaActivity.class);
        intent.putExtra(MultimediaBaseActivity.MULTIMEDIA_CONFIG,mChooseConfig);
        mActivity.get().startActivityForResult(intent,requestCode);
    }

    /**
     *
     * @param listener
     */
    public void startVoice(MultimediaVoiceResultListener listener){
        if (listener == null)
            return;
        MultimediaConfig.voiceResultListener = new WeakReference<>(listener).get();

        Intent intent = new Intent(mActivity.get(), MultimediaVoiceActivity.class);
        intent.putExtra(MultimediaBaseActivity.MULTIMEDIA_CONFIG,mChooseConfig);
        mActivity.get().startActivity(intent);
    }

    /**
     *
     * @param requestCode
     */
    public void startVoice(int requestCode){
        Intent intent = new Intent(mActivity.get(), MultimediaVoiceActivity.class);
        intent.putExtra(MultimediaBaseActivity.MULTIMEDIA_CONFIG,mChooseConfig);
        mActivity.get().startActivityForResult(intent,requestCode);
    }

    /**
     *
     * @param url
     */
    public void startPlayer(String url){
        if (TextUtils.isEmpty(url))
            return;
        Intent intent = new Intent(mActivity.get(), MultimediaPlayerActivity.class);
        intent.putExtra(MultimediaBaseActivity.MULTIMEDIA_CONFIG,mChooseConfig);
        intent.putExtra(MultimediaBaseActivity.REQUEST_DATA,url);
        mActivity.get().startActivity(intent);
    }

    /**
     *
     * @param resultListener
     */
    public void startCamera(MultimediaResultListener resultListener){
        if (resultListener == null)
            return;
        MultimediaConfig.cameraListener = new WeakReference<>(resultListener).get();

        Intent intent = new Intent(mActivity.get(), MultimediaCameraActivity.class);
        intent.putExtra(MultimediaBaseActivity.MULTIMEDIA_CONFIG,mChooseConfig);
        mActivity.get().startActivity(intent);
    }

    /**
     *
     * @param requestCode
     */
    public void startCamera(int requestCode){
        Intent intent = new Intent(mActivity.get(), MultimediaCameraActivity.class);
        intent.putExtra(MultimediaBaseActivity.MULTIMEDIA_CONFIG,mChooseConfig);
        mActivity.get().startActivityForResult(intent,requestCode);
    }
}

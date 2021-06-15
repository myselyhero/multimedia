package com.yongyongwang.multimedia.choose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;



import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
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
     * 最大可选数量
     * @param maxNum
     * @return
     */
    public MultimediaBuild setMaxNum(int maxNum){
        mChooseConfig.setMaxNum(maxNum);
        return this;
    }

    /**
     * 设置图片的最小选择数量
     * @param minNum
     * @return
     */
    public MultimediaBuild setMinNum(int minNum){
        mChooseConfig.setMinNum(minNum);
        return this;
    }

    /**
     *
     * @param mixture
     * @return
     */
    public MultimediaBuild isMixture(boolean mixture){
        mChooseConfig.setMixture(mixture);
        return this;
    }

    /**
     * 设置显示列数
     * @param spanCount
     * @return
     */
    public MultimediaBuild setSpanCount(int spanCount){
        mChooseConfig.setSpanCount(spanCount);
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
     *
     * @param duration
     * @return
     */
    public MultimediaBuild minDuration(long duration){
        mChooseConfig.setMinDuration(duration);
        return this;
    }

    /**
     *
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
     *
     * @param text
     * @return
     */
    public MultimediaBuild confirmText(String text){
        mChooseConfig.setConfirmText(text);
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
}

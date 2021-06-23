package com.yongyongwang.multimedia.choose.util;

import android.content.Context;
import android.text.TextUtils;

import com.yongyongwang.multimedia.choose.MultimediaConfig;
import com.yongyongwang.multimedia.choose.compress.Luban;
import com.yongyongwang.multimedia.choose.compress.model.OnCompressListener;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.model.MultimediaResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 压缩
 */
public class MultimediaCompressUtil {

    private static MultimediaCompressUtil instance;

    private int count;

    public static MultimediaCompressUtil getInstance() {
        if (instance == null){
            synchronized (MultimediaCompressUtil.class){
                instance = new MultimediaCompressUtil();
            }
        }
        return instance;
    }

    /**
     *
     * @param context
     * @param data
     * @param resultListener
     */
    public void compress(Context context,String dir, List<MultimediaEntity> data, MultimediaResultListener resultListener){

        List<MultimediaEntity> list = new ArrayList<>();
        for (MultimediaEntity entity : data) {
            if (!FileUtils.isGif(entity.getPath()) && !FileUtils.isVideo(entity.getMimeType())){
                list.add(entity);
            }
        }

        //
        if (TextUtils.isEmpty(dir))
            FileUtils.initPath(context);

        Luban.with(context)
                .load(list)
                .ignoreBy(100)
                .setTargetDir(TextUtils.isEmpty(dir) ? FileUtils.DIR : dir)
                .setFocusAlpha(false)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(MultimediaEntity entity) {
                        count++;
                        for (int i = 0; i < data.size(); i++) {
                            MultimediaEntity multimediaEntity = data.get(i);
                            if (TextUtils.equals(entity.getPath(),multimediaEntity.getPath())){
                                data.remove(i);
                                data.add(i,multimediaEntity);
                                break;
                            }
                        }
                        if (count == list.size()){
                            count = 0;
                            resultListener.onResult(data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();
    }
}

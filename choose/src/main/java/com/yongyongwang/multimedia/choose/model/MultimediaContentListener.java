package com.yongyongwang.multimedia.choose.model;

import androidx.annotation.NonNull;

import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;

import java.util.List;

/**
 * @author yongyong
 *
 * desc:媒体查询回调
 *
 * @// TODO: 2021/6/5
 */
public interface MultimediaContentListener {

    /**
     *
     * @param bucketId
     * @param data
     */
    void onMultimedia(long bucketId,@NonNull List<MultimediaEntity> data);
}

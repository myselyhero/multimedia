package com.yongyongwang.multimedia.choose.model;

import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;

import java.util.List;

/**
 * @author myselyhero
 *
 * @desc:
 * 
 * @// TODO: 2021/6/5
 */
public interface MultimediaResultListener {

    /**
     *
     * @param data
     */
    void onResult(List<MultimediaEntity> data);
}

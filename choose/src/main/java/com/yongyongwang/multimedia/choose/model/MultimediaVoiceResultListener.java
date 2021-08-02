package com.yongyongwang.multimedia.choose.model;

import com.yongyongwang.multimedia.choose.entity.MultimediaVoiceEntity;

import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/8/1
 */
public interface MultimediaVoiceResultListener {

    /**
     *
     * @param list
     */
    void onListener(List<MultimediaVoiceEntity> list);
}

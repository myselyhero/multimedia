package com.yongyongwang.multimedia.choose.model;

import com.yongyongwang.multimedia.choose.entity.MultimediaFolderEntity;

import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/5
 */
public interface MultimediaContentFolderListener {

    /**
     *
     * @param data
     */
    void onResult(List<MultimediaFolderEntity> data);
}

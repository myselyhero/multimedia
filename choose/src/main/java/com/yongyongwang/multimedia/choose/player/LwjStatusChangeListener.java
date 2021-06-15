package com.yongyongwang.multimedia.choose.player;

/**
 * @author yongyong
 *
 * @desc:状态改变了的监听
 *
 * @// TODO: 2020/12/24
 */
public interface LwjStatusChangeListener {

    /**
     *
     * @param statusEnum
     */
    void onChangeStatus(LwjStatusEnum statusEnum);
}

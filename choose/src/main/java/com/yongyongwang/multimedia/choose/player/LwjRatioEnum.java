package com.yongyongwang.multimedia.choose.player;

/**
 * @author myselyhero
 *
 * desc:播放器高宽比
 *
 * @// TODO: 2021/6/7
 */
public enum LwjRatioEnum {

    /** 默认，自适应(原高宽) */
    RATIO_DEFAULT,
    /**  */
    RATIO_16_9,
    /**  */
    RATIO_4_3,
    /** 宽度占满、高度随原高度且居中于屏幕 */
    RATIO_CROP,
    /** 缩小视频居中展示 */
    RATIO_SHRINK,
    /** 全屏 */
    RATIO_FULL;
}

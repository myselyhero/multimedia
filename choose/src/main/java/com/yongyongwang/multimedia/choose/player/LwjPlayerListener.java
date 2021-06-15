package com.yongyongwang.multimedia.choose.player;

/**
 * @author myselyhero 
 *
 * desc:播放器监听
 *
 * @// TODO: 2021/6/7
 */
public interface LwjPlayerListener {

    /**
     * 准备结束
     */
    void onPreparedEnd();

    /**
     * 缓冲值
     * @param buffer
     * @param speed
     */
    void onBuffering(int buffer,long speed);

    /**
     * 缓存/缓冲结束
     * @param what
     * @param extra
     */
    void onInfo(int what, int extra);

    /**
     * 宽高改变了
     * @param width
     * @param height
     */
    void onSizeChanged(int width,int height);

    /**
     * 发生错误了
     */
    void onError();

    /**
     * 播放结束
     */
    void onCompletion();
}

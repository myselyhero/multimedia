package com.yongyongwang.multimedia.choose.crop.strategy;

import android.graphics.Bitmap;

import com.yongyongwang.multimedia.choose.crop.exceptions.HandleStrategyException;

/**
 * ImageEditStrategy Interface
 */
public interface IEditStrategy {
    /**
     * Image processing method
     *
     * @param <T>    Specifies the parameter type, which is defined by the image editing algorithm.
     * @param origin Source PixelMap
     * @param params Image editing algorithm parameters.
     * @return PixelMap after edit
     * @throws HandleStrategyException Handle exception
     */
    <T> Bitmap handle(Bitmap origin, EditParams<T> params) throws HandleStrategyException;

    /**
     * Obtains the strategy name.
     *
     * @return Strategy name
     */
    String getName();

    /**
     * Create an corresponding action instance
     *
     * @return An empty action instance
     */
    IEditAction createAction();
}

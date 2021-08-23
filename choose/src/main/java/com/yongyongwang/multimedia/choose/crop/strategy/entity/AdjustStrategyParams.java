package com.yongyongwang.multimedia.choose.crop.strategy.entity;

import android.graphics.Bitmap;

/**
 * Adjust Strategy Params
 * Used to construct Adjust strategy EditParams
 */
public class AdjustStrategyParams {

    private final float progress;
    private Bitmap srcPixelMap;

    /**
     * AdjustStrategyParams Constructor
     *
     * @param progress SlideBar progress
     */
    public AdjustStrategyParams(float progress) {
        this.progress = progress;
    }

    /**
     * Get Progress
     *
     * @return Adjust SlideBar Progress
     */
    public float getProgress() {
        return progress;
    }

    /**
     * Get Source PixelMap
     *
     * @return Source PixelMap
     */
    public Bitmap getSrcPixelMap() {
        return srcPixelMap;
    }

    /**
     * Set Source PixelMap
     *
     * @param srcPixelMap Source PixelMap
     */
    public void setSrcPixelMap(Bitmap srcPixelMap) {
        this.srcPixelMap = srcPixelMap;
    }
}

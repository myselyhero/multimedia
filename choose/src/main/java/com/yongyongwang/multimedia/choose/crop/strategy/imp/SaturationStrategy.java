package com.yongyongwang.multimedia.choose.crop.strategy.imp;

import android.graphics.ColorMatrix;

import com.yongyongwang.multimedia.choose.crop.strategy.IEditAction;
import com.yongyongwang.multimedia.choose.crop.strategy.action.SaturationAction;

/**
 * Saturation Strategy
 */
public class SaturationStrategy extends BaseAdjustStrategy {

    private static final String ADJUST_NAME = "Saturation";
    private static final float MIN_VALUE = 0f;
    private static final float MAX_VALUE = 2f;

    @Override
    protected float getMinValue() {
        return MIN_VALUE;
    }

    @Override
    protected float getMaxValue() {
        return MAX_VALUE;
    }

    @Override
    protected ColorMatrix setColorMatrix(float value) {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(value);
        return cm;
    }


    @Override
    public String getName() {
        return ADJUST_NAME;
    }

    @Override
    public IEditAction createAction() {
        return new SaturationAction(this);
    }
}

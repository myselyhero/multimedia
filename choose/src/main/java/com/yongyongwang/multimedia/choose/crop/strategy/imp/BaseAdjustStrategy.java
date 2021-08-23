package com.yongyongwang.multimedia.choose.crop.strategy.imp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.yongyongwang.multimedia.choose.crop.exceptions.HandleStrategyException;
import com.yongyongwang.multimedia.choose.crop.strategy.EditParams;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditStrategy;
import com.yongyongwang.multimedia.choose.crop.strategy.entity.AdjustStrategyParams;

/**
 * Base Adjust Strategy
 * Common part of different adjustment operations
 */
public abstract class BaseAdjustStrategy implements IEditStrategy {
    /**
     * Adjust Strategy Handle method
     *
     * @param origin Source PixelMap
     * @param params Image editing algorithm parameters.
     * @param <T>    AdjustStrategy params
     * @return PixelMap after adjust
     * @throws HandleStrategyException HandleStrategyException
     */
    @Override
    public <T> Bitmap handle(Bitmap origin, EditParams<T> params) throws HandleStrategyException {
        if (!(params.getParams() instanceof AdjustStrategyParams)) {
            throw new HandleStrategyException("invalid params");
        }
        // Get params
        AdjustStrategyParams strategyParams = (AdjustStrategyParams) params.getParams();
        Bitmap optionBmp = strategyParams.getSrcPixelMap() == null ? origin : strategyParams.getSrcPixelMap();

        // Set canvas
        Bitmap bitmap = Bitmap.createBitmap(optionBmp.getWidth(),optionBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Set paint
        Paint editPaint = getEditPaint(convertProgress(strategyParams.getProgress()));
        // Draw
        canvas.drawBitmap(optionBmp, new Matrix(), editPaint);
        return bitmap;
    }

    /**
     * Get min value
     *
     * @return MIN Value of Adjust
     */
    protected abstract float getMinValue();

    /**
     * Get max value
     *
     * @return MAX Value of Adjust
     */
    protected abstract float getMaxValue();

    /**
     * Set paint colorMatrix
     *
     * @param value Set Color Matrix According to this Value
     * @return ColorMatrix
     */
    protected abstract ColorMatrix setColorMatrix(float value);

    private Paint getEditPaint(float value) {
        Paint originalPaint = new Paint();
        originalPaint.setAntiAlias(true);
        originalPaint.setColorFilter(new ColorMatrixColorFilter(setColorMatrix(value)));
        return originalPaint;
    }

    private float convertProgress(float progress) {
        float minValue = getMinValue();
        float maxValue = getMaxValue();
        if (minValue >= maxValue) {
            new HandleStrategyException("invalid adjust strategy value").printStackTrace();
        }
        float currentValue = (maxValue - minValue) * progress + minValue;
        return currentValue > maxValue ? maxValue : (Math.max(currentValue, minValue));
    }
}
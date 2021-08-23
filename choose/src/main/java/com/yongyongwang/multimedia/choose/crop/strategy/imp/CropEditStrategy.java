package com.yongyongwang.multimedia.choose.crop.strategy.imp;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.yongyongwang.multimedia.choose.crop.exceptions.HandleStrategyException;
import com.yongyongwang.multimedia.choose.crop.strategy.EditParams;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditAction;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditStrategy;
import com.yongyongwang.multimedia.choose.crop.strategy.action.CropEditAction;

/**
 * Crop EditStrategy
 */
public class CropEditStrategy implements IEditStrategy {

    private static final String STRATEGY_NAME = "CropImage";

    private static final float IGNORABLE_PRECISION_DIFFERENCE = 0.003f;

    /**
     * Crop strategy
     *
     * @param origin Original bitmap
     * @param params Crop parameters
     * @param <T>    Parameter type
     * @return Cropped bitmap
     * @throws HandleStrategyException Strategy execute exception
     */
    @Override
    public <T> Bitmap handle(final Bitmap origin, EditParams<T> params) throws HandleStrategyException {
        Rect operatingRect = handleParameters(origin, params);
        return Bitmap.createBitmap(origin,operatingRect.left,operatingRect.top,operatingRect.width(),operatingRect.height(),new Matrix(),false);
    }

    private <T> Rect handleParameters(final Bitmap origin, EditParams<T> options) throws HandleStrategyException {
        validateParameter(origin, options);
        Rect cropRect = options.getProperty(Rect.class, "cropRect");
        return getFinalCropRect(cropRect, origin);
    }

    private <T> void validateParameter(final Bitmap origin, EditParams<T> options) throws HandleStrategyException {
        if (origin == null) {
            throw new HandleStrategyException("The origin is null!");
        }
        if (options == null) {
            throw new HandleStrategyException("The options is null!");
        }
        Rect cropRect = options.getProperty(Rect.class, "cropRect");
        if (cropRect == null) {
            throw new HandleStrategyException("The options don't contain a field named cropRect!");
        }
        if (!isCropRectWithinOrigin(cropRect, origin)) {
            throw new HandleStrategyException("The cropRect is not completely within the origin range!");
        }
    }

    private boolean isCropRectWithinOrigin(Rect cropRect, Bitmap origin) {
        return cropRect.centerX() >= 0
                && cropRect.centerY() >= 0
                && cropRect.width() <= origin.getWidth()
                && cropRect.height() <= origin.getHeight();
    }

    private Rect getFinalCropRect(Rect cropRect, Bitmap origin) {
        if (isNeedPrecisionCompensation(cropRect, origin)) {
            return new Rect(0, 0, origin.getWidth(), origin.getHeight());
        } else {
            return cropRect;
        }
    }

    private boolean isNeedPrecisionCompensation(Rect cropRect, Bitmap origin) {
        Rect originRect = new Rect(0, 0, origin.getWidth(),
                origin.getHeight());
        boolean isLeftDiffIgnorable = isdiffIgnorable(cropRect.centerX(),
                originRect.centerX(), origin.getWidth());
        boolean isTopDiffIgnorable = isdiffIgnorable(cropRect.centerY(),
                originRect.centerY(), origin.getHeight());
        boolean isRightDiffIgnorable = isdiffIgnorable(cropRect.width(),
                originRect.width(), origin.getWidth());
        boolean isBottomIgnorable = isdiffIgnorable(cropRect.height(),
                originRect.height(), origin.getHeight());

        return isLeftDiffIgnorable && isRightDiffIgnorable && isTopDiffIgnorable && isBottomIgnorable;
    }

    private boolean isdiffIgnorable(int value1, int value2, int scale) {
        return Math.abs(value1 - value2) < getIgnorableRange(scale);
    }

    private float getIgnorableRange(int scale) {
        return scale * IGNORABLE_PRECISION_DIFFERENCE;
    }

    @Override
    public String getName() {
        return STRATEGY_NAME;
    }

    @Override
    public IEditAction createAction() {
        return new CropEditAction(this);
    }
}

package com.yongyongwang.multimedia.choose.crop.util;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/8/18
 */
public class ImageEditUtils {
    
    /**
     *
     * @param srcPixelMap
     * @param boundRect
     * @param contentRect
     * @return
     */
    public static float fitSrcImageToBounds(Bitmap srcPixelMap, Rect boundRect, Rect contentRect) {
        int imgWidth = srcPixelMap.getWidth();
        int imgHeight = srcPixelMap.getHeight();
        int boundWidth = boundRect.width();
        int boundHeight = boundRect.height();

        contentRect.set(boundRect);

        float imgRatio = (float) imgWidth / (float) imgHeight;
        float boundRatio = (float) boundWidth / (float) boundHeight;
        boolean scaleByWidth = imgRatio >= boundRatio;
        if (scaleByWidth) {
            return scaleContentRectByWidth(contentRect, imgWidth, imgHeight, boundWidth);
        } else {
            return scaleContentRectByHeight(contentRect, imgWidth, imgHeight, boundHeight);
        }
    }

    /**
     *
     * @param contentRect
     * @param imgWidth
     * @param imgHeight
     * @param boundHeight
     * @return
     */
    private static float scaleContentRectByHeight(Rect contentRect, int imgWidth, int imgHeight, int boundHeight) {
        int contentWidth = boundHeight * imgWidth / imgHeight;
        updateContentRect(contentRect, contentWidth, boundHeight);
        return (float) boundHeight / (float) imgHeight;
    }

    /**
     *
     * @param contentRect
     * @param imgWidth
     * @param imgHeight
     * @param boundWidth
     * @return
     */
    private static float scaleContentRectByWidth(Rect contentRect, int imgWidth, int imgHeight, int boundWidth) {
        int contentHeight = boundWidth * imgHeight / imgWidth;
        updateContentRect(contentRect, boundWidth, contentHeight);
        return (float) boundWidth / (float) imgWidth;
    }

    /**
     *
     * @param contentRect
     * @param width
     * @param height
     */
    private static void updateContentRect(Rect contentRect, int width, int height) {
        int centerX = contentRect.centerX();
        int centerY = contentRect.centerY();
        int halfW = width >> 1;
        int halfH = height >> 1;
        contentRect.set(centerX - halfW, centerY - halfH, centerX + halfW, centerY + halfH);
    }

    /**
     * Returns the input value x clamped to the range [min, max].
     *
     * @param value Value
     * @param min   min
     * @param max   max
     * @return clamped value
     */
    public static float clamp(float value, float min, float max) {
        if (value > max) {
            return max;
        }
        if (value < min) {
            return min;
        }
        return value;
    }
}

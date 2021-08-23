package com.yongyongwang.multimedia.choose.crop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.crop.util.ImageEditUtils;
import com.yongyongwang.multimedia.choose.util.ScreenUtils;

/**
 * Crop Picker
 */
public class CropPicker extends ImagePicker {

    private static final String TAG = "CropPicker";

    private static final int MOVE_LEFT = 1;
    private static final int MOVE_TOP = 2;
    private static final int MOVE_RIGHT = 4;
    private static final int MOVE_BOTTOM = 8;
    private static final int MOVE_BLOCK = 16;
    private static final int TOUCH_TOLERANCE = 20;
    private static final int RULER_LINE_COUNTS = 3;
    private static final int COLOR_SHADOW = 0x66000000;
    private static final float OUTLINE_WIDTH = 3f;
    private static final float HALF_DIVIDER = 2.0f;
    private static final float RECT_STROKE_WIDTH = 3.0f;
    private static final float MIN_SELECTION_LENGTH = 50f;
    private static final float CORNER_THICKNESS = 3f;
    private static final float BOARDER_THICKNESS = 3f;
    private static final float CORNER_LENGTH = 15f;
    private static final float STROKE_WIDTH = 1f;
    private static final float UNSPECIFIED = -1f;
    private static final float RATIO_RECT_EDGE = 1f;

    public static final float SIXTEEN_NINE = 16f / 9F;
    public static final float NINE_SIXTEEN = 9F / 16F;
    public static final float FOUR_ONE = 4F / 3F;
    public static final float SQUARE = 1F / 1F;

    private int touchTolerance;
    private float minSelectionLength;
    private float cornerThickness;
    private float borderThickness;
    private float cornerLength;
    private RectF highlightRect = new RectF(0f, 0f, RATIO_RECT_EDGE, RATIO_RECT_EDGE);
    private RectF baseHighlightRect = new RectF(0f, 0f, RATIO_RECT_EDGE, RATIO_RECT_EDGE);
    private RectF tempRect = new RectF();
    private int movingEdges = 0;
    private float referenceX;
    private float referenceY;
    private Paint gridPaint = new Paint();
    private float aspectRatio = UNSPECIFIED;

    public CropPicker(Context context) {
        super(context);
        initView();
    }

    public CropPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CropPicker(Context context, AttributeSet attrSet, int resId) {
        super(context, attrSet, resId);
        initView();
    }

    /**
     *
     */
    private void initView() {
        initConstants();
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rectF = getRatioTargetRect(baseLineRect, highlightRect,1f, 1f);
        if (rectF == null) {
            return;
        }
        tempRect.set(rectF);
        drawHighlightRectangle(canvas, tempRect);
        drawRuleOfThird(canvas, tempRect);
        drawAspectRatioArrow(canvas, tempRect);
        if (onPickActionListener != null) {
            onPickActionListener.onPickUpdate(highlightRect, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    /**
     *
     * @param baseLineRect
     * @param ratioRect
     * @param targetWidthRatio
     * @param targetHeightRatio
     * @return
     */
    private RectF getRatioTargetRect(RectF baseLineRect, RectF ratioRect, float targetWidthRatio, float targetHeightRatio) {
        if ((baseLineRect == null) || (ratioRect == null)) {
            return null;
        }
        RectF tempResultRectF = new RectF();
        float left = (baseLineRect.left + ratioRect.left * baseLineRect.width()) / targetWidthRatio;
        float top = (baseLineRect.top + ratioRect.top * baseLineRect.height()) / targetHeightRatio;
        float right = (baseLineRect.right - (1F - ratioRect.right) * baseLineRect.width()) / targetWidthRatio;
        float bottom = (baseLineRect.bottom - (1F - ratioRect.bottom) * baseLineRect.height()) / targetHeightRatio;
        tempResultRectF.set(left, top, right, bottom);
        return tempResultRectF;
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchEvent) {
        if (touchEvent == null) {
            return false;
        }
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                return dealWithActionDown(touchEvent);
            }
            case MotionEvent.ACTION_MOVE: {
                return dealWithActionMove(touchEvent);
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                return dealWithActionUpOrCancel();
            }
            default:
                return false;
        }
    }

    private void initConstants() {
        touchTolerance = ScreenUtils.dp2px(getContext(),TOUCH_TOLERANCE);
        minSelectionLength = ScreenUtils.dp2px(getContext(),MIN_SELECTION_LENGTH);
        borderThickness = ScreenUtils.dp2px(getContext(),BOARDER_THICKNESS);
        cornerThickness = ScreenUtils.dp2px(getContext(),CORNER_THICKNESS);
        cornerLength = ScreenUtils.dp2px(getContext(),CORNER_LENGTH);
    }

    /**
     *
     * @param ratio
     */
    public void setAspectRatio(float ratio) {
        if (baseLineRect == null) {
            Log.e(TAG, "setAspectRatio: Baseline not initialized yet");
            return;
        }

        aspectRatio = ratio;
        setHighlightRectangle(null);
    }

    /**
     * Obtains RatioRect
     *
     * @return RatioRect
     */
    public RectF getPickRatioRect() {
        return highlightRect;
    }

    /**
     * Update CropPicker BaseLine Rect.
     *
     * @param baseLineRect CropPicker BaseLine Rect
     */
    public void update(Rect baseLineRect) {
        if (baseLineRect.width() < minSelectionLength || baseLineRect.height() < minSelectionLength) {
            Log.e(TAG, "update: baseline size MUST > " + minSelectionLength);
        }
        if (this.baseLineRect == null) {
            this.baseLineRect = new RectF(baseLineRect);
        } else {
            this.baseLineRect.set(baseLineRect);
        }
        if (aspectRatio == UNSPECIFIED) {
            setHighlightRectangle(baseHighlightRect);
        } else {
            setHighlightRectangle(null);
        }
    }

    /**
     * Set Highlight rect
     *
     * @param destRatioRect HighlightRect
     */
    private void setHighlightRectangle(RectF destRatioRect) {
        if (destRatioRect == null) {
            float ratio = aspectRatio == UNSPECIFIED ? RATIO_RECT_EDGE : aspectRatio;
            RectF highlightArea = getHighlightRect(ratio);
            this.highlightRect.set(highlightArea);
        } else {
            highlightRect.set(destRatioRect);
        }
        invalidate();
    }

    /**
     * Reset Highlight Rect to baseHighlightRect
     */
    public void resetHighlightRectangle() {
        aspectRatio = UNSPECIFIED;
        setHighlightRectangle(baseHighlightRect);
    }

    /**
     *
     * @param aspectRatio
     * @return
     */
    private RectF getHighlightRect(float aspectRatio) {
        RectF highlightArea = new RectF();
        float baselineAspectRatio = baseLineRect.width() / baseLineRect.height();
        boolean scaleByWidth = aspectRatio >= baselineAspectRatio;
        if (scaleByWidth) {
            float width = baseLineRect.width();
            float height = width / aspectRatio;
            float left = 0f;
            float right = RATIO_RECT_EDGE;
            float top = (baseLineRect.height() - height) / HALF_DIVIDER / baseLineRect.height();
            float bottom = RATIO_RECT_EDGE - top;
            highlightArea.set(left, top, right, bottom);
        } else {
            float height = baseLineRect.height();
            float width = height * aspectRatio;
            float top = 0f;
            float bottom = RATIO_RECT_EDGE;
            float left = (baseLineRect.width() - width) / HALF_DIVIDER / baseLineRect.width();
            float right = RATIO_RECT_EDGE - left;
            highlightArea.set(left, top, right, bottom);
        }
        return highlightArea;
    }

    /**
     *
     * @param event
     * @param highlightRect
     */
    private void moveEdges(MotionEvent event, RectF highlightRect) {
        float dx = (event.getX() - referenceX) / baseLineRect.width();
        float dy = (event.getY() - referenceY) / baseLineRect.height();
        referenceX = event.getX();
        referenceY = event.getY();

        if ((movingEdges & MOVE_BLOCK) != 0) {
            dx = ImageEditUtils.clamp(dx, -highlightRect.left, 1 - highlightRect.right);
            dy = ImageEditUtils.clamp(dy, -highlightRect.top, 1 - highlightRect.bottom);

            highlightRect.left += dx;
            highlightRect.top += dy;
            highlightRect.right += dx;
            highlightRect.bottom += dy;

        } else {
            float ratioX = (referenceX - baseLineRect.left) / baseLineRect.width();
            float ratioY = (referenceY - baseLineRect.top) / baseLineRect.height();
            float left = ImageEditUtils.clamp(highlightRect.left + minSelectionLength /
                    baseLineRect.width(), 0.0f, 1.0f);
            float right = ImageEditUtils.clamp(highlightRect.right - minSelectionLength /
                    baseLineRect.width(), 0.0f, 1.0f);
            float top = ImageEditUtils.clamp(highlightRect.top + minSelectionLength /
                    baseLineRect.height(), 0.0f, 1.0f);
            float bottom = ImageEditUtils.clamp(highlightRect.bottom - minSelectionLength /
                    baseLineRect.height(), 0.0f, 1.0f);
            if ((movingEdges & MOVE_RIGHT) != 0) {
                highlightRect.right = ImageEditUtils.clamp(ratioX, left, 1f);
            }
            if ((movingEdges & MOVE_LEFT) != 0) {
                highlightRect.left = ImageEditUtils.clamp(ratioX, 0, right);
            }
            if ((movingEdges & MOVE_TOP) != 0) {
                highlightRect.top = ImageEditUtils.clamp(ratioY, 0, bottom);
            }
            if ((movingEdges & MOVE_BOTTOM) != 0) {
                highlightRect.bottom = ImageEditUtils.clamp(ratioY, top, 1f);
            }
            if (aspectRatio != UNSPECIFIED) {
                moveEdgesWithSpecifiedRatio(highlightRect, left, top, right, bottom, aspectRatio);
            }
        }
    }

    private void moveEdgesWithSpecifiedRatio(
            RectF highlightRect, float left, float top, float right, float bottom, float specRatio) {
        float maxThreshHold = 1f;
        float minThreshHold = 0f;
        float targetRatio = getTargetRatio(specRatio);
        boolean isGreaterThanTargetRatio = highlightRect.width() / highlightRect.height() > targetRatio;
        if (isGreaterThanTargetRatio) {
            float height = highlightRect.width() / targetRatio;
            if ((movingEdges & MOVE_BOTTOM) != 0) {
                highlightRect.bottom = ImageEditUtils.clamp(highlightRect.top + height, top, maxThreshHold);
            } else {
                highlightRect.top = ImageEditUtils.clamp(highlightRect.bottom - height, minThreshHold, bottom);
            }
        } else {
            float width = highlightRect.height() * targetRatio;
            if ((movingEdges & MOVE_LEFT) != 0) {
                highlightRect.left = ImageEditUtils.clamp(highlightRect.right - width, minThreshHold, right);
            } else {
                highlightRect.right = ImageEditUtils.clamp(highlightRect.left + width, left, maxThreshHold);
            }
        }
        if (isGreaterThanTargetRatio) {
            float width = highlightRect.height() * targetRatio;
            if ((movingEdges & MOVE_LEFT) != 0) {
                highlightRect.left = ImageEditUtils.clamp(highlightRect.right - width, minThreshHold, right);
            } else {
                highlightRect.right = ImageEditUtils.clamp(highlightRect.left + width, left, maxThreshHold);
            }
        } else {
            float height = highlightRect.width() / targetRatio;
            if ((movingEdges & MOVE_BOTTOM) != 0) {
                highlightRect.bottom = ImageEditUtils.clamp(highlightRect.top + height, top, maxThreshHold);
            } else {
                highlightRect.top = ImageEditUtils.clamp(highlightRect.bottom - height, minThreshHold, bottom);
            }
        }
    }

    private float getTargetRatio(float specRatio) {
        return specRatio * baseLineRect.height() / baseLineRect.width();
    }

    /**
     * Update HighlightRectangle moving states
     *
     * @param event TouchEvent
     */
    private void setMovingEdges(MotionEvent event) {
        RectF rectF = tempRect;
        rectF.set(getRatioTargetRect(baseLineRect, highlightRect,1f,1f));
        float posX = event.getX();
        float posY = event.getY();

        if (isInsideMovingBlock(rectF, posX, posY)) {
            movingEdges = MOVE_BLOCK;
        } else {
            movingEdges = handleTouchOnEdge(rectF, posX, posY);
        }
    }

    private int handleTouchOnEdge(RectF rectF, float posX, float posY) {
        int movingEdge = 0;
        boolean inVerticalRange = (rectF.top - touchTolerance) <= posY
                && posY <= (rectF.bottom + touchTolerance);
        boolean inHorizontalRange = (rectF.left - touchTolerance) <= posX
                && posX <= (rectF.right + touchTolerance);

        if (inVerticalRange) {
            boolean isLeft = Math.abs(posX - rectF.left) <= touchTolerance;
            boolean isRight = Math.abs(posX - rectF.right) <= touchTolerance;
            if (isLeft && isRight) {
                isLeft = Math.abs(posX - rectF.left) < Math.abs(posX - rectF.right);
                isRight = !isLeft;
            }
            if (isLeft) {
                movingEdge |= MOVE_LEFT;
            }
            if (isRight) {
                movingEdge |= MOVE_RIGHT;
            }
            if (aspectRatio != UNSPECIFIED && inHorizontalRange) {
                movingEdge |= (posY > (rectF.top + rectF.bottom) / HALF_DIVIDER) ? MOVE_BOTTOM : MOVE_TOP;
            }
        }
        if (inHorizontalRange) {
            boolean isTop = Math.abs(posY - rectF.top) <= touchTolerance;
            boolean isBottom = Math.abs(posY - rectF.bottom) <= touchTolerance;
            if (isTop && isBottom) {
                isTop = Math.abs(posY - rectF.top) < Math.abs(posY - rectF.bottom);
                isBottom = !isTop;
            }
            if (isTop) {
                movingEdge |= MOVE_TOP;
            }
            if (isBottom) {
                movingEdge |= MOVE_BOTTOM;
            }
            if (aspectRatio != UNSPECIFIED && inVerticalRange) {
                movingEdge |= (posX > (rectF.left + rectF.right) / HALF_DIVIDER) ? MOVE_RIGHT : MOVE_LEFT;
            }
        }
        return movingEdge;
    }

    private boolean isInsideMovingBlock(RectF rectF, float posX, float posY) {
        return posX > rectF.left + touchTolerance && posX < rectF.right - touchTolerance
                && posY > rectF.top + touchTolerance && posY < rectF.bottom - touchTolerance;
    }

    private boolean dealWithActionMove(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            return false;
        }
        moveEdges(event, highlightRect);
        invalidate();
        return true;
    }

    private boolean dealWithActionDown(MotionEvent event) {
        referenceX = event.getX();
        referenceY = event.getY();
        setMovingEdges(event);
        invalidate();
        return true;
    }

    private boolean dealWithActionUpOrCancel() {
        movingEdges = 0;
        invalidate();
        return true;
    }

    private void drawRuleOfThird(Canvas canvas, RectF hlightRectF) {
        float stepX = hlightRectF.width() / RULER_LINE_COUNTS;
        float stepY = hlightRectF.height() / RULER_LINE_COUNTS;
        float lineX = hlightRectF.left + stepX;
        float lineY = hlightRectF.top + stepY;
        for (int i = 0; i < RULER_LINE_COUNTS - 1; i++) {
            canvas.drawLine(lineX, hlightRectF.top, lineX, hlightRectF.bottom, gridPaint);
            lineX += stepX;
        }
        for (int j = 0; j < RULER_LINE_COUNTS - 1; j++) {
            canvas.drawLine(hlightRectF.left, lineY, hlightRectF.right, lineY, gridPaint);
            lineY += stepY;
        }
    }

    private void drawAspectRatioArrow(Canvas canvas, RectF hlightRectF) {
        drawCorners(canvas, hlightRectF);
    }

    private void drawCorners(Canvas canvas, RectF hlightRectF) {
        final Paint cornerPaint = new Paint();
        cornerPaint.setStyle(Paint.Style.STROKE);
        cornerPaint.setStrokeWidth(cornerThickness);
        cornerPaint.setColor(Color.WHITE);
        float centerY = (hlightRectF.top + hlightRectF.bottom) / HALF_DIVIDER;
        float centerX = (hlightRectF.left + hlightRectF.right) / HALF_DIVIDER;
        // Absolute value of the offset by which to draw the corner line
        // Such that its inner edge is flush with the border's inner edge.
        final float lateralOffset = (cornerThickness - borderThickness) / HALF_DIVIDER;
        // Absolute value of the offset by which to start the corner line
        // Such that the line is drawn all the way to form a corner edge with the adjacent side.
        final float cornerStartOffset = cornerThickness - (borderThickness / HALF_DIVIDER);

        // Top-left corner: left side
        canvas.drawLine(hlightRectF.left - lateralOffset,
                hlightRectF.top - cornerStartOffset, hlightRectF.left - lateralOffset,
                hlightRectF.top + cornerLength, cornerPaint);
        // Top-left corner: top side
        canvas.drawLine(hlightRectF.left - cornerStartOffset,
                hlightRectF.top - lateralOffset, hlightRectF.left + cornerLength,
                hlightRectF.top - lateralOffset, cornerPaint);

        // Top-right corner: right side
        canvas.drawLine(hlightRectF.right + lateralOffset,
                hlightRectF.top - cornerStartOffset, hlightRectF.right + lateralOffset,
                hlightRectF.top + cornerLength, cornerPaint);
        // Top-right corner: top side
        canvas.drawLine(hlightRectF.right + cornerStartOffset,
                hlightRectF.top - lateralOffset, hlightRectF.right - cornerLength,
                hlightRectF.top - lateralOffset, cornerPaint);

        // Bottom-left corner: left side
        canvas.drawLine(hlightRectF.left - lateralOffset,
                hlightRectF.bottom + cornerStartOffset, hlightRectF.left - lateralOffset,
                hlightRectF.bottom - cornerLength, cornerPaint);
        // Bottom-left corner: bottom side
        canvas.drawLine(hlightRectF.left - cornerStartOffset,
                hlightRectF.bottom + lateralOffset, hlightRectF.left + cornerLength,
                hlightRectF.bottom + lateralOffset, cornerPaint);

        // Bottom-right corner: right side
        canvas.drawLine(hlightRectF.right + lateralOffset,
                hlightRectF.bottom + cornerStartOffset, hlightRectF.right + lateralOffset,
                hlightRectF.bottom - cornerLength, cornerPaint);
        // Bottom-right corner: bottom side
        canvas.drawLine(hlightRectF.right + cornerStartOffset,
                hlightRectF.bottom + lateralOffset, hlightRectF.right - cornerLength,
                hlightRectF.bottom + lateralOffset, cornerPaint);

        canvas.drawLine(hlightRectF.left - lateralOffset,
                centerY - (cornerLength / HALF_DIVIDER), hlightRectF.left - lateralOffset,
                centerY + (cornerLength / HALF_DIVIDER), cornerPaint);

        canvas.drawLine(hlightRectF.right + lateralOffset,
                centerY - (cornerLength / HALF_DIVIDER), hlightRectF.right + lateralOffset,
                centerY + (cornerLength / HALF_DIVIDER), cornerPaint);

        canvas.drawLine(centerX - (cornerLength / HALF_DIVIDER),
                hlightRectF.top - lateralOffset, centerX + (cornerLength / HALF_DIVIDER),
                hlightRectF.top - lateralOffset, cornerPaint);
        canvas.drawLine(centerX - (cornerLength / HALF_DIVIDER),hlightRectF.bottom + lateralOffset,
                centerX + (cornerLength / HALF_DIVIDER),hlightRectF.bottom + lateralOffset, cornerPaint);
    }


    private void drawHighlightRectangle(Canvas canvas, RectF hlightRectF) {
        canvas.save();
        Paint tmpPaint = new Paint();
        RectF tmpRectF = new RectF();

        tmpPaint.setStrokeWidth(RECT_STROKE_WIDTH);
        tmpPaint.setColor(Color.TRANSPARENT);
        tmpPaint.setStyle(Paint.Style.FILL);
        tmpRectF.set(hlightRectF.left, hlightRectF.top, hlightRectF.right, hlightRectF.bottom);
        canvas.drawRect(tmpRectF, tmpPaint);

        // Shadow Area
        canvas.clipRect(tmpRectF, Region.Op.DIFFERENCE);
        tmpPaint.setColor(COLOR_SHADOW);
        tmpRectF.set(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
        canvas.drawRect(tmpRectF, tmpPaint);

        // Highlight Area
        canvas.clipRect(tmpRectF, Region.Op.DIFFERENCE);
        tmpPaint.setColor(Color.TRANSPARENT);
        tmpRectF.set(0, 0, getWidth(), getHeight());
        canvas.drawRect(tmpRectF, tmpPaint);

        // Edge
        tmpPaint.setColor(gridPaint.getColor());
        tmpPaint.setStyle(Paint.Style.STROKE);
        tmpPaint.setStrokeWidth(OUTLINE_WIDTH);
        canvas.drawRect(tmpRectF, tmpPaint);

        canvas.restore();
    }
}

package com.yongyongwang.multimedia.choose.crop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.crop.exceptions.CropPickerException;
import com.yongyongwang.multimedia.choose.crop.handler.ImageEditorProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Image Edit View
 */
public class ImageEditView extends FrameLayout {

    private final List<UpdateInterceptor> interceptors = new ArrayList<>();
    private EditorAdapter adapter;
    private BackGroundLayer backgroundLayer;
    private ImagePicker imagePicker;

    public ImageEditView(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public ImageEditView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ImageEditView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context ctx, AttributeSet attrs) {
        if (adapter == null) {
            return;
        }
        backgroundLayer = adapter.getBackgroundView(ctx, attrs);
        imagePicker = adapter.getImagePicker(ctx, attrs);
        backgroundLayer.setOnUpdateListener((baselineRect, scaleFactor) -> {
            try {
                imagePicker.update(baselineRect);
            } catch (CropPickerException e) {
                e.printStackTrace();
            }
        });
        addView(backgroundLayer);
        addView(imagePicker);
    }

    /**
     * Obtains Edit Rect
     *
     * @return Edit Rect
     */
    public Rect getEditRect() {
        RectF cropRatioRect = imagePicker.getPickRatioRect();
        Bitmap srcBmp = backgroundLayer.getPreviewImage();
        float width = srcBmp.getWidth();
        float height = srcBmp.getHeight();
        return new Rect(Math.round(cropRatioRect.left * width),
                Math.round(cropRatioRect.top * height),
                Math.round(cropRatioRect.right * width),
                Math.round(cropRatioRect.bottom * height));
    }

    public void setAdapter(EditorAdapter adapter) {
        this.adapter = adapter;
        initView(getContext(), null);
        setEditProxy(this.adapter.getImageEditorProxy());
        this.adapter.getImageEditorProxy().export();
    }

    private void setEditProxy(ImageEditorProxy editorProxy) {
        editorProxy.addObserver((proxy, data) -> {
            for (UpdateInterceptor interceptor : interceptors) {
                if (!interceptor.preUpdate(data)) {
                    return;
                }
            }
            handleStateUpdate(data);
            for (UpdateInterceptor interceptor : interceptors) {
                interceptor.postUpdate(data);
            }
        });
    }

    private void handleStateUpdate(Object data) {
        if (data instanceof ImageEditorProxy.EditorState) {
            final ImageEditorProxy.EditorState editorState = (ImageEditorProxy.EditorState) data;
            handleEditorStateUpdate(editorState);
        }
    }

    private void handleEditorStateUpdate(final ImageEditorProxy.EditorState editorState) {
        Bitmap srcBmp = editorState.previewPixelMap;
        Bitmap current = backgroundLayer.getPreviewImage();
        if (current == null) {
            return;
        }
        backgroundLayer.setPreviewImage(srcBmp);
    }

    public BackGroundLayer getBackgroundLayer() {
        return backgroundLayer;
    }

    public ImagePicker getImagePicker() {
        return imagePicker;
    }

    /**
     * Update Interceptor
     */
    public interface UpdateInterceptor {
        default boolean preUpdate(Object data) {
            // Do Something after update
            return false;
        }

        default void postUpdate(Object data) {
            // Do Something before update
        }
    }

    /**
     * Editor Adapter
     */
    public abstract static class EditorAdapter {
        /**
         * Obtains Image Editor Proxy
         *
         * @return Image Editor Proxy
         */
        protected abstract ImageEditorProxy getImageEditorProxy();

        /**
         * Obtains Background Layer
         *
         * @param ctx   context
         * @param attrs AttributeSet
         * @return Background Layer Instance
         */
        protected BackGroundLayer getBackgroundView(Context ctx, AttributeSet attrs) {
            return new BackGroundLayer(ctx, attrs);
        }

        /**
         * Obtains ImagePicker
         *
         * @param ctx   context
         * @param attrs AttributeSet
         * @return ImagePicker
         */
        protected ImagePicker getImagePicker(Context ctx, AttributeSet attrs) {
            return new CropPicker(ctx, attrs);
        }
    }
}

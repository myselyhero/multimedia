package com.yongyongwang.multimedia.choose.crop.handler;

import android.graphics.Bitmap;
import android.util.Log;

import com.yongyongwang.multimedia.choose.crop.exceptions.EditActionException;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditAction;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditStrategy;
import com.yongyongwang.multimedia.choose.crop.strategy.IImageEditor;
import com.yongyongwang.multimedia.choose.crop.util.ImageLoader;

import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * ImageEditor implementation
 */
public class ImageEditorImp implements IImageEditor {

    private String originImgPath;
    private Bitmap originPixelMap;
    private EditStrategyManager editStrategyManager = new EditStrategyManager();

    private LinkedList<IEditAction> actionStack = new LinkedList<>();
    private int lastAction = 0;

    private volatile EditActionFactory editActionFactory;

    private Bitmap previewBmp;
    private Map<IEditAction, Bitmap> bmpCache = new WeakHashMap<>();

    /**
     * ImageEditor Constructor
     *
     * @param imgPath Image file path
     */
    public ImageEditorImp(String imgPath) {
        try {
            setImageSource(imgPath);
            getEditActionFactory();
        } catch (EditActionException ignored) {
            Log.e(ImageEditorImp.class.getSimpleName(), "ImageEditorImp: SET IMAGE SOURCE EXCEPTION");
        }
    }

    @Override
    public void setImageSource(String imgPath) throws EditActionException {
        if (previewBmp != null) {
            throw new EditActionException("ImageSource already set");
        }
        originImgPath = imgPath;
        reset();
    }

    @Override
    public void addEditStrategy(IEditStrategy strategy) throws EditActionException {
        editStrategyManager.addStrategy(strategy);
    }

    private EditActionFactory getEditActionFactory() {
        if (editActionFactory == null) {
            synchronized (ImageEditorImp.class) {
                editActionFactory = new EditActionFactory(editStrategyManager);
            }
        }
        return editActionFactory;
    }

    @Override
    public void applyAction(IEditAction act) throws EditActionException {
        if (isRedoable()) {
            cleanRedoActions();
        }
        processAction(act);
    }

    private void processAction(IEditAction editAction)
            throws EditActionException {
        Bitmap srcBmp = previewBmp;
        Bitmap outBmp = editAction.execute(srcBmp);
        pushToStack(editAction);
        bmpCache.put(editAction, outBmp);
        previewBmp = outBmp;
    }

    private void cleanRedoActions() {
        final int size = actionStack.size();
        for (int i = lastAction + 1; i <= size; i++) {
            actionStack.removeLast();
        }
    }

    private void pushToStack(IEditAction act) {
        actionStack.add(act);
        lastAction++;
    }

    /**
     * Check if editor can process redo action
     *
     * @return True if redoable, otherwise False
     */
    public boolean isRedoable() {
        return lastAction < actionStack.size();
    }

    /**
     * Check if editor can process undo action
     *
     * @return True if undoable, otherwise False
     */
    public boolean isUndoable() {
        return lastAction > 0;
    }

    @Override
    public void redo() {
        if (!isRedoable()) {
            return;
        }

        IEditAction act = actionStack.get(lastAction++);
        previewBmp = bmpCache.get(act);
    }

    @Override
    public void undo() {
        if (!isUndoable()) {
            return;
        }
        if (--lastAction > 0) {
            IEditAction action = actionStack.get(lastAction - 1);
            previewBmp = bmpCache.get(action);
        } else {
            previewBmp = getOriginPixelMap();
        }
    }

    private Bitmap getOriginPixelMap() {
        if (originPixelMap == null) {
            originPixelMap = ImageLoader.getPixelMap(originImgPath);
        }
        return originPixelMap;
    }

    @Override
    public Bitmap export() {
        return previewBmp;
    }

    @Override
    public void drop() {
        reset();
    }

    /**
     * reset previewBmp
     *
     * @param pixelMap new previewBmp
     */
    public void reSetPreviewBmp(Bitmap pixelMap) {
        this.previewBmp = pixelMap;
    }

    private void reset() {
        previewBmp = getOriginPixelMap();

        lastAction = 0;
        actionStack.clear();

        bmpCache.clear();
    }
}

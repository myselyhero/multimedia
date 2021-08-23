package com.yongyongwang.multimedia.choose.crop.strategy;

import android.graphics.Bitmap;

import com.yongyongwang.multimedia.choose.crop.exceptions.EditActionException;

/**
 * Image Editor Interface
 */
public interface IImageEditor {
    /**
     * Set Original PixelMap
     *
     * @param imgPath File path, example: <code>/sdcard/path/to/image.png</code>
     * @throws EditActionException Image load failed exception
     */
    void setImageSource(String imgPath) throws EditActionException;

    /**
     * Add edit strategy to manager
     *
     * @param strategy Image edit strategy
     * @throws EditActionException Add strategy exceptions
     */
    void addEditStrategy(IEditStrategy strategy) throws EditActionException;

    /**
     * Apply ImageEdit Action
     *
     * @param act ImageEdit Action
     * @throws EditActionException Add strategy exceptions
     */
    void applyAction(IEditAction act) throws EditActionException;

    /**
     * Redo ImageEdit Action
     */
    void redo();

    /**
     * Undo ImageEdit Action
     */
    void undo();

    /**
     * Check whether can perform redo action according to ImageEdit action history
     *
     * @return Return True if is redoable
     */
    boolean isRedoable();

    /**
     * Check whether can perform undo action according to ImageEdit action history
     *
     * @return Return True if is undoable
     */
    boolean isUndoable();

    /**
     * Out put preview image
     *
     * @return Preview PixelMap
     */
    Bitmap export();

    /**
     * Drop ImageEdit results, reset image
     */
    void drop();
}

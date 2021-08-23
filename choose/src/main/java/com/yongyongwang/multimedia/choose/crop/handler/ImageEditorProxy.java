package com.yongyongwang.multimedia.choose.crop.handler;

import android.graphics.Bitmap;

import com.yongyongwang.multimedia.choose.crop.exceptions.EditActionException;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditAction;
import com.yongyongwang.multimedia.choose.crop.strategy.IImageEditor;
import com.yongyongwang.multimedia.choose.crop.strategy.action.StateAction;

import java.util.Observable;
import java.util.WeakHashMap;

/**
 * Image editor proxy
 * <p>
 * The image editing agent provides a background asynchronous execution mechanism for image editing commands.
 * A message queue is used to ensure that the image editor executes the image
 * editing commands sent by the main thread in sequence to avoid concurrency.
 */
public class ImageEditorProxy extends Observable implements EditActionPipeline.EditPipelineListener {
    private EditActionPipeline actionPipeline;

    private WeakHashMap<IEditAction, OnActionListener> actionListeners = new WeakHashMap<>();

    private volatile boolean isRedoable = false;
    private volatile boolean isUndoable = false;

    public ImageEditorProxy(IImageEditor imageEditor) {
        super();
        actionPipeline = new EditActionPipeline(imageEditor);
        actionPipeline.setOnEditActionListener(this);
    }

    /**
     * Apply Edit Action
     *
     * @param act Image Edit Action
     */
    public void applyAction(IEditAction act) {
        sendAction(act);
    }

    /**
     * Redo Edit Action
     */
    public void redo() {
        sendAction(new StateAction(IEditAction.ActionType.ACT_REDO));
    }

    /**
     * Undo Edit Action
     */
    public void undo() {
        sendAction(new StateAction(IEditAction.ActionType.ACT_UNDO));
    }

    /**
     * Check If Edit Action Redoable
     *
     * @return True If Redoable
     */
    public boolean isRedoable() {
        return isRedoable;
    }

    /**
     * Check If Edit Action Undoable
     *
     * @return True If Undoable
     */
    public boolean isUndoable() {
        return isUndoable;
    }

    /**
     * Export Preview PixelMap
     */
    public void export() {
        sendAction(new StateAction(IEditAction.ActionType.ACT_EXPORT));
    }

    private void sendAction(IEditAction editAction) {
        if (actionPipeline == null) {
            return;
        }
        actionPipeline.sendMessage(actionPipeline.obtainMessage(editAction.getActionType().getValue(), editAction));
    }

    @Override
    public void onStateUpdate(Bitmap preview, boolean isUndoable, boolean isRedoable, IEditAction action) {
        this.isRedoable = isRedoable;
        this.isUndoable = isUndoable;

        EditorState editorState = new EditorState();
        editorState.isRedoable = isRedoable;
        editorState.isUndoable = isUndoable;
        editorState.previewPixelMap = preview;
        editorState.action = action;

        setChanged();
        notifyObservers(editorState);

        if (actionListeners.containsKey(action)) {
            actionListeners.get(action).onActionResult(preview);
        }
    }

    @Override
    public void onEditActionDone(IEditAction act, EditActionException error) {
        setChanged();
        if (error == null) {
            notifyObservers(act);
        } else {
            notifyObservers(error);
        }
    }

    /**
     * Action Listener
     */
    public interface OnActionListener {
        void onActionResult(Object result);
    }

    /**
     * Editor State For Updating Observers' State
     */
    public static class EditorState {
        /**
         * Is Redoable
         */
        public boolean isRedoable;

        /**
         * Is Undoable
         */
        public boolean isUndoable;

        /**
         * Preview PixelMap
         */
        public Bitmap previewPixelMap;

        /**
         * Edit Action
         */
        public IEditAction action;
    }

}

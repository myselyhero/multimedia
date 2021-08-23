package com.yongyongwang.multimedia.choose.crop.handler;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.yongyongwang.multimedia.choose.crop.exceptions.EditActionException;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditAction;
import com.yongyongwang.multimedia.choose.crop.strategy.IImageEditor;

/**
 * Image Edit Pipeline
 * ImageEditorProxy use this pipeline to arrange edit actions
 */
public final class EditActionPipeline extends Handler {

    private IImageEditor imageEditor;
    private EditPipelineListener editPipelineListener;
    /**
     * Pipeline Constructor
     *
     * @param imageEditor ImageEditor instance
     */
    public EditActionPipeline(IImageEditor imageEditor) {
        this.imageEditor = imageEditor;
    }

    /**
     * Bind Action Listener
     *
     * @param listener Pipeline action listener
     */
    protected void setOnEditActionListener(EditPipelineListener listener) {
        editPipelineListener = listener;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if (!(msg.obj instanceof IEditAction)) {
            return;
        }
        IEditAction act = (IEditAction) msg.obj;
        try {
            if (act.getActionType() == IEditAction.ActionType.ACT_EDIT) {
                imageEditor.applyAction(act);
                notifyEditResult(act, null);
            } else {
                act.setParams(imageEditor);
                Bitmap output = act.execute(null);
                notifyStateUpdate(act, output);
            }
        } catch (EditActionException e) {
            notifyEditResult(act, e);
        }
    }

    private void notifyEditResult(IEditAction action, EditActionException error) {
        if (editPipelineListener == null) {
            return;
        }

        final boolean isUndoable = imageEditor.isUndoable();
        final boolean isRedoable = imageEditor.isRedoable();

        if (error != null) {
            editPipelineListener.onEditActionDone(action, error);
        }
        editPipelineListener.onStateUpdate(imageEditor.export(), isUndoable, isRedoable, action);
    }

    private void notifyStateUpdate(IEditAction action, Bitmap preview) {
        if (editPipelineListener == null) {
            return;
        }
        editPipelineListener.onStateUpdate(preview, imageEditor.isUndoable(), imageEditor.isRedoable(), action);
    }

    interface EditPipelineListener {
        /**
         * Image edit status update
         *
         * @param preview    Image edit preview
         * @param isUndoable Image edit step undoable
         * @param isRedoable Image edit step redoable
         * @param act        Image edit action
         */
        void onStateUpdate(Bitmap preview, boolean isUndoable, boolean isRedoable, IEditAction act);

        /**
         * Image edit completed.
         *
         * @param act Image edit action
         * @param e   Image edit action exception
         */
        void onEditActionDone(IEditAction act, EditActionException e);
    }
}

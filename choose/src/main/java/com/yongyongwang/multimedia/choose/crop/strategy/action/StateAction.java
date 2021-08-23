package com.yongyongwang.multimedia.choose.crop.strategy.action;

import android.graphics.Bitmap;

import com.yongyongwang.multimedia.choose.crop.exceptions.EditActionException;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditAction;
import com.yongyongwang.multimedia.choose.crop.strategy.IImageEditor;

/**
 * State edit action
 */
public class StateAction implements IEditAction {
    private ActionType type;
    private IImageEditor imageEditor;

    /**
     * StateAction Constructor
     *
     * @param type EditAction Type
     */
    public StateAction(ActionType type) {
        this.type = type;
    }

    @Override
    public Bitmap execute(Bitmap pixelMap) throws EditActionException {
        if (imageEditor == null) {
            throw new EditActionException("imageEditor not set via setParams yet!");
        }

        if (type == ActionType.ACT_UNDO) {
            imageEditor.undo();
        } else if (type == ActionType.ACT_REDO) {
            imageEditor.redo();
        } else if (type == ActionType.ACT_DROP) {
            imageEditor.drop();
        } else if (type == ActionType.ACT_EXPORT) {
            return imageEditor.export();
        } else {
            throw new EditActionException("unknown EditAction Type");
        }
        return imageEditor.export();
    }

    @Override
    public IEditAction.ActionType getActionType() {
        return this.type;
    }

    @Override
    public <T> void setParams(T params) {
        if (params instanceof IImageEditor) {
            this.imageEditor = (IImageEditor) params;
        }
    }

}

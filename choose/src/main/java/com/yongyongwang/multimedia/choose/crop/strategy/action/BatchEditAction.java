package com.yongyongwang.multimedia.choose.crop.strategy.action;

import android.graphics.Bitmap;

import com.yongyongwang.multimedia.choose.crop.exceptions.EditActionException;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditAction;

import java.util.LinkedList;
import java.util.List;

/**
 * Image edit batch action
 * <p>
 * Provides serial batch processing streams for image editing. Different image editing steps can be combined
 * into one image editing operation in series, The output of each step is the input of the next step.
 */
public class BatchEditAction implements IEditAction {
    private final List<IEditAction> actionPipeline = new LinkedList<>();

    @Override
    public Bitmap execute(Bitmap pixelMap) throws EditActionException {
        Bitmap input = pixelMap;
        Bitmap output = null;
        for (IEditAction action : actionPipeline) {
            output = action.execute(input);
            input = output;
        }
        return output;
    }

    @Override
    public ActionType getActionType() {
        return ActionType.ACT_EDIT;
    }

    @Override
    public <T> void setParams(T params) {
    }

    /**
     * Add Action to the Queue
     *
     * @param action Edit Action
     */
    public void appendEditAction(IEditAction action) {
        actionPipeline.add(action);
    }
}

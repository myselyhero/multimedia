package com.yongyongwang.multimedia.choose.crop.strategy.action;

import android.graphics.Bitmap;

import com.yongyongwang.multimedia.choose.crop.exceptions.EditActionException;
import com.yongyongwang.multimedia.choose.crop.exceptions.HandleStrategyException;
import com.yongyongwang.multimedia.choose.crop.strategy.EditParams;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditAction;
import com.yongyongwang.multimedia.choose.crop.strategy.imp.BrightnessStrategy;

/**
 * Brightness edit action
 */
public class BrightnessAction implements IEditAction {

    private EditParams<?> editParams;
    private BrightnessStrategy strategy;

    /**
     * BrightnessAction Constructor
     *
     * @param strategy BrightnessStrategy Instance
     */
    public BrightnessAction(BrightnessStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Bitmap execute(Bitmap pixelMap) throws EditActionException {
        try {
            return strategy.handle(pixelMap, editParams);
        } catch (HandleStrategyException e) {
            throw new EditActionException(e.getMessage());
        }
    }

    @Override
    public ActionType getActionType() {
        return IEditAction.ActionType.ACT_EDIT;
    }

    @Override
    public <T> void setParams(T params) {
        editParams = new EditParams<>(params);
    }
}

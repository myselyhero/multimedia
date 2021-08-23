package com.yongyongwang.multimedia.choose.crop.handler;

import com.yongyongwang.multimedia.choose.crop.exceptions.EditActionException;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditAction;

import java.util.Set;

/**
 * Image edit action factory
 */
public class EditActionFactory {
    private static EditStrategyManager editStrgyManager;

    protected EditActionFactory(EditStrategyManager editStrgyManager) {
        EditActionFactory.editStrgyManager = editStrgyManager;
    }

    /**
     * Obtains image edit action by strategy name
     *
     * @param strgyName Image edit strategy name
     * @return Image edit action instance
     * @throws EditActionException Create action exception
     */
    public static IEditAction createAction(String strgyName) throws EditActionException {
        if (editStrgyManager == null) {
            throw new EditActionException("EditStrategyManager not init yet");
        }

        if (!editStrgyManager.has(strgyName)) {
            throw new EditActionException("unknown action type: " + strgyName);
        }
        return editStrgyManager.get(strgyName).createAction();
    }
}

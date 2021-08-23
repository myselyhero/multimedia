package com.yongyongwang.multimedia.choose.crop.strategy.entity;

import com.yongyongwang.multimedia.choose.crop.view.SlideStepBar;

/**
 * Adjust type
 */
public enum AdjustType {

    BRIGHTNESS(SlideStepBar.Mode.ADJUST_MODE, "Brightness"),
    CONTRAST(SlideStepBar.Mode.ADJUST_MODE, "Contrast"),
    SATURATION(SlideStepBar.Mode.ADJUST_MODE, "Saturation");

    private SlideStepBar.Mode mode;
    private String name;

    AdjustType(SlideStepBar.Mode mode, String name) {
        this.mode = mode;
        this.name = name;
    }

    /**
     * Get Adjust Mode
     *
     * @return Adjust Mode
     */
    public SlideStepBar.Mode getMode() {
        return mode;
    }

    /**
     * Get Adjust Mode Name
     *
     * @return Adjust Mode Name
     */
    public String getName() {
        return name;
    }

    /**
     * Set Adjust Mode Name
     *
     * @param name Adjust Mode Name
     */
    public void setName(String name) {
        this.name = name;
    }
}

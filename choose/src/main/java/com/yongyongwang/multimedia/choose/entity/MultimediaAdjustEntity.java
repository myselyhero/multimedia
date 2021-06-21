package com.yongyongwang.multimedia.choose.entity;

import java.io.Serializable;

/**
 *
 */
public class MultimediaAdjustEntity implements Serializable {

    private float value = 3f;
    private String name;
    private int icon;
    private boolean choose;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public MultimediaAdjustEntity(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public MultimediaAdjustEntity(String name, int icon, boolean choose) {
        this.name = name;
        this.icon = icon;
        this.choose = choose;
    }

    @Override
    public String toString() {
        return "MultimediaAdjustEntity{" +
                "value=" + value +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", choose=" + choose +
                '}';
    }
}

package com.yongyongwang.multimedia.choose.entity;

import java.io.Serializable;

/**
 *
 */
public class MultimediaBeautifyEntity implements Serializable {

    private int icon;
    private int value;
    private String name;
    private boolean choose;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public MultimediaBeautifyEntity(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public MultimediaBeautifyEntity(int icon, String name, boolean choose) {
        this.icon = icon;
        this.name = name;
        this.choose = choose;
    }

    @Override
    public String toString() {
        return "MultimediaBeautifyEntity{" +
                "icon=" + icon +
                ", value=" + value +
                ", name='" + name + '\'' +
                ", choose=" + choose +
                '}';
    }
}

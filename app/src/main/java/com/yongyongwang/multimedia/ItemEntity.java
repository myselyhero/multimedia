package com.yongyongwang.multimedia;

import java.io.Serializable;

/**
 *
 */
public class ItemEntity implements Serializable {

    private String name;
    private int data;
    private boolean choose;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public ItemEntity(String name) {
        this.name = name;
    }

    public ItemEntity(String name, boolean choose) {
        this.name = name;
        this.choose = choose;
    }

    public ItemEntity(String name, int data) {
        this.name = name;
        this.data = data;
    }

    public ItemEntity(String name, int data, boolean choose) {
        this.name = name;
        this.data = data;
        this.choose = choose;
    }

    @Override
    public String toString() {
        return "ItemEntity{" +
                "name='" + name + '\'' +
                ", choose=" + choose +
                '}';
    }
}

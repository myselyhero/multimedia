package com.yongyongwang.multimedia.choose.entity;

import com.yongyongwang.multimedia.choose.crop.strategy.entity.AdjustType;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/8/16
 */
public class MultimediaCropItemEntity {

    private AdjustType type;
    private String name;
    private int normalRes;
    private int pressRes;
    private int progress;
    private boolean choose;

    public AdjustType getType() {
        return type;
    }

    public void setType(AdjustType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNormalRes() {
        return normalRes;
    }

    public void setNormalRes(int normalRes) {
        this.normalRes = normalRes;
    }

    public int getPressRes() {
        return pressRes;
    }

    public void setPressRes(int pressRes) {
        this.pressRes = pressRes;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    @Override
    public String toString() {
        return "MultimediaCropItemEntity{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", normalRes=" + normalRes +
                ", pressRes=" + pressRes +
                ", progress=" + progress +
                ", choose=" + choose +
                '}';
    }
}

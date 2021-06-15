package com.yongyongwang.multimedia.choose.entity;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/7
 */
public class MultimediaPreviewEntity {

    private String path;
    private boolean choose;
    private int position = -1;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "MultimediaPreviewEntity{" +
                "path='" + path + '\'' +
                ", choose=" + choose +
                ", position=" + position +
                '}';
    }
}

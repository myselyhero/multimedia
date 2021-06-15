package com.yongyongwang.multimedia.choose.entity;

import java.io.Serializable;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/5
 */
public class MultimediaEntity implements Serializable {

    /**
     * 原图地址
     */
    private String path;

    /**
     * 剪切地址
     */
    private String cropPath;

    /**
     * 压缩地址
     */
    private String compressPath;

    /**
     * 时长
     */
    private long duration;

    /**
     * 是否选择
     */
    private boolean choose;

    /**
     * 媒体类型
     */
    private String mimeType;

    /**
     * 宽高
     */
    private int width;
    private int height;

    /**
     *
     */
    private String name;

    /**
     * 文件大小
     */
    private long size;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCropPath() {
        return cropPath;
    }

    public void setCropPath(String cropPath) {
        this.cropPath = cropPath;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "MultimediaEntity{" +
                "path='" + path + '\'' +
                ", cropPath='" + cropPath + '\'' +
                ", compressPath='" + compressPath + '\'' +
                ", duration=" + duration +
                ", choose=" + choose +
                ", mimeType='" + mimeType + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}

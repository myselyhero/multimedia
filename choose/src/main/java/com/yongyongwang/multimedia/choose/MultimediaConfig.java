package com.yongyongwang.multimedia.choose;

import com.yongyongwang.multimedia.choose.model.MultimediaResultListener;

import java.io.Serializable;

/**
 * @author myselyhero 
 * 
 * @desc
 * 
 * @// TODO: 2021/6/5
 */
public class MultimediaConfig implements Serializable {

    /**
     *
     */
    private static MultimediaConfig instance;


    /**
     *
     * @return
     */
    public static MultimediaConfig getInstance() {
        if (instance == null){
            instance = new MultimediaConfig();
        }
        return instance;
    }

    /**
     * 选择类型
     */
    private MultimediaEnum multimediaType = MultimediaEnum.All;

    /**
     * 最大选择数量
     */
    private int maxNum = 9;

    /**
     *图片的最少选择数量
     */
    private int minNum = -1;

    /**
     * 单选（设置此参数时最大数量不可用）
     */
    private boolean only;

    /**
     * 单选模式下预览页面是否需要已选列表展示
     *
     *{@link com.yongyongwang.multimedia.choose.view.MultimediaPreviewRecyclerview}
     *
     */
    private boolean onlyPreview = true;

    /**
     * 选中后的阴影
     */
    private boolean shade = true;

    /**
     * 图片的最大（超过后过滤）
     */
    private int maxSize;

    /**
     * 剪切、只在单选时可用、暂不考虑多图剪切
     */
    private boolean crop;

    /**
     * 压缩
     */
    private boolean compress;

    /**
     * 是否可以图片、视频一起选择
     */
    private boolean mixture;

    /**
     * 是否可拍照
     */
    private boolean camera;

    /**
     * 是否显示gif
     */
    private boolean gif;

    /**
     * 最小时长
     */
    private long minDuration = 3000;

    /**
     * 最大时长
     */
    private long maxDuration = 15000;

    /**
     * 图片列表列数
     */
    private int spanCount = -1;

    /**
     * 所有产生的文件保存地址
     */
    private String dir;

    /**
     * 确认按钮
     */
    private int confirmDrawable;
    private int confirmTextColor;
    private String confirmText;

    /**
     *
     */
    public static MultimediaResultListener resultListener;
    public static MultimediaResultListener cameraListener;

    public static void setInstance(MultimediaConfig instance) {
        MultimediaConfig.instance = instance;
    }

    public MultimediaEnum getMultimediaType() {
        return multimediaType;
    }

    public void setMultimediaType(MultimediaEnum multimediaType) {
        this.multimediaType = multimediaType;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public boolean isOnly() {
        return only;
    }

    public void setOnly(boolean only) {
        this.only = only;
    }

    public boolean isOnlyPreview() {
        return onlyPreview;
    }

    public void setOnlyPreview(boolean onlyPreview) {
        this.onlyPreview = onlyPreview;
    }

    public boolean isShade() {
        return shade;
    }

    public void setShade(boolean shade) {
        this.shade = shade;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isCrop() {
        return crop;
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public boolean isCompress() {
        return compress;
    }

    public void setCompress(boolean compress) {
        this.compress = compress;
    }

    public boolean isMixture() {
        return mixture;
    }

    public void setMixture(boolean mixture) {
        this.mixture = mixture;
    }

    public boolean isCamera() {
        return camera;
    }

    public void setCamera(boolean camera) {
        this.camera = camera;
    }

    public boolean isGif() {
        return gif;
    }

    public void setGif(boolean gif) {
        this.gif = gif;
    }

    public long getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(long minDuration) {
        this.minDuration = minDuration;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getConfirmDrawable() {
        return confirmDrawable;
    }

    public void setConfirmDrawable(int confirmDrawable) {
        this.confirmDrawable = confirmDrawable;
    }

    public int getConfirmTextColor() {
        return confirmTextColor;
    }

    public void setConfirmTextColor(int confirmTextColor) {
        this.confirmTextColor = confirmTextColor;
    }

    public String getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
    }

    @Override
    public String toString() {
        return "MultimediaConfig{" +
                "multimediaType=" + multimediaType +
                ", maxNum=" + maxNum +
                ", minNum=" + minNum +
                ", only=" + only +
                ", onlyPreview=" + onlyPreview +
                ", shade=" + shade +
                ", crop=" + crop +
                ", compress=" + compress +
                ", mixture=" + mixture +
                ", camera=" + camera +
                ", gif=" + gif +
                ", minDuration=" + minDuration +
                ", maxDuration=" + maxDuration +
                ", spanCount=" + spanCount +
                ", dir='" + dir + '\'' +
                ", confirmDrawable=" + confirmDrawable +
                ", confirmTextColor=" + confirmTextColor +
                ", confirmText='" + confirmText + '\'' +
                '}';
    }
}

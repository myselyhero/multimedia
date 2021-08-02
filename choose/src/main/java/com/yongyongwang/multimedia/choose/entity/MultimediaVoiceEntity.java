package com.yongyongwang.multimedia.choose.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/8/1
 */
public class MultimediaVoiceEntity implements Serializable {

    private String name;//歌曲名称
    private String author;//歌曲作者
    private String path;//歌曲路径
    private long size;//大小
    private long duration;//时长
    private boolean isMusic;
    private boolean choose;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        if (!TextUtils.isEmpty(author) && author.contains("unknown"))
            return null;
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isMusic() {
        return isMusic;
    }

    public void setMusic(boolean music) {
        isMusic = music;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    @Override
    public String toString() {
        return "MultimediaMusicEntity{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                ", isMusic=" + isMusic +
                ", choose=" + choose +
                '}';
    }
}

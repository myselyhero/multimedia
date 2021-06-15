package com.yongyongwang.multimedia.choose.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yongyong
 *
 * desc: 文件夹实体类
 *
 * @// TODO: 2020/8/26
 */
public class MultimediaFolderEntity implements Serializable, Comparable<MultimediaFolderEntity> {

    private String folder;
    private String path;
    private long bucketId = -1;
    private int num = 0;
    private boolean checked;
    private List<MultimediaEntity> data;


    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getBucketId() {
        return bucketId;
    }

    public void setBucketId(long bucketId) {
        this.bucketId = bucketId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<MultimediaEntity> getData() {
        return data;
    }

    public void setData(List<MultimediaEntity> data) {
        this.data = data;
    }

    /**
     *
     * @param entity
     */
    public void add(MultimediaEntity entity){
        if (data == null)
            data = new ArrayList<>();
        data.add(entity);
    }

    /**
     *
     * @param index
     * @param entity
     */
    public void add(int index,MultimediaEntity entity){
        if (data == null)
            data = new ArrayList<>();
        data.add(index,entity);
    }

    @Override
    public String toString() {
        return "MultimediaSelectedFolderEntity{" +
                "folder='" + folder + '\'' +
                ", path='" + path + '\'' +
                ", bucketId=" + bucketId +
                ", num=" + num +
                ", checked=" + checked +
                ", data=" + data +
                '}';
    }

    @Override
    public int compareTo(MultimediaFolderEntity o) {
        if(this == null && o == null) {
            return 0;
        }
        if(this == null) {
            return 1;
        }
        if(o == null) {
            return -1;
        }
        return num > o.getNum() ? -1 : 1;
    }
}

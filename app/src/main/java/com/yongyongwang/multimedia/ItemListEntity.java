package com.yongyongwang.multimedia;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class ItemListEntity implements Serializable {

    private String itemTips;

    private boolean choose;

    private List<ItemEntity> data;

    public String getItemTips() {
        return itemTips;
    }

    public void setItemTips(String itemTips) {
        this.itemTips = itemTips;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public List<ItemEntity> getData() {
        return data;
    }

    public void setData(List<ItemEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ItemListEntity{" +
                "itemTips='" + itemTips + '\'' +
                ", choose=" + choose +
                ", data=" + data +
                '}';
    }
}

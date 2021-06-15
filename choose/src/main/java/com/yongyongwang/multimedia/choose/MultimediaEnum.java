package com.yongyongwang.multimedia.choose;

import java.io.Serializable;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/5
 */
public enum MultimediaEnum implements Serializable {
    
    All(0),
    IMAGE(1),
    VIDEO(2);

    private int type;

    public int getType(){
        return type;
    }

    MultimediaEnum(int type) {
        this.type = type;
    }
}

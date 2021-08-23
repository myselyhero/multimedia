package com.yongyongwang.multimedia.choose.crop.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

/**
 * ImageFile Loader
 */
public class ImageLoader {

    /**
     * Obtains PixelMap instance From Image File
     *
     * @param fileName File Name
     * @return PixelMap
     */
    public static Bitmap getPixelMap(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return null;
        try{
            return BitmapFactory.decodeFile(fileName);
        }catch (Exception e){
            return null;
        }
    }
}

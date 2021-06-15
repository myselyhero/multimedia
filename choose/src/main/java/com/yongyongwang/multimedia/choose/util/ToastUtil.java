package com.yongyongwang.multimedia.choose.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/6
 */
public class ToastUtil {

    /**
     *
     * @param context
     * @param arg
     */
    public static void showShort(Context context,String arg){
        Toast toast = Toast.makeText(context,arg,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}

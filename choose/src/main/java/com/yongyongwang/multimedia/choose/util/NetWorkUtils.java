package com.yongyongwang.multimedia.choose.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author yongyong
 *
 * desc:网络工具类
 * 
 * @// TODO: 2021/6/7
 */
public class NetWorkUtils {

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean IsNetWorkEnable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

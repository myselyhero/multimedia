package com.yongyongwang.multimedia.choose.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;

import com.yongyongwang.multimedia.choose.R;

/**
 * @author yongyong
 * 
 * desc:全局的弹窗
 * 
 * @// TODO: 2021/6/8
 */
public class ProgressManagerKit {

    /**
     *
     */
    private static ProgressManagerKit instance;

    /**
     *
     */
    private AlertDialog mAlertDialog;

    /**
     *
     * @return
     */
    public static ProgressManagerKit getInstance() {
        if (instance == null){
            synchronized (ProgressManagerKit.class){
                instance = new ProgressManagerKit();
            }
        }
        return instance;
    }

    /**
     *
     * @param context
     */
    public void start(Context context){

        if (mAlertDialog != null)
            close();

        mAlertDialog = new AlertDialog.Builder(context, R.style.dialogStyle).create();
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
        mAlertDialog.setContentView(R.layout.progress_window);
        Window window = mAlertDialog.getWindow();
        window.setDimAmount(0f);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     *
     */
    public void close(){
        if (mAlertDialog != null){
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }
}

package com.yongyongwang.multimedia.choose.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.model.MultimediaResultListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author myselyhero
 *
 * desc:单独扫描图片的工具类
 *
 * @// TODO: 2021/6/5
 */
public class MultimediaImageContentResolver {

    private static MultimediaImageContentResolver instance;

    /**
     *
     * @return
     */
    public static MultimediaImageContentResolver getInstance() {
        if (instance == null){
            synchronized (MultimediaImageContentResolver.class){
                instance = new MultimediaImageContentResolver();
            }
        }
        return instance;
    }

    /**
     * 获取相册最新的照片
     * @param mContext
     * @param num
     * @param resultListener
     */
    public void getFirstImage(Context mContext, int num, MultimediaResultListener resultListener) {
        try {
            //索引字段
            String columns[] =
                    new String[]{MediaStore.Images.Media.DATA,//图片地址
                            MediaStore.Images.Media.WIDTH,//图片宽度
                            MediaStore.Images.Media.HEIGHT,//图片高度
                            MediaStore.Images.Media.DISPLAY_NAME,//图片全名，带后缀
                            MediaStore.MediaColumns.MIME_TYPE,
                            MediaStore.Images.Media.SIZE//图片文件大小
                    };


            String selection = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?";
            //只筛选png、jpg、jpeg、PNG、JPG、JPEG
            String[] selectionArgs = {"image/png", "image/jpg", "image/jpeg", "image/PNG", "image/JPG", "image/JPEG"};
            String sortOrder = MediaStore.Images.Media.DATE_ADDED + " desc";
            //得到一个游标
            ContentResolver resolver = mContext.getContentResolver();
            Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, sortOrder);

            List<MultimediaEntity> list = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst() && list.size() < num) {
                // 获取指定列的索引
                do {
                    String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    int width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH));
                    int height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT));
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE));
                    mimeType = TextUtils.isEmpty(mimeType) ? "image/jpeg" : mimeType;
                    if (TextUtils.isEmpty(name)) {
                        if (TextUtils.isEmpty(imagePath))
                            continue;
                        int index = imagePath.lastIndexOf(File.separator);
                        name = imagePath.substring(index + 1);
                        if (TextUtils.isEmpty(name))
                            continue;
                    }

                    //创建图片对象
                    MultimediaEntity entity = new MultimediaEntity();
                    entity.setPath(imagePath);
                    entity.setMimeType(mimeType);
                    entity.setName(name);
                    entity.setWidth(width);
                    entity.setHeight(height);
                    entity.setSize(size);
                    list.add(entity);
                } while (cursor.moveToNext());
                cursor.close();
            }

            if (resultListener != null)
                resultListener.onResult(list);
        } catch (Exception e) {
            resultListener.onResult(null);
        }
    }
}

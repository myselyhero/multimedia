package com.yongyongwang.multimedia.choose.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.yongyongwang.multimedia.choose.MultimediaConfig;
import com.yongyongwang.multimedia.choose.MultimediaEnum;
import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.entity.MultimediaFolderEntity;
import com.yongyongwang.multimedia.choose.model.MultimediaContentFolderListener;
import com.yongyongwang.multimedia.choose.model.MultimediaContentListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author yongyong
 *
 * desc:内容提供者
 *
 * @// TODO: 2020/8/28
 */
public final class MultimediaContentResolver {

    /**
     * 适配安卓Q
     */
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC";
    private static final String NOT_GIF_UNKNOWN = "!='image/*'";
    private static final String NOT_GIF = "!='image/gif' AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF_UNKNOWN;
    private static final String GROUP_BY_BUCKET_Id = " GROUP BY (bucket_id";
    private static final String COLUMN_COUNT = "count";

    /**
     *
     */
    private Context mContext;
    private MultimediaConfig mChooseConfig;

    /**
     * 图片
     */
    private final String SELECTION = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? )"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0)" + GROUP_BY_BUCKET_Id;

    private final String SELECTION_29 = MediaStore.Files.FileColumns.MEDIA_TYPE + "=? "
            + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    private final String SELECTION_NOT_GIF = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF + ") AND " + MediaStore.MediaColumns.SIZE + ">0)" + GROUP_BY_BUCKET_Id;

    private final String SELECTION_NOT_GIF_29 = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    /**
     * 获取所有
     * @param timeCondition
     * @param isGif
     * @return
     */
    private String getSelectionArgsForAllMediaCondition(String timeCondition, boolean isGif) {
        if (isAndroidQ) {
            return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + (isGif ? "" : " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF)
                    + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? AND " + timeCondition + ") AND " + MediaStore.MediaColumns.SIZE + ">0";
        }

        return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + (isGif ? "" : " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF)
                + " OR " + (MediaStore.Files.FileColumns.MEDIA_TYPE + "=? AND " + timeCondition) + ")" + " AND " + MediaStore.MediaColumns.SIZE + ">0)" + GROUP_BY_BUCKET_Id;
    }

    /**
     * 图片和视频
     */
    private final String[] SELECTION_ALL_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };

    /**
     * 获取指定文件夹的文件
     * @param mediaType
     * @param bucketId
     * @return
     */
    private String[] getSelectionArgsForPageSingleMediaType(int mediaType, long bucketId) {
        return bucketId == -1 ? new String[]{String.valueOf(mediaType)} : new String[]{String.valueOf(mediaType), toString(bucketId)};
    }

    /**
     *
     */
    private static final String[] PROJECTION_29 = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.BUCKET_ID,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE};

    /**
     *
     */
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.BUCKET_ID,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            "COUNT(*) AS " + COLUMN_COUNT};

    /**
     *
     */
    private static final String[] PROJECTION_PAGE = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.WIDTH,
            MediaStore.MediaColumns.HEIGHT,
            MediaStore.MediaColumns.DURATION,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.BUCKET_ID};

    /**
     * 构造参数
     * @param context
     * @param mChooseConfig
     */
    public MultimediaContentResolver(Context context, MultimediaConfig mChooseConfig) {
        this.mContext = context;
        this.mChooseConfig = mChooseConfig;
    }

    /**
     * 获取多媒体
     * @param bucketId 文件夹
     * @param listener 回调
     */
    public void loadMultimedia(long bucketId, @NonNull MultimediaContentListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor data = null;
                try {
                    data = mContext.getContentResolver().query(QUERY_URI, PROJECTION_PAGE, getPageSelection(bucketId), getPageSelectionArgs(bucketId), MediaStore.Files.FileColumns._ID + " DESC");
                    if (data != null) {
                        List<MultimediaEntity> result = new ArrayList<>();
                        data.moveToFirst();
                        do {
                            long id = data.getLong
                                    (data.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));

                            String absolutePath = data.getString
                                    (data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));

                            String url = isAndroidQ ? getPathByAndroid_Q(id) : absolutePath;

                            String mimeType = data.getString
                                    (data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));

                            mimeType = TextUtils.isEmpty(mimeType) ? "image/png" : mimeType;
                            // Here, it is solved that some models obtain mimeType and return the format of image / *,
                            // which makes it impossible to distinguish the specific type, such as mi 8,9,10 and other models
                            if (mimeType.endsWith("image/*")) {
                                if (FileUtils.isContent(url)) {
                                    mimeType = FileUtils.getMimeType(absolutePath);
                                } else {
                                    mimeType = FileUtils.getMimeType(url);
                                }
                                if (!mChooseConfig.isGif()) {
                                    boolean isGif = FileUtils.isGif(mimeType);
                                    if (isGif) {
                                        continue;
                                    }
                                }
                            }

                            int width = data.getInt
                                    (data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH));

                            int height = data.getInt
                                    (data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT));

                            long duration = data.getLong
                                    (data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DURATION));

                            long size = data.getLong
                                    (data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));

                            String fileName = data.getString
                                    (data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));

                            if (!TextUtils.isEmpty(mimeType) && mimeType.startsWith("video")) {

                                /**
                                 * 过滤调时长为0或大小为0的视频
                                 */
                                if (duration == 0 || size <= 0) {
                                    continue;
                                }

                                /**
                                 * 如果时长小于最小时长
                                 */
                                if (mChooseConfig.getMaxDuration() > 0 && duration < mChooseConfig.getMinDuration()) {
                                    continue;
                                }

                                /**
                                 * 如果时长大于最小时长
                                 */
                                if (mChooseConfig.getMaxDuration() > 0 && duration > mChooseConfig.getMaxDuration()) {
                                    continue;
                                }
                            }else {
                                //
                                if (mChooseConfig.getMaxSize() > 0 && FileUtils.formFileSize(size) > mChooseConfig.getMaxSize()){
                                    continue;
                                }
                            }

                            MultimediaEntity entity = new MultimediaEntity();
                            entity.setPath(url);
                            entity.setName(fileName);
                            entity.setDuration(duration);
                            entity.setHeight(height);
                            entity.setWidth(width);
                            entity.setMimeType(mimeType);
                            entity.setSize(size);
                            result.add(entity);
                        } while (data.moveToNext());
                        if (listener != null)
                            listener.onMultimedia(bucketId,result);
                    }else {
                        if (listener != null)
                            listener.onMultimedia(bucketId,null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null)
                        listener.onMultimedia(bucketId,null);
                } finally {
                    if (data != null && !data.isClosed()) {
                        data.close();
                    }
                }
            }
        }).start();
    }

    /**
     * Query the local gallery data
     *
     * @param listener
     */
    public void loadFolder(MultimediaContentFolderListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor data = mContext.getContentResolver().query(QUERY_URI, isAndroidQ ? PROJECTION_29 : PROJECTION, getSelection(), getSelectionArgs(), ORDER_BY);
                try {
                    if (data != null && data.getCount() > 0) {
                        int totalCount = 0;
                        List<MultimediaFolderEntity> mediaFolders = new ArrayList<>();
                        if (isAndroidQ) {
                            Map<Long, Long> countMap = new HashMap<>();
                            while (data.moveToNext()) {
                                long bucketId = data.getLong(data.getColumnIndex(MediaStore.MediaColumns.BUCKET_ID));
                                Long newCount = countMap.get(bucketId);
                                if (newCount == null) {
                                    newCount = 1L;
                                } else {
                                    newCount++;
                                }
                                countMap.put(bucketId, newCount);
                            }

                            if (data.moveToFirst()) {
                                Set<Long> hashSet = new HashSet<>();
                                do {
                                    long bucketId = data.getLong(data.getColumnIndex(MediaStore.MediaColumns.BUCKET_ID));
                                    if (hashSet.contains(bucketId)) {
                                        continue;
                                    }
                                    MultimediaFolderEntity mediaFolder = new MultimediaFolderEntity();
                                    mediaFolder.setBucketId(bucketId);
                                    String bucketDisplayName = data.getString(data.getColumnIndex(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME));
                                    long size = countMap.get(bucketId);
                                    long id = data.getLong(data.getColumnIndex(MediaStore.Files.FileColumns._ID));
                                    mediaFolder.setFolder(bucketDisplayName);
                                    mediaFolder.setNum(toInt(size));
                                    mediaFolder.setPath(getPathByAndroid_Q(id));
                                    mediaFolders.add(mediaFolder);
                                    hashSet.add(bucketId);
                                    totalCount += size;
                                } while (data.moveToNext());
                            }

                        } else {
                            data.moveToFirst();
                            do {
                                MultimediaFolderEntity mediaFolder = new MultimediaFolderEntity();
                                long bucketId = data.getLong(data.getColumnIndex(MediaStore.MediaColumns.BUCKET_ID));
                                String bucketDisplayName = data.getString(data.getColumnIndex(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME));
                                int size = data.getInt(data.getColumnIndex(COLUMN_COUNT));
                                mediaFolder.setBucketId(bucketId);
                                String url = data.getString(data.getColumnIndex(MediaStore.MediaColumns.DATA));
                                mediaFolder.setPath(url);
                                mediaFolder.setFolder(bucketDisplayName);
                                mediaFolder.setNum(size);
                                mediaFolders.add(mediaFolder);
                                totalCount += size;
                            } while (data.moveToNext());
                        }

                        // 相机胶卷
                        String type = mContext.getString(R.string.multimedia_choose_folder_all);
                        if (mChooseConfig.getMultimediaType() == MultimediaEnum.IMAGE){
                            type = mContext.getString(R.string.multimedia_choose_folder_image);
                        }else if (mChooseConfig.getMultimediaType() == MultimediaEnum.VIDEO){
                            type = mContext.getString(R.string.multimedia_choose_folder_video);
                        }

                        MultimediaFolderEntity allMediaFolder = new MultimediaFolderEntity();
                        allMediaFolder.setNum(totalCount);
                        allMediaFolder.setChecked(true);
                        allMediaFolder.setBucketId(-1);
                        if (data.moveToFirst()) {
                            String firstUrl = isAndroidQ ? getFirstUri(data) : getFirstUrl(data);
                            allMediaFolder.setPath(firstUrl);
                        }
                        allMediaFolder.setFolder(type);
                        mediaFolders.add(0, allMediaFolder);
                        if (listener != null)
                            listener.onResult(mediaFolders);
                    }else {
                        if (listener != null)
                            listener.onResult(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null)
                        listener.onResult(null);
                } finally {
                    if (data != null && !data.isClosed()) {
                        data.close();
                    }
                }
            }
        }).start();
    }

    /**
     * Get cover uri
     *
     * @param cursor
     * @return
     */
    private String getFirstUri(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
        return getPathByAndroid_Q(id);
    }

    /**
     * Get cover url
     *
     * @param cursor
     * @return
     */
    private static String getFirstUrl(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
    }


    /**
     *
     * @param bucketId
     * @return
     */
    private String getPageSelection(long bucketId) {
        switch (mChooseConfig.getMultimediaType()) {
            case All:
                if (bucketId == -1) {

                    // ofAll
                    return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                            + (mChooseConfig.isGif() ? "" : " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF)
                            + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? ) AND " + MediaStore.MediaColumns.SIZE + ">0";
                }
                // Gets the specified album directory
                return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + (mChooseConfig.isGif() ? "" : " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF)
                        + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? ) AND " + MediaStore.MediaColumns.BUCKET_ID + "=? AND " + MediaStore.MediaColumns.SIZE + ">0";

            case IMAGE:
                // Gets the image of the specified type
                if (bucketId == -1) {
                    // ofAll
                    return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                            + (mChooseConfig.isGif() ? "" : " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF)
                            + ") AND " + MediaStore.MediaColumns.SIZE + ">0";
                }
                // Gets the specified album directory
                return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + (mChooseConfig.isGif() ? "" : " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF)
                        + ") AND " + MediaStore.MediaColumns.BUCKET_ID + "=? AND " + MediaStore.MediaColumns.SIZE + ">0";
            case VIDEO:
                if (bucketId == -1) {
                    return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? ) AND " + MediaStore.MediaColumns.SIZE + ">0";
                }
                // Gets the specified album directory
                return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? ) AND " + MediaStore.MediaColumns.BUCKET_ID + "=? AND " + MediaStore.MediaColumns.SIZE + ">0";
        }
        return null;
    }

    private String[] getPageSelectionArgs(long bucketId) {
        switch (mChooseConfig.getMultimediaType()) {
            case All:
                if (bucketId == -1) {
                    // ofAll
                    return new String[]{
                            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                    };
                }
                //  Gets the specified album directory
                return new String[]{
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                        toString(bucketId)
                };
            case IMAGE:
                // Get photo
                return getSelectionArgsForPageSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, bucketId);
            case VIDEO:
                // Get video
                return getSelectionArgsForPageSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO, bucketId);
        }
        return null;
    }


    private String getSelection() {
        switch (mChooseConfig.getMultimediaType()) {
            case All:
                // Get all, not including audio
                return getSelectionArgsForAllMediaCondition(getDurationCondition(0, 0), mChooseConfig.isGif());
            case IMAGE:
                if (isAndroidQ) {
                    return mChooseConfig.isGif() ? SELECTION_29 : SELECTION_NOT_GIF_29;
                }
                return mChooseConfig.isGif() ? SELECTION : SELECTION_NOT_GIF;
            case VIDEO:
                if (isAndroidQ) {
                    return MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                            + " AND " + MediaStore.MediaColumns.SIZE + ">0";
                }
                return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + ") AND " + MediaStore.MediaColumns.SIZE + ">0)" + GROUP_BY_BUCKET_Id;
        }
        return null;
    }

    private String[] getSelectionArgs() {
        switch (mChooseConfig.getMultimediaType()) {
            case All:
                return SELECTION_ALL_ARGS;
            case IMAGE:
                // Get photo
                return new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)};
            case VIDEO:
                // Get video
                return new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};
        }
        return null;
    }

    /**
     * Android Q
     *
     * @param id
     * @return
     */
    private String getPathByAndroid_Q(long id) {
        return QUERY_URI.buildUpon().appendPath(toString(id)).build().toString();
    }

    /**
     *
     * @param exMaxLimit
     * @param exMinLimit
     * @return
     */
    private String getDurationCondition(long exMaxLimit, long exMinLimit) {
        long maxS = mChooseConfig.getMaxDuration() == 0 ? Long.MAX_VALUE : mChooseConfig.getMaxDuration();
        if (exMaxLimit != 0) {
            maxS = Math.min(maxS, exMaxLimit);
        }
        return String.format(Locale.CHINA, "%d <%s " + MediaStore.MediaColumns.DURATION + " and " + MediaStore.MediaColumns.DURATION + " <= %d",
                Math.max(exMinLimit, mChooseConfig.getMinDuration()),
                Math.max(exMinLimit, mChooseConfig.getMinDuration()) == 0 ? "" : "=",
                maxS);
    }

    /**
     *
     * @param o
     * @return
     */
    private int toInt(Object o) {
        int value = 0;
        if (o == null) {
            return value;
        }
        try {
            String s = o.toString().trim();
            if (s.contains(".")) {
                value = Integer.valueOf(s.substring(0, s.lastIndexOf(".")));
            } else {
                value = Integer.valueOf(s);
            }
        } catch (Exception e) {
        }
        return value;
    }

    /**
     *
     * @param o
     * @return
     */
    private String toString(Object o){
        try {
            return o.toString();
        } catch (Exception e) {
            return "";
        }
    }
}

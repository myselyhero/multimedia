package com.yongyongwang.multimedia.choose.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/5
 */
public class FileUtils {

    public static final int SIZE_B = 1;
    public static final int SIZE_KB = 2;
    public static final int SIZE_MB = 3;
    public static final int SIZE_GB = 4;

    /** 缓存目录 */
    public static String DIR;

    /**
     *
     * @param context
     */
    public static void initPath(Context context){
        DIR = Environment.getExternalStorageDirectory().getPath() + File.separator + context.getPackageName();
        File file = new File(DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     *  是否为GIF
     * @param path
     * @return
     */
    public static boolean isGif(String path) {
        if (!TextUtils.isEmpty(path)) {
            int lastIndex = path.lastIndexOf(".");
            if (lastIndex == -1)
                return false;
            String type = path.substring(lastIndex);
            return type.startsWith(".gif") || type.startsWith(".GIF");
        }
        return false;
    }

    /**
     * 是否是视频
     *
     * @param pictureType
     * @return
     */
    public static boolean isVideo(String pictureType) {
        switch (pictureType) {
            case "video/3gp":
            case "video/3gpp":
            case "video/3gpp2":
            case "video/avi":
            case "video/mp4":
            case "video/quicktime":
            case "video/x-msvideo":
            case "video/x-matroska":
            case "video/mpeg":
            case "video/webm":
            case "video/mp2ts":
                return true;
            default:
                return false;
        }
    }

    /**
     * 获取文件类型
     * @param url
     * @return
     */
    public static String fileToType(String url) {
        if (!TextUtils.isEmpty(url)) {
            File file = new File(url);
            String name = file.getName();
            if (name.endsWith(".mp4") || name.endsWith(".avi")
                    || name.endsWith(".3gpp") || name.endsWith(".3gp") || name.startsWith(".mov")) {
                return "video/mp4";
            } else if (name.endsWith(".PNG") || name.endsWith(".png") || name.endsWith(".jpeg")
                    || name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".jpg")
                    || name.endsWith(".webp") || name.endsWith(".WEBP") || name.endsWith(".JPEG")
                    || name.endsWith(".bmp")) {
                return "image/jpeg";
            } else if (name.endsWith(".mp3") || name.endsWith(".amr")
                    || name.endsWith(".aac") || name.endsWith(".war")
                    || name.endsWith(".flac") || name.endsWith(".lamr")) {
                return "audio/mpeg";
            }
        }
        return "image/jpeg";
    }

    /**
     *
     * @param url
     * @return
     */
    public static boolean isContent(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.startsWith("content://");
    }

    /**
     *
     * @param path
     * @return
     */
    public static String getMimeType(String path) {
        if (TextUtils.isEmpty(path) || path.startsWith("content:"))
            return "image/png";
        try {
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                String fileName = file.getName();
                int last = fileName.lastIndexOf(".") + 1;
                String temp = fileName.substring(last);
                return "image/" + temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "image/png";
    }

    /**
     * 文件是否存在
     * @param path
     * @return
     */
    public static boolean isExists(Context context,String path){
        if (TextUtils.isEmpty(path))
            return false;
        if (path.startsWith("content")){
            path = getPathFromUri(context,Uri.parse(path));
        }
        if (TextUtils.isEmpty(path))
            return false;
        return new File(path).exists();
    }

    /**
     *
     * @param context
     * @param dir
     * @param bitmap
     * @return
     */
    public static String saveBitmap(Context context,String dir, Bitmap bitmap) {
        String jpegName = dir + File.separator + System.currentTimeMillis() + ".jpg";
        File file = new File(jpegName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            /** 通知更新系统相册 */
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            context.sendBroadcast(intent);

            return jpegName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取视频信息 时长 微秒 us
     * @param url
     * @return
     */
    public static long getDuration(String url) {
        if (TextUtils.isEmpty(url))
            return 0;
        try {
            MediaExtractor mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(url);
            int videoExt = selectVideoTrack(mediaExtractor);
            MediaFormat mediaFormat = mediaExtractor.getTrackFormat(videoExt);
            long res = mediaFormat.containsKey(MediaFormat.KEY_DURATION) ? mediaFormat.getLong(MediaFormat.KEY_DURATION) : 0;//时长
            mediaExtractor.release();
            return res;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取视频宽高
     * @param videoPath
     * @return
     */
    public static int[] getVideoWidthAndHeight(String videoPath) {
        if (TextUtils.isEmpty(videoPath))
            return new int[]{0,0};
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(videoPath);
        String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
        String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        if ("90".equals(rotation) || "270".equals(rotation)) {//当视频是竖屏的时候 orientation = 90，横屏 orientation = 0 ，正确转换宽高
            return new int[]{Integer.parseInt(height),Integer.parseInt(width)};
        }
        retr.release();
        return new int[]{Integer.parseInt(width),Integer.parseInt(height)};
    }

    /**
     * 查找视频轨道
     * @param extractor
     * @return
     */
    public static int selectVideoTrack(MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPathFromUri(@NonNull Context context, Uri uri) {
        String path = "";
        try {
            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= 19) {
                path = getPath(context, uri);
            } else {
                path = getRealFilePath(context,uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (path == null) {
            path = "";
        }
        return path;
    }

    /**
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 专为Android4.4以上设计的从Uri获取文件路径
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (uri != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    final String path = id.replaceFirst("raw:", "");
                    return path;
                }
                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 在某些android8+的手机上，无法获取路径，所以用拷贝的方式，获取新文件名，然后把文件发出去
                String destinationPath = getPathByCopyFile(context, uri);
                return destinationPath;
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                String path = getDataColumn(context, contentUri, selection, selectionArgs);
                if (TextUtils.isEmpty(path)) {
                    path = getPathByCopyFile(context, uri);
                }
                return path;
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            String path = getDataColumn(context, uri, null, null);
            if (TextUtils.isEmpty(path)) {
                // 在某些华为android9+的手机上，无法获取路径，所以用拷贝的方式，获取新文件名，然后把文件发出去
                path = getPathByCopyFile(context, uri);
            }
            return path;
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getPathByCopyFile(Context context, Uri uri) {
        String fileName = getFileName(context, uri);
        File cacheDir = getDocumentCacheDir(context);
        File file = generateFileName(fileName, cacheDir);
        String destinationPath = null;
        if (file != null) {
            destinationPath = file.getAbsolutePath();
            saveFileFromUri(context, uri, destinationPath);
        }

        return destinationPath;
    }

    /**
     *
     * @param name
     * @param directory
     * @return
     */
    @Nullable
    private static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        return file;
    }

    /**
     *
     * @param context
     * @return
     */
    private static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), "documents");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    /**
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            filename = getName(uri.toString());
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null,
                    null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

    /**
     *
     * @param filename
     * @return
     */
    private static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }

    /**
     *
     * @param context
     * @param uri
     * @param destinationPath
     */
    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        boolean result = false;
        File file = new File(path);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    /**
     * 判断是否有DK卡
     * @return
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static int[] getImageSize(String path) {
        int size[] = new int[2];
        try {
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, onlyBoundsOptions);
            int originalWidth = onlyBoundsOptions.outWidth;
            int originalHeight = onlyBoundsOptions.outHeight;
            //size[0] = originalWidth;
            //size[1] = originalHeight;

            int degree = getBitmapDegree(path);
            if (degree == 0) {
                size[0] = originalWidth;
                size[1] = originalHeight;
            } else {
                //图片分辨率以480x800为标准
                float hh = 800f;//这里设置高度为800f
                float ww = 480f;//这里设置宽度为480f
                if (degree == 90 || degree == 270) {
                    hh = 480;
                    ww = 800;
                }
                //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
                int be = 1;//be=1表示不缩放
                if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
                    be = (int) (originalWidth / ww);
                } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
                    be = (int) (originalHeight / hh);
                }
                if (be <= 0)
                    be = 1;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = be;//设置缩放比例
                bitmapOptions.inDither = true;//optional
                bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
                Bitmap bitmap = BitmapFactory.decodeFile(path, bitmapOptions);
                bitmap = rotateBitmapByDegree(bitmap, degree);
                size[0] = bitmap.getWidth();
                size[1] = bitmap.getHeight();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;

    }

    public static int getBitmapDegree(String fileName) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(fileName);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     */
    private static long getFileSizes(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * Copies one file into the other with the given paths.
     * In the event that the paths are the same, trying to copy one file to the other
     * will cause both files to become null.
     * Simply skipping this step if the paths are identical.
     */
    public static void copyFile(@NonNull String pathFrom, @NonNull String pathTo) throws IOException {
        if (pathFrom.equalsIgnoreCase(pathTo)) {
            return;
        }

        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            inputChannel = new FileInputStream(new File(pathFrom)).getChannel();
            outputChannel = new FileOutputStream(new File(pathTo)).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }
    }
}

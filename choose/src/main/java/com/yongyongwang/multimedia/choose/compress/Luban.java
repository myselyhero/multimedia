package com.yongyongwang.multimedia.choose.compress;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.yongyongwang.multimedia.choose.compress.model.CompressionPredicate;
import com.yongyongwang.multimedia.choose.compress.model.InputStreamProvider;
import com.yongyongwang.multimedia.choose.compress.model.OnCompressListener;
import com.yongyongwang.multimedia.choose.compress.model.OnRenameListener;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
public class Luban implements Handler.Callback {

  private static final String TAG = "Luban";

  private static final int MSG_COMPRESS_SUCCESS = 0;
  private static final int MSG_COMPRESS_START = 1;
  private static final int MSG_COMPRESS_ERROR = 2;

  private String mTargetDir;
  private boolean focusAlpha;
  private final int mLeastCompressSize;
  private final OnRenameListener mRenameListener;
  private final OnCompressListener mCompressListener;
  private final CompressionPredicate mCompressionPredicate;
  private final List<InputStreamProvider> mStreamProviders;

  //
  private static int count;

  private Handler mHandler;

  private Luban(Builder builder) {
    this.mTargetDir = builder.mTargetDir;
    this.mRenameListener = builder.mRenameListener;
    this.mStreamProviders = builder.mStreamProviders;
    this.mCompressListener = builder.mCompressListener;
    this.mLeastCompressSize = builder.mLeastCompressSize;
    this.mCompressionPredicate = builder.mCompressionPredicate;
    mHandler = new Handler(Looper.getMainLooper(), this);
  }

  public static Builder with(Context context) {
    return new Builder(context);
  }

  /**
   * Returns a file with a cache image name in the private cache directory.
   *
   * @param context A context.
   */
  private File getImageCacheFile(Context context, String suffix) {
    if (TextUtils.isEmpty(mTargetDir)) {
      mTargetDir = getImageCacheDir(context).getAbsolutePath();
    }

    String cacheBuilder = mTargetDir + "/" +
        System.currentTimeMillis() +
        (int) (Math.random() * 1000) +
        (TextUtils.isEmpty(suffix) ? ".jpg" : suffix);

    return new File(cacheBuilder);
  }

  private File getImageCustomFile(Context context, String filename) {
    if (TextUtils.isEmpty(mTargetDir)) {
      mTargetDir = getImageCacheDir(context).getAbsolutePath();
    }

    String cacheBuilder = mTargetDir + "/" + filename;

    return new File(cacheBuilder);
  }

  /**
   * Returns a directory with a default name in the private cache directory of the application to
   * use to store retrieved audio.
   *
   * @param context A context.
   * @see #getImageCacheDir(Context, String)
   */
  private File getImageCacheDir(Context context) {
    return getImageCacheDir(context, FileUtils.DIR);
  }

  /**
   * Returns a directory with the given name in the private cache directory of the application to
   * use to store retrieved media and thumbnails.
   *
   * @param context   A context.
   * @param cacheName The name of the subdirectory in which to store the cache.
   * @see #getImageCacheDir(Context)
   */
  private static File getImageCacheDir(Context context, String cacheName) {
    File cacheDir = context.getExternalCacheDir();
    if (cacheDir != null) {
      File result = new File(cacheDir, cacheName);
      if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
        // File wasn't able to create a directory, or the result exists but not a directory
        return null;
      }
      return result;
    }
    if (Log.isLoggable(TAG, Log.ERROR)) {
      Log.e(TAG, "default disk cache dir is null");
    }
    return null;
  }

  /**
   * start asynchronous compress thread
   */
  private void launch(final Context context) {
    if (mStreamProviders == null || mStreamProviders.size() == 0 && mCompressListener != null) {
      mCompressListener.onError(new NullPointerException("image file cannot be null"));
    }

    Iterator<InputStreamProvider> iterator = mStreamProviders.iterator();

    while (iterator.hasNext()) {
      final InputStreamProvider provider = iterator.next();
      MultimediaEntity entity = provider.getData();

      AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
        @Override
        public void run() {
          try {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_START));

            File result = compress(context, provider);
            if (entity != null){
              entity.setCompressPath(result.getAbsolutePath());
              mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_SUCCESS, entity));
            }else {
              mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_SUCCESS, result));
            }
          } catch (IOException e) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_ERROR, e));
          }
        }
      });
      iterator.remove();
    }
  }

  /**
   * start compress and return the file
   */
  private File get(InputStreamProvider input, Context context) throws IOException {
    try {
      return new Engine(input, getImageCacheFile(context, Checker.SINGLE.extSuffix(input)), focusAlpha).compress();
    } finally {
      input.close();
    }
  }

  private List<File> get(Context context) throws IOException {
    List<File> results = new ArrayList<>();
    Iterator<InputStreamProvider> iterator = mStreamProviders.iterator();

    while (iterator.hasNext()) {
      results.add(compress(context, iterator.next()));
      iterator.remove();
    }

    return results;
  }

  private File compress(Context context, InputStreamProvider path) throws IOException {
    try {
      return compressReal(context,path);
    } finally {
      path.close();
    }
  }

  private File compressReal(Context context, InputStreamProvider provider) throws IOException {
    File result;

    MultimediaEntity entity = provider.getData();
    File outFile = getImageCacheFile(context, Checker.SINGLE.extSuffix(provider));

    if (mRenameListener != null) {
      String filename = mRenameListener.rename(entity.getPath());
      outFile = getImageCustomFile(context, filename);
    }

    if (mCompressionPredicate != null) {
      if (mCompressionPredicate.apply(entity.getPath())
          && Checker.SINGLE.needCompress(mLeastCompressSize, entity.getPath())) {
        result = new Engine(provider, outFile, focusAlpha).compress();
      } else {
        result = new File(entity.getPath());
      }
    } else {
      result = Checker.SINGLE.needCompress(mLeastCompressSize, entity.getPath()) ?
          new Engine(provider, outFile, focusAlpha).compress() :
          new File(entity.getPath());
    }

    return result;
  }

  @Override
  public boolean handleMessage(Message msg) {
    if (mCompressListener == null) return false;

    switch (msg.what) {
      case MSG_COMPRESS_START:
        mCompressListener.onStart();
        break;
      case MSG_COMPRESS_SUCCESS:
        mCompressListener.onSuccess((MultimediaEntity) msg.obj);
        break;
      case MSG_COMPRESS_ERROR:
        mCompressListener.onError((Throwable) msg.obj);
        break;
    }
    return false;
  }

  public static class Builder {

    private final Context context;
    private String mTargetDir;
    private boolean focusAlpha;
    private int mLeastCompressSize = 100;
    private OnRenameListener mRenameListener;
    private OnCompressListener mCompressListener;
    private CompressionPredicate mCompressionPredicate;
    private final List<InputStreamProvider> mStreamProviders;

    Builder(Context context) {
      this.context = context;
      this.mStreamProviders = new ArrayList<>();
    }

    private Luban build() {
      return new Luban(this);
    }

    public Builder load(InputStreamProvider inputStreamProvider) {
      mStreamProviders.add(inputStreamProvider);
      return this;
    }

    /**
     *
     * @param entity
     * @return
     */
    public Builder load(final MultimediaEntity entity) {
      mStreamProviders.add(new InputStreamAdapter() {
        @Override
        public InputStream openInternal() throws IOException {
          return new FileInputStream(entity.getPath());
        }

        @Override
        public MultimediaEntity getData() {
          return entity;
        }
      });
      return this;
    }

    public <T> Builder load(List<T> list) {
      for (T src : list) {
        if (src instanceof MultimediaEntity) {
          load((MultimediaEntity)src);
        } else {
          throw new IllegalArgumentException("Incoming data type exception, it must be String, File, Uri or Bitmap");
        }
      }
      return this;
    }

    public Builder setRenameListener(OnRenameListener listener) {
      this.mRenameListener = listener;
      return this;
    }

    public Builder setCompressListener(OnCompressListener listener) {
      this.mCompressListener = listener;
      return this;
    }

    public Builder setTargetDir(String targetDir) {
      this.mTargetDir = targetDir;
      return this;
    }

    /**
     * Do I need to keep the image's alpha channel
     *
     * @param focusAlpha <p> true - to keep alpha channel, the compress speed will be slow. </p>
     *                   <p> false - don't keep alpha channel, it might have a black background.</p>
     */
    public Builder setFocusAlpha(boolean focusAlpha) {
      this.focusAlpha = focusAlpha;
      return this;
    }

    /**
     * do not compress when the origin image file size less than one value
     *
     * @param size the value of file size, unit KB, default 100K
     */
    public Builder ignoreBy(int size) {
      this.mLeastCompressSize = size;
      return this;
    }

    /**
     * do compress image when return value was true, otherwise, do not compress the image file
     *
     * @param compressionPredicate A predicate callback that returns true or false for the given input path should be compressed.
     */
    public Builder filter(CompressionPredicate compressionPredicate) {
      this.mCompressionPredicate = compressionPredicate;
      return this;
    }


    /**
     * begin compress image with asynchronous
     */
    public void launch() {
      build().launch(context);
    }
  }
}
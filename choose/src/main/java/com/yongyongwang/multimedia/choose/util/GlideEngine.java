package com.yongyongwang.multimedia.choose.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.request.RequestOptions;
import com.yongyongwang.multimedia.choose.R;

import java.security.MessageDigest;

/**
 * @author myselyhero
 *
 * desc:加载引擎
 *
 * @// TODO: 2021/6/6
 */
public class GlideEngine {

    /**
     * 常规加载图片
     * @param context
     * @param imageView
     */
    public static void loader(Context context, String url, ImageView imageView){

        if (FileUtils.isGif(url)){
            loaderGif(context,url,imageView);
            return;
        }
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.multimedia_default_image)
                        .error(R.drawable.multimedia_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);
    }

    /**
     * 加载gif图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void loaderGif(Context context, String url, ImageView imageView){
        Glide.with(context)
                .asGif()
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.multimedia_default_image)
                        .error(R.drawable.multimedia_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);
    }

    /**
     * 加载指定大小的圆角图片
     * @param context
     * @param url
     * @param imageView
     * @param size
     */
    public static void loaderCircle(Context context,@NonNull String url, @NonNull final ImageView imageView, int size){
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CornerTransform(size)))
                .into(imageView);
    }

    /**
     * @apiNote yongyong
     *
     * @email 1947920597@qq.com
     *
     * @desc: 圆角图片辅助类
     *
     * @// TODO: 2021/6/7
     */
    static class CornerTransform implements Transformation<Bitmap> {

        private float radius;

        public CornerTransform(float radius) {
            this.radius = radius;
        }

        @NonNull
        @Override
        public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
            Bitmap source = resource.get();
            //修正圆角
            radius *= (float) outWidth / (float) outHeight;
            BitmapPool mBitmapPool = Glide.get(context).getBitmapPool();
            Bitmap outBitmap = mBitmapPool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            if (outBitmap == null) {
                outBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(outBitmap);
            Paint paint = new Paint();
            //关联画笔绘制的原图bitmap
            BitmapShader shader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            //计算中心位置,进行偏移
            int width = (source.getWidth() - outWidth) / 2;
            int height = (source.getHeight() - outHeight) / 2;
            if (width != 0 || height != 0) {
                Matrix matrix = new Matrix();
                matrix.setTranslate((float) (-width), (float) (-height));
                shader.setLocalMatrix(matrix);
            }

            paint.setShader(shader);
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0.0F, 0.0F, (float) canvas.getWidth(), (float) canvas.getHeight());
            canvas.drawRoundRect(rectF, this.radius, this.radius, paint); //先绘制圆角矩形

            return BitmapResource.obtain(outBitmap, mBitmapPool);
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }
}

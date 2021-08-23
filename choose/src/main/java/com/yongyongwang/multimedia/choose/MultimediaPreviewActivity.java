package com.yongyongwang.multimedia.choose;

import android.content.Intent;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.crop.MultimediaCropActivity;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.entity.MultimediaFolderEntity;
import com.yongyongwang.multimedia.choose.entity.MultimediaPreviewEntity;
import com.yongyongwang.multimedia.choose.photo.view.PhotoView;
import com.yongyongwang.multimedia.choose.util.FileUtils;
import com.yongyongwang.multimedia.choose.util.GlideEngine;
import com.yongyongwang.multimedia.choose.view.MultimediaPreviewBottomLayout;
import com.yongyongwang.multimedia.choose.view.MultimediaPreviewRecyclerview;
import com.yongyongwang.multimedia.choose.view.MultimediaPreviewTopLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/6
 */
public class MultimediaPreviewActivity extends MultimediaBaseActivity implements ViewPager.OnPageChangeListener {

    private String TAG = "MultimediaPreviewActivity";

    public static final String PREVIEW_MODEL = "preview_model";
    public static final String PREVIEW_POSITION = "preview_position";

    private boolean isAll;
    private int mPosition;

    private List<MultimediaEntity> dataSource;
    private ViewPager viewPager;
    private ViewPageAdapter mAdapter;
    private RelativeLayout background;
    private MultimediaPreviewTopLayout mTopLayout;
    private MultimediaPreviewBottomLayout mBottomLayout;

    private MultimediaPreviewRecyclerview mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.multimedia_preview_activity;
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.multimedia_preview_pager);
        background = findViewById(R.id.multimedia_preview_bg);
        mTopLayout = findViewById(R.id.multimedia_preview_top);
        mBottomLayout = findViewById(R.id.multimedia_preview_bottom);
        mRecyclerView = findViewById(R.id.multimedia_preview_item);

        background.setBackgroundColor(mChooseConfig.isDarkTheme() ? getResources().getColor(R.color.multimedia_theme_background) : getResources().getColor(R.color.multimedia_white_background));
        mTopLayout.setConfig(mChooseConfig);
        mBottomLayout.setConfig(mChooseConfig);
        mTopLayout.setCheckedNum(mChooseDataSource.size());

        isAll = getIntent().getBooleanExtra(PREVIEW_MODEL,true);
        mPosition = getIntent().getIntExtra(PREVIEW_POSITION,0);

        //设置数据源
        if (isAll){
            for (MultimediaFolderEntity entity : mFolderDataSource) {
                if (entity.isChecked()){
                    dataSource = entity.getData();
                    break;
                }
            }
        }

        mTopLayout.setButtonOnClickListener(v -> {
            if (!isAll){//返回上一级将选中列表中未选中的清除
                for (int i = 0; i < mChooseDataSource.size(); i++) {
                    if (!mChooseDataSource.get(i).isChoose()){
                        mChooseDataSource.remove(i);
                    }
                }
                onUpdatePreview(mChooseDataSource.size());
            }
            setResult(RESULT_OK);
            finish();
        });
        mBottomLayout.addEditClickListener(v -> {
            MultimediaEntity entity = getEntity(viewPager.getCurrentItem());
            if (entity == null || FileUtils.isGif(entity.getPath()) || FileUtils.isVideo(entity.getMimeType()))
                return;
            Intent intent = new Intent(this, MultimediaCropActivity.class);
            intent.putExtra(REQUEST_DATA,entity.getPath());
            startActivityForResult(intent,PERMISSION_REQUEST_CROP);
        });
        mBottomLayout.addCheckListener(v -> {
            MultimediaEntity entity = getEntity(viewPager.getCurrentItem());
            if (entity == null)
                return;
            choose(entity);
        });
        mRecyclerView.setClickListener(entity -> {
            viewPager.setCurrentItem(entity.getPosition());
        });

        /**
         * 初始化viewPager
         */
        mAdapter = new ViewPageAdapter();
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mAdapter);

        /**
         * 这里设置一下，让ViewPager进入PageChangeListener去初始化点击事件
         */
        if (isAll){
            viewPager.setCurrentItem(mPosition);
            mTopLayout.setPosition(mPosition+1,dataSource == null ? 0 : dataSource.size());
        }else {
            initPageSelected(viewPager.getCurrentItem());
        }

        /**
         * 如果已选列表不为空，则添加
         */
        if (mChooseDataSource.size() != 0){
            if (mChooseConfig.isOnly() && !mChooseConfig.isOnlyPreview())
                return;
            MultimediaEntity currencyEntity = getEntity(viewPager.getCurrentItem());
            List<MultimediaPreviewEntity> list = new ArrayList<>();
            for (int i = 0; i < mChooseDataSource.size(); i++) {
                MultimediaEntity multimediaEntity = mChooseDataSource.get(i);
                MultimediaPreviewEntity entity = new MultimediaPreviewEntity();
                entity.setPath(multimediaEntity.getPath());
                entity.setChoose(currencyEntity != null && TextUtils.equals(currencyEntity.getPath(),multimediaEntity.getPath()));
                entity.setPosition(isAll ? getPosition(multimediaEntity.getPath()) : i);
                list.add(entity);
            }
            mRecyclerView.setDataSource(list);
        }
    }

    @Override
    protected void onUpdatePreview(int num) {
        mTopLayout.setCheckedNum(num);
    }

    /**
     *
     * @param entity
     */
    private void choose(MultimediaEntity entity){

        boolean flag = previewChooseItem(entity,isAll);
        mBottomLayout.setChecked(flag);

        if (mChooseConfig.isOnly() && !mChooseConfig.isOnlyPreview())
            return;

        /**
         * 为true表示添加成功、添加到底部列表
         */
        if (flag){

            if (mChooseConfig.isOnly()){
                mRecyclerView.clear();
            }

            if (!mRecyclerView.isExist(entity.getPath())){
                MultimediaPreviewEntity previewEntity = new MultimediaPreviewEntity();
                previewEntity.setPath(entity.getPath());
                previewEntity.setPosition(viewPager.getCurrentItem());
                previewEntity.setChoose(entity.isChoose());
                mRecyclerView.addDataSource(previewEntity);
            }
        }else {

            /**
             * 不删除、但是要置于未选中
             */
            if (!isAll){
                mRecyclerView.clearCurrentPosition();
            }else {
                /**
                 * 为false表示删除
                 */
                mRecyclerView.remove(entity.getPath());
            }
        }
    }

    /**
     * ViewPager执行PageSelected
     * @param position
     */
    private void initPageSelected(int position){
        MultimediaEntity entity = getEntity(position);
        //设置下标及是否选择
        mTopLayout.setPosition(position+1,mAdapter.getCount());
        if (entity != null){
            mBottomLayout.setChecked(entity.isChoose());
        }
    }

    /**
     * 底部列表position
     * @return
     */
    private int getPosition(String path){
        int position = -1;
        if (isAll){
            if (dataSource == null || dataSource.size() == 0)
                return position;
            for (int i = 0; i < dataSource.size(); i++) {
                MultimediaEntity entity = dataSource.get(i);
                if (TextUtils.equals(entity.getPath(),path)){
                    position = i;
                    break;
                }
            }
        }else {
            if (mChooseDataSource.size() == 0)
                return position;
            for (int i = 0; i < mChooseDataSource.size(); i++) {
                MultimediaEntity entity = mChooseDataSource.get(i);
                if (TextUtils.equals(entity.getPath(),path)){
                    position = i;
                    break;
                }
            }
        }

        return position;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CROP && RESULT_OK == resultCode && data != null){
            MultimediaEntity cropEntity = (MultimediaEntity) data.getSerializableExtra(RESULT_DATA);
            if (cropEntity == null)
                return;

            /*for (MultimediaFolderEntity folderEntity : mFolderDataSource) {
                if (folderEntity.isChecked() || folderEntity.getBucketId() == -1){
                    folderEntity.add(0,cropEntity);
                }
            }

            choose(cropEntity);*/
        }
    }

    /**
     * 分模式获取数据
     * @param position
     * @return
     */
    private MultimediaEntity getEntity(int position){
        if (isAll){
            return (dataSource == null || dataSource.size() == 0) ? null :  dataSource.get(position);
        }else {
            return mChooseDataSource.get(position);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        initPageSelected(position);
        if (mChooseConfig.isOnly() && !mChooseConfig.isOnlyPreview())
            return;
        MultimediaEntity entity = getEntity(position);
        if (entity != null){
            mRecyclerView.scrollerPosition(entity.getPath());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     *
     */
    class ViewPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (isAll){
                return dataSource == null ? 0 : dataSource.size();
            }else {
                return mChooseDataSource.size();
            }
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return  view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
            MultimediaEntity mediaEntity = getEntity(position);

            if (FileUtils.isVideo(mediaEntity.getMimeType())){
                View view = LayoutInflater.from(MultimediaPreviewActivity.this).inflate(R.layout.multimedia_preview_item_layout,null);
                ImageView imageView = view.findViewById(R.id.multimedia_preview_item_image);
                ImageView playerImageView = view.findViewById(R.id.multimedia_preview_item_player);
                container.addView(view);

                GlideEngine.loader(MultimediaPreviewActivity.this,mediaEntity.getPath(),imageView);
                playerImageView.setOnClickListener(v -> {
                    Intent intent = new Intent(MultimediaPreviewActivity.this,MultimediaPlayerActivity.class);
                    intent.putExtra(REQUEST_DATA,mediaEntity.getPath());
                    startActivity(intent);
                });

                return view;
            }else {
                if (FileUtils.isGif(mediaEntity.getPath())){
                    ImageView imageView = new ImageView(MultimediaPreviewActivity.this);
                    container.addView(imageView);
                    GlideEngine.loaderGif(MultimediaPreviewActivity.this,mediaEntity.getPath(),imageView);
                    return imageView;
                }else {
                    PhotoView photoView = new PhotoView(MultimediaPreviewActivity.this);
                    Matrix misplayMatrix = new Matrix();
                    container.addView(photoView);
                    photoView.setDisplayMatrix(misplayMatrix);
                    photoView.setOnSingleFlingListener((e1, e2, velocityX, velocityY) -> {
                        return true;
                    });
                    String str = mediaEntity.getPath();
                    GlideEngine.loader(MultimediaPreviewActivity.this,str,photoView);
                    return photoView;
                }
            }
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }
}

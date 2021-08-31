package com.yongyongwang.multimedia.choose.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.util.FileUtils;
import com.yongyongwang.multimedia.choose.util.GlideEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/6
 */
public class MultimediaRecyclerView extends RecyclerView {

    private static final int MULTIMEDIA_CAMERA = 1;
    private static final int MULTIMEDIA_PICTURE = 2;

    private AttributeSet attributeSet;
    private int spanCount;
    private boolean isCamera;
    private boolean isShade;

    private MultimediaAdapter mAdapter;
    private List<MultimediaEntity> dataSource;

    private OnMultimediaAdapterListener adapterListener;

    public MultimediaRecyclerView(Context context) {
        super(context);
        init();
    }

    public MultimediaRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attributeSet = attrs;
        init();
    }

    public MultimediaRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attributeSet = attrs;
        init();
    }

    /**
     *
     */
    private void init(){
        if (attributeSet != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.MultimediaRecyclerView, 0, 0);
            try {
                spanCount = typedArray.getInt(R.styleable.MultimediaRecyclerView_spanCount, 4);
                isCamera = typedArray.getBoolean(R.styleable.MultimediaRecyclerView_isCamera, true);
            } finally {
                typedArray.recycle();
            }
        }

        setOverScrollMode(OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(getContext(),spanCount);
        setLayoutManager(manager);
        addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 5;
                outRect.right = 5;
                outRect.top = 5;
                outRect.bottom = 5;
            }
        });
        mAdapter = new MultimediaAdapter();
        setAdapter(mAdapter);
    }

    /**
     *
     * @param data
     */
    public void setDataSource(@NonNull List<MultimediaEntity> data) {
        dataSource = data;
        mAdapter.notifyDataSetChanged();
    }

    public List<MultimediaEntity> getDataSource() {
        return dataSource;
    }

    /**
     *
     * @param entity
     */
    public void addItem(MultimediaEntity entity){
        if (dataSource == null)
            return;
        dataSource.add(entity);
        update();
    }

    /**
     *
     */
    public void update(){
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param position
     */
    public void update(int position){
        if (mAdapter != null)
            mAdapter.notifyItemChanged(position);
    }

    /**
     *
     * @param adapterListener
     */
    public void setAdapterListener(OnMultimediaAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    /**
     *
     * @param camera
     */
    public void setCamera(boolean camera) {
        isCamera = camera;
    }

    /**
     *
     * @param shade
     */
    public void setShade(boolean shade) {
        isShade = shade;
    }

    /**
     *
     * @param spanCount
     */
    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        GridLayoutManager manager = new GridLayoutManager(getContext(),spanCount);
        setLayoutManager(manager);
    }

    /**
     *
     * @param duration
     * @return
     */
    private String formatDurationTime(long duration) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    /**
     *
     */
    class MultimediaAdapter extends RecyclerView.Adapter<MultimediaHolder> {

        @NonNull
        @Override
        public MultimediaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_item_holder,parent,false);
            return new MultimediaHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MultimediaHolder holder, int position) {
            if (isCamera && getItemViewType(position) == MULTIMEDIA_CAMERA) {
                holder.imageView.setImageResource(R.drawable.multimedia_camera);
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
                holder.chooseImageView.setVisibility(View.GONE);
                holder.tagTextView.setVisibility(View.GONE);
                if (adapterListener != null){
                    holder.itemView.setOnClickListener(v -> {
                        adapterListener.onCamera();
                    });
                }
            } else {
                holder.chooseImageView.setVisibility(View.VISIBLE);
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                int pos = isCamera ? position -1 : position;

                MultimediaEntity mediaEntity = dataSource.get(pos);

                holder.shadeBackground.setVisibility(isShade && mediaEntity.isChoose() ? View.VISIBLE : View.GONE);
                holder.chooseImageView.setImageResource(mediaEntity.isChoose() ? R.drawable.multimedia_choose_sel : R.drawable.multimedia_choose_un);
                if (FileUtils.isVideo(mediaEntity.getMimeType())){
                    holder.tagTextView.setVisibility(View.VISIBLE);
                    holder.tagTextView.setText(formatDurationTime(mediaEntity.getDuration()));
                }else if (FileUtils.isGif(mediaEntity.getPath())){
                    holder.tagTextView.setVisibility(View.VISIBLE);
                    holder.tagTextView.setText(getContext().getString(R.string.multimedia_gif));
                }else {
                    holder.tagTextView.setVisibility(View.GONE);
                }

                GlideEngine.loader(getContext(),mediaEntity.getPath(),holder.imageView);

                if (adapterListener != null){
                    holder.chooseImageView.setOnClickListener(v -> {
                        adapterListener.onCheckClick(position,mediaEntity);
                    });
                    holder.itemView.setOnClickListener(v -> {
                        adapterListener.onItemClick(pos,mediaEntity);
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return isCamera ? dataSource == null ? 0 : dataSource.size() + 1 : dataSource == null ? 0 : dataSource.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isCamera && position == 0) {
                return MULTIMEDIA_CAMERA;
            } else {
                return MULTIMEDIA_PICTURE;
            }
        }
    }

    /**
     *
     */
    static class MultimediaHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        ImageView chooseImageView;
        View shadeBackground;
        TextView tagTextView;

        public MultimediaHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.multimedia_item_image);
            chooseImageView = itemView.findViewById(R.id.multimedia_item_choose);
            tagTextView = itemView.findViewById(R.id.multimedia_item_tag);
            shadeBackground = itemView.findViewById(R.id.multimedia_item_shade);
        }
    }

    /**
     *
     */
    public interface OnMultimediaAdapterListener{

        /**
         *
         * @param position
         * @param entity
         */
        void onItemClick(int position, MultimediaEntity entity);

        /**
         *
         * @param position
         * @param entity
         */
        void onCheckClick(int position,MultimediaEntity entity);

        /**
         * 去拍照
         */
        void onCamera();
    }
}

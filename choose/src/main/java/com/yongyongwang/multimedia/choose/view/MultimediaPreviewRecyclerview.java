package com.yongyongwang.multimedia.choose.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.entity.MultimediaPreviewEntity;
import com.yongyongwang.multimedia.choose.util.FileUtils;
import com.yongyongwang.multimedia.choose.util.GlideEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/7
 */
public class MultimediaPreviewRecyclerview extends RecyclerView {

    private MultimediaPreviewAdapter mAdapter;
    private final List<MultimediaPreviewEntity> dataSource = new ArrayList<>();

    private MultimediaPreviewItemClickListener clickListener;

    public MultimediaPreviewRecyclerview(@NonNull Context context) {
        super(context);
        init();
    }

    public MultimediaPreviewRecyclerview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultimediaPreviewRecyclerview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(layoutManager);
        addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 10;
                outRect.right = 10;
                outRect.top = 10;
                outRect.bottom = 10;
            }
        });
        mAdapter = new MultimediaPreviewAdapter();
        setAdapter(mAdapter);
    }

    /**
     *
     * @param entities
     */
    public void setDataSource(List<MultimediaPreviewEntity> entities){
        if (entities == null)
            return;
        dataSource.addAll(entities);
        mAdapter.notifyDataSetChanged();
        if (getVisibility() != View.VISIBLE)
            setVisibility(View.VISIBLE);
        scrollToPosition(dataSource.size()-1);
    }

    /**
     *
     * @param entity
     */
    public void addDataSource(MultimediaPreviewEntity entity){
        if (entity == null)
            return;
        clearCurrentPosition();
        int count = dataSource.size();
        dataSource.add(entity);
        mAdapter.notifyItemRangeInserted(count,dataSource.size());
        if (getVisibility() != View.VISIBLE)
            setVisibility(View.VISIBLE);
        scrollToPosition(dataSource.size()-1);
    }

    /**
     *
     */
    public void clearCurrentPosition(){
        for (int i = 0; i < dataSource.size(); i++) {
            if (dataSource.get(i).isChoose()){
                dataSource.get(i).setChoose(false);
                mAdapter.notifyItemChanged(i);
            }
        }
    }

    /**
     *
     * @param path
     */
    public void remove(String path){
        for (int i = 0; i < dataSource.size(); i++) {
            if (TextUtils.equals(dataSource.get(i).getPath(),path)){
                dataSource.remove(i);
                mAdapter.notifyItemRemoved(i);
                mAdapter.notifyItemRangeChanged(i, dataSource.size());
                break;
            }
        }
    }

    /**
     *
     * @param path
     * @return 是否已添加
     */
    public boolean isExist(String path){
        for (int i = 0; i < dataSource.size(); i++) {
            if (TextUtils.equals(dataSource.get(i).getPath(),path)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param clickListener
     */
    public void setClickListener(MultimediaPreviewItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     *
     */
    class MultimediaPreviewAdapter extends RecyclerView.Adapter<MultimediaPreviewItemHolder>{

        @NonNull
        @Override
        public MultimediaPreviewItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_preview_item_holder,parent,false);
            return new MultimediaPreviewItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MultimediaPreviewItemHolder holder, int position) {
            MultimediaPreviewEntity entity = dataSource.get(position);
            GlideEngine.loaderCircle(getContext(),entity.getPath(),holder.imageView,12);

            if (entity.isChoose()){
                holder.imageView.setBackground(getResources().getDrawable(R.drawable.multimedia_preview_holder_background));
            }else {
                holder.imageView.setBackground(null);
            }

            if (clickListener != null){
                holder.itemView.setOnClickListener(v -> {
                    /**
                     * -1表示是其它文件的图片或视频，不执行回调
                     */
                    if (entity.getPosition() == -1 || entity.isChoose()){
                        return;
                    }
                    for (int i = 0; i < dataSource.size(); i++) {
                        if (dataSource.get(i).isChoose()){
                            dataSource.get(i).setChoose(false);
                            notifyItemChanged(i);
                            break;
                        }
                    }
                    entity.setChoose(true);
                    notifyItemChanged(position);
                    clickListener.onItemClick(entity);
                });
            }
        }

        @Override
        public int getItemCount() {
            return dataSource == null ? 0 : dataSource.size();
        }
    }

    /**
     *
     */
    static class MultimediaPreviewItemHolder extends RecyclerView.ViewHolder{

        RelativeLayout relativeLayout;
        ImageView imageView;

        public MultimediaPreviewItemHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.multimedia_preview_holder_bg);
            imageView = itemView.findViewById(R.id.multimedia_preview_holder_iv);
        }
    }

    /**
     *
     */
    public interface MultimediaPreviewItemClickListener {
        /**
         *
         * @param entity
         */
        void onItemClick(MultimediaPreviewEntity entity);
    }
}

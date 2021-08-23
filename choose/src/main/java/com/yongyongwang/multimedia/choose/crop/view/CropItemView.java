package com.yongyongwang.multimedia.choose.crop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
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
import com.yongyongwang.multimedia.choose.crop.strategy.entity.AdjustType;
import com.yongyongwang.multimedia.choose.entity.MultimediaCropItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/8/16
 */
public class CropItemView extends RecyclerView {

    private AttributeSet attributeSet;

    private List<MultimediaCropItemEntity> dataSource = new ArrayList<>();
    private CropItemAdapter mAdapter;

    private OnCropItemListener itemListener;

    private int mModel;

    public CropItemView(@NonNull Context context) {
        super(context);
        init();
    }

    public CropItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attributeSet = attrs;
        init();
    }

    public CropItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attributeSet = attrs;
        init();
    }

    /**
     *
     */
    private void init(){
        if (attributeSet != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.CropItemView, 0, 0);
            try {
                mModel = typedArray.getInt(R.styleable.CropItemView_cropModel, 0);
            } finally {
                typedArray.recycle();
            }
        }

        setOverScrollMode(OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(getContext(),mModel == 0 ? 4 : 3);
        setLayoutManager(manager);

        mAdapter = new CropItemAdapter();
        setAdapter(mAdapter);
        MultimediaCropItemEntity entity;
        if (mModel == 0){
            entity = new MultimediaCropItemEntity();
            entity.setChoose(true);
            entity.setNormalRes(R.drawable.crop_edit_size_free_normal);
            entity.setPressRes(R.drawable.crop_edit_size_free_press);
            dataSource.add(entity);

            entity = new MultimediaCropItemEntity();
            entity.setNormalRes(R.drawable.crop_edit_size1_normal);
            entity.setPressRes(R.drawable.crop_edit_size1_press);
            dataSource.add(entity);

            entity = new MultimediaCropItemEntity();
            entity.setNormalRes(R.drawable.crop_edit_size2_normal);
            entity.setPressRes(R.drawable.crop_edit_size2_press);
            dataSource.add(entity);

            entity = new MultimediaCropItemEntity();
            entity.setNormalRes(R.drawable.crop_edit_size3_normal);
            entity.setPressRes(R.drawable.crop_edit_size3_press);
            dataSource.add(entity);
        }else {
            entity = new MultimediaCropItemEntity();
            entity.setChoose(true);
            entity.setType(AdjustType.BRIGHTNESS);
            entity.setProgress(entity.getType().getMode() == SlideStepBar.Mode.ADJUST_MODE ? SlideStepBar.MAX_LEVEL : 0);
            entity.setName(getResources().getString(R.string.crop_abjust_brightness));
            entity.setNormalRes(R.drawable.crop_abjust_brightness_normal);
            entity.setPressRes(R.drawable.crop_abjust_brightness_press);
            dataSource.add(entity);

            entity = new MultimediaCropItemEntity();
            entity.setType(AdjustType.CONTRAST);
            entity.setProgress(entity.getType().getMode() == SlideStepBar.Mode.ADJUST_MODE ? SlideStepBar.MAX_LEVEL : 0);
            entity.setName(getResources().getString(R.string.crop_abjust_contrast));
            entity.setNormalRes(R.drawable.crop_abjust_contrast_normal);
            entity.setPressRes(R.drawable.crop_abjust_contrast_press);
            dataSource.add(entity);

            entity = new MultimediaCropItemEntity();
            entity.setType(AdjustType.SATURATION);
            entity.setProgress(entity.getType().getMode() == SlideStepBar.Mode.ADJUST_MODE ? SlideStepBar.MAX_LEVEL : 0);
            entity.setName(getResources().getString(R.string.crop_abjust_saturation));
            entity.setNormalRes(R.drawable.crop_abjust_saturation_normal);
            entity.setPressRes(R.drawable.crop_abjust_saturation_press);
            dataSource.add(entity);
        }
        mAdapter.notifyItemRangeInserted(0,dataSource.size());
    }

    /**
     *
     * @param itemListener
     */
    public void setItemListener(OnCropItemListener itemListener) {
        this.itemListener = itemListener;
        if (itemListener != null && mModel != 0)
            itemListener.onItemClick(0,dataSource.get(0));
    }

    /**
     *
     */
    public void clearSelected(){
        if (dataSource == null || dataSource.size() == 0)
            return;
        for (int i = 0; i < dataSource.size(); i++) {
            if (dataSource.get(i).isChoose()){
                dataSource.get(i).setChoose(false);
                notifySet(i);
                break;
            }
        }
    }

    /**
     *
     * @param position
     */
    public void notifySet(int position){
        if (mAdapter != null)
            mAdapter.notifyItemChanged(position);
    }

    /**
     *
     */
    public void notifySet(){
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    /**
     *
     */
    class CropItemAdapter extends Adapter<CropItemHolder>{

        @NonNull
        @Override
        public CropItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_crop_item_view,parent,false);
            return new CropItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CropItemHolder holder, int position) {
            MultimediaCropItemEntity entity = dataSource.get(position);

            holder.textView.setText(entity.getName());
            holder.imageView.setImageResource(entity.isChoose() ? entity.getPressRes() : entity.getNormalRes());
            holder.textView.setTextColor(entity.isChoose() ? getResources().getColor(R.color.multimedia_theme_bright) : getResources().getColor(R.color.white));

            holder.itemView.setOnClickListener(v -> {
                if (entity.isChoose())
                    return;
                for (int i = 0; i < dataSource.size(); i++) {
                    if (dataSource.get(i).isChoose()){
                        dataSource.get(i).setChoose(false);
                        notifyItemChanged(i);
                        break;
                    }
                }
                entity.setChoose(true);
                notifyItemChanged(position);

                if (itemListener != null)
                    itemListener.onItemClick(position,entity);
            });
        }

        @Override
        public int getItemCount() {
            return dataSource == null ? 0 : dataSource.size();
        }
    }

    /**
     *
     */
    static class CropItemHolder extends ViewHolder {

        TextView textView;
        ImageView imageView;

        public CropItemHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.crop_item_tv);
            imageView = itemView.findViewById(R.id.crop_item_iv);
        }
    }

    /**
     *
     */
    public interface OnCropItemListener {

        /**
         *
         * @param position
         * @param entity
         */
        void onItemClick(int position,MultimediaCropItemEntity entity);
    }
}

package com.yongyongwang.multimedia.choose.edit.view;

import android.content.Context;
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
import com.yongyongwang.multimedia.choose.entity.MultimediaAdjustEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MultimediaAdjustList extends RecyclerView {

    private List<MultimediaAdjustEntity> dataSource = new ArrayList<>();
    private MultimediaAdjustAdapter mAdapter;

    private List<OnAdjustClickListener> listeners;

    public MultimediaAdjustList(@NonNull Context context) {
        super(context);
        init();
    }

    public MultimediaAdjustList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultimediaAdjustList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        setOverScrollMode(OVER_SCROLL_NEVER);

        dataSource.add(new MultimediaAdjustEntity(getContext().getString(R.string.multimedia_adjust_bright),R.drawable.multimedia_adjust_bright,true));
        dataSource.add(new MultimediaAdjustEntity(getContext().getString(R.string.multimedia_adjust_contrast),R.drawable.multimedia_adjust_contrast));
        dataSource.add(new MultimediaAdjustEntity(getContext().getString(R.string.multimedia_adjust_saturation),R.drawable.multimedia_adjust_saturation));
        dataSource.add(new MultimediaAdjustEntity(getContext().getString(R.string.multimedia_adjust_sharpness),R.drawable.multimedia_adjust_sharpness));
        dataSource.add(new MultimediaAdjustEntity(getContext().getString(R.string.multimedia_adjust_hue),R.drawable.multimedia_adjust_hue));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),dataSource.size());
        setLayoutManager(gridLayoutManager);
        mAdapter = new MultimediaAdjustAdapter();
        setAdapter(mAdapter);
    }

    /**
     *
     * @param clickListener
     */
    public void setClickListener(OnAdjustClickListener clickListener) {
        if (listeners == null)
            listeners = new ArrayList<>();
        listeners.add(clickListener);
    }

    /**
     *
     */
    class MultimediaAdjustAdapter extends RecyclerView.Adapter<MultimediaAdjustViewHolder>{

        @NonNull
        @Override
        public MultimediaAdjustViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_edit_adjust_holder,parent,false);
            return new MultimediaAdjustViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MultimediaAdjustViewHolder holder, int position) {
            MultimediaAdjustEntity entity = dataSource.get(position);

            if (entity.getIcon() > 0)
                holder.imageView.setImageResource(entity.getIcon());
            holder.textView.setText(entity.getName());
            holder.textView.setEnabled(entity.isChoose());
            holder.imageView.setEnabled(entity.isChoose());

            if (listeners != null && listeners.size() > 0){
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
                    for (OnAdjustClickListener listener : listeners) {
                        listener.onClick(entity);
                    }
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
    static class MultimediaAdjustViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public MultimediaAdjustViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.multimedia_adjust_iv);
            textView = itemView.findViewById(R.id.multimedia_adjust_tv);
        }
    }

    /**
     *
     */
    public interface OnAdjustClickListener {

        /**
         *
         * @param entity
         */
        void onClick(MultimediaAdjustEntity entity);
    }
}

package com.yongyongwang.multimedia.choose.edit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yongyongwang.multimedia.choose.R;

/**
 *
 */
public class MultimediaFilterLayout extends RecyclerView {

    public MultimediaFilterLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public MultimediaFilterLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultimediaFilterLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        setOverScrollMode(OVER_SCROLL_NEVER);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(layoutManager);
        MultimediaFilterAdapter mAdapter = new MultimediaFilterAdapter();
        setAdapter(mAdapter);
    }

    /**
     *
     */
    class MultimediaFilterAdapter extends RecyclerView.Adapter<MultimediaFilterHolder>{

        @NonNull
        @Override
        public MultimediaFilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_edit_filter_holder,parent,false);
            return new MultimediaFilterHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MultimediaFilterHolder holder, int position) {
            /*MagicFilterType type = arrays[position];
            holder.imageView.setImageResource(FilterTypeHelper.FilterType2Thumb(type));
            if (clickListener != null){
                holder.imageView.setOnClickListener(v -> {
                    clickListener.onClick(type);
                });
            }*/
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    /**
     *
     */
    static class MultimediaFilterHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MultimediaFilterHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.multimedia_edit_filter_iv);
        }
    }
}

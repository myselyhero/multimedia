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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc: 底部按钮
 * 
 * @// TODO: 2021/6/16
 */
public class MultimediaEditButtonLayout extends RecyclerView {

    private final List<MultimediaEditButton> dataSource = new ArrayList<>();

    private OnEditButtonClickListener clickListener;

    public MultimediaEditButtonLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public MultimediaEditButtonLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultimediaEditButtonLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        setOverScrollMode(OVER_SCROLL_NEVER);

        dataSource.add(new MultimediaEditButton(R.drawable.multimedia_crop,getContext().getString(R.string.multimedia_edit_crop)));
        dataSource.add(new MultimediaEditButton(R.drawable.multimedia_filter,getContext().getString(R.string.multimedia_edit_filter)));
        dataSource.add(new MultimediaEditButton(R.drawable.multimedia_beauty,getContext().getString(R.string.multimedia_edit_beauty)));
        dataSource.add(new MultimediaEditButton(R.drawable.multimedia_adjust,getContext().getString(R.string.multimedia_edit_abjust)));

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),dataSource.size());
        setLayoutManager(layoutManager);
        MultimediaEditButtonAdapter mAdapter = new MultimediaEditButtonAdapter();
        setAdapter(mAdapter);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                MultimediaEditButton button = dataSource.get(0);
                if (clickListener != null){
                    clickListener.onClick(button.title);
                }
            }
        },500);
    }

    /**
     *
     * @param clickListener
     */
    public void setClickListener(OnEditButtonClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     *
     */
    static class MultimediaEditButton implements Serializable {

        int icon;
        String title;

        public MultimediaEditButton(int icon, String title) {
            this.icon = icon;
            this.title = title;
        }

        @Override
        public String toString() {
            return "MultimediaEditButton{" +
                    "icon=" + icon +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    /**
     *
     */
    class MultimediaEditButtonAdapter extends RecyclerView.Adapter<MultimediaEditButtonHolder>{

        @NonNull
        @Override
        public MultimediaEditButtonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_edit_button_holder,parent,false);
            return new MultimediaEditButtonHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MultimediaEditButtonHolder holder, int position) {
            MultimediaEditButton entity = dataSource.get(position);

            if (entity.icon > 0){
                holder.imageView.setImageResource(entity.icon);
            }
            holder.textView.setText(entity.title);

            if (clickListener != null){
                holder.itemView.setOnClickListener(v -> {
                    clickListener.onClick(entity.title);
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
    static class MultimediaEditButtonHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public MultimediaEditButtonHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.multimedia_edit_button_iv);
            textView = itemView.findViewById(R.id.multimedia_edit_button_tv);
        }
    }

    /**
     *
     */
    public interface OnEditButtonClickListener {

        /**
         *
         * @param arg
         */
        void onClick(String arg);
    }
}

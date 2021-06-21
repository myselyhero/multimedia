package com.yongyongwang.multimedia.choose.edit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yongyongwang.multimedia.choose.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MultimediaRatioLayout extends RecyclerView {

    private final List<String> dataSource = new ArrayList<>();

    private OnRationClickListener clickListener;

    public MultimediaRatioLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public MultimediaRatioLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultimediaRatioLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setOverScrollMode(OVER_SCROLL_NEVER);

        dataSource.add(getContext().getString(R.string.multimedia_ration_1));
        dataSource.add(getContext().getString(R.string.multimedia_ration_4));
        dataSource.add(getContext().getString(R.string.multimedia_ration_16));
        dataSource.add(getContext().getString(R.string.multimedia_ration_9));

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),dataSource.size());
        setLayoutManager(layoutManager);
        MultimediaRatioAdapter mAdapter = new MultimediaRatioAdapter();
        setAdapter(mAdapter);
    }

    /**
     *
     * @param clickListener
     */
    public void setClickListener(OnRationClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     *
     */
    class MultimediaRatioAdapter extends RecyclerView.Adapter<MultimediaRatioHolder>{

        @NonNull
        @Override
        public MultimediaRatioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_edit_ration_holder,parent,false);
            return new MultimediaRatioHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MultimediaRatioHolder holder, int position) {
            String ration = dataSource.get(position);
            holder.textView.setText(ration);
            if (clickListener != null){
                holder.textView.setOnClickListener(v -> {
                    clickListener.onRation(ration);
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
    static class MultimediaRatioHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MultimediaRatioHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.multimedia_edit_ration_tv);
        }
    }

    /**
     *
     */
    public interface OnRationClickListener {

        /**
         *
         * @param ration
         */
        void onRation(String ration);
    }
}

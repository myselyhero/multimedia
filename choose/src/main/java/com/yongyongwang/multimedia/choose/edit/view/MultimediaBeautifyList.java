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
import com.yongyongwang.multimedia.choose.entity.MultimediaBeautifyEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MultimediaBeautifyList extends RecyclerView {

    private final List<MultimediaBeautifyEntity> dataSource = new ArrayList<>();

    private OnBeautifyListener listener;

    public MultimediaBeautifyList(@NonNull Context context) {
        super(context);
        init();
    }

    public MultimediaBeautifyList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultimediaBeautifyList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        setOverScrollMode(OVER_SCROLL_NEVER);

        dataSource.add(new MultimediaBeautifyEntity(R.drawable.multimedia_beautify_buffing,getContext().getString(R.string.multimedia_beautify_buffing),true));
        dataSource.add(new MultimediaBeautifyEntity(R.drawable.multimedia_beautify_whitening,getContext().getString(R.string.multimedia_beautify_whitening)));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),dataSource.size());
        setLayoutManager(gridLayoutManager);
        MultimediaBeautifyListAdapter mAdapter = new MultimediaBeautifyListAdapter();
        setAdapter(mAdapter);
    }

    /**
     *
     * @param listener
     */
    public void setListener(OnBeautifyListener listener) {
        this.listener = listener;
    }

    /**
     *
     */
    class MultimediaBeautifyListAdapter extends RecyclerView.Adapter<MultimediaBeautifyListHolder>{

        @NonNull
        @Override
        public MultimediaBeautifyListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_edit_beautify_holder,parent,false);
            return new MultimediaBeautifyListHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MultimediaBeautifyListHolder holder, int position) {
            MultimediaBeautifyEntity entity = dataSource.get(position);
            holder.imageView.setImageResource(entity.getIcon());
            holder.textView.setText(entity.getName());
            holder.imageView.setEnabled(entity.isChoose());
            holder.textView.setEnabled(entity.isChoose());

            if (listener != null){
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
                    listener.onListener(entity);
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
    static class MultimediaBeautifyListHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MultimediaBeautifyListHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.multimedia_beautify_holder_iv);
            textView = itemView.findViewById(R.id.multimedia_beautify_holder_tv);
        }
    }

    /**
     *
     */
    public interface OnBeautifyListener {

        /**
         *
         * @param entity
         */
        void onListener(MultimediaBeautifyEntity entity);
    }
}

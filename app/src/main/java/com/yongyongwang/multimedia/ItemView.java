package com.yongyongwang.multimedia;

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

import java.util.List;

/**
 *
 */
public class ItemView extends RecyclerView {

    private List<ItemEntity> dataSource;
    private ItemAdapter mAdapter;

    private OnItemClickListener clickListener;

    public ItemView(@NonNull Context context) {
        super(context);
        init();
    }

    public ItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        setOverScrollMode(OVER_SCROLL_NEVER);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        setLayoutManager(layoutManager);
        mAdapter = new ItemAdapter();
        setAdapter(mAdapter);
    }

    /**
     *
     * @param data
     */
    public void setDataSource(List<ItemEntity> data) {
        if (data == null)
            return;
        dataSource = data;
        mAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param clickListener
     */
    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     *
     */
    class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_holder,parent,false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemView.ItemHolder holder, int position) {
            ItemEntity entity = dataSource.get(position);
            holder.textView.setText(entity.getName());
            holder.imageView.setVisibility(entity.isChoose() ? View.VISIBLE : View.GONE);

            holder.itemView.setOnClickListener(v -> {
                if (entity.isChoose())
                    entity.setChoose(false);
                else
                    entity.setChoose(true);
                notifyItemChanged(position);

                if (clickListener != null)
                    clickListener.onClick(entity);
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
    static class ItemHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_tv);
            imageView = itemView.findViewById(R.id.item_iv);
        }
    }

    /**
     *
     */
    public interface OnItemClickListener {

        /**
         *
         * @param entity
         */
        void onClick(ItemEntity entity);
    }
}

package com.yongyongwang.multimedia;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 *
 */
public class ItemsListView extends RecyclerView {

    private List<ItemListEntity> dataSource;
    private ItemsListAdapter mAdapter;

    public ItemsListView(@NonNull Context context) {
        super(context);
        init();
    }

    public ItemsListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemsListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        setOverScrollMode(OVER_SCROLL_NEVER);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(VERTICAL);
        setLayoutManager(layoutManager);
        addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 20;
            }
        });
        mAdapter = new ItemsListAdapter();
        setAdapter(mAdapter);
    }

    /**
     *
     * @param data
     */
    public void setDataSource(List<ItemListEntity> data) {
        if (data == null)
            return;
        dataSource = data;
        mAdapter.notifyDataSetChanged();
    }

    /**
     *
     */
    class ItemsListAdapter extends RecyclerView.Adapter<ItemsListHolder>{

        @NonNull
        @Override
        public ItemsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_holder,parent,false);
            return new ItemsListHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemsListView.ItemsListHolder holder, int position) {
            ItemListEntity entity = dataSource.get(position);
            holder.textView.setText(entity.getItemTips());
            holder.imageView.setVisibility(entity.isChoose() ? View.VISIBLE : View.GONE);
            holder.item.setVisibility(entity.isChoose() ? View.VISIBLE : View.GONE);
            if (entity.getData() != null && entity.getData().size() > 0){
                holder.item.setDataSource(entity.getData());
            }

            holder.linearLayout.setOnClickListener(v -> {
                if (entity.isChoose())
                    return;
                for (int i = 0; i < dataSource.size(); i++) {
                    ItemListEntity listEntity = dataSource.get(i);
                    if (listEntity.isChoose()){
                        listEntity.setChoose(false);
                        notifyItemChanged(i);
                        break;
                    }
                }
                entity.setChoose(true);
                notifyItemChanged(position);
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
    static class ItemsListHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView textView;
        ImageView imageView;
        ItemView item;

        public ItemsListHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.item_list_tips);
            textView = itemView.findViewById(R.id.item_list_tv);
            imageView = itemView.findViewById(R.id.item_list_iv);
            item = itemView.findViewById(R.id.item_list_item);
        }
    }
}

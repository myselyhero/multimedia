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
public class MultimediaBeautifyEstate extends RecyclerView {

    private MultimediaBeautifyEstateAdapter mAdapter;
    private final List<BeautifyEstateEntity> dataSource = new ArrayList<>();

    private OnBeautifyEstateListener estateListener;

    public MultimediaBeautifyEstate(@NonNull Context context) {
        super(context);
        init();
    }

    public MultimediaBeautifyEstate(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultimediaBeautifyEstate(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);

        dataSource.add(new BeautifyEstateEntity(getContext().getString(R.string.multimedia_beautify_0),true));
        dataSource.add(new BeautifyEstateEntity(getContext().getString(R.string.multimedia_beautify_1)));
        dataSource.add(new BeautifyEstateEntity(getContext().getString(R.string.multimedia_beautify_2)));
        dataSource.add(new BeautifyEstateEntity(getContext().getString(R.string.multimedia_beautify_3)));
        dataSource.add(new BeautifyEstateEntity(getContext().getString(R.string.multimedia_beautify_4)));
        dataSource.add(new BeautifyEstateEntity(getContext().getString(R.string.multimedia_beautify_5)));

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),dataSource.size());
        setLayoutManager(layoutManager);
        mAdapter = new MultimediaBeautifyEstateAdapter();
        setAdapter(mAdapter);
    }

    /**
     *
     * @param value
     */
    public void setValue(int value){
        for (BeautifyEstateEntity entity : dataSource) {
            if (value == Integer.parseInt(entity.getValue())){
                entity.setChoose(true);
            }else {
                entity.setChoose(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param estateListener
     */
    public void setEstateListener(OnBeautifyEstateListener estateListener) {
        this.estateListener = estateListener;
    }

    /**
     *
     */
    static class BeautifyEstateEntity {

        private String value;
        private boolean choose;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isChoose() {
            return choose;
        }

        public void setChoose(boolean choose) {
            this.choose = choose;
        }

        public BeautifyEstateEntity(String value, boolean choose) {
            this.value = value;
            this.choose = choose;
        }

        public BeautifyEstateEntity(String value) {
            this.value = value;
        }
    }


    /**
     *
     */
    class MultimediaBeautifyEstateAdapter extends RecyclerView.Adapter<MultimediaBeautifyEstateHolder>{

        @NonNull
        @Override
        public MultimediaBeautifyEstateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_edit_beautify_estate,parent,false);
            return new MultimediaBeautifyEstateHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MultimediaBeautifyEstateHolder holder, int position) {
            BeautifyEstateEntity entity = dataSource.get(position);

            holder.textView.setText(entity.getValue());
            holder.textView.setEnabled(entity.isChoose());

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

                if (estateListener != null)
                    estateListener.onListener(Integer.parseInt(entity.getValue()));
            });
        }

        @Override
        public int getItemCount() {
            return dataSource.size();
        }
    }

    /**
     *
     */
    static class MultimediaBeautifyEstateHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MultimediaBeautifyEstateHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.multimedia_edit_beautify_estate_tv);
        }
    }

    /**
     *
     */
    public interface OnBeautifyEstateListener {

        /**
         *
         * @param value
         */
        void onListener(int value);
    }
}

package com.yongyongwang.multimedia.choose.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.util.GlideEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yongyong
 *
 * desc: 添加选择视图
 * 
 * @// TODO: 2020/8/26
 */
public class MultimediaChooseView extends RecyclerView {

    private String TAG = MultimediaChooseView.class.getSimpleName();

    private static final int IV_MULTIMEDIA_CHOOSE_HOLDER_ADD = 1;
    private static final int IV_MULTIMEDIA_CHOOSE_HOLDER_PICTURE = 2;

    private MultimediaChooseAdapter mAdapter;
    private List<MultimediaEntity> dataSource = new ArrayList<>();

    private AttributeSet attributeSet;
    private int spanCount;
    private int maxNum ;
    private boolean isAdd;
    private boolean isRemove;
    private int addIcon;
    private int removeIcon;

    /**
     * 拖拽
     */
    private ItemTouchHelper mTouchHelper;

    /**
     * 回调
     */
    private MultimediaChooseClickListener mChooseListener;

    public MultimediaChooseView(Context context) {
        super(context);
        initView();
    }

    public MultimediaChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attributeSet == null)
            attributeSet = attrs;
        initView();
    }

    public MultimediaChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attributeSet == null)
            attributeSet = attrs;
        initView();
    }

    /**
     *
     */
    private void initView(){

        if (attributeSet != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.MultimediaChooseView, 0, 0);
            try {
                spanCount = typedArray.getInt(R.styleable.MultimediaChooseView_columnCount, 3);
                isAdd = typedArray.getBoolean(R.styleable.MultimediaChooseView_isAdd, true);
                isRemove = typedArray.getBoolean(R.styleable.MultimediaChooseView_isRemove, true);
                maxNum = typedArray.getInteger(R.styleable.MultimediaChooseView_maxNum,9);
                addIcon = typedArray.getResourceId(R.styleable.MultimediaChooseView_addIcon,R.drawable.multimedia_add);
                removeIcon = typedArray.getResourceId(R.styleable.MultimediaChooseView_removeIcon,R.drawable.multimedia_remove);
            } finally {
                typedArray.recycle();
            }
        }

        setHasFixedSize(true);
        setOverScrollMode(OVER_SCROLL_NEVER);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),spanCount);
        setLayoutManager(gridLayoutManager);
        initTouchHelper();

        ((SimpleItemAnimator) getItemAnimator()).setSupportsChangeAnimations(false);
        addItemDecoration(new ItemDecoration(){

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 5;
                outRect.right = 5;
                outRect.top = 5;
                outRect.bottom = 5;
            }
        });

        mAdapter = new MultimediaChooseAdapter();
        setAdapter(mAdapter);
    }

    /**
     *
     * @param data
     */
    public void setDataSource(@NonNull List<MultimediaEntity> data){
        if (data == null)
            return;
        if (dataSource.size() > 0)
            dataSource.clear();
        dataSource.addAll(data);
        notifyData();
    }

    /**
     *
     */
    public void clear(){
        if (dataSource == null || dataSource.size() == 0)
            return;
        dataSource.clear();
        notifyData();
    }

    /**
     *
     */
    public void notifyData(){
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param position
     */
    public void notifyData(int position){
        if (mAdapter != null)
            mAdapter.notifyItemChanged(position);
    }

    /**
     *
     * @return
     */
    public List<MultimediaEntity> getDataSource() {
        return dataSource;
    }

    /**
     * 获取可选数量
     * @return
     */
    public int getUsableNum(){
        return dataSource == null ? maxNum : maxNum - dataSource.size();
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    public void setAddIcon(int addIcon) {
        this.addIcon = addIcon;
    }

    public void setRemoveIcon(int removeIcon) {
        this.removeIcon = removeIcon;
    }

    /**
     *
     * @param mChooseListener
     */
    public void addChooseListener(@NonNull MultimediaChooseClickListener mChooseListener) {
        this.mChooseListener = mChooseListener;
    }

    /**
     *
     */
    private void initTouchHelper(){
        if (isAdd)
            return;
        destroyedTouchHelper();
        mTouchHelper = new ItemTouchHelper(new MyItemTouchHelper());
        mTouchHelper.attachToRecyclerView(this);
    }

    /**
     *
     */
    private void destroyedTouchHelper(){
        if (mTouchHelper != null){
            mTouchHelper.attachToRecyclerView(null);
            mTouchHelper = null;
        }
    }

    /**
     *
     */
    class MultimediaChooseAdapter extends Adapter<MultimediaChooseHolder> {

        @NonNull
        @Override
        public MultimediaChooseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_choose_item_holder,parent,false);
            return new MultimediaChooseHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MultimediaChooseHolder holder, int position) {
            /**
             * 少于8张，显示继续添加的图标
             */
            if (isAdd && getItemViewType(position) == IV_MULTIMEDIA_CHOOSE_HOLDER_ADD) {
                holder.imageView.setImageResource(addIcon);
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
                holder.imageView.setOnClickListener(v -> {
                    if (mChooseListener != null){
                        mChooseListener.onAdd();
                    }
                });
            } else {
                if (isRemove){
                    holder.removeImageView.setImageResource(removeIcon);
                    holder.removeImageView.setVisibility(View.VISIBLE);
                    holder.removeImageView.setOnClickListener(v -> {
                        int index = holder.getAdapterPosition();
                        if (index != RecyclerView.NO_POSITION) {
                            dataSource.remove(index);
                            notifyItemRemoved(index);
                            notifyItemRangeChanged(index, dataSource.size());
                        }
                    });
                }

                final MultimediaEntity entity = dataSource.get(position);

                String path = entity.getPath();
                if (!TextUtils.isEmpty(entity.getCompressPath())){
                    path = entity.getCompressPath();
                }else if (!TextUtils.isEmpty(entity.getCropPath())){
                    path = entity.getCropPath();
                }
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GlideEngine.loader(getContext(),path,holder.imageView);

                holder.itemView.setOnClickListener(v -> {
                    if (mChooseListener != null){
                        mChooseListener.onItemClick(position,entity);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (isAdd && dataSource.size() < maxNum) {
                return dataSource.size() + 1;
            } else {
                return dataSource.size();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isAdd && isAddItem(position)) {
                return IV_MULTIMEDIA_CHOOSE_HOLDER_ADD;
            } else {
                return IV_MULTIMEDIA_CHOOSE_HOLDER_PICTURE;
            }
        }


        /**
         *
         * @param position
         * @return
         */
        private boolean isAddItem(int position) {
            int size = dataSource == null ? 0 : dataSource.size();
            return position == size;
        }
    }

    /**
     *
     */
    public static class MultimediaChooseHolder extends ViewHolder {

        ImageView imageView;
        ImageView removeImageView;

        public MultimediaChooseHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.multimedia_choose_item_image);
            removeImageView = itemView.findViewById(R.id.multimedia_choose_item_remove);
        }
    }

    /**
     *
     */
    public interface MultimediaChooseClickListener {

        /**
         *
         */
        void onAdd();

        /**
         *
         * @param position
         * @param entity
         */
        void onItemClick(int position,@NonNull MultimediaEntity entity);
    }

    /**
     *
     */
    public class MyItemTouchHelper extends ItemTouchHelper.Callback{


        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        }



        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder, @NonNull ViewHolder target) {
            //得到当拖拽的viewHolder的Position
            int fromPosition = viewHolder.getAdapterPosition();
            //拿到当前拖拽到的item的viewHolder
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(dataSource, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(dataSource, i, i - 1);
                }
            }
            mAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(@NonNull ViewHolder viewHolder, int direction) {

        }


        /**
         * 长按选中Item的时候开始调用
         *
         * @param viewHolder
         * @param actionState
         */
        @Override
        public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 手指松开的时候还原
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        /**
         * 重写拖拽不可用
         * @return
         */
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }
    }
}

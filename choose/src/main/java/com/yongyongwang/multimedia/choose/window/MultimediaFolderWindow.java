package com.yongyongwang.multimedia.choose.window;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.entity.MultimediaFolderEntity;
import com.yongyongwang.multimedia.choose.util.GlideEngine;

import java.util.List;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/6
 */
public class MultimediaFolderWindow extends PopupWindow {

    private Context mContext;
    private List<MultimediaFolderEntity> dataSource;
    private OnMultimediaFolderWindowListener windowListener;

    /**
     *
     * @param context
     */
    public MultimediaFolderWindow(Context context, List<MultimediaFolderEntity> data, OnMultimediaFolderWindowListener listener){
        mContext = context;
        dataSource = data;
        windowListener = listener;

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置pw中的控件能够获取焦点
        setFocusable(true);
        //设置可以通过点击mPopupWindow外部关闭mPopupWindow
        setOutsideTouchable(true);
        //设置mPopupWindow的进出动画
        setAnimationStyle(R.style.multimedia_folder_window_anim);

        View view = View.inflate(context, R.layout.multimedia_folder_window, null);
        RecyclerView recyclerView = view.findViewById(R.id.multimedia_folder_window_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MultimediaFolderWindowAdapter adapter = new MultimediaFolderWindowAdapter();
        recyclerView.setAdapter(adapter);

        setContentView(view);
        update();//刷新mPopupWindow

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (windowListener != null)
                    windowListener.onDismiss();
            }
        });
    }

    /**
     *
     */
    class MultimediaFolderWindowAdapter extends RecyclerView.Adapter<MultimediaFolderWindowViewHolder> {

        @NonNull
        @Override
        public MultimediaFolderWindowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.multimedia_folder_window_item,parent,false);
            return new MultimediaFolderWindowViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MultimediaFolderWindowViewHolder holder, int position) {
            MultimediaFolderEntity entity = dataSource.get(position);

            if (!TextUtils.isEmpty(entity.getPath())){
                GlideEngine.loader(mContext,entity.getPath(),holder.imageView);
            }

            String str = entity.getFolder()+" ("+entity.getNum()+")";
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#565656")), entity.getFolder().length(),str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.textView.setText(spannableString);

            if (entity.isChecked()){
                holder.checkedImageView.setVisibility(View.VISIBLE);
            }else {
                holder.checkedImageView.setVisibility(View.GONE);
            }

            if (position == dataSource.size() -1){
                holder.line.setVisibility(View.GONE);
            }else {
                holder.line.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(v -> {
                if (entity.isChecked()){
                    return;
                }
                for (MultimediaFolderEntity folderEntity:dataSource) {
                    folderEntity.setChecked(false);
                }
                entity.setChecked(true);
                notifyDataSetChanged();
                if (windowListener != null)
                    windowListener.onListener(entity);

                dismiss();
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
    static class MultimediaFolderWindowViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        private ImageView checkedImageView;
        private View line;

        public MultimediaFolderWindowViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.multimedia_folder_window_item_thumbnail);
            textView = itemView.findViewById(R.id.multimedia_folder_window_item_name);
            checkedImageView = itemView.findViewById(R.id.multimedia_folder_window_item_checked);
            line = itemView.findViewById(R.id.multimedia_folder_window_item_line);
        }
    }

    /**
     *
     */
    public interface OnMultimediaFolderWindowListener{

        /**
         *
         */
        void onDismiss();

        /**
         *
         * @param entity
         */
        void onListener(MultimediaFolderEntity entity);
    }
}

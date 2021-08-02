package com.yongyongwang.multimedia.choose.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.entity.MultimediaVoiceEntity;
import com.yongyongwang.multimedia.choose.util.FileUtils;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/8/1
 */
public class MultimediaVoiceView extends RecyclerView {

    private static final int HOLDER = 10;
    private static final int FOOTER = 11;

    private List<MultimediaVoiceEntity> dataSource;
    private MultimediaVoiceAdapter mAdapter;

    private boolean isDarkTheme;

    private OnMultimediaVoiceClickListener clickListener;

    public MultimediaVoiceView(@NonNull Context context) {
        super(context);
        initView();
    }

    public MultimediaVoiceView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MultimediaVoiceView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     *
     */
    private void initView(){
        setOverScrollMode(OVER_SCROLL_NEVER);
        setLayoutManager(new LinearLayoutManager(getContext()));
        addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 5;
            }
        });
        mAdapter = new MultimediaVoiceAdapter();
        setAdapter(mAdapter);
    }

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }

    /**
     *
     * @param clickListener
     */
    public void setClickListener(OnMultimediaVoiceClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     *
     * @param data
     */
    public void setDataSource(List<MultimediaVoiceEntity> data) {
        if (data == null)
            return;
        dataSource = data;
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
     */
    public void clear(){
        if (dataSource == null)
            return;
        dataSource.clear();
        notifyData();
    }

    /**
     *
     */
    public void clearChoose(){
        if (dataSource == null)
            return;
        for (int i = 0; i < dataSource.size(); i++) {
            MultimediaVoiceEntity entity = dataSource.get(i);
            if (entity.isChoose()){
                entity.setChoose(false);
                notifyData(i);
                break;
            }
        }
    }

    /**
     *
     * @param duration
     * @return
     */
    private String formatDurationTime(long duration) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    /**
     *
     */
    class MultimediaVoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == FOOTER){
                View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_voice_footer,parent,false);
                return new MultimediaVoiceFooterHolder(view);
            }else {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.multimedia_voice_holder,parent,false);
                return new MultimediaVoiceViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (getItemViewType(position) == FOOTER){
                //MultimediaVoiceFooterHolder footerHolder = (MultimediaVoiceFooterHolder) holder;
            }else {
                MultimediaVoiceEntity entity = dataSource.get(position);
                MultimediaVoiceViewHolder voiceViewHolder = (MultimediaVoiceViewHolder) holder;

                voiceViewHolder.itemView.setBackgroundColor(isDarkTheme ? getResources().getColor(R.color.multimedia_theme) : getResources().getColor(R.color.multimedia_white_theme));
                voiceViewHolder.nameTextView.setTextColor(isDarkTheme ? getResources().getColor(R.color.white) : getResources().getColor(R.color.multimedia_white_black));
                voiceViewHolder.infoTextView.setTextColor(isDarkTheme ? getResources().getColor(R.color.white) : getResources().getColor(R.color.multimedia_white_black));

                voiceViewHolder.iconImageView.setImageResource(entity.isMusic() ? getMusic() : getVoice());
                voiceViewHolder.chooseImageView.setImageResource(entity.isChoose() ? R.drawable.multimedia_choose_sel : getUn());
                voiceViewHolder.nameTextView.setText(TextUtils.isEmpty(entity.getAuthor()) ? entity.getName() : String.format("%s - %s",entity.getName(),entity.getAuthor()));
                String time = formatDurationTime(entity.getDuration());
                String size = FileUtils.FormetFileSize(entity.getSize());
                voiceViewHolder.infoTextView.setText(String.format("时长: %s , 大小: %s",time,size));

                if (clickListener != null){
                    holder.itemView.setOnClickListener(v -> {
                        clickListener.itemClick(position,entity);
                    });
                }
            }
        }


        @Override
        public int getItemViewType(int position) {
            return position == dataSource.size() ? FOOTER : HOLDER;
        }

        @Override
        public int getItemCount() {
            return dataSource == null ? 1 : dataSource.size()+1;
        }
    }

    /**
     *
     * @return
     */
    private int getMusic(){
        return !isDarkTheme ? R.drawable.multimedia_voice_m_black : R.drawable.multimedia_voice_m;
    }

    /**
     *
     * @return
     */
    private int getVoice(){
        return !isDarkTheme ? R.drawable.multimedia_voice_v_black: R.drawable.multimedia_voice_v;
    }

    /**
     *
     * @return
     */
    private int getUn(){
        return isDarkTheme ? R.drawable.multimedia_choose_un: R.drawable.multimedia_choose_un_black;
    }

    /**
     *
     */
    static class MultimediaVoiceViewHolder extends RecyclerView.ViewHolder{

        ImageView iconImageView;
        TextView nameTextView;
        TextView infoTextView;
        ImageView chooseImageView;

        public MultimediaVoiceViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.multimedia_voice_icon);
            nameTextView = itemView.findViewById(R.id.multimedia_voice_name);
            infoTextView = itemView.findViewById(R.id.multimedia_voice_info);
            chooseImageView = itemView.findViewById(R.id.multimedia_voice_choose);
        }
    }

    /**
     *
     */
    static class MultimediaVoiceFooterHolder extends RecyclerView.ViewHolder {

        public MultimediaVoiceFooterHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     *
     */
    public interface OnMultimediaVoiceClickListener {
        /**
         *
         * @param position
         * @param entity
         */
        void itemClick(int position,MultimediaVoiceEntity entity);
    }
}

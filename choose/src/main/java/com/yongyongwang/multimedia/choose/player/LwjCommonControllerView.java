package com.yongyongwang.multimedia.choose.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.R;

/**
 * @author myselyhero
 * 
 * @desc:
 *
 * @// TODO: 2021/6/7
 */
public class LwjCommonControllerView extends LwjControllerBaseView implements View.OnClickListener {

    private LwjStatusView statusView;
    private LinearLayout bottomBackground;
    private TextView currencyTextView;
    private SeekBar seekBar;
    private TextView totalTextView;
    private ImageView volumeImageView;

    public LwjCommonControllerView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.multimedia_player_controller_layout;
    }

    @Override
    protected void initView() {
        statusView = findViewById(R.id.lwj_common_controller_status);
        bottomBackground = findViewById(R.id.lwj_common_controller_bottom);
        currencyTextView = findViewById(R.id.lwj_common_controller_bottom_current);
        seekBar = findViewById(R.id.lwj_common_controller_bottom_seek_bar);
        totalTextView = findViewById(R.id.lwj_common_controller_bottom_total);
        volumeImageView = findViewById(R.id.lwj_common_controller_bottom_voice);

        seekBar.setEnabled(false);
        volumeImageView.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currencyTextView.setText(longTimeToString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(seekBar.getProgress());
            }
        });

        setMute(isVoice());
    }

    @Override
    protected void onClick(MotionEvent event) {
        startController();
    }

    @Override
    protected void onDblClick(MotionEvent event) {
        startAndStop();
    }

    @Override
    public void bufferUpdate(int buffering) {
        //解决缓冲进度不能100%问题
        if (buffering >= 95) {
            seekBar.setSecondaryProgress((int) getDuration());
        } else {
            seekBar.setSecondaryProgress(buffering * 10);
        }
    }

    @Override
    public void currencyPosition(long position) {
        seekBar.setProgress((int) getCurrentPosition());
        currencyTextView.setText(longTimeToString(getCurrentPosition()));
    }

    @Override
    public void statusListener(LwjStatusEnum statusEnum) {
        switch (statusEnum){
            case STATUS_PLAYING:
                totalTextView.setText(longTimeToString(getDuration()));
                seekBar.setMax((int) getDuration());
                statusView.onIdle();
                break;
            case STATUS_BUFFERING:
                statusView.onLoader();
                break;
            case STATUS_PAUSED:
                statusView.onStop();
                break;
            case STATUS_BUFFEEND:
            case STATUS_IDLE:
            case STATUS_PREPARING:
                statusView.onIdle();
                break;
            case STATUS_COMPLETED:

                break;
            case STATUS_ERROR:
                statusView.onFail();
                break;
        }
    }

    /**
     *
     */
    private void startController(){
        if (bottomBackground.getVisibility() == View.VISIBLE){
            stopController();
        }else {
            showControllerAnim(bottomBackground);
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    stopController();
                }
            };
            mHandler.postDelayed(mRunnable,mControllerTime);
        }
    }

    /**
     *
     */
    private void stopController(){
        if (mHandler != null){
            mHandler.removeCallbacks(mRunnable);
            mRunnable = null;
        }
        hideControllerAnim(bottomBackground);
    }

    /**
     *
     * @param mute
     */
    public void setMute(boolean mute){
        if (setVoice(mute)){
            volumeImageView.setImageResource(R.drawable.multimedia_mute);
        }else {
            volumeImageView.setImageResource(R.drawable.multimedia_volume);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lwj_common_controller_bottom_voice){
            /**
             * 音量开启与关闭
             */
            setMute(!isVoice());
        }
    }
}

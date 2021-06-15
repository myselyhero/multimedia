package com.yongyongwang.multimedia.choose;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.player.LwjCommonControllerView;
import com.yongyongwang.multimedia.choose.player.LwjPlayerView;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/7
 */
public class MultimediaPlayerActivity extends MultimediaBaseActivity implements View.OnClickListener {

    private ImageView backImageView;

    private LwjPlayerView lwjPlayerView;
    private String mPath;

    @Override
    protected int getLayoutId() {
        return R.layout.multimedia_player_activity;
    }

    @Override
    protected void initView() {
        backImageView = findViewById(R.id.multimedia_player_back);
        lwjPlayerView = findViewById(R.id.multimedia_player_lwj);

        backImageView.setOnClickListener(this);

        mPath = getIntent().getStringExtra(MULTIMEDIA_REQUEST_DATA);
        lwjPlayerView.setControllerView(new LwjCommonControllerView(this));
        if (!TextUtils.isEmpty(mPath)){
            lwjPlayerView.setVoice(true);
            lwjPlayerView.setDataSource(mPath);
            lwjPlayerView.setVoice(false);
            lwjPlayerView.onStart();
        }
    }

    @Override
    protected void onUpdatePreview(int num) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.multimedia_player_back){
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        lwjPlayerView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lwjPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lwjPlayerView.onRelease();
    }
}

package com.yongyongwang.multimedia.choose.edit;

import android.view.View;
import android.widget.ImageView;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.edit.view.CropView;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/6/7
 */
public class MultimediaEditActivity extends MultimediaBaseActivity implements View.OnClickListener {

    private ImageView backImageView;

    private CropView cropView;

    private MultimediaEntity mEntity;

    public MultimediaEditActivity() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.multimedia_edit_activity;
    }

    @Override
    protected void initView() {
        backImageView = findViewById(R.id.multimedia_edit_back);
        cropView = findViewById(R.id.multimedia_edit_crop);

        backImageView.setOnClickListener(this);

        mEntity = (MultimediaEntity) getIntent().getSerializableExtra(MULTIMEDIA_REQUEST_DATA);
        if (mEntity != null){
            cropView.setImage(mEntity.getPath());
        }
    }

    @Override
    protected void onUpdatePreview(int num) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.multimedia_edit_back){
            finish();
        }
    }
}

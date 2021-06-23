package com.yongyongwang.multimedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yongyongwang.multimedia.choose.MultimediaBuild;
import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.view.MultimediaChooseView;

public class MainActivity extends AppCompatActivity {

    private MultimediaChooseView chooseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chooseView = findViewById(R.id.main_choose_view);
        chooseView.addChooseListener(new MultimediaChooseView.MultimediaChooseClickListener() {
            @Override
            public void onAdd() {
                MultimediaBuild.create(MainActivity.this)
                        .startCamera(data -> {
                            chooseView.setDataSource(data);
                        });
            }

            @Override
            public void onItemClick(int position, @NonNull MultimediaEntity entity) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK){
            MultimediaEntity entity = (MultimediaEntity) data.getSerializableExtra(MultimediaBaseActivity.MULTIMEDIA_RESULT_DATA);
            if (entity != null){
                Log.e(MainActivity.class.getSimpleName(), "onActivityResult: "+entity.toString());
            }
        }
    }
}
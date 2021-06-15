package com.yongyongwang.multimedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yongyongwang.multimedia.choose.MultimediaBuild;
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
                        .isCamera(true)
                        .isCompress(true)
                        .start(data -> {
                            chooseView.setDataSource(data);
                        });
            }

            @Override
            public void onItemClick(int position, @NonNull MultimediaEntity entity) {

            }
        });
    }
}
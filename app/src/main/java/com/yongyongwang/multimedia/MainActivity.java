package com.yongyongwang.multimedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.yongyongwang.multimedia.choose.MultimediaBuild;
import com.yongyongwang.multimedia.choose.MultimediaEnum;
import com.yongyongwang.multimedia.choose.base.MultimediaBaseActivity;
import com.yongyongwang.multimedia.choose.camera.view.JCameraView;
import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;
import com.yongyongwang.multimedia.choose.view.MultimediaChooseView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MultimediaChooseView chooseView;
    private ItemsListView listView;

    private List<ItemListEntity> dataSource = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chooseView = findViewById(R.id.main_choose_view);
        listView = findViewById(R.id.main_item);

        ItemListEntity entity = new ItemListEntity();
        entity.setItemTips(getString(R.string.photo));
        entity.setChoose(true);
        entity.setData(getItem(entity.getItemTips()));
        dataSource.add(entity);
        entity = new ItemListEntity();
        entity.setItemTips(getString(R.string.camera));
        entity.setData(getItem(entity.getItemTips()));
        dataSource.add(entity);
        entity = new ItemListEntity();
        entity.setItemTips(getString(R.string.player));
        entity.setData(getItem(entity.getItemTips()));
        dataSource.add(entity);
        entity = new ItemListEntity();
        entity.setItemTips(getString(R.string.voice));
        entity.setData(getItem(entity.getItemTips()));
        dataSource.add(entity);
        listView.setDataSource(dataSource);

        String path = "/storage/emulated/0/Movies/QQ??????_370a64196c2cffb6ae876a77ebc5dbe51610300661.mp4";

        chooseView.addChooseListener(new MultimediaChooseView.MultimediaChooseClickListener() {
            @Override
            public void onAdd() {
                if (getType() == 1){
                    MultimediaBuild.create(MainActivity.this)
                            .cameraType(getCamera())//???????????????
                            .recordMaxDuration(getData(getString(R.string.came_record_max_duration),10000))//?????????????????????
                            .recordMinDuration(getData(getString(R.string.came_record_min_duration),3000))//?????????????????????
                            //.leftIcon()//??????????????????????????????????????????
                            //.leftIconClickListener()
                            //.rightIcon()
                            //.rightIconClickListener()
                            .startCamera(data -> {
                                chooseView.setDataSource(data);
                            });
                }else if (getType() == 2){
                    MultimediaBuild.create(MainActivity.this)
                            .mute(get(getString(R.string.player_mute)))//????????????
                            .isLoop(get(getString(R.string.player_loop)))//??????
                            .autoPlayer(get(getString(R.string.player_auto)))//????????????
                            .startPlayer(path);
                }else if (getType() == 3){
                    MultimediaBuild.create(MainActivity.this)
                            .isOnly(get(getString(R.string.photo_only)))//????????????
                            .darkTheme(get(getString(R.string.dark_theme)))
                            .maxNum(getData(getString(R.string.photo_max_num),6))//??????????????????
                            .minDuration(getData(getString(R.string.photo_min_duration),5000))//????????????????????????(5000)
                            .maxDuration(getData(getString(R.string.photo_max_duration),60 * 1000))//????????????????????????(60 * 1000)
                            .startVoice(list -> {

                            });
                }else {
                    MultimediaBuild.create(MainActivity.this)
                            .setMultimediaType(get())//????????????
                            .isOnly(get(getString(R.string.photo_only)))//????????????
                            .darkTheme(get(getString(R.string.dark_theme)))
                            .chooseData(chooseView.getDataSource())
                            .isOnlyPreview(get(getString(R.string.photo_only_preview)))//?????????????????????????????????????????????????????????
                            .isShade(get(getString(R.string.photo_shade)))//?????????????????????????????????
                            .maxNum(getData(getString(R.string.photo_max_num),6))//??????????????????
                            .minNum(getData(getString(R.string.photo_min_num),0))//?????????????????????????????????
                            .mixture(get(getString(R.string.photo_mixture)))//????????????(??????/??????)
                            .spanCount(getSpanCount())//???????????????????????????
                            .maxSize(getData(getString(R.string.photo_max_size),10))//???????????????????????????(MB)
                            .isCrop(get(getString(R.string.photo_crop)))//????????????
                            .isCamera(get(getString(R.string.photo_camera)))//????????????
                            .minDuration(getData(getString(R.string.photo_min_duration),5000))//????????????????????????(5000)
                            .maxDuration(getData(getString(R.string.photo_max_duration),60 * 1000))//????????????????????????(60 * 1000)
                            .isGif(get(getString(R.string.photo_gif)))//????????????gif
                            //.dir() //???????????????/???????????????????????????
                            //.confirmText() //????????????
                            //.confirmTextColor()
                            //.confirmDrawable()
                            /* ??????????????? */
                            .mute(get(getString(R.string.player_mute)))//????????????
                            .isLoop(get(getString(R.string.player_loop)))//??????
                            .autoPlayer(get(getString(R.string.player_auto)))//????????????
                            /* ????????? */
                            .cameraType(getCamera())//???????????????
                            .recordMaxDuration(getData(getString(R.string.came_record_max_duration),10000))//?????????????????????
                            .recordMinDuration(getData(getString(R.string.came_record_min_duration),3000))//?????????????????????
                            //.leftIcon()//??????????????????????????????????????????
                            //.leftIconClickListener()
                            //.rightIcon()
                            //.rightIconClickListener()
                            .start(data -> {
                                chooseView.setDataSource(data);
                            });
                }
            }

            @Override
            public void onItemClick(int position, @NonNull MultimediaEntity entity) {

            }
        });
    }

    /**
     *
     * @return
     */
    private int getType(){
        ItemListEntity entity = null;
        for (ItemListEntity e : dataSource) {
            if (e.isChoose()){
                entity = e;
                break;
            }
        }

        if (entity == null)
            return 0;
        if (TextUtils.equals(entity.getItemTips(),getString(R.string.camera)))
            return 1;
        else if (TextUtils.equals(entity.getItemTips(),getString(R.string.player)))
            return 2;
        else if (TextUtils.equals(entity.getItemTips(),getString(R.string.voice)))
            return 3;
        return 0;
    }

    /**
     *
     * @return
     */
    private MultimediaEnum get(){
        boolean flag = get(getString(R.string.photo_img));
        if (flag)
            return MultimediaEnum.IMAGE;
        flag = get(getString(R.string.photo_video));
        return flag? MultimediaEnum.VIDEO : MultimediaEnum.All;
    }

    /**
     *
     * @return
     */
    private int getCamera(){
        int i = getData(getString(R.string.camera_picture),JCameraView.BUTTON_STATE_BOTH);
        if (i > 0)
            return i;
        i = getData(getString(R.string.came_record),JCameraView.BUTTON_STATE_BOTH);
        if (i > 0)
            return i;
        i = JCameraView.BUTTON_STATE_BOTH;
        return i;
    }

    /**
     *
     * @return
     */
    private int getSpanCount(){
        int i = getData(getString(R.string.photo_span_count_three),-1);
        if (i <= 0)
            i = getData(getString(R.string.photo_span_count_four),4);
        return i;
    }

    /**
     *
     * @param arg
     * @return
     */
    private boolean get(String arg){
        for (ItemListEntity entity : dataSource) {
            for (ItemEntity e: entity.getData()) {
                if (TextUtils.equals(e.getName(),arg))
                    return e.isChoose();
            }
        }
        return false;
    }

    /**
     *
     * @param arg
     * @param value
     * @return
     */
    private int getData(String arg,int value){
        for (ItemListEntity entity : dataSource) {
            for (ItemEntity e: entity.getData()) {
                if (TextUtils.equals(e.getName(),arg) && e.isChoose())
                    return e.getData();
            }
        }
        return value;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK){
            MultimediaEntity entity = (MultimediaEntity) data.getSerializableExtra(MultimediaBaseActivity.RESULT_DATA);
            if (entity != null){
                Log.e(MainActivity.class.getSimpleName(), "onActivityResult: "+entity.toString());
            }
        }
    }

    /**
     *
     * @param arg
     * @return
     */
    private List<ItemEntity> getItem(String arg){
        List<ItemEntity> list = new ArrayList<>();
        if (TextUtils.equals(arg,getString(R.string.photo))){
            list.add(new ItemEntity(getString(R.string.photo_all),true));
            list.add(new ItemEntity(getString(R.string.photo_img)));
            list.add(new ItemEntity(getString(R.string.photo_video)));
            list.add(new ItemEntity(getString(R.string.dark_theme)));
            list.add(new ItemEntity(getString(R.string.photo_only)));
            list.add(new ItemEntity(getString(R.string.photo_only_preview)));
            list.add(new ItemEntity(getString(R.string.photo_shade)));
            list.add(new ItemEntity(getString(R.string.photo_min_num),3));
            list.add(new ItemEntity(getString(R.string.photo_max_num),9));
            list.add(new ItemEntity(getString(R.string.photo_mixture)));
            list.add(new ItemEntity(getString(R.string.photo_span_count_three),3));
            list.add(new ItemEntity(getString(R.string.photo_span_count_four),4));
            list.add(new ItemEntity(getString(R.string.photo_max_size)));
            list.add(new ItemEntity(getString(R.string.photo_crop)));
            list.add(new ItemEntity(getString(R.string.photo_camera)));
            list.add(new ItemEntity(getString(R.string.photo_min_duration),5000));
            list.add(new ItemEntity(getString(R.string.photo_max_duration),20000));
            list.add(new ItemEntity(getString(R.string.photo_gif)));
        }else if (TextUtils.equals(arg,getString(R.string.camera))){
            list.add(new ItemEntity(getString(R.string.camera_all),JCameraView.BUTTON_STATE_BOTH));
            list.add(new ItemEntity(getString(R.string.camera_picture),JCameraView.BUTTON_STATE_ONLY_CAPTURE));
            list.add(new ItemEntity(getString(R.string.came_record),JCameraView.BUTTON_STATE_ONLY_RECORDER));
            list.add(new ItemEntity(getString(R.string.came_record_max_duration),60 * 1000));
            list.add(new ItemEntity(getString(R.string.came_record_min_duration),5000));
        }else if (TextUtils.equals(arg,getString(R.string.player))){
            list.add(new ItemEntity(getString(R.string.player_auto)));
            list.add(new ItemEntity(getString(R.string.player_mute)));
            list.add(new ItemEntity(getString(R.string.player_loop)));
        }else {
            list.add(new ItemEntity(getString(R.string.dark_theme)));
            list.add(new ItemEntity(getString(R.string.photo_only)));
            list.add(new ItemEntity(getString(R.string.photo_max_num),9));
            list.add(new ItemEntity(getString(R.string.photo_min_duration),5000));
            list.add(new ItemEntity(getString(R.string.photo_max_duration),20000));
        }
        return list;
    }
}
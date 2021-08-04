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

        String path = "/storage/emulated/0/Movies/QQ视频_370a64196c2cffb6ae876a77ebc5dbe51610300661.mp4";

        chooseView.addChooseListener(new MultimediaChooseView.MultimediaChooseClickListener() {
            @Override
            public void onAdd() {
                if (getType() == 1){
                    MultimediaBuild.create(MainActivity.this)
                            .cameraType(getCamera())//摄像机类型
                            .recordMaxDuration(getData(getString(R.string.came_record_max_duration),10000))//录制的最大时长
                            .recordMinDuration(getData(getString(R.string.came_record_min_duration),3000))//录制的最小时长
                            //.leftIcon()//拍摄页面自定义左右按钮及监听
                            //.leftIconClickListener()
                            //.rightIcon()
                            //.rightIconClickListener()
                            .startCamera(data -> {
                                chooseView.setDataSource(data);
                            });
                }else if (getType() == 2){
                    MultimediaBuild.create(MainActivity.this)
                            .mute(get(getString(R.string.player_mute)))//静音播放
                            .isLoop(get(getString(R.string.player_loop)))//循环
                            .autoPlayer(get(getString(R.string.player_auto)))//自动播放
                            .startPlayer(path);
                }else if (getType() == 3){
                    MultimediaBuild.create(MainActivity.this)
                            .isOnly(get(getString(R.string.photo_only)))//是否单选
                            .darkTheme(get(getString(R.string.dark_theme)))
                            .maxNum(getData(getString(R.string.photo_max_num),6))//最大可选数量
                            .minDuration(getData(getString(R.string.photo_min_duration),5000))//过滤小于该值视频(5000)
                            .maxDuration(getData(getString(R.string.photo_max_duration),60 * 1000))//过滤大于该值视频(60 * 1000)
                            .startVoice(list -> {

                            });
                }else {
                    MultimediaBuild.create(MainActivity.this)
                            .setMultimediaType(get())//选择类型
                            .isOnly(get(getString(R.string.photo_only)))//是否单选
                            .darkTheme(get(getString(R.string.dark_theme)))
                            .chooseData(chooseView.getDataSource())
                            .isOnlyPreview(get(getString(R.string.photo_only_preview)))//单选模式下预览页面是否需要已选列表展示
                            .isShade(get(getString(R.string.photo_shade)))//选中后是否需要阴影效果
                            .maxNum(getData(getString(R.string.photo_max_num),6))//最大可选数量
                            .minNum(getData(getString(R.string.photo_min_num),0))//设置图片的最小选择数量
                            .mixture(get(getString(R.string.photo_mixture)))//混合选择(图片/视频)
                            .spanCount(getSpanCount())//设置多媒体展示列数
                            .maxSize(getData(getString(R.string.photo_max_size),10))//过滤图片的最大大小(MB)
                            .isCrop(get(getString(R.string.photo_crop)))//是否剪切
                            .isCamera(get(getString(R.string.photo_camera)))//是否压缩
                            .minDuration(getData(getString(R.string.photo_min_duration),5000))//过滤小于该值视频(5000)
                            .maxDuration(getData(getString(R.string.photo_max_duration),60 * 1000))//过滤大于该值视频(60 * 1000)
                            .isGif(get(getString(R.string.photo_gif)))//是否显示gif
                            //.dir() //产生的图片/视频自定义保存地址
                            //.confirmText() //确认按钮
                            //.confirmTextColor()
                            //.confirmDrawable()
                            /* 播放器相关 */
                            .mute(get(getString(R.string.player_mute)))//静音播放
                            .isLoop(get(getString(R.string.player_loop)))//循环
                            .autoPlayer(get(getString(R.string.player_auto)))//自动播放
                            /* 摄像机 */
                            .cameraType(getCamera())//摄像机类型
                            .recordMaxDuration(getData(getString(R.string.came_record_max_duration),10000))//录制的最大时长
                            .recordMinDuration(getData(getString(R.string.came_record_min_duration),3000))//录制的最小时长
                            //.leftIcon()//拍摄页面自定义左右按钮及监听
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
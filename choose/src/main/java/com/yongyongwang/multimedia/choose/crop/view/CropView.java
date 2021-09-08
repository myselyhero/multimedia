package com.yongyongwang.multimedia.choose.crop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyongwang.multimedia.choose.R;
import com.yongyongwang.multimedia.choose.crop.exceptions.EditActionException;
import com.yongyongwang.multimedia.choose.crop.handler.EditActionFactory;
import com.yongyongwang.multimedia.choose.crop.handler.ImageEditorImp;
import com.yongyongwang.multimedia.choose.crop.handler.ImageEditorProxy;
import com.yongyongwang.multimedia.choose.crop.strategy.IEditAction;
import com.yongyongwang.multimedia.choose.crop.strategy.action.BatchEditAction;
import com.yongyongwang.multimedia.choose.crop.strategy.action.StateAction;
import com.yongyongwang.multimedia.choose.crop.strategy.entity.AdjustStrategyParams;
import com.yongyongwang.multimedia.choose.crop.strategy.entity.AdjustType;
import com.yongyongwang.multimedia.choose.crop.strategy.imp.BrightnessStrategy;
import com.yongyongwang.multimedia.choose.crop.strategy.imp.ContrastStrategy;
import com.yongyongwang.multimedia.choose.crop.strategy.imp.CropEditStrategy;
import com.yongyongwang.multimedia.choose.crop.strategy.imp.SaturationStrategy;
import com.yongyongwang.multimedia.choose.entity.MultimediaCropItemEntity;

import java.util.Observable;
import java.util.Observer;

/**
 * @author myselyhero 
 * 
 * @desc:
 * 
 * @// TODO: 2021/8/16
 */
public class CropView extends FrameLayout implements Observer, View.OnClickListener {

    private static final String TAG = "CropView";

    private static final int CROP_RATION = 0;
    private static final int CROP_ABJUST = 1;

    private TextView editTextView,abjustTextView;
    private ImageView editImageView,abjustImageView;
    private CropItemView ratioView;
    private CropItemView abjustView;
    private SlideStepBar stepBar;

    private int oldPosition = -1;

    private ImageEditView editView;
    private CropPicker cropPicker;
    private BackGroundLayer backgroundLayer;

    private ImageEditorProxy imageEditorProxy;
    private ImageEditorImp imageEditorImp;

    private MultimediaCropItemEntity cropItemEntity;

    private Bitmap preview;
    private Bitmap tempOriginalImage;
    private boolean alreadyAdjust = false;

    public CropView(@NonNull Context context) {
        super(context);
        init();
    }

    public CropView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CropView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.multimedia_crop_view,this);
        LinearLayout editBackground = findViewById(R.id.crop_edit_background);
        LinearLayout abjustBackground = findViewById(R.id.crop_abjust_background);
        editTextView = findViewById(R.id.crop_edit_tv);
        abjustTextView = findViewById(R.id.crop_abjust_tv);
        editImageView = findViewById(R.id.crop_edit_iv);
        abjustImageView = findViewById(R.id.crop_abjust_iv);
        ratioView = findViewById(R.id.crop_ratio);
        abjustView = findViewById(R.id.crop_abjust);
        stepBar = findViewById(R.id.crop_bar);
        editView = findViewById(R.id.crop_edit);

        ratioView.setItemListener((position, entity) -> {
            switch (position){
                case 0:
                    cropPicker.resetHighlightRectangle();
                    break;
                case 1:
                    cropPicker.setAspectRatio(CropPicker.SQUARE);
                    break;
                case 2:
                    cropPicker.setAspectRatio(CropPicker.SIXTEEN_NINE);
                    break;
                case 3:
                    cropPicker.setAspectRatio(CropPicker.NINE_SIXTEEN);
                    break;
            }
        });

        abjustView.setItemListener((position, entity) -> {
            cropItemEntity = entity;
            stepBar.setMode(entity.getType().getMode());
            stepBar.setProgressValue(entity.getProgress());
        });

        stepBar.setOnSlideBarChangeListener(new SlideStepBar.OnSlideBarChangeListener() {
            @Override
            public void onProgressChanged(SlideStepBar slideStepBar) {
                alreadyAdjust = true;
                try {
                    IEditAction action = getAdjustAction();
                    imageEditorProxy.applyAction(action);
                }catch (EditActionException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SlideStepBar slideStepBar) {

            }

            @Override
            public void onStopTrackingTouch(SlideStepBar slideStepBar) {

            }

            @Override
            public void longPressedStateChanged(boolean isLongPressed) {

            }
        });

        editBackground.setOnClickListener(this);
        abjustBackground.setOnClickListener(this);

        switchFunction(CROP_RATION);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.crop_edit_background){
            switchFunction(CROP_RATION);
        }else if (v.getId() == R.id.crop_abjust_background){
            switchFunction(CROP_ABJUST);
        }
    }

    /**
     *
     * @param position
     */
    private void switchFunction(int position){
        if (oldPosition == position)
            return;
        oldPosition = position;

        if (oldPosition == CROP_RATION){
            abjustImageView.setImageResource(R.drawable.crop_adjust_normal);
            abjustTextView.setTextColor(getResources().getColor(R.color.white));
            editImageView.setImageResource(R.drawable.crop_edit_press);
            editTextView.setTextColor(getResources().getColor(R.color.multimedia_theme_bright));

            abjustView.setVisibility(GONE);
            stepBar.setVisibility(GONE);
            ratioView.setVisibility(VISIBLE);
            if (cropPicker != null)
                cropPicker.setVisibility(VISIBLE);
        }else {
            editImageView.setImageResource(R.drawable.crop_edit_normal);
            editTextView.setTextColor(getResources().getColor(R.color.white));
            abjustImageView.setImageResource(R.drawable.crop_adjust_press);
            abjustTextView.setTextColor(getResources().getColor(R.color.multimedia_theme_bright));

            crop();

            try {
                IEditAction adjustAction = getAdjustAction();
                if (adjustAction != null) {
                    tempOriginalImage = preview;
                }
            } catch (EditActionException e) {

            }

            if (cropPicker != null)
                cropPicker.setVisibility(GONE);
            ratioView.setVisibility(GONE);
            abjustView.setVisibility(VISIBLE);
            stepBar.setVisibility(VISIBLE);
        }
    }

    /**
     *
     * @param path
     */
    private void initEdit(String path){
        editView.setAdapter(new ImageEditView.EditorAdapter() {
            @Override
            protected ImageEditorProxy getImageEditorProxy() {
                if (imageEditorProxy == null) {
                    try {
                        imageEditorProxy = loadImageEditor(path);
                    } catch (EditActionException e) {
                        e.printStackTrace();
                    }
                }
                return imageEditorProxy;
            }

            @Override
            protected BackGroundLayer getBackgroundView(Context ctx, AttributeSet attrs) {
                backgroundLayer = new BackGroundLayer(ctx, attrs);
                return backgroundLayer;
            }

            @Override
            protected ImagePicker getImagePicker(Context ctx, AttributeSet attrs) {
                cropPicker = new CropPicker(ctx, attrs);
                return cropPicker;
            }
        });
    }

    /**
     *
     * @param imgFilePath
     * @return
     * @throws EditActionException
     */
    private ImageEditorProxy loadImageEditor(String imgFilePath) throws EditActionException {
        ImageEditorProxy imageEditProxy = createImageEditProxy(imgFilePath);
        imageEditProxy.export();
        return imageEditProxy;
    }

    /**
     *
     * @param imgFilePath
     * @return
     * @throws EditActionException
     */
    private ImageEditorProxy createImageEditProxy(String imgFilePath) throws EditActionException {
        imageEditorImp = new ImageEditorImp(imgFilePath);

        // Add ImageEdit strategy here
        imageEditorImp.addEditStrategy(new CropEditStrategy());
        imageEditorImp.addEditStrategy(new BrightnessStrategy());
        imageEditorImp.addEditStrategy(new SaturationStrategy());
        imageEditorImp.addEditStrategy(new ContrastStrategy());

        // Bind image editor to proxy
        final ImageEditorProxy editorProxy = new ImageEditorProxy(imageEditorImp);
        // Observe proxy
        editorProxy.addObserver(this);
        return editorProxy;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof EditActionException) {
            ((EditActionException) arg).printStackTrace();
        } else if (arg instanceof ImageEditorProxy.EditorState) {
            final ImageEditorProxy.EditorState editorState = (ImageEditorProxy.EditorState) arg;
            preview = editorState.previewPixelMap;
            // Reset the edit state after redo and undo
            if (editorState.action.getActionType() == IEditAction.ActionType.ACT_UNDO || editorState.action.getActionType() == IEditAction.ActionType.ACT_REDO) {
                alreadyAdjust = false;
            }
            if (!alreadyAdjust) {
                // adjust and crop option after reset tempOriginalImage
                tempOriginalImage = preview;
            }
            backgroundLayer.setPreviewImage(preview);
        }
    }

    /**
     *
     * @return
     * @throws EditActionException
     */
    private IEditAction getAdjustAction() throws EditActionException {
        BatchEditAction batchEditAction = new BatchEditAction();
        if (imageEditorProxy.isUndoable() && alreadyAdjust) {
            batchEditAction.appendEditAction(getUndoAction());
        }
        // get adjust num
        int adjustNum = stepBar.getProgress();
        if (cropItemEntity != null){
            cropItemEntity.setProgress(adjustNum);
            if ((cropItemEntity.getType().getMode() == SlideStepBar.Mode.SHARPEN_MODE && adjustNum != 0) ||
                    (cropItemEntity.getType().getMode() == SlideStepBar.Mode.ADJUST_MODE && adjustNum != SlideStepBar.MAX_LEVEL)) {
                String strategyName = cropItemEntity.getType().getName();
                IEditAction editAction = EditActionFactory.createAction(strategyName);
                float progress = (adjustNum * 1.0f) /
                        (cropItemEntity.getType().getMode() == SlideStepBar.Mode.NORMAL_MODE ?
                                SlideStepBar.MAX_LEVEL : SlideStepBar.MAX_LEVEL * 2);
                AdjustStrategyParams strategyParams = new AdjustStrategyParams(progress);
                // set first PixelMap origin
                strategyParams.setSrcPixelMap(tempOriginalImage);
                editAction.setParams(strategyParams);
                batchEditAction.appendEditAction(editAction);
            }
        }
        return batchEditAction;
    }

    /**
     *
     * @return
     */
    private IEditAction getUndoAction() {
        StateAction stateAction = new StateAction(IEditAction.ActionType.ACT_UNDO);
        stateAction.setParams(imageEditorImp);
        return stateAction;
    }

    /**
     *
     * @return
     */
    private IEditAction getRedoAction() {
        StateAction stateAction = new StateAction(IEditAction.ActionType.ACT_REDO);
        stateAction.setParams(imageEditorImp);
        return stateAction;
    }

    /* 对外api */

    /**
     *
     * @param path
     */
    public void setDataSource(String path){
        if (TextUtils.isEmpty(path))
            return;
        initEdit(path);
    }

    /**
     *
     * @return
     */
    public Bitmap getBitmap() {
        return preview;
    }

    /**
     *
     * @return
     */
    public boolean isCrop(){
        if (cropPicker.getVisibility() != VISIBLE)
            return false;
        Rect contentRect = backgroundLayer.getContentRect();
        Rect cropRect = editView.getEditRect();
        return cropRect.width() < contentRect.width() || cropRect.height() < contentRect.height();
    }

    /**
     *
     * @return
     */
    public Rect getCropRect(){
        return editView.getEditRect();
    }

    /**
     *
     * @return
     */
    public void crop(){
        if (imageEditorProxy == null)
            return;
        try {
            IEditAction cropAction = EditActionFactory.createAction("CropImage");
            cropAction.setParams(new Object(){
                public Rect cropRect = editView.getEditRect();
            });
            imageEditorProxy.applyAction(cropAction);
        } catch (EditActionException e) {
            e.printStackTrace();
        }
    }
}

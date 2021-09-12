package com.zcgc.loveu.activity;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.kyleduo.switchbutton.SwitchButton;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.zcgc.loveu.R;
import com.zcgc.loveu.engine.GlideEngine;
import com.zcgc.loveu.utils.DialogUtils;
import com.zcgc.loveu.utils.GlideImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.bigkoo.pickerview.configure.PickerOptions.TYPE_PICKER_OPTIONS;
import static com.bigkoo.pickerview.configure.PickerOptions.TYPE_PICKER_TIME;

public class AddMemoryActivity extends AppCompatActivity implements View.OnClickListener {
    private ConstraintLayout mCLAddViewContainer;
    private ImageView mAddViewClose;
    private ConstraintLayout mAddViewDate;
    private TimePickerView ptView;
    private String dateString;
    private TextView mTVDate;
    private Date memoryDate = new Date();
    private ConstraintLayout mClAddViewRepeat;
    private TextView mTVRepeat;
    private int OPTIONS1 = 0;
    private ConstraintLayout mCLAddViewRemindTime;
    private TextView mTVRemindTime;
    private ConstraintLayout mCLAddViewCare;
    private ConstraintLayout mCLAddViewBG;
    private ConstraintLayout mCLAddViewIfRemind;
    private SwitchButton mSBIfRemind,mSBCare;
    private boolean ifRemind;
    private boolean mostCare;
    private SwitchButton mSBBG;
    private ImageView mIVAddViewBG;
    private boolean canChooseImage;
    private EditText mETAddContent;
    private TextView mTVContentLength;
    private ConstraintLayout mPuppet0;
    private LocalMedia localMedia;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_memory);
        initAddUI();
        initData();

    }

    private void initData() {
        dateString= new SimpleDateFormat("yyyy-MM-dd").format(memoryDate)
                +" "+new SimpleDateFormat("E").format(memoryDate);
        mTVDate.setText(dateString);
    }

    private void initAddUI() {
        mCLAddViewContainer = findViewById(R.id.view_add_memory);
        mAddViewClose = findViewById(R.id.iv_add_memory_close);
        mAddViewDate = findViewById(R.id.cl_add_memory_date);
        mTVDate = findViewById(R.id.tv_add_view_date);
        mClAddViewRepeat = findViewById(R.id.cl_add_memory_repeat);
        mTVRepeat = findViewById(R.id.tv_add_view_repeat);
        mCLAddViewRemindTime = findViewById(R.id.cl_add_memory_remind_time);
        mTVRemindTime = findViewById(R.id.tv_add_view_remind_time);
        mCLAddViewCare = findViewById(R.id.cl_add_memory_care);
        mCLAddViewBG = findViewById(R.id.cl_add_memory_bg);
        mCLAddViewIfRemind = findViewById(R.id.cl_add_memory_if_remind);
        mSBIfRemind = findViewById(R.id.switch_button_if_remind);
        mSBCare = findViewById(R.id.switch_button_care);
        mSBBG = findViewById(R.id.switch_button_bg);
        mIVAddViewBG = findViewById(R.id.iv_add_memory_view_bg);
        mETAddContent = findViewById(R.id.et_add_memory_content);
        mTVContentLength = findViewById(R.id.tv_content_length);
        mPuppet0 = findViewById(R.id.mPuppet0);
        mETAddContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mETAddContent.getText().length() == 144){
                    mTVContentLength.setTextColor(Color.parseColor("#d81e06"));
                }else {
                    mTVContentLength.setTextColor(Color.parseColor("#333333"));
                }
                mTVContentLength.setText(mETAddContent.getText().length()+"/144");
            }
        });
        mSBIfRemind.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ifRemind = isChecked;
        });
        mSBCare.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mostCare = isChecked;
        });
        mSBBG.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked){
                localMedia = null;
                canChooseImage = false;
                GlideImageLoader.displayImage(getApplicationContext(),R.color.transparent,mIVAddViewBG);
            }else {
                canChooseImage = true;
            }
        });
        mAddViewDate.setOnClickListener(this);
        mCLAddViewRemindTime.setOnClickListener(this);
        mClAddViewRepeat.setOnClickListener(this);
        mCLAddViewContainer.setOnClickListener(this);
        mAddViewClose.setOnClickListener(this);
        mCLAddViewCare.setOnClickListener(this);
        mCLAddViewBG.setOnClickListener(this);
        mCLAddViewIfRemind.setOnClickListener(this);
        mCLAddViewContainer.post(new Runnable() {
            @Override
            public void run() {
                startAddViewAnim(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_memory_close:
                startAddViewAnim(false);
                break;
            case R.id.cl_add_memory_date:
                showTimePickerDialog();
                break;
            case R.id.cl_add_memory_repeat:
                showRepeatPickDialog();
                break;
            case R.id.cl_add_memory_remind_time:
                showRemindTimePickDialog();
                break;
            case R.id.cl_add_memory_care:
                mSBCare.setChecked(!mSBCare.isChecked());
                break;
            case R.id.cl_add_memory_bg:
                checkChooseImage();
                break;
            case R.id.cl_add_memory_if_remind:
                mSBIfRemind.setChecked(!mSBIfRemind.isChecked());
                break;
        }
    }
    private void checkChooseImage() {
        if (!canChooseImage){
            return;
        }
        chooseImage();
    }

    private void chooseImage() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // 结果回调
                        localMedia=result.get(0);
                        GlideImageLoader.displayImage(getApplicationContext(),localMedia
                                        .getPath()
                                ,mIVAddViewBG,25,3);
                    }

                    @Override
                    public void onCancel() {
                        // 取消
                    }
                });

    }

    private void showRemindTimePickDialog() {
        PickerOptions pickerOptions=new PickerOptions(TYPE_PICKER_TIME);
        pickerOptions.context=AddMemoryActivity.this;
        pickerOptions.textSizeContent=18;
        pickerOptions.itemsVisibleCount = 5;
        pickerOptions.lineSpacingMultiplier = 3.0f;
        pickerOptions.type =new boolean[]{false, false, false, true, true, false};
        pickerOptions.x_offset_hours= Calendar.getInstance().get(Calendar.HOUR);
        pickerOptions.x_offset_minutes=Calendar.getInstance().get(Calendar.MINUTE);
        pickerOptions.timeSelectListener = (date, v) -> {
            mTVRemindTime.setText(new SimpleDateFormat("HH:mm").format(date));
        };
        DialogUtils.showTimePickView(pickerOptions,"时间(日历提醒开启生效)");
    }

    private void showRepeatPickDialog() {
        PickerOptions pickerOptions=new PickerOptions(TYPE_PICKER_OPTIONS);
        pickerOptions.context=AddMemoryActivity.this;
        pickerOptions.textSizeContent=25;
        pickerOptions.lineSpacingMultiplier = 2.0f;
        pickerOptions.option1 = OPTIONS1;
        ArrayList<String> items=new ArrayList<>();
        items.add("无");
        items.add("每天");
        items.add("每周");
        items.add("每月");
        items.add("每年");
        pickerOptions.optionsSelectListener = (options1, options2, options3, v) ->{
            OPTIONS1 = options1;
            mTVRepeat.setText(items.get(options1));
        };
        DialogUtils.showOptionsPickView(pickerOptions,"重复(日历提醒开启生效)",items);
    }

    private void showTimePickerDialog() {
        boolean[] types=new boolean[]{true, true, true, false, false, false};
        Calendar calendar1=Calendar.getInstance();
        calendar1.set(Calendar.YEAR,calendar1.get(Calendar.YEAR)-30);
        Calendar calendar2=Calendar.getInstance();
        calendar2.set(Calendar.YEAR,calendar2.get(Calendar.YEAR)+30);
        ptView =DialogUtils.showTimePickView(AddMemoryActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        memoryDate = date;
                        dateString= new SimpleDateFormat("yyyy-MM-dd").format(date)
                                +" "+new SimpleDateFormat("E").format(date);
                        mTVDate.setText(dateString);
                        ptView.dismiss();
                    }
                },types,"年","月","日","时","分","秒"
                ,calendar1,calendar2,"选择日期",4);
        ptView.setDate(Calendar.getInstance());
        ptView.show();
    }

    private void startAddViewAnim(boolean isOpen) {
        float hypot = (float) Math.hypot(mCLAddViewContainer.getHeight(), mCLAddViewContainer.getWidth());
        float endRadius = isOpen ? hypot : 0;
        float startRadius = isOpen ? 0 : hypot;

        Animator animator = ViewAnimationUtils.createCircularReveal(
                mPuppet0, 0, 0,
                startRadius,
                endRadius);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isOpen){
                    mPuppet0.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isOpen) {
                    mPuppet0.setVisibility(View.GONE);
                    AddMemoryActivity.this.finish();
                    overridePendingTransition(0,0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    public void onBackPressed() {
        startAddViewAnim(false);
    }
}

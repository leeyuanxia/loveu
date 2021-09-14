package com.zcgc.loveu.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.kyleduo.switchbutton.SwitchButton;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tencent.mmkv.MMKV;
import com.zcgc.loveu.AddMemorySuccessEvent;
import com.zcgc.loveu.R;
import com.zcgc.loveu.database.SqlLiteHelper;
import com.zcgc.loveu.dialog.PromptDialog;
import com.zcgc.loveu.engine.GlideEngine;
import com.zcgc.loveu.manager.ConstantManager;
import com.zcgc.loveu.manager.UserManager;
import com.zcgc.loveu.net.OkhttpUtil;
import com.zcgc.loveu.po.Memory;
import com.zcgc.loveu.utils.CalendarReminderUtils;
import com.zcgc.loveu.utils.DialogUtils;
import com.zcgc.loveu.utils.GlideImageLoader;
import com.zcgc.loveu.utils.KeyboardUtils;
import com.zcgc.loveu.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;

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
    private boolean mostCare = false;
    private SwitchButton mSBBG;
    private ImageView mIVAddViewBG;
    private boolean canChooseImage;
    private EditText mETAddContent;
    private TextView mTVContentLength;
    private ConstraintLayout mPuppet0;
    private LocalMedia localMedia;
    private EditText mETMemoryTitle;
    private PromptDialog ifChangeDialog;
    private ImageView mAddSubmit;
    private SqlLiteHelper mDatabaseHelper;
    private SQLiteDatabase database;
    private ConstraintLayout mCLLoadingView;
    private Memory oldMemory;
    private ArrayList<String> items;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_memory);
        items=new ArrayList<>();
        items.add("无");
        items.add("每天");
        items.add("每周");
        items.add("每月");
        items.add("每年");
        String memoryString=getIntent().getStringExtra("memory");
        if (!"".equals(memoryString)){
            oldMemory = new Gson().fromJson(memoryString,Memory.class);
        }
        mDatabaseHelper = new SqlLiteHelper(getApplicationContext(), ConstantManager.DATABASE_NAME, null, 1);
        database = mDatabaseHelper.getWritableDatabase();
        initAddUI();
        initData();

    }

    private void initData() {
        if (oldMemory ==null) {
            dateString = new SimpleDateFormat("yyyy-MM-dd").format(memoryDate)
                    + " " + new SimpleDateFormat("E").format(memoryDate);
            mTVDate.setText(dateString);
        }else {
            setMemoryData();
        }
    }

    private void setMemoryData() {
        if (!TextUtils.isEmpty(oldMemory.getBg())) {
            mSBBG.setChecked(true);
            GlideImageLoader.displayImage(getApplicationContext(), oldMemory.getBg(), mIVAddViewBG,20,3);
        }
        mETMemoryTitle.setText(oldMemory.getTitle());
        mETAddContent.setText(oldMemory.getContent());
        if (MMKV.defaultMMKV().getInt("most_care_id",-1)==oldMemory.getId()){
            mSBCare.setChecked(true);
        }
        OPTIONS1 = oldMemory.getRepeat();
        mTVRepeat.setText(items.get(OPTIONS1));
        if (CalendarReminderUtils.findCalendarEvent(getApplicationContext(),
                oldMemory.getTitle(),oldMemory.getContent())){
            mSBIfRemind.setChecked(true);
        }
        mTVContentLength.setText(oldMemory.getContent().length()+"/144");
        if (mETAddContent.getText().length() == 144){
            mTVContentLength.setTextColor(Color.parseColor("#d81e06"));
        }else {
            mTVContentLength.setTextColor(Color.parseColor("#333333"));
        }
        mTVDate.setText(new SimpleDateFormat("yyyy-MM-dd EE").format(new Date(oldMemory.getTime())));
        mTVRemindTime.setText(new SimpleDateFormat("HH:mm").format(new Date(oldMemory.getTime())));
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
        mETMemoryTitle = findViewById(R.id.et_add_memory_title);
        mAddSubmit = findViewById(R.id.iv_add_memory_ok);
        mCLLoadingView = findViewById(R.id.cl_loading_view);
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
        mAddSubmit.setOnClickListener(this);
        mCLAddViewContainer.post(new Runnable() {
            @Override
            public void run() {
                startAddViewAnim(true);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
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
            case R.id.iv_add_memory_ok:
                checkAddMemory();
                break;
        }
    }

    private void checkAddMemory() {
        KeyboardUtils.hideKeyboard(AddMemoryActivity.this);
        if (mETMemoryTitle.getText().toString().trim().length() == 0){
            Toast.makeText(getApplicationContext(),"记得需要记得名才能记得哦",Toast.LENGTH_SHORT).show();
            return;
        }
        if (oldMemory!=null) {
            if (mostCare && MMKV.defaultMMKV().getInt("most_care_id", -1)!=oldMemory.getId()){
                showIfChangeDialog();
                return;
            }
        }else {
            if (mostCare && MMKV.defaultMMKV().getInt("most_care_id", -1) != -1) {
                showIfChangeDialog();
                return;
            }
        }
        addMemory();
    }

    private void addMemory() {
        Memory memory =new Memory();
        memory.setAddTime(System.currentTimeMillis());
        if (localMedia!=null){
            memory.setBg(localMedia.getCompressPath());
        }else if (oldMemory!=null){
            memory.setBg(oldMemory.getBg());
        }
        memory.setTime(TimeUtils.getTimeFromDateString(mTVDate.getText().toString().split(" ")[0])+(
                "无".equals( mTVRemindTime.getText().toString())?0:
                        TimeUtils.getTimeFromHAMString(mTVRemindTime.getText().toString())));
        memory.setTitle(mETMemoryTitle.getText().toString());
        memory.setContent(mETAddContent.getText().toString());
        memory.setRepeat(OPTIONS1);
        showLoadingView();
        if (UserManager.getInstance().isLogin()){
            uploadMemory(memory);
        }else {
            saveMemory(memory);
        }
    }

    private void showLoadingView() {
        mCLLoadingView.setVisibility(View.VISIBLE);
    }

    private void saveMemory(Memory memory) {
        long id =mDatabaseHelper.insertMemory(database,memory);
        if (id >0 ){
            if (mostCare){
                MMKV.defaultMMKV().putInt("most_care_id", (int) id);
            }
            EventBus.getDefault().post(new AddMemorySuccessEvent(0));
            new Handler().postDelayed( () -> {
                Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
                checkCalenderPermission(memory);
                startAddViewAnim(false);

            },3000);
        }else {
            new Handler().postDelayed( () -> {
                Toast.makeText(getApplicationContext(),"添加失败",Toast.LENGTH_SHORT).show();
                mCLLoadingView.setVisibility(View.GONE);
                startAddViewAnim(false);
            },3000);
        }
    }

    private void checkCalenderPermission(Memory memory ) {
        XXPermissions.with(AddMemoryActivity.this)
                .permission(Permission.Group.CALENDAR)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            if (ifRemind) {
                                if (oldMemory!=null) {
                                    CalendarReminderUtils.deleteCalendarEvent(getApplicationContext()
                                            ,oldMemory.getTitle(),oldMemory.getContent());
                                }
                                CalendarReminderUtils.addCalendarEvent(getApplicationContext(),
                                        memory.getTitle(), memory.getContent(), memory.getTime(), 0, memory.getRepeat());
                                Toast.makeText(getApplicationContext(), "记得添加事件成功！", Toast.LENGTH_SHORT).show();
                            }else {
                                CalendarReminderUtils.deleteCalendarEvent(getApplicationContext(),
                                        memory.getTitle(), memory.getContent());
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"记得添加事件失败！",Toast.LENGTH_SHORT).show();
                        }
                        mCLLoadingView.setVisibility(View.GONE);
                        startAddViewAnim(false);
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        Toast.makeText(getApplicationContext(),"你禁止了权限，记得将无法为您添加入事件！",Toast.LENGTH_SHORT).show();
                        mCLLoadingView.setVisibility(View.GONE);
                        startAddViewAnim(false);
                    }
                });
    }

    private void uploadMemory(Memory memory) {

    }

    private void showIfChangeDialog() {
        ifChangeDialog = new PromptDialog.Builder(AddMemoryActivity.this)
                .setTitle("记得想告诉你")
                .setContent("已经有个记得被你设置为最关心了呢，是否需要替换成这个")
                .setConfirmStr("好的")
                .setCancelStr("算了吧")
                .setOnDialogButtonClickListener(new PromptDialog.Builder.onDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        ifChangeDialog.dismiss();
                        mSBCare.setChecked(true);
                        addMemory();
                    }

                    @Override
                    public void onCancel() {
                        mSBCare.setChecked(false);
                        ifChangeDialog.dismiss();
                        addMemory();
                    }
                }).build();
        ifChangeDialog.show();
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
                .isCompress(true)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // 结果回调
                        localMedia=result.get(0);
                        GlideImageLoader.displayImage(getApplicationContext(),localMedia
                                        .getCompressPath()
                                ,mIVAddViewBG,20,3);
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
        if (mCLLoadingView.getVisibility() == View.VISIBLE){
            return;
        }
        startAddViewAnim(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        mDatabaseHelper.close();
    }
}

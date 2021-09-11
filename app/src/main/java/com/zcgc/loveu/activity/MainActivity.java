package com.zcgc.loveu.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.kyleduo.switchbutton.SwitchButton;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tencent.mmkv.MMKV;
import com.zcgc.loveu.R;
import com.zcgc.loveu.adapter.MyFragmentPagerAdapter;
import com.zcgc.loveu.engine.GlideEngine;
import com.zcgc.loveu.fragment.CareMemoryFragment;
import com.zcgc.loveu.manager.UserManager;
import com.zcgc.loveu.po.Memory;
import com.zcgc.loveu.utils.DialogUtils;
import com.zcgc.loveu.utils.GlideImageLoader;
import com.zcgc.loveu.utils.PackageInfoUtils;

import org.raphets.roundimageview.RoundImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.bigkoo.pickerview.configure.PickerOptions.TYPE_PICKER_OPTIONS;
import static com.bigkoo.pickerview.configure.PickerOptions.TYPE_PICKER_TIME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundImageView mUserIcon;
    private TextView mNickname, mVersionText, mLoginText;
    private TextView mLoginHint;
    private ConstraintLayout mPrivacySetting, mVersionSetting, mUserInfoSetting;
    private ConstraintLayout mSettingView;
    private boolean isIn=true;
    private int screenWidth;
    private ConstraintLayout mSettingViewClickArea;
    private ConstraintLayout mSettingViewBackground;
    private ConstraintLayout mSettingViewLeft;
    private ArrayList<Memory> memories;
    private Button mBTAddMemory;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter fragmentAdapter;
    private ConstraintLayout mCLAddViewContainer;
    private ImageView mAddViewClose;
    private boolean isExit = false;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initAddUI();
        initData();
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
    }

    private void initData() {
      setLoginStatus();
      initViewpager();
      dateString= new SimpleDateFormat("yyyy-MM-dd").format(memoryDate)
                +" "+new SimpleDateFormat("E").format(memoryDate);
      mTVDate.setText(dateString);
    }

    private void initViewpager() {
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(new CareMemoryFragment());
        FragmentManager fragmentManager=this.getSupportFragmentManager();
        fragmentAdapter=new MyFragmentPagerAdapter(fragmentManager,fragments);
        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setCurrentItem(0);

    }

    private void setLoginStatus() {
        if (!"".equals(MMKV.defaultMMKV().getString("Set-Cookie",""))
                && UserManager.getInstance().isLogin()){
            GlideImageLoader.displayImage(getApplicationContext(),
                    UserManager.getInstance().getUser().getUserIcon(),R.mipmap.icon,mUserIcon);
            mNickname.setText( UserManager.getInstance().getUser().getNickname());
            mLoginHint.setVisibility(View.GONE);
            mUserInfoSetting.setVisibility(View.VISIBLE);
            mLoginText.setText("退出登录");
            mLoginText.setTextColor(getApplicationContext().getResources().getColor(R.color._ff0000));
        }else {
            GlideImageLoader.displayImage(getApplicationContext(),
                   R.mipmap.icon,mUserIcon);
            mNickname.setText(getApplicationContext().getResources().getString(R.string.not_login));
            mLoginHint.setVisibility(View.VISIBLE);
            mUserInfoSetting.setVisibility(View.GONE);
            mLoginText.setText("登录");
            mLoginText.setTextColor(getApplicationContext().getResources().getColor(R.color._0099ff));
        }
    }

    private void initUI() {
        mUserIcon = findViewById(R.id.iv_user_icon);
        mUserIcon.setOnClickListener(this);
        mNickname = findViewById(R.id.tv_nickname);
        mLoginHint = findViewById(R.id.tv_home_login_hint);
        mPrivacySetting = findViewById(R.id.cl_home_setting_privacy);
        mVersionSetting = findViewById(R.id.cl_home_setting_version);
        mUserInfoSetting = findViewById(R.id.cl_home_setting_user_info);
        mSettingViewClickArea = findViewById(R.id.cl_setting_view_click_area);
        mSettingViewClickArea.setOnClickListener(this);
        mSettingViewBackground = findViewById(R.id.cl_setting_view_background);
        mBTAddMemory = findViewById(R.id.bt_bottom_add);
        mBTAddMemory.setOnClickListener(this);
        mSettingViewLeft = findViewById(R.id.cl_setting_view_left);
        mSettingView = findViewById(R.id.cl_setting_view);
        mVersionText = findViewById(R.id.tv_version);
        mLoginText = findViewById(R.id.tv_login);
        mViewPager = findViewById(R.id.vp_memory_home);
        mSettingViewBackground.setVisibility(View.GONE);
        mVersionText.setText(PackageInfoUtils.getVersionName(getApplicationContext()));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_user_icon:
                startSettingViewScorll();
                break;
            case R.id.cl_home_setting_privacy:
                break;
            case R.id.cl_home_setting_version:
                break;
            case R.id.cl_home_setting_user_info:
                break;
            case R.id.cl_setting_view_click_area:
                startSettingViewScorll();
                break;
            case R.id.bt_bottom_add:
                startAddViewAnim(true);
                break;
            case R.id.view_add_memory:
                break;
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
                        LocalMedia localMedia=result.get(0);
                        GlideImageLoader.displayImage(getApplicationContext(),localMedia
                                        .getPath()
                                ,mIVAddViewBG,25,10);
                    }

                    @Override
                    public void onCancel() {
                        // 取消
                    }
                });

    }

    private void showRemindTimePickDialog() {
        PickerOptions pickerOptions=new PickerOptions(TYPE_PICKER_TIME);
        pickerOptions.context=MainActivity.this;
        pickerOptions.textSizeContent=18;
        pickerOptions.itemsVisibleCount = 5;
        pickerOptions.type =new boolean[]{false, false, false, true, true, false};
        pickerOptions.x_offset_hours=Calendar.getInstance().get(Calendar.HOUR);
        pickerOptions.x_offset_minutes=Calendar.getInstance().get(Calendar.MINUTE);
        pickerOptions.timeSelectListener =new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mTVRemindTime.setText(new SimpleDateFormat("HH:mm").format(date));
            }
        };
        DialogUtils.showTimePickView(pickerOptions,"时间(日历提醒开启生效)");
    }

    private void showRepeatPickDialog() {
        PickerOptions pickerOptions=new PickerOptions(TYPE_PICKER_OPTIONS);
        pickerOptions.context=MainActivity.this;
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
        ptView =DialogUtils.showTimePickView(MainActivity.this, new OnTimeSelectListener() {
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
                mCLAddViewContainer, mCLAddViewContainer.getTop(), 0,
                startRadius,
                endRadius);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isOpen){
                    mCLAddViewContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isOpen) {
                    mCLAddViewContainer.setVisibility(View.GONE);
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

    private void startSettingViewScorll() {
        if(mSettingView.getVisibility()==View.GONE){
            mSettingView.setVisibility(View.VISIBLE);
            AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
            alphaAnimation.setDuration(800);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mSettingViewBackground.setVisibility(View.VISIBLE);
                    mSettingViewClickArea.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mSettingViewBackground.clearAnimation();
                    mSettingViewClickArea.setClickable(true);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mSettingViewBackground.startAnimation(alphaAnimation);
           mSettingView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.from_left_in));
           mSettingViewClickArea.startAnimation(new AlphaAnimation(1,0));

        }else {
            mSettingView.setVisibility(View.GONE);
            AlphaAnimation alphaAnimation=new AlphaAnimation(1,0);
            alphaAnimation.setDuration(800);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mSettingViewClickArea.setClickable(false);

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mSettingViewBackground.clearAnimation();
                    mSettingViewBackground.setVisibility(View.GONE);
                    mSettingViewClickArea.setClickable(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mSettingViewBackground.startAnimation(alphaAnimation);
            overridePendingTransition(0,R.anim.to_left_out);
            mSettingView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.to_left_out));
        }
    }

    @Override
    public void onBackPressed() {
        if (mCLAddViewContainer.getVisibility() == View.VISIBLE){
            startAddViewAnim(false);
        }else {
            if (!isExit){
                isExit = true;
                Toast.makeText(getApplicationContext(),"再按一次返回键退出",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> isExit = false,3000);
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
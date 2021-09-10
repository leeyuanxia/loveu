package com.zcgc.loveu.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.tencent.mmkv.MMKV;
import com.zcgc.loveu.R;
import com.zcgc.loveu.manager.UserManager;
import com.zcgc.loveu.utils.GlideImageLoader;
import com.zcgc.loveu.utils.PackageInfoUtils;

import org.raphets.roundimageview.RoundImageView;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

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
    private ConstraintLayout mCLAddMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initData();
    }

    private void initData() {
      setLoginStatus();
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
            mLoginText.setTextColor(getApplicationContext().getResources().getColor(R.color._1296db));
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
        mCLAddMemory = findViewById(R.id.cl_null_memory);
        mCLAddMemory.setOnClickListener(this);
        mSettingViewLeft = findViewById(R.id.cl_setting_view_left);
        mSettingView = findViewById(R.id.cl_setting_view);
        mVersionText = findViewById(R.id.tv_version);
        mLoginText = findViewById(R.id.tv_login);
        mSettingViewBackground.setVisibility(View.GONE);
        mVersionText.setText(PackageInfoUtils.getVersionName(getApplicationContext()));
    }

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
            case R.id.cl_null_memory:
                Log.e("click","click");
                break;
            case R.id.cl_setting_view_click_area:
                startSettingViewScorll();
                break;
        }
    }

    private void startSettingViewScorll() {
        if(mSettingView.getVisibility()==View.GONE){
            mSettingView.setVisibility(View.VISIBLE);
            AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
            alphaAnimation.setDuration(1000);
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
            alphaAnimation.setDuration(1000);
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
}
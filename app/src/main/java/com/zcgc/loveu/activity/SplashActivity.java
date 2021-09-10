package com.zcgc.loveu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import com.zcgc.loveu.R;
import com.zcgc.loveu.manager.UrlManager;
import com.zcgc.loveu.manager.UserManager;
import com.zcgc.loveu.net.CallBackUtil;
import com.zcgc.loveu.net.JsonResult;
import com.zcgc.loveu.net.OkhttpUtil;
import com.zcgc.loveu.po.Memory;
import com.zcgc.loveu.po.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private ConstraintLayout mLoadingView;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MMKV.initialize(this);
        initUI();
        initData();
    }

    private void initData() {
        mLoadingView.postDelayed(() -> runOnUiThread(this::doLogin),3000);
    }

    private void doLogin() {
        OkhttpUtil.okHttpPost(UrlManager.GET_USER, new CallBackUtil() {
            @Override
            public Object onParseResponse(Call call, Response response) {
                try {
                    return new Gson().fromJson(response.body().string(), new TypeToken<JsonResult<User>>(){}.getType());
                } catch (IOException e) {
                    e.printStackTrace();
                    return new JsonResult<User>(JsonResult.FAIL);
                }
            }

            @Override
            public void onFailure(Call call, Exception e) {
                enterMainActivity("");
            }

            @Override
            public void onResponse(Object response) {
                JsonResult<User> jsonResult = (JsonResult<User>) response;
                if (jsonResult.getCode() == JsonResult.OK){
                    User user = jsonResult.getData();
                    if (user != null){
                        UserManager.getInstance().setUser(user);
                        getMemoryFromServer();
                    }
                }
            }
        });
    }

    private void getMemoryFromServer() {
        OkhttpUtil.okHttpPost(UrlManager.GET_MEMORY, new CallBackUtil() {
            @Override
            public Object onParseResponse(Call call, Response response) {
                try {
                    return new Gson().fromJson(response.body().string(), new TypeToken<JsonResult<Memory>>(){}.getType());
                } catch (IOException e) {
                    e.printStackTrace();
                    return new JsonResult<Memory>(JsonResult.FAIL);
                }
            }

            @Override
            public void onFailure(Call call, Exception e) {
                enterMainActivity("");
            }

            @Override
            public void onResponse(Object response) {
                JsonResult<Memory> jsonResult = (JsonResult<Memory>) response;
                if (jsonResult.getCode() == JsonResult.OK ){
                    if (jsonResult.getList() != null ){
                        enterMainActivity(new Gson().toJson(jsonResult.getList()));
                        return;
                    }
                }
                enterMainActivity("");
            }
        });
    }

    private void enterMainActivity(String memoryJson) {
        startActivity(new Intent(SplashActivity.this,MainActivity.class)
                .putExtra("memoryJson",memoryJson));
        finish();
    }

    private void initUI() {
        mLoadingView = findViewById(R.id.cl_loading_view);
        doLoadingAnim();

    }

    private void doLoadingAnim() {
        AlphaAnimation alphaAnimation=new AlphaAnimation(0f,1f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setRepeatCount(0);
        mLoadingView.startAnimation(alphaAnimation);
    }

}

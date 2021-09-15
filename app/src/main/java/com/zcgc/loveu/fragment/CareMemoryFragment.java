package com.zcgc.loveu.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;
import com.zcgc.loveu.AddMemorySuccessEvent;
import com.zcgc.loveu.R;
import com.zcgc.loveu.activity.AddMemoryActivity;
import com.zcgc.loveu.database.SqlLiteHelper;
import com.zcgc.loveu.manager.ConstantManager;
import com.zcgc.loveu.manager.UserManager;
import com.zcgc.loveu.po.Memory;
import com.zcgc.loveu.po.User;
import com.zcgc.loveu.utils.GlideImageLoader;
import com.zcgc.loveu.utils.NoDoubleClickListener;
import com.zcgc.loveu.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class CareMemoryFragment extends Fragment {
    private SqlLiteHelper mDatabaseHelper;
    private SQLiteDatabase database;
    private Memory memory;
    private View view;
    private TextView mDayDistanceHint;
    private TextView mDayDistance;
    private TextView mMemoryTitle;
    private TextView mDayInWeek;
    private TextView mDayDate;
    private TextView mMemoryContent;
    private ImageView mCareBg;
    private ImageView mIVEditMemory;
    private ConstraintLayout mCLTopContent;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_care_memory,container,false);
        initUI();
        initData();
        return view;
    }

    private void initData() {
        mIVEditMemory.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (memory!=null){
                    startActivity(new Intent(getActivity(), AddMemoryActivity.class)
                            .putExtra("memory",new Gson().toJson(memory)));
                }
            }
        });
    }

    private void initUI() {
        mMemoryTitle=view.findViewById(R.id.tv_memory_title);
        mDayDistanceHint=view.findViewById(R.id.tv_day_distance_hint);
        mDayDistance=view.findViewById(R.id.tv_day_distance);
        mDayInWeek=view.findViewById(R.id.text_memory_day_in_week);
        mDayDate=view.findViewById(R.id.text_memory_day_in_year);
        mMemoryContent=view.findViewById(R.id.text_memory_content);
        mCareBg=view.findViewById(R.id.iv_care_bg);
        mIVEditMemory=view.findViewById(R.id.iv_care_edit_memory);
        mCLTopContent=view.findViewById(R.id.cl_memory_top_content);
        if (UserManager.getInstance().isLogin()){

        }else {
            getMostCareFromLocalById();
        }
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mDatabaseHelper = new SqlLiteHelper(getContext(), ConstantManager.DATABASE_NAME, null, 1);
        database = mDatabaseHelper.getWritableDatabase();
    }

    @Subscribe
    public void onAddMemorySuccessEvent(AddMemorySuccessEvent event){
        if (event.getAddType() == 0){
            getMostCareFromLocalById();
        }else {
            getMostCareFromServerById();
        }
    }

    private void getMostCareFromServerById() {
        if (MMKV.defaultMMKV().getInt("most_care_id",-1) == -1){
            return;
        }
    }

    private void getMostCareFromLocalById() {
        int id= MMKV.defaultMMKV().getInt("most_care_id",-1);
        if (id  == -1){
            return;
        }
        memory = mDatabaseHelper.findMemoryById(database,id);
        initMostCareView(memory);
    }

    private void initMostCareView(Memory memory) {
        mMemoryTitle.setText(memory.getTitle());
        mMemoryContent.setText(memory.getContent());
        int timeDistance = TimeUtils.calDayDistanceFromNow( memory.getTime());
        if (timeDistance<0){
            mDayDistanceHint.setText("过了");
            mDayDistance.setText(String.valueOf( Math.abs(timeDistance)));
        }else if (timeDistance == 0){
            mDayDistanceHint.setText("就在");
            mDayDistance.setText("今");
        }else {
            mDayDistanceHint.setText("还差");
            mDayDistance.setText(String.valueOf( Math.abs(timeDistance)));
        }
        mDayDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(memory.getTime())));
        mDayInWeek.setText(new SimpleDateFormat("EE").format(new Date(memory.getTime())));
        if (!TextUtils.isEmpty(memory.getBg())) {
            mCLTopContent.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
            GlideImageLoader.displayImage(getContext(), memory.getBg(), mCareBg,20,3);
        }else {
            mCLTopContent.setBackgroundColor(getContext().getResources().getColor(R.color._0099ff));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        database.close();
        mDatabaseHelper.close();
    }
}

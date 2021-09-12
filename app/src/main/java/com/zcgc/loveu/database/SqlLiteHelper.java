package com.zcgc.loveu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.mmkv.MMKV;
import com.zcgc.loveu.manager.ConstantManager;
import com.zcgc.loveu.po.Memory;

public class SqlLiteHelper extends SQLiteOpenHelper {
    public static final String CREATE_MEMORY_LOCAL = "create table memorylocal(" +
            "id integer primary key autoincrement," +
            "title string," +
            "content string," +
            "time long," +
            "addTime long," +
            "bg string," +
            "repeat integer"+
            ")";
    private SqlLiteHelper(Context context){
        super(context, ConstantManager.DATABASE_NAME,null, 1);

    }
    public SqlLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SqlLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public SqlLiteHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MEMORY_LOCAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertMemory(SQLiteDatabase db, Memory memory){
        ContentValues contentValues=new ContentValues();
        contentValues.put("title",memory.getTitle());
        contentValues.put("content",memory.getContent());
        contentValues.put("time",memory.getTime());
        contentValues.put("addTime",memory.getAddTime());
        contentValues.put("bg",memory.getBg());
        contentValues.put("repeat",memory.getRepeat());
        return db.insert(ConstantManager.MEMORY_LOCAL_TABLE,"",contentValues);
    }
}

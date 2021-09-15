package com.zcgc.loveu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public Memory findMemoryById(SQLiteDatabase db, int id) {
        Cursor cursor = db.query(ConstantManager.MEMORY_LOCAL_TABLE,new String[]{"*"},
                "id =?",new String[]{String.valueOf(id)},null,null,null);
        cursor.moveToFirst();
        Memory memory= new Memory();
        memory.setId(cursor.getInt(cursor.getColumnIndex("id")));
        memory.setRepeat(cursor.getInt(cursor.getColumnIndex("repeat")));
        memory.setContent(cursor.getString(cursor.getColumnIndex("content")));
        memory.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        memory.setTime(cursor.getLong(cursor.getColumnIndex("time")));
        memory.setAddTime(cursor.getLong(cursor.getColumnIndex("addTime")));
        memory.setBg(cursor.getString(cursor.getColumnIndex("bg")));
        return memory;
    }

    public void updateMemory(SQLiteDatabase database, Memory memory) {
        ContentValues contentValues=new ContentValues();
        contentValues.put("title",memory.getTitle());
        contentValues.put("content",memory.getContent());
        contentValues.put("time",memory.getTime());
        contentValues.put("addTime",memory.getAddTime());
        contentValues.put("bg",memory.getBg());
        contentValues.put("repeat",memory.getRepeat());
        database.update(ConstantManager.MEMORY_LOCAL_TABLE,contentValues,
                "id=?",new String[]{String.valueOf(memory.getId())});
    }
}

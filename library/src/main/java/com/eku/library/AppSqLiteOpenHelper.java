package com.eku.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * CODBHelper.java
 * <p/>
 * create by kima.wang on 2016/08/30
 */
public final class AppSqLiteOpenHelper extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 18;
    static final String DATABASE_NAME = "eku_android_client_";
    static volatile AppSqLiteOpenHelper instance;

    private static final String SPLITE_TAG = "_";

    private SqLiteGenerater mGenerater;

    private int mDatabaseOldVersion;

    public static AppSqLiteOpenHelper getInstance() {
        return instance;
    }

    public static void init(Context context) {
        if (null == instance) {
            synchronized (AppSqLiteOpenHelper.class) {
                if (null == instance) {
                    instance = new AppSqLiteOpenHelper(context);
                }
            }
        }
    }

    private AppSqLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mGenerater = new SqLiteGenerater();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //获取用户手机上个版本的版本号
        mDatabaseOldVersion = oldVersion;
    }

    /**
     * 创建数据库表
     *
     * @param tableName 数据库表名称
     * @param ormClass  orm映射对象
     */
    public void createTable(String tableName, Class<?> ormClass) {
        mGenerater.createTableByOrm(getWritableDatabase(), tableName, ormClass);
    }

    /**
     * 更新数据库表字段
     *
     * @param tableName 数据库名称
     * @param ormClass  orm映射对象
     */
    public void upGradeTable(String tableName, Class<?> ormClass) {
        mGenerater.upgradeTableByOrm(getWritableDatabase(), tableName, ormClass);
    }

    /**
     * 清空数据库
     */
    public void truncateTable() {

    }

    /**
     * 删除数据库表
     */
    public void dropTable() {

    }
}

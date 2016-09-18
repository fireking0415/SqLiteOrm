package com.eku.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库创建、数据表创建、删除操作类
 *
 * @author kima.wang
 * @version 0.1
 */
public final class AppSqLiteOpenHelper extends SQLiteOpenHelper {

    /**
     * 数据库当前更迭版本
     */
    static int DATABASE_VERSION = 18;

    /**
     * 数据库名称
     */
    static String DATABASE_NAME = "app.db";

    /**
     * 数据库操作实例
     */
    static volatile AppSqLiteOpenHelper instance;

    /**
     * 数据库解构构建对象
     */
    private SqLiteGenerater mGenerater;

    private int mDatabaseOldVersion;

    public static AppSqLiteOpenHelper getInstance() {
        return instance;
    }

    public static void init(Context _context, int _dbVersion, String _dbName) {
        if (null == instance) {
            synchronized (AppSqLiteOpenHelper.class) {
                if (null == instance) {
                    instance = new AppSqLiteOpenHelper(_context);
                }
            }
        }
        DATABASE_VERSION = _dbVersion;
        DATABASE_NAME = _dbName;
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
     * 删除数据库表
     */
    public void dropTable(String tableName) {
        mGenerater.dropTable(getWritableDatabase(), tableName);
    }
}

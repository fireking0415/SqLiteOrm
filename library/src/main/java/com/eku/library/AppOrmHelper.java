package com.eku.library;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kima.wang on 2016/8/30.
 */
public class AppOrmHelper {

    private volatile static AppOrmHelper instance = null;

    private final byte[] LOCK = new byte[0];

    private static final String TAG = AppOrmHelper.class.getSimpleName();

    private SQLiteOpenHelper mHelper;

    /**
     * 注入数据库操作对象
     *
     * @param helper 数据库操作助手类
     */
    public void registerSqLiteDatabase(SQLiteOpenHelper helper) {
        mHelper = helper;
    }

    /**
     * 取消数据库对象注入
     */
    public void unRegisterSqLiteDatabase() {
        mHelper = null;
    }

    private AppOrmHelper() {
    }

    public static AppOrmHelper getInstance() {
        if (instance == null) {
            synchronized (AppOrmHelper.class) {
                if (instance == null) {
                    instance = new AppOrmHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 插入数据信息
     *
     * @param tableName 数据库表名称
     * @param object    数据库映射对象
     * @return 更新数据量
     */
    public long insertByOrm(String tableName, Object object) {
        ContentValues values = getContentValueByOrm(object);
        synchronized (LOCK) {
            if (mHelper != null) {
                return mHelper.getWritableDatabase().insert(tableName, null, values);
            }
            return -1;
        }
    }

    /**
     * 批量插入数据信息
     *
     * @param tableName  操作数据库名称
     * @param objectList 数据插入对象集合
     * @return 更新数据量
     */
    public long insertBitchByOrm(String tableName, List objectList) {
        List<ContentValues> valuesList = getContentValuesByOrm(objectList);
        synchronized (LOCK) {
            long count = 0;
            if (mHelper != null) {
                mHelper.getWritableDatabase().beginTransaction();
                try {
                    for (int i = 0; i < valuesList.size(); i++) {
                        mHelper.getWritableDatabase().insert(tableName, null, valuesList.get(i));
                    }
                    mHelper.getWritableDatabase().setTransactionSuccessful();
                    count++;
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + ", " + e.getStackTrace().toString());
                } finally {
                    mHelper.getWritableDatabase().endTransaction();
                }
                return count;
            }
        }
        return -1;
    }

    /**
     * 查询数据库
     *
     * @param sql        查询sql
     * @param selectArgs 查询参数
     * @return 数据库游标
     */
    public Cursor getDataInfosByOrm(String sql, String[] selectArgs) {
        if (mHelper != null) {
            Cursor cursor = mHelper.getReadableDatabase().rawQuery(sql, selectArgs);
            return cursor;
        }
        return null;
    }

    /**
     * 查询数据库
     *
     * @param table         数据库操作表
     * @param columns       查询要使用字段
     * @param selection     查询条件字段
     * @param selectionArgs 查询字段内容填充
     * @param groupBy       分组
     * @param having
     * @param orderBy       排序
     * @return 数据库操作游标
     */
    public Cursor getDataInfosByOrm(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        if (mHelper != null) {
            Cursor cursor = mHelper.getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            return cursor;
        }
        return null;
    }


    /**
     * 删除数据信息
     *
     * @return 更新数据量
     */
    public long deleteByOrm(String table, String where, String[] args) {
        synchronized (LOCK) {
            long count = 0;
            if (mHelper != null) {
                mHelper.getWritableDatabase().beginTransaction();
                try {
                    count = mHelper.getWritableDatabase().delete(table, where, args);
                    mHelper.getWritableDatabase().setTransactionSuccessful();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + ", " + e.getStackTrace().toString());
                } finally {
                    mHelper.getWritableDatabase().endTransaction();
                }
                return count;
            }
        }
        return -1;
    }

    /**
     * 更新数据信息
     *
     * @return 更新数据量
     */
    public long updateByOrm(String table, ContentValues contentValues, String where, String[] args) {
        synchronized (LOCK) {
            long count = 0;
            if (mHelper != null) {
                try {
                    mHelper.getWritableDatabase().beginTransaction();
                    count = mHelper.getWritableDatabase().update(table, contentValues, where, args);
                    mHelper.getWritableDatabase().setTransactionSuccessful();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + "," + e.getStackTrace().toString());
                } finally {
                    mHelper.getWritableDatabase().endTransaction();
                }
            }
            return count;
        }
    }

    /**
     * 架构 ContentValue集合
     *
     * @param objectList 对象集合
     * @return 表字段封装集合
     */
    private List<ContentValues> getContentValuesByOrm(List<Object> objectList) {
        List<ContentValues> contentValues = new ArrayList<>();
        for (int i = 0; i < objectList.size(); i++) {
            contentValues.add(getContentValueByOrm(objectList.get(i)));
        }
        return contentValues;
    }

    /**
     * 构造 ContentValue
     *
     * @param object 实体
     * @return 表字段封装
     */
    private ContentValues getContentValueByOrm(Object object) {
        ContentValues resultContentValues = new ContentValues();

        /*--获取指定类中所有添加了SQLiteColumn注解的字段--*/
        List<Field> mAnnotationsField = OrmUtils.getAnnotionsFields(object.getClass(), SQLiteColumn.class);

        /*--遍历集合信息，获取每个字段的值,并构造ContentValues--*/
        for (Field field : mAnnotationsField) {
            SQLiteColumn iField = field.getAnnotation(SQLiteColumn.class);
            if (null == iField) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object content = field.get(object);
                if (null != content) {
                    if (iField.PrimaryKey()) {
                        continue;
                    }
                    resultContentValues.put(iField.ColumnName(), content.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            } finally {
                field.setAccessible(false);
            }
        }
        return resultContentValues;
    }

}

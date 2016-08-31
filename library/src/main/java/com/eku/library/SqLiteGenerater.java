package com.eku.library;

import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by kima.wang on 2016/8/30.
 */
public class SqLiteGenerater {

    /**
     * 创建数据库表前置拼接部分
     */
    private static final String CREATE_TABLE_LABEL = " CREATE TABLE IF NOT EXISTS %s(";

    /**
     * 创建数据库表数据字段拼接部分
     */
    private static final String CREATE_TABLE_FIELD_LABEL = " %s %s %s,";

    /**
     * 创建数据库表结尾拼接部分
     */
    private static final String CREATE_TABLE_END_LABEL = ");";

    /**
     * 创建数据库表解构
     *
     * @param database     数据库操作对象
     * @param tableName    数据库表名称
     * @param declareClass orm映射对象
     */
    public void createTableByOrm(SQLiteDatabase database, String tableName, Class<?> declareClass) {

        StringBuffer sqlString = new StringBuffer();
        sqlString.append(String.format(CREATE_TABLE_LABEL, tableName));

        /*--获取指定类中配置了SQLiteColumn注解的字段集合--*/
        List<Field> mAnnotaionsFields = OrmUtils.getAnnotionsFields(declareClass, SQLiteColumn.class);

        for (Field f : mAnnotaionsFields) {
            SQLiteColumn iField = f.getAnnotation(SQLiteColumn.class);
            if (null != iField) {
                sqlString.append(String.format(CREATE_TABLE_FIELD_LABEL, iField.ColumnName(),
                        iField.ColumnType(), iField.Nullable() ? " null " : " not null"));
            }
        }
        /*--去除最后一个字段的逗号--*/
        String eachSql = sqlString.substring(0, sqlString.length() - 1);
        eachSql += CREATE_TABLE_END_LABEL;
        try {
            database.execSQL(eachSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新数据库表解构
     *
     * @param database     数据库操作对象
     * @param tableName    数据库表名称
     * @param declareClass orm映射对象
     */
    public void upgradeTableByOrm(SQLiteDatabase database, String tableName, Class<?> declareClass) {

    }
}

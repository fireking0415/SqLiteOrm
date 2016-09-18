package com.eku.library;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 数据库数据表创建、删除、修改操作
 *
 * @author kima.wang
 * @version 0.1
 */
public class SqLiteGenerater {

    /**
     * 创建数据库表前置拼接部分
     */
    private static final String CREATE_TABLE_LABEL = " CREATE TABLE IF NOT EXISTS %s(";

    /**
     * 创建数据库表数据字段拼接部分
     */
    private static final String CREATE_TABLE_FIELD_LABEL = " %s %s %s %s,";

    /**
     * 创建数据库表结尾拼接部分
     */
    private static final String CREATE_TABLE_END_LABEL = ");";

    /**
     * 数据库字段不能为空
     */
    private static final String DB_TABLE_NOT_NULL = "not null";

    /**
     * 数据库字段可以为空
     */
    private static final String DB_TABLE_NULL = " null";

    /**
     * 数据库主键
     */
    private static final String DB_TABLE_PRIMARY_KEY = " primary key ";

    /**
     * 创建数据库表解构
     *
     * @param database     数据库操作对象
     * @param tableName    数据库表名称
     * @param declareClass orm映射对象
     */
    public void createTableByOrm(SQLiteDatabase database, String tableName, Class<?> declareClass) {

        StringBuilder sqlString = new StringBuilder("");
        sqlString.append(String.format(CREATE_TABLE_LABEL, tableName));

        /*--获取指定类中配置了SQLiteColumn注解的字段集合--*/
        List<Field> mAnnotaionsFields = OrmUtils.getAnnotionsFields(declareClass, SQLiteColumn.class);

        for (Field f : mAnnotaionsFields) {
            SQLiteColumn iField = f.getAnnotation(SQLiteColumn.class);
            if (null != iField) {
                String temp = String.format(
                        CREATE_TABLE_FIELD_LABEL,
                        iField.ColumnName(),
                        iField.ColumnType(),
                        iField.PrimaryKey() ? DB_TABLE_PRIMARY_KEY + " autoincrement " : "",
                        iField.Nullable() ? DB_TABLE_NULL : DB_TABLE_NOT_NULL);
                sqlString.append(temp);
            }
        }
        /*--去除最后一个字段的逗号--*/
        String eachSql = sqlString.substring(0, sqlString.length() - 1);
        eachSql += CREATE_TABLE_END_LABEL;
        try {
            Log.e("info", "sql->" + eachSql);
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

    /**
     * 删除数据库表
     *
     * @param tableName
     */
    public void dropTable(SQLiteDatabase database, String tableName) {
        database.execSQL("drop table " + tableName);
    }
}

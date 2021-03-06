package com.eku.library;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库关联实体使用注解
 * @author kima.wang
 * @version 0.1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLiteColumn {

    /**
     * 标记数据库字段
     *
     * @return
     */
    String ColumnName();

    /**
     * 标记数据库字段类型
     *
     * @return
     */
    String ColumnType();

    /**
     * 判断数据库字段是否可以为空, 默认可以为空
     *
     * @return
     */
    boolean Nullable() default true;

    /**
     * 设置是否为主键
     *
     * @return
     */
    boolean PrimaryKey() default false;
}

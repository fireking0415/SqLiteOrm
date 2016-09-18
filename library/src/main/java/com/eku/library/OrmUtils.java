package com.eku.library;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * orm操作反射工具类
 *
 * @author kima.wang
 * @version 0.1
 */
public class OrmUtils {

    /**
     * 获取制定类中的所有添加制定Annotation的字段
     *
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static List<Field> getAnnotionsFields(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        Field[] childfields = clazz.getDeclaredFields();
        Field[] superfields = clazz.getSuperclass().getDeclaredFields();

        /*--对该类和其父类的所有字段进行合并操作--*/
        Field[] fields = new Field[childfields.length + superfields.length];
        System.arraycopy(childfields, 0, fields, 0, childfields.length);
        System.arraycopy(superfields, 0, fields, childfields.length, superfields.length);

        final List<Field> annotaionsFields = new ArrayList<>();

        /*--循环获取添加了置顶annotation的字段--*/
        for (Field eachField : fields) {
            Annotation iField = eachField.getAnnotation(annotationClass);
            if (null == iField) {
                continue;
            }
            annotaionsFields.add(eachField);
        }
        return annotaionsFields;
    }
}

package com.lvchehui.www.xiangbc.utils;

/**
 * Created by 张灿能 on 2016/8/25.
 * 作用：
 */

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

/**
 * 获取实体类型的属性名和类型
 *
 * @param model 为实体类
 * @author kou  为传入参数
 */
public class GetModelNameAndType {

    public static HashMap<String, Object> reflect(Object model) {
        HashMap<String, Object> params = null;
        try {
            params = new HashMap<>();
            // 获取实体类的所有属性，返回Field数组
            Field[] field = model.getClass().getDeclaredFields();
            // 获取属性的名字
            String[] modelName = new String[field.length];
            String[] modelType = new String[field.length];
            for (int i = 0; i < field.length; i++) {
                // 获取属性的名字
                String name = field[i].getName();
                modelName[i] = name;
                // 获取属性类型
                String type = field[i].getGenericType().toString();
                modelType[i] = type;

                //关键。。。可访问私有变量
                field[i].setAccessible(true);
                //给属性设置
                //field[i].set(model, field[i].getType().getConstructor(field[i].getType()).newInstance("kou"));

                // 将属性的首字母大写
                name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1)
                        .toUpperCase());

                if (type.equals("class java.lang.String")) {
                    // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = model.getClass().getMethod("get" + name);
                    // 调用getter方法获取属性值
                    String value = (String) m.invoke(model);
                    if (value != null) {

                        System.out.println("attribute value:" + value);
                        params.put(modelName[i], value);
                    }

                }
                if (type.equals("class java.lang.Integer")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(model);
                    if (value != null) {
                        System.out.println("attribute value:" + value);
                        params.put(modelName[i], value);
                    }
                }
                if (type.equals("class java.lang.Short")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Short value = (Short) m.invoke(model);
                    if (value != null) {
                        System.out.println("attribute value:" + value);
                        params.put(modelName[i], value);
                    }
                }
                if (type.equals("class java.lang.Double")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Double value = (Double) m.invoke(model);
                    if (value != null) {
                        System.out.println("attribute value:" + value);
                        params.put(modelName[i], value);
                    }
                }
                if (type.equals("class java.lang.Boolean")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Boolean value = (Boolean) m.invoke(model);
                    if (value != null) {
                        System.out.println("attribute value:" + value);
                        params.put(modelName[i], value);
                    }
                }
                if (type.equals("class java.util.Date")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Date value = (Date) m.invoke(model);
                    if (value != null) {
                        System.out.println("attribute value:"
                                + value.toLocaleString());
                        params.put(modelName[i], value);
                    }
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return params;
    }


}
package com.cy.rvadapterniubility;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ************************************************************
 * author：cy
 * version：
 * create：2018/12/27 14:28
 * desc：
 * ************************************************************
 */

public class ReflexUtils {

    public static Class getClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static Object newInstance(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz == null) return null;

            return clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 调用构造函数
     *
     * @param className
     * @param parameterTypes
     * @param parameters
     * @return
     */
    public static Object invokeConstructor(String className, Class<?>[] parameterTypes, Object[] parameters) {

        try {
            Class clazz = getClass(className);
            if (clazz == null) return null;
            //  1.2获取某一个，需要参数列表
            Constructor<Object> constructor = (Constructor<Object>) clazz.getConstructor(parameterTypes);
            if (constructor == null) return null;

            //2. 调用构造器的 newInstance() 方法创建对象

            return constructor.newInstance(parameters);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return null;
    }
//    getDeclaredMethod*()获取的是类自身声明的所有方法，包含public、protected和private方法。
//
//    getMethod*()获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method getDeclaredMethod(String className, String methodName, Class<?>[] parameterTypes) {


        try {
            Class clazz = getClass(className);
            if (clazz == null) return null;

            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 获取 clazz 的 methodName 方法. 该方法可能是私有方法, 还可能在父类中(私有方法)
     * 如果在该类中找不到此方法，就向他的父类找，一直到Object类为止
     * 　　　* 这个方法的另一个作用是根据一个类名，一个方法名，追踪到并获得此方法
     *
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method getMethod(String className, String methodName, Class<?>[] parameterTypes) {
        Class clazz = getClass(className);
        if (clazz == null) return null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Method method = null;
            try {
                method = clazz.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return method;
        }
        return null;
    }

    public static Object invoke(Method method, Object receiver, Object[] args) {
        if (method == null||receiver==null) return null;
        method.setAccessible(true);
        try {
            return method.invoke(receiver, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int[] getStyleableIntArray(Context context, String name_styleable) {
        try {
            Field field = Class.forName(context.getPackageName() + ".R$styleable").getDeclaredField(name_styleable);
            if (field != null) return (int[]) field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getStyleableAttrIndex(Context context, String name_styleable, String name_attr) {
        try {
            Field field = Class.forName(context.getPackageName() + ".R$styleable").getDeclaredField(name_styleable + "_" + name_attr);
            if (field != null) return (int) field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }
}

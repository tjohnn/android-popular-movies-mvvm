package com.udacity.sandwichclub.utils;


import android.util.Log;

import com.udacity.sandwichclub.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JsonUtilsTest {

    private static final Set<Class<?>> NATIVE_TYPES = new HashSet<>(Arrays.asList(
            Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class,
            String.class));

    public static <T>  T parseSandwichJson(String json, Class<T> type) {
        Field[] fields = type.getFields();
        T data = null;
        try {
            data = type.newInstance();
            JSONObject jsonObject = new JSONObject(json);
            for (Field field: fields) {
                String fieldName = field.getName();
                try {
                    Object o = jsonObject.get(fieldName);
                    Class t = field.getType();
                    field.set(data, objectToType(o, t));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return data;

    }


    private static  <T>  T objectToType(Object o, Class<T> type) {
        if (NATIVE_TYPES.contains(type)){
            return (T)o;
        }

        return null;
    }



}

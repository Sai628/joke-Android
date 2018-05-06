package com.sai628.joke.utils;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;


/**
 * @author Sai
 * @ClassName: JSONUtil
 * @Description: JSON工具类
 * @date 02/05/2018 16:54
 */
public class JSONUtil
{
    public static <T> List<T> readModels(String jsonArray, Class<T[]> clazz)
    {
        T[] objects = new Gson().fromJson(jsonArray, clazz);
        return Arrays.asList(objects);
    }
}

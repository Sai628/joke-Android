package com.sai628.joke.utils;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;


/**
 * @author Sai
 * @ClassName: CollectionUtil
 * @Description: 集合工具类
 * @date 18/05/2018 17:01
 */
public class CollectionUtil
{
    /**
     * 打乱列表中的元素顺序
     *
     * @param list
     */
    public static void shuffle(List<?> list)
    {
        Random random = new Random();
        Object[] arr = list.toArray();
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            swap(arr, i, random.nextInt(size));
        }

        ListIterator it = list.listIterator();
        for (int i = 0; i < arr.length; i++)
        {
            it.next();
            it.set(arr[i]);
        }
    }


    private static void swap(Object[] arr, int i, int j)
    {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}

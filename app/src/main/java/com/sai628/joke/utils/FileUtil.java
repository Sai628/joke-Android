package com.sai628.joke.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author Sai
 * @ClassName: FileUtil
 * @Description: 文件操作工具类
 * @date 02/05/2018 14:58
 */
public class FileUtil
{
    /**
     * 读取 Assets 目录下的文件内容为字符串
     *
     * @param context  上下文
     * @param filePath Assets文件夹下的相对路径
     * @return String 文件内容字符串
     */
    public static String readAssetContent(Context context, String filePath)
    {
        StringBuilder sb = new StringBuilder();
        InputStream in = null;

        try
        {
            in = context.getAssets().open(filePath);
            byte[] b = new byte[in.available()];
            while (in.read(b) != -1)
            {
                sb.append(new String(b));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}

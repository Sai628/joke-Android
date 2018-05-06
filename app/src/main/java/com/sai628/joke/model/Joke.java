package com.sai628.joke.model;

/**
 * @author Sai
 * @ClassName: Joke
 * @Description: 笑话实体类
 * @date 02/05/2018 16:39
 */
public class Joke
{
    private long id;
    private String content;


    public long getId()
    {
        return id;
    }


    public void setId(long id)
    {
        this.id = id;
    }


    public String getContent()
    {
        return content != null ? content : "";
    }


    public void setContent(String content)
    {
        this.content = content;
    }


    @Override
    public String toString()
    {
        return "Joke:[" +
                "id=" + getId() + "," +
                "content=" + getContent() +
                "]";
    }
}

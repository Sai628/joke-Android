package com.sai628.joke.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sai628.joke.R;
import com.sai628.joke.model.Joke;

import java.util.ArrayList;
import java.util.List;


public class JokeListActivity extends Activity
{
    public static final String EXTRA_JOKES = "jokes";  // 跳转到该视图时, 待显示的列表数据键名
    public static final String EXTRA_INDEX = "index";  // 跳转到该视图时, 自动定位到列表位置的索引值键名

    public static final String EXTRA_RESULT_JOKE = "joke";  // 返回上一页后, 传参时的Joke类参数名
    public static final String EXTRA_RESULT_INDEX = "index";  // 返回上一页后, 传参时的索引位置参数名

    private ListView listView;
    private int index;  // 自动定位到的索引值


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_list);

        initView();
        scrollToIndex();
    }


    @SuppressWarnings("unchecked")
    private void initView()
    {
        ArrayList<Joke> jokes = (ArrayList<Joke>) getIntent().getSerializableExtra(EXTRA_JOKES);
        index = getIntent().getIntExtra(EXTRA_INDEX, 0);  // 若没传该参数值, 默认为0

        listView = findViewById(R.id.activity_joke_list_listview);
        listView.setAdapter(new JokeListAdapter(this, jokes));
        listView.setOnItemClickListener(jokeListOnItemClickListener);
    }


    private void scrollToIndex()
    {
        new Handler().post(new Runnable()
        {
            @Override
            public void run()
            {
                listView.smoothScrollToPositionFromTop(index, 0, 350);
            }
        });
    }


    private AdapterView.OnItemClickListener jokeListOnItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Joke joke = (Joke) parent.getItemAtPosition(position);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_RESULT_JOKE, joke);
            intent.putExtra(EXTRA_RESULT_INDEX, position);
            setResult(RESULT_OK, intent);
            finish();
        }
    };


    private class JokeListAdapter extends BaseAdapter
    {
        private Context context;
        private List<Joke> jokes;


        JokeListAdapter(Context context, List<Joke> jokes)
        {
            this.context = context;
            this.jokes = jokes;
        }


        @Override
        public int getCount()
        {
            return jokes.size();
        }


        @Override
        public Object getItem(int position)
        {
            return jokes.get(position);
        }


        @Override
        public long getItemId(int position)
        {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;
            if (convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_joke, parent, false);
                holder = new ViewHolder();
                holder.textView = convertView.findViewById(R.id.list_item_joke_textview);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            Joke joke = (Joke) getItem(position);
            holder.textView.setText(joke.getContent());

            return convertView;
        }
    }


    private static class ViewHolder
    {
        TextView textView;
    }
}

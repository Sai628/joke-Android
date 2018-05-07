package com.sai628.joke.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sai628.joke.R;
import com.sai628.joke.model.Joke;
import com.sai628.joke.utils.FileUtil;
import com.sai628.joke.utils.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity
{
    private View bgView;
    private ScrollView scrollView;
    private TextView contentTv;
    private ProgressBar progressBar;
    private ImageView listIv;

    private LoadDataTask task;
    private ArrayList<Joke> jokes;
    private Animation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preInitData();
        initView();
        loadDataSync();
        //loadDataAsync();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        task.setListener(null);
    }


    private void preInitData()
    {
        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
    }


    private void initView()
    {
        bgView = findViewById(R.id.activity_main_bg_rl);
        scrollView = findViewById(R.id.activity_main_scrollview);
        contentTv = findViewById(R.id.activity_main_content_tv);
        progressBar = findViewById(R.id.activity_main_progressbar);
        listIv = findViewById(R.id.activity_main_list_iv);
        listIv.setOnClickListener(listMenuOnClickListener);
    }


    private void loadDataSync()
    {
        try
        {
            String content = FileUtil.readAssetContent(this, "joke.json");
            JSONObject jsonObject = new JSONObject(content);
            String data = jsonObject.optString("data");
            List<Joke> jokes = JSONUtil.readModels(data, Joke[].class);
            updateUI(jokes);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    private void loadDataAsync()
    {
        progressBar.setVisibility(View.VISIBLE);

        task = new LoadDataTask(this);
        task.setListener(new LoadDataTask.LoadDataTaskListener()
        {
            @Override
            public void onLoadDataTaskFinished(List<Joke> jokes)
            {
                updateUI(jokes);
            }
        });
        task.execute("joke.json");
    }


    private void updateUI(List<Joke> jokes)
    {
        progressBar.setVisibility(View.GONE);
        if (jokes == null || jokes.isEmpty())
        {
            return;
        }

        this.jokes = new ArrayList<>();
        this.jokes.addAll(jokes);
        contentTv.setOnClickListener(contentOnClickListener);

        Joke joke = getRandomJoke();
        showJokeContent(joke);
    }


    private void showJokeContent(Joke joke)
    {
        int colorId = (joke.getId() % 2 == 1) ? R.color.text_bg_color_deep : R.color.text_bg_color_shallow;
        bgView.setBackgroundColor(getResources().getColor(colorId));
        scrollView.scrollTo(0, 0);

        contentTv.clearAnimation();
        contentTv.setText(joke.getContent());
        contentTv.startAnimation(animation);
    }


    private Joke getRandomJoke()
    {
        int index = (int) (Math.random() * jokes.size());
        return jokes.get(index);
    }


    private View.OnClickListener contentOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Joke joke = getRandomJoke();
            showJokeContent(joke);
        }
    };


    private View.OnClickListener listMenuOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(MainActivity.this, JokeListActivity.class);
            intent.putExtra(JokeListActivity.EXTRA_JOKES, jokes);
            startActivityForResult(intent, 0);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0)
        {
            Joke joke = (Joke) data.getSerializableExtra(JokeListActivity.EXTRA_RESULT_JOKE);
            showJokeContent(joke);
        }
    }


    private static class LoadDataTask extends AsyncTask<String, Integer, List<Joke>>
    {
        private WeakReference<Activity> reference;
        private LoadDataTaskListener listener;


        LoadDataTask(Activity activity)
        {
            reference = new WeakReference<>(activity);
        }


        @Override
        protected List<Joke> doInBackground(String... params)
        {
            Activity activity = reference.get();
            if (activity == null || activity.isFinishing())
            {
                return null;
            }

            try
            {
                String content = FileUtil.readAssetContent(activity, params[0]);
                JSONObject jsonObject = new JSONObject(content);
                String data = jsonObject.optString("data");
                return JSONUtil.readModels(data, Joke[].class);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(List<Joke> jokes)
        {
            if (listener != null)
            {
                listener.onLoadDataTaskFinished(jokes);
            }
        }


        void setListener(LoadDataTaskListener listener)
        {
            this.listener = listener;
        }


        public interface LoadDataTaskListener
        {
            void onLoadDataTaskFinished(List<Joke> jokes);
        }
    }
}

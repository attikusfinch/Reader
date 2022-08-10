package com.amik.reader;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amik.reader.constructor.mainpage.CourseAdapter;
import com.amik.reader.constructor.mainpage.CourseModel;
import com.amik.reader.tools.InternetConnection;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private RecyclerView courseRV;

    private CourseAdapter adapter;

    private Button internetProblem;
    private Button menu;

    private TextView language;

    private ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();

    private int page = 1;

    private String lang = "ru-RU,ru;q=0.5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        courseRV = findViewById(R.id.PostView);
        internetProblem = findViewById(R.id.reconnect);
        language = findViewById(R.id.ChangeLanguage);
        menu = findViewById(R.id.OpenMenu);

        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CourseAdapter(this, courseModelArrayList);
        courseRV.setAdapter(adapter);

        CheckConnection();

        ParsePost parsePost = new ParsePost();
        parsePost.execute();

        init();
    }

    private void init(){
        internetProblem.setOnClickListener(v -> CheckConnection());
        language.setOnClickListener(v -> ChangeLanguage());
        menu.setOnClickListener(v -> OpenMenu());
        courseRV.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0) //check for scroll down
                {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) //check if we are at the bottom
                    {
                        LoadMore();
                    }
                }
            }
        });
    }

    private void CheckConnection(){
        if (!InternetConnection.checkConnection(getApplicationContext())) {
            internetProblem.setVisibility(View.VISIBLE);
        } else {
            internetProblem.setVisibility(View.GONE);
            ParsePost parsePost = new ParsePost();
            parsePost.execute();
        }
    }

    private void OpenMenu(){
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }

    private void ChangeLanguage(){
        String ru_lang = "ru-RU,ru;q=0.5";
        if (lang.equals(ru_lang)){
            lang = "en-US,en;q=0.5";
            language.setText("EN");
            courseModelArrayList.clear();
        } else {
            lang = ru_lang;
            language.setText("RU");
            courseModelArrayList.clear();
        }
        page = 1;
        ParsePost parsePost = new ParsePost();
        parsePost.execute();
    }

    private void LoadMore(){
        page++;
        if (courseModelArrayList.size() == 0){
            Toast.makeText(this, "Дальше пусто", Toast.LENGTH_SHORT).show();
            page--;
        } else {
            ParsePost parsePost = new ParsePost();
            parsePost.execute();
        }
    }

    private class ParsePost extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://slabber.io/api/posts?page=" + page + "&withImage=1")
                    .header("Accept-Language", lang)
                    .build();

            Call call = client.newCall(request);

            try {
                JSONObject jsonObject = new JSONObject(call.execute().body().string());
                Document document = Jsoup.parse(jsonObject.get("html").toString());
                Elements elements = document.select("div.card.bPopularPosts__item.bCard");
                for (int i = 0; i < elements.size(); i++) {
                    document = Jsoup.parse(elements.get(i).toString());
                    String link = document.select("a").get(0).attr("href");
                    link = "https://slabber.io" + link;
                    Elements card_body = document.select("div.card-body");
                    Elements data = card_body.select("a");
                    Elements image_style = document.select("div.bCard__cover");
                    String image = image_style.attr("style");
                    String image_path = image.split("'")[1];
                    String title = data.get(0).text();
                    String description = data.get(1).text();

                    courseModelArrayList.add(new CourseModel(title, description, link, image_path));
                    Log.d("Do in bg: ",title + " " + description + " " + link + " " + image_path);
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
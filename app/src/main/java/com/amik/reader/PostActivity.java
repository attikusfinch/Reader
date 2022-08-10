package com.amik.reader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amik.reader.constructor.mainpage.CourseAdapter;
import com.amik.reader.constructor.mainpage.CourseModel;
import com.amik.reader.constructor.postpage.PostAdapter;
import com.amik.reader.constructor.postpage.PostModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import io.noties.markwon.Markwon;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PostActivity extends AppCompatActivity {

    ImageView BackgroundImage;

    FloatingActionButton OpenPost, ChangeTheme;

    String Url, Name, Description, Image;

    AppBarLayout appBarLayout;

    RecyclerView recyclerViewPost;

    PostAdapter adapter;

    private boolean isDarkTheme = false;

    private ArrayList<PostModel> postModelArrayList = new ArrayList<PostModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        Url = intent.getStringExtra("courseUrl");
        Name = intent.getStringExtra("courseName");
        Description = intent.getStringExtra("courseDescription");
        Image = intent.getStringExtra("courseImage");

        Init();
    }

    private void Init(){
        BackgroundImage = findViewById(R.id.main_backdrop);
        OpenPost = findViewById(R.id.post_fab_link);
        appBarLayout = findViewById(R.id.main_appbar);
        recyclerViewPost = findViewById(R.id.PostContent);
        ChangeTheme = findViewById(R.id.post_change_theme);

        recyclerViewPost.setHasFixedSize(true);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new PostAdapter(this, postModelArrayList);
        recyclerViewPost.setAdapter(adapter);

        OpenPost.setOnClickListener(v -> OpenPost(Url));
        ChangeTheme.setOnClickListener(v -> ChangeTheme());

        Picasso.get().load("https://slabber.io/" + Image).into(BackgroundImage);

        ParsePost parsePost = new ParsePost();
        parsePost.execute();
    }

    private void OpenPost(String link){
        // open link in browser
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    private void ChangeTheme(){
        if(isDarkTheme){
            getApplication().setTheme(R.style.Theme_Reader);
            //ChangeTheme.setImageResource(R.drawable.ic_brightness_high_black_24dp);
            isDarkTheme = false;
        }else{
            getApplication().setTheme(R.style.Theme_Reader_Dark);
            //ChangeTheme.setImageResource(R.drawable.ic_brightness_low_black_24dp);
            isDarkTheme = true;
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
                    .url(Url)
                    .build();

            Call call = client.newCall(request);

            try {
                Document document = Jsoup.parse(call.execute().body().string());
                Elements elements = document.select("div.bPage__text");
                Element title = document.selectFirst("div.bPage__title");
                postModelArrayList.add(new PostModel("<h3>" + title.text() + "</h3>"));
                //get children elements
                Elements children = elements.select("div");
                Element textElement;
                for (int i = 1; i < children.size(); i++) {
                    String html = children.get(i).toString();
                    document = Jsoup.parse(html);
                    if(html.contains("editor-js-content") || html.contains("editor-js-block") || html.contains("cdx-quote")){
                        //format as markwon and add to recycler view
                        textElement = document.selectFirst("div.editor-js-content");
                        if(textElement == null){
                            textElement = document.selectFirst("div.editor-js-block");
                        }
                        if(textElement == null){
                            textElement = document.selectFirst("div.cdx-quote__text");
                        }
                        postModelArrayList.add(new PostModel(textElement.html()));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            call.enqueue(new okhttp3.Callback() {
//                @Override
//                public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
//                    Log.e("Error", e.getMessage());
//                }
//
//                @Override
//                public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
//                    // do something with the response
//                    // for example, parse the response body as JSON, then access the data within that JSON
//                    // and then display the data in the UI
//                    Document document = null;
//                    document = Jsoup.parse(response.body().string());
//                    Elements elements = document.select("div.bPage__text");
//                    //get children elements
//                    Elements children = elements.select("div");
//
//                    for (int i = 0; i < children.size(); i++) {
//                        String html = children.get(i).toString();
//                        document = Jsoup.parse(html);
//                        final Markwon markwon = Markwon.create(PostActivity.this);
//                        if(html.contains("editor-js-content")) {
//                            //format as markwon and add to recycler view
//                            elements = document.select("div.editor-js-content");
//                            TextView textView = new TextView(PostActivity.this);
//                            markwon.setMarkdown(textView, html);
//                            recyclerViewPost.addView(textView);
//                        }
//                    }
//                }
//            });
            return null;
        }
    }
}

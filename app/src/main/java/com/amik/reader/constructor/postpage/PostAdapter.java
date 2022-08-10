package com.amik.reader.constructor.postpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amik.reader.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.noties.markwon.Markwon;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.image.picasso.PicassoImagesPlugin;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder> {

    private Context context;
    private ArrayList<PostModel> postModelArrayList;

    // Constructor
    public PostAdapter(Context context, ArrayList<PostModel> recyclerViewPost) {
        this.context = context;
        this.postModelArrayList = recyclerViewPost;
    }

    @NonNull
    @Override
    public PostAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new PostAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        PostModel model = postModelArrayList.get(position);
        final Markwon markwon = Markwon.builder(context)
                .usePlugin(HtmlPlugin.create())
                .usePlugin(PicassoImagesPlugin.create(Picasso.get()))
                .build();

        markwon.setMarkdown(holder.text, model.getText());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return postModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder{

        private TextView text;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.PostTextView);
        }
    }
}

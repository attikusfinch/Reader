package com.amik.reader.constructor.mainpage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amik.reader.PostActivity;
import com.amik.reader.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.Viewholder> {

    private Context context;
    private ArrayList<CourseModel> courseModelArrayList;

    // Constructor
    public CourseAdapter(Context context, ArrayList<CourseModel> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public CourseAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        CourseModel model = courseModelArrayList.get(position);
        holder.courseNameTV.setText(model.getCourse_name());
        holder.courseDescriptionTV.setText(model.getCourse_description());

        holder.courseUrl = model.getCourse_url();
        holder.ImageUrl = model.getImage_url();

        Picasso.get().load("https://slabber.io" + model.getImage_url()).into(holder.courseIV);
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return courseModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView courseIV;
        private TextView courseNameTV;
        private TextView courseDescriptionTV;
        private String courseUrl;
        private String ImageUrl;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            courseDescriptionTV = itemView.findViewById(R.id.idTVCourseDescription);
            courseIV = itemView.findViewById(R.id.idIVCourseImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, PostActivity.class);
            intent.putExtra("courseUrl", courseUrl);
            intent.putExtra("courseName", courseNameTV.getText().toString());
            intent.putExtra("courseDescription", courseDescriptionTV.getText().toString());
            intent.putExtra("courseImage", ImageUrl);
            context.startActivity(intent);
        }
    }
}

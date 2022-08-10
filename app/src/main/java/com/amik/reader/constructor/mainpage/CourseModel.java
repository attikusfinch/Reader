package com.amik.reader.constructor.mainpage;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class CourseModel {

    private String course_name;
    private String course_description;

    private ImageView course_image;
    private String course_url;

    private String image_url;

    // Constructor
    public CourseModel(String course_name, String course_description, String course_url, String image_url) {
        this.course_name = course_name;
        this.course_description = course_description;
        this.course_url = course_url;
        this.image_url = image_url;
    }

    // Getter and Setter
    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public ImageView getCourse_image() {
        return course_image;
    }

    public void setCourse_image(ImageView course_image) {
        this.course_image = course_image;
    }

    public String getCourse_description() {
        return course_description;
    }

    public void setCourse_description(String course_description) {
        this.course_description = course_description;
    }

    public String getCourse_url() {
        return course_url;
    }

    public void setCourse_url(String course_url) {
        this.course_url = course_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

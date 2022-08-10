package com.amik.reader.constructor.postpage;

public class PostModel {

    private String PostText;

    public PostModel(String PostText) {
        this.PostText = PostText;
    }

    public String getText() {
        return PostText;
    }

    public void setText(String PostText) {
        this.PostText = PostText;
    }
}

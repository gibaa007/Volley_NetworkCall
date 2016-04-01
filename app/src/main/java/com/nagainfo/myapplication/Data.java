package com.nagainfo.myapplication;

/**
 * Created by nagainfo on 30/3/16.
 */
public class Data {
    int id;
    String title;
    String img;

    public Data(String title, String img_url) {
        this.title=title;
        this.img=img_url;

    }

    public Data() {

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

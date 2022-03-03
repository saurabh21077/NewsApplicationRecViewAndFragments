package com.mc2022.template;

public class NewsInfo {
    private String title;
    private String body;
    private String image_url;
    private String comment;
    private int rating;

    public NewsInfo(String title, String body, String image_url) {
        this.title = title;
        this.body = body;
        this.image_url = image_url;
        this.comment = "";
        this.rating = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "NewsInfo{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", image_url='" + image_url + '\'' +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                '}';
    }
}
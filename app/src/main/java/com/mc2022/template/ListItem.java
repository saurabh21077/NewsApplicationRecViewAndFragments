package com.mc2022.template;

public class ListItem {
    private int newsNumber;
    private NewsInfo news;

    public ListItem(int newsNumber, NewsInfo newsTitle) {
        this.newsNumber = newsNumber;
        this.news = newsTitle;
    }

    public int getNewsNumber() {
        return newsNumber;
    }

    public void setNewsNumber(int newsNumber) {
        this.newsNumber = newsNumber;
    }

    public NewsInfo getNews() {
        return news;
    }

    public void setNews(NewsInfo news) {
        this.news = news;
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "newsNumber=" + newsNumber +
                ", newsTitle='" + news + '\'' +
                '}';
    }
}

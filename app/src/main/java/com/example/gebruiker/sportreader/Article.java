package com.example.gebruiker.sportreader;

/**
 * Created by gebruiker on 8-6-2017.
 */

public class Article {
    public String author;
    public String tittle;
    public String url;
    public String description;
    public String urlImage;
    public String date;

    public Article(String urlImage, String tittle, String description, String url, String date, String author) {
        this.author = author;
        this.tittle = tittle;
        this.url = url;
        this.urlImage = urlImage;
        this.date = date;
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public String getTittle() {
        return tittle;
    }


    public String getUrl() {
        return url;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getDate() {
        return date;
    }


    public String getDescription() {
        return description;
    }
}


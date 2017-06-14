package com.example.gebruiker.sportreader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by gebruiker on 9-6-2017.
 */

class ArticleLoader extends AsyncTaskLoader<ArrayList<Article>> {
    public String url;

    public ArticleLoader(Context context, String sUrl) {
        super(context);
        url = sUrl;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Article> loadInBackground() {
        if (url == null) {
            return null;
        } else {
            return QueryUtils.fetchSportData(url);


        }

    }

}

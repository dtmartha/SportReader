package com.example.gebruiker.sportreader;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.R.id.input;
import static com.example.gebruiker.sportreader.R.id.imageView;

/**
 * Created by gebruiker on 8-6-2017.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {
    private static final String LOG_TAG = ArticleAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private static final String DATE_IN = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_OUT = "dd MMMM yyyy HH:mm ";
    private static final String TIMEZONE_IN = "GMT+1";


    //constructor
    public ArticleAdapter(Activity context, ArrayList<Article> sportnews) {
        super(context, 0, sportnews);
        inflater = LayoutInflater.from(getContext());


    }

    //Recycled views
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = inflater.inflate(
                    R.layout.article_list_item, parent, false);
        }
        Article article = getItem(position);


        //ImageView
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.imageView);
        Picasso.with(getContext()).load(article.getUrlImage()).into(imageView);
        imageView.setVisibility(View.VISIBLE);


        //Tittle
        TextView tittle = (TextView) listItemView.findViewById(R.id.tittle);
        tittle.setText(article.getTittle());


        //date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDateTime(article.getDate());
        dateView.setText(formattedDate);


        //Author
        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(article.getAuthor());

        //description

        TextView description = (TextView) listItemView.findViewById(R.id.description);
        description.setText(article.getDescription());


        return listItemView;
    }


    private String formatDateTime(String datum) {
        SimpleDateFormat sdfIn = new SimpleDateFormat(DATE_IN);
        sdfIn.setTimeZone(TimeZone.getTimeZone(TIMEZONE_IN));
        Date dateIn = new Date();
        try {
            dateIn = sdfIn.parse(datum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdfOut = new SimpleDateFormat(DATE_OUT, Locale.getDefault());
        return sdfOut.format(dateIn);
    }


}


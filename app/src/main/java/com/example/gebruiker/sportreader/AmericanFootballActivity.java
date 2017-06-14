package com.example.gebruiker.sportreader;

/**
 * Created by gebruiker on 13-6-2017.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by gebruiker on 13-6-2017.
 */

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AmericanFootballActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<ArrayList<Article>> {

    public static final String LOG_TAG = HomePageActivity.class.getName();
    private static final String URL_SOCCER = "https://newsapi.org/v1/articles?source=four-four-two&sortBy=top&apiKey=e706c87136454836aae1ac155b3f0eac";
    private static final String URL_NFL = "https://newsapi.org/v1/articles?source=nfl-news&sortBy=top&apiKey=e706c87136454836aae1ac155b3f0eac";
    private static final String URL_HOME_PAGE = "https://newsapi.org/v1/articles?source=bbc-sport&sortBy=top&apiKey=e706c87136454836aae1ac155b3f0eac";
    private static final String URL_CRICKET = "https://newsapi.org/v1/articles?source=espn-cric-info&sortBy=top&apiKey=e706c87136454836aae1ac155b3f0eac";
    public ArrayList<Article> sportnews;

    ArticleAdapter adapter;
    ProgressBar progressBar;
    TextView progresText;
    TextView emptyStateView;
    int loaderID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Listviews, Adapters, Arraylist, Loadermanager, progressbar & emptystate;


        sportnews = new ArrayList<>();

        ListView sportListView = (ListView) findViewById(R.id.list);

        adapter = new ArticleAdapter(this, sportnews);

        sportListView.setAdapter(adapter);


        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        progresText = (TextView) findViewById(R.id.progresstext);

        emptyStateView = (TextView) findViewById(R.id.emptystate);
        sportListView.setEmptyView(emptyStateView);

        //checks if there's an internet connection

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            getLoaderManager().initLoader(loaderID++, null, this);
            Log.d(LOG_TAG, "There's an internet connection");


        } else {
            emptyStateView.setText(R.string.No_Internet_connection);
            progressBar.setVisibility(View.GONE);
            progresText.setVisibility(View.GONE);
            Log.d(LOG_TAG, "No Internet connection");

        }


        //onItemClickListener. if an list item is clicked, it opens the web browser to the article.

        sportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = sportnews.get(position);
                String url = article.getUrl();
                if (url != null) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));


                }

            }


        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.refresh_button) {
            adapter.clear();
            getLoaderManager().initLoader(loaderID++, null, this);
            progressBar.setVisibility(View.VISIBLE);
            progresText.setVisibility(View.VISIBLE);


        }

        return super.onOptionsItemSelected(item);
    }

    //Navbar
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent i = new Intent(AmericanFootballActivity.this, HomePageActivity.class);
            startActivity(i);
        } else if (id == R.id.soccer) {
            Intent i = new Intent(AmericanFootballActivity.this, SoccerActivity.class);
            startActivity(i);


        } else if (id == R.id.american_football) {


        } else if (id == R.id.cricket) {
            Intent i = new Intent(AmericanFootballActivity.this, CricketActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //CreateLoader, onloadfinished(UpdateUi), OnloaderReset


    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader is called ");
        return new ArticleLoader(this, URL_NFL);
    }


    @Override
    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
        Log.d(LOG_TAG, "onLoadFinished is called, update UI ");
        adapter.clear();

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            if (data != null && !data.isEmpty()) {
                adapter.addAll(data);
                emptyStateView.setText("");


            }


        } else if (activeNetwork == null) {
            emptyStateView.setText(R.string.No_Internet_connection);

        } else {
            emptyStateView.setText(R.string.No_Articles_Found);
        }
        progressBar.setVisibility(View.GONE);
        progresText.setVisibility(View.GONE);


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Article>> loader) {
        Log.d(LOG_TAG, "onLoaderReset is called ");
        adapter.clear();

    }
}

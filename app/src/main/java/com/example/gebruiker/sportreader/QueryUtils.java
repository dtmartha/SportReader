package com.example.gebruiker.sportreader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by gebruiker on 9-6-2017.
 */

final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    public static ArrayList<Article> fetchSportData(String requestUrl) {

        // Create URL object


        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i(LOG_TAG, jsonResponse);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }


        return extractFeatureFromJson(jsonResponse);
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Sportdata JSON results.", e);


        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Article> extractFeatureFromJson(String SportDataJson) {
        Context context = null;

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(SportDataJson)) {
            return null;
        }

        ArrayList<Article> articleData = null;

        try {
            articleData = new ArrayList<>();
            JSONObject baseJsonResponse = new JSONObject(SportDataJson);
            JSONArray SportDataArray = baseJsonResponse.getJSONArray("articles");

            for (int i = 0; i < SportDataArray.length(); i++) {
                JSONObject currentData = SportDataArray.getJSONObject(i);
                String foto = currentData.getString("urlToImage");
                String titel = currentData.getString("title");
                String beschrijving = currentData.getString("description");
                String link = currentData.getString("url");
                String datum = currentData.getString("publishedAt");
                String auteur = currentData.getString("author");
                Article article = new Article(foto, titel, beschrijving, link, datum, auteur);
                articleData.add(article);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the Sportdata JSON results", e);
        }
        return articleData;

    }


}
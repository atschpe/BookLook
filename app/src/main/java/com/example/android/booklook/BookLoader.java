package com.example.android.booklook;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Load a list of {@link Book}s aided by the {@link AsyncTaskLoader}.
 */

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    private String url; //query url

    /**
     * Construct a new {@link BookLoader}.
     *
     * @param context of the activity.
     * @param url     to load data from.
     */
    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        if (url == null) {
            return null;
        }

        ArrayList<Book> books = QueryUtils.fetchBookData(url);
        return books;
    }
}
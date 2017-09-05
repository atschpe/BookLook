package com.example.android.booklook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * {@link BookAdapter} is an {@link RecyclerView.Adapter} which uses the {@link BookHolder} to
 * display the information in the layout of the {@link Book} in question.
 */

public class BookAdapter extends RecyclerView.Adapter<BookHolder> {

    private final ArrayList<Book> book;
    private int resourceId;
    private Context ctxt;

    /**
     * Construct a BookAdapter
     *
     * @param context    is the activity within which the information is displayed.
     * @param resourceId is the layout used to display the {@link Book} object.
     * @param books      is the list of {@link Book}s to be displayed.
     */
    public BookAdapter(Context context, int resourceId, ArrayList<Book> books) {
        this.book = books;
        this.resourceId = resourceId;
        this.ctxt = context;
    }

    @Override
    public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.resourceId, parent, false);
        return new BookHolder(this.ctxt, view);
    }

    @Override
    public void onBindViewHolder(BookHolder holder, int position) {
        Book books = this.book.get(position);
        holder.bindBook(books);
    }

    @Override
    public int getItemCount() {
        return this.book.size();
    }
}
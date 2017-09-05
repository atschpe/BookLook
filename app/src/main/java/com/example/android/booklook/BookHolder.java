package com.example.android.booklook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * {@link BookHolder} extends {@link RecyclerView.ViewHolder} enabling the {@link RecyclerView} to
 * display the information pertaining to the {@link Book} in question.
 */

public class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //The views to be populated.
    private TextView titleText;
    private TextView authorText;
    private TextView categoryText;

    private Book book;
    private Context ctxt;

    public BookHolder(Context context, View itemView) {
        super(itemView);

        this.ctxt = context;

        //Locating the views.
        this.titleText = (TextView) itemView.findViewById(R.id.list_title);
        this.authorText = (TextView) itemView.findViewById(R.id.list_author);
        this.categoryText = (TextView) itemView.findViewById(R.id.list_category);

        itemView.setOnClickListener(this);
    }

    public void bindBook(Book book) {
        ArrayList<String> bookCategory = new ArrayList<>();
        bookCategory.addAll(book.getCategory());

        ArrayList<String> bookAuthor = new ArrayList<>();
        bookAuthor.addAll(book.getAuthor());

        this.book = book;
        this.titleText.setText(book.getTitle());
        if (bookAuthor.contains("XXX")) {
            this.authorText.setText("");
        } else {
            this.authorText.setText(bookAuthor.get(0));
        }
        if (bookCategory.contains("XXX")) {
            this.categoryText.setText("");
        } else {
            this.categoryText.setText(bookCategory.get(0));
        }
    }

    /**
     * Bundle all the info concerning the book for the infoActivity
     *
     * @param v is the view clicked
     */
    @Override
    public void onClick(View v) {

        Intent i = new Intent(ctxt, InfoActivity.class);

        Bundle bookBundle = new Bundle();
        bookBundle.putString("title", book.getTitle());
        bookBundle.putStringArrayList("author", book.getAuthor());
        bookBundle.putString("publisher", book.getPublisher());
        bookBundle.putString("language", book.getLanguage());
        bookBundle.putStringArrayList("category", book.getCategory());
        bookBundle.putString("description", book.getDescription());
        bookBundle.putString("date", book.getDate());
        bookBundle.putString("reader", book.getReader());
        bookBundle.putString("fullImage", book.getImage());

        i.putExtras(bookBundle);
        ctxt.startActivity(i);
    }
}
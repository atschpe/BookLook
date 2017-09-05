package com.example.android.booklook;

import java.util.ArrayList;

/**
 * A class object containing the various information provided for {@link Book}s.
 */

public class Book {

    private String title; //title of book

    private ArrayList<String> author; //author(s) of book

    private String publisher; //publisher of book

    private String language; //language the book is written in

    private ArrayList<String> category; //category(s) the book is listed in

    private String description; //description provided

    private String date; //date published

    private String reader; // link to an online reader

    private String image; //thumbnail and image of book cover.

    /**
     * Construct a {@link Book} object.
     *
     * @param title       is the title of the book
     * @param author      is the author of the book
     * @param publisher   is the publisher of the book
     * @param language    is the language the book is written in
     * @param category    is the category in which the book is listed
     * @param description is the description provided for the book
     * @param date        is the date of publication
     * @param reader      is an online reader to sample (or even read) the book
     * @param image   is the book cover image
     */
    public Book(String title, ArrayList<String> author, String publisher, String language,
                ArrayList<String> category, String description, String date, String reader,
                String image) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.language = language;
        this.category = category;
        this.description = description;
        this.date = date;
        this.reader = reader;
        this.image = image;
    }

    // ------ Getters ------
    public String getTitle() {
        return title;
    }

    public ArrayList<String> getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getLanguage() {
        return language;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getReader() {
        return reader;
    }

    public String getImage() {
        return image;
    }
}
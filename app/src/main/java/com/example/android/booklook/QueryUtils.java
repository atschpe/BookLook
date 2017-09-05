package com.example.android.booklook;

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
 * Helper methods to support request and retrieval from the Google Book API. It contains static
 * variables and methods, which can be called directly from the class name QueryUtils.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName(); //Tag for the logs

    private static final int READ_TIMEOUT = 10000; //in milliseconds
    private static final int CONNECT_TIMEOUT = 15000; // in milliseconds

    private QueryUtils() { //Empty private constructor
    }

    /**
     * Query the Google Book API and retrieve a list of {@link Book} objects.
     *
     * @param requestBookUrl is the url sent to the API to retrieve the data.
     * @return is the data returned from the API
     */
    public static ArrayList<Book> fetchBookData(String requestBookUrl) {

        URL url = createUrl(requestBookUrl); //Create URL object

        String jsonBookResponse = null;
        try {
            jsonBookResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "HTTP request could not be made.", e);
        }
        ArrayList<Book> books = ExtractBookFromJson(jsonBookResponse);

        return books;
    }

    private static ArrayList<Book> ExtractBookFromJson(String jsonBookResponse) {
        if (TextUtils.isEmpty(jsonBookResponse)) {
            return null; //return early if null.
        }
        ArrayList<Book> books = new ArrayList<>();//Will hold the book data.

        try {
            //Get JsonObject form the JsonBookResponse string and access the item array.
            JSONObject baseResponse = new JSONObject(jsonBookResponse);
            JSONArray bookArray = baseResponse.getJSONArray("items");

            //go through the bookArray to retrieve items and their info.
            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);//get item within array.

                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String bookTitle = volumeInfo.getString("title"); //Title of the book

                ArrayList<String> bookAuthors = new ArrayList<>();// Author(s) of book
                if (volumeInfo.has("authors")) {
                    JSONArray authorJson = volumeInfo.getJSONArray("authors");
                    for (int aut = 0; aut < authorJson.length(); aut++) {
                        bookAuthors.add(authorJson.getString(aut));
                    }
                } else {
                    bookAuthors.add("XXX");
                }

                String bookPublisher; //publisher of book
                if (volumeInfo.has("publisher")) {
                    bookPublisher = volumeInfo.getString("publisher");
                } else {
                    bookPublisher = "XXX";
                }

                String bookDate;// publishing date of book
                if (volumeInfo.has("publishedDate")) {
                    bookDate = volumeInfo.getString("publishedDate");
                } else {
                    bookDate = "XXX";
                }

                ArrayList<String> bookCategory = new ArrayList<>(); //category(s) of listing
                if (volumeInfo.has("categories")) {
                    JSONArray categoryJson = volumeInfo.getJSONArray("categories");
                    for (int cat = 0; cat < categoryJson.length(); cat++) {
                        bookCategory.add(categoryJson.getString(cat));
                    }
                } else {
                    bookCategory.add("XXX");
                }

                String bookLanguage = volumeInfo.getString("language"); // language of book

                String bookDescription; //description of book
                if (volumeInfo.has("description")) {
                    bookDescription = volumeInfo.getString("description");
                } else {
                    bookDescription = "XXX";
                }

                JSONObject image = volumeInfo.getJSONObject("imageLinks");
                String fullImage = image.getString("thumbnail"); //image of cover

                JSONObject accessInfo = currentBook.getJSONObject("accessInfo");
                String bookReader = accessInfo.getString("webReaderLink"); //access to read book

                Book book = new Book(bookTitle, bookAuthors, bookPublisher, bookLanguage,
                        bookCategory, bookDescription, bookDate, bookReader,fullImage);

                books.add(book);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Book JSON could not be parsed.", e);
        }
        return books; //return items
    }

    /**
     * Http request to the given url.
     *
     * @param url the url provided
     * @return the jsonResponse.
     * @throws IOException Handles any problems occurring whilst retrieveing the data.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (jsonResponse == null) {
            return jsonResponse; //return early if null.
        }

        HttpURLConnection urlConnect = null;
        InputStream input = null;

        try {
            urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setReadTimeout(READ_TIMEOUT);
            urlConnect.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnect.setRequestMethod("GET");
            urlConnect.connect();

            if (urlConnect.getResponseCode() == 200) {
                input = urlConnect.getInputStream();
                jsonResponse = readFromStream(input);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnect.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book data.", e);
        } finally {
            if (urlConnect == null) {
                urlConnect.disconnect();
            }
            if (input == null) {
                input.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream input) throws IOException {
        StringBuilder output = new StringBuilder();
        if (input != null) {
            InputStreamReader isr = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while (line != null) {
                output.append(line);
                line = br.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return new URL object based on the provided string URL.
     *
     * @param requestUrl is the provided string URL
     * @return is the new URL object.
     */
    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "URL could not be built", e);
        }
        return url;
    }
}
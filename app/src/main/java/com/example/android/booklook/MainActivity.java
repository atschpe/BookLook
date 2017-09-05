package com.example.android.booklook;

import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.LoaderManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    //API url to retrieve data from each API queried.
    private static final String GOOGLE_BOOK_API = "https://www.googleapis.com/books/v1/volumes?";

    private static final int BOOK_LOADER_ID = 1; //constant value for book loader.

    private BookAdapter bookAdapter; //adapter to create list items.

    ArrayList<Book> books;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView bookItem;
    EditText searchString;
    RadioGroup selection;
    Button search;
    Spinner langSpin;
    TextView status;
    RelativeLayout statusHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialising all components.
        books = new ArrayList<>();
        searchString = (EditText) findViewById(R.id.search_string);
        selection = (RadioGroup) findViewById(R.id.selection);
        search = (Button) findViewById(R.id.search);
        bookItem = (RecyclerView) findViewById(R.id.recyclerview);
        langSpin = (Spinner) findViewById(R.id.spinner_language);
        layoutManager = new LinearLayoutManager(this);
        status = (TextView) findViewById(R.id.status_text);
        statusHolder = (RelativeLayout) findViewById(R.id.status_holder);

        //Check network connectivity.
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ni != null && ni.isConnected()) {
                    //get LoaderManager and initialise it.
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                } else {
                    status.setText(R.string.no_internet);
                }
            }
        });

        //create spinner with available languages listed alphabetically + "any language" option.
        Locale[] availableLocales = Locale.getAvailableLocales();
        ArrayList<String> spinLangArray = new ArrayList<>();
        String language;
        for (Locale l : availableLocales) {
            language = l.getDisplayLanguage();
            if (language.length() > 0 && !spinLangArray.contains(language)) {
                spinLangArray.add(language);
            }
        }
        Collections.sort(spinLangArray, String.CASE_INSENSITIVE_ORDER);
        spinLangArray.add(0, getString(R.string.any_language));

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinLangArray);
        spinAdapter.setDropDownViewResource(R.layout.spinner_item);
        langSpin.setAdapter(spinAdapter);

        //show message on start up
        status.setText(R.string.search_prompt);
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        Uri baseUrl = Uri.parse(GOOGLE_BOOK_API);

        //get selected language.
        String language;
        String languageSelected = langSpin.getSelectedItem().toString();
        if (languageSelected.contains(getString(R.string.any_language))) {
            language = "any";
        } else {
            language = toCode(languageSelected);
        }

        //get string from user's search
        String stringRequest = searchString.getText().toString().trim();
        String searchRequest = null; // initialise the string to be appended to the uri.

        //determine what the user wants to search (via the radio buttons)
        switch (selection.getCheckedRadioButtonId()) {
            case (R.id.free_text_button): //Free Text
                searchRequest = stringRequest;
                break;
            case (R.id.author_button): //Author
                searchRequest = "inauthor=" + stringRequest;
                break;
            case (R.id.title_button): //Title
                searchRequest = "intitle=" + stringRequest;
                break;
        }

        //build the uri, based on user input and selection
        Uri.Builder uriBuild = baseUrl.buildUpon();

        uriBuild.appendQueryParameter("API", String.valueOf(R.string.api_key));
        if (searchRequest.isEmpty()) {
            uriBuild.appendQueryParameter("q", "all");
        } else {
            uriBuild.appendQueryParameter("q", searchRequest);
        }
        uriBuild.appendQueryParameter("langRestrict", language);

        return new BookLoader(this, uriBuild.toString());
    }

    //get 2-letter code for the language selected.
    private String toCode(String languageSelected) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (languageSelected.equals(locale.getDisplayLanguage())) {
                return locale.getLanguage();
            }
        }
        throw new IllegalArgumentException("No language found: " + languageSelected);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        bookAdapter = new BookAdapter(this, R.layout.list_item, books);

        if (books.isEmpty()) {
            status.setText(R.string.no_results);
        } else {
        bookAdapter.notifyDataSetChanged();
        bookItem.setLayoutManager(layoutManager);
        bookItem.setAdapter(bookAdapter);
        statusHolder.setVisibility(View.GONE);}
    }

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<Book>> loader) {
        books.clear();
        bookAdapter.notifyDataSetChanged();
    }
}
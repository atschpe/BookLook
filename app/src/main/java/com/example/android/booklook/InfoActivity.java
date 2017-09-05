package com.example.android.booklook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InfoActivity extends AppCompatActivity {

    private static final String LOG_TAG = InfoActivity.class.getSimpleName(); //Tag for the log messages

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //book cover
        new LoadBookCover((ImageView) findViewById(R.id.info_cover)).execute(getIntent().getExtras()
                .getString("fullImage"));

        //title
        TextView infoTitle = (TextView) findViewById(R.id.info_title);
        String jsonTitle = getIntent().getExtras().getString("title");
        infoTitle.setText(jsonTitle);

        //author
        TextView infoAuthor = (TextView) findViewById(R.id.info_author);
        ArrayList<String> jsonAuthorList = getIntent().getExtras().getStringArrayList("author");
        if (jsonAuthorList.get(0).contains("XXX")) {
            infoAuthor.setText(R.string.author_empty);
        } else {
            String jsonAuthor = getString(R.string.by);
            for (int i = 0; i < jsonAuthorList.size(); i++) {
                String newAuthor = jsonAuthorList.get(i);
                jsonAuthor += newAuthor + getString(R.string.comma);
            }
            String editedAuthor = removePunctuation(jsonAuthor);
            infoAuthor.setText(editedAuthor);
        }

        //description
        TextView infoDescription = (TextView) findViewById(R.id.info_description);
        String jsonDescription = getIntent().getExtras().getString("description");
        assert jsonDescription != null;
        if (jsonDescription.contains("XXX")) {
            infoDescription.setText(R.string.description_empty);
        } else {
            infoDescription.setText(jsonDescription);
        }

        //publication date
        TextView infoDate = (TextView) findViewById(R.id.info_date);
        String jsonDate = getIntent().getExtras().getString("date");
        assert jsonDate != null;
        if (jsonDate.contains("XXX")) {
            infoDate.setVisibility(View.GONE);
        }
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat DesiredFormat = new SimpleDateFormat("MMM, yyyy");
        Date date;
        try {
            if (jsonDate.length() == 4) {
                infoDate.setText(jsonDate);
            } else {
                date = sourceFormat.parse(jsonDate);
                String formattedDate = DesiredFormat.format(date);
                infoDate.setText(formattedDate);
            }
        } catch (ParseException e) {
            Log.e(LOG_TAG, "parsing jsonDate: " + jsonDate, e);
        }

        //publisher
        TextView infoPublisher = (TextView) findViewById(R.id.info_publisher);
        String jsonPublisher = getIntent().getExtras().getString("publisher") + getString(R.string.comma);
        if (jsonPublisher.contains("XXX, ")) {
            infoPublisher.setVisibility(View.GONE);
        }
        infoPublisher.setText(jsonPublisher);

        //language
        TextView infoLanguage = (TextView) findViewById(R.id.info_language);
        String jsonLanguage = getIntent().getExtras().getString("language");
        Locale locale = new Locale(jsonLanguage);
        String nameLanguage = locale.getDisplayLanguage();
        infoLanguage.setText(nameLanguage);

        //link to google for further information
        ImageView infoGoogle = (ImageView) findViewById(R.id.info_Google);
        final String jsonUrl = getIntent().getExtras().getString("reader");
        infoGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToGoogle = new Intent(Intent.ACTION_VIEW, Uri.parse(jsonUrl));
                startActivity(goToGoogle);
            }
        });

        //category
        TextView infoCategory = (TextView) findViewById(R.id.info_category);
        ArrayList<String> jsonCategoryList = getIntent().getExtras().getStringArrayList("category");
        if (jsonCategoryList.get(0).contains("XXX")) {
            infoCategory.setVisibility(View.GONE);
        } else {
            String jsonCategory = "";
            for (int i = 0; i < jsonCategoryList.size(); i++) {
                String newCategory = jsonCategoryList.get(i);
                jsonCategory += newCategory + getString(R.string.newLine);
            }
            String editedCategory = removePunctuation(jsonCategory);
            infoCategory.setText(editedCategory);
        }
    }

    private String removePunctuation(String concatenatedJson) {
        if (concatenatedJson != null && concatenatedJson.length() > 0) {
            concatenatedJson = concatenatedJson.substring(0, concatenatedJson.length() - 2);
        }
        return concatenatedJson;
    }

    //Load book cover and display in the Image View.
    private class LoadBookCover extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public LoadBookCover(ImageView infoCover) {
            this.imageView = infoCover;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String coverUrl = urls[0];
            Bitmap bitCover = null;
            try {
                InputStream is = new java.net.URL(coverUrl).openStream();
                bitCover = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
            return bitCover;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
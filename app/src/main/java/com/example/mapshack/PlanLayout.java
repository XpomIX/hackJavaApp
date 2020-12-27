package com.example.mapshack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.mapshack.NetworkUtils.getResponseFromURL;

public class PlanLayout extends AppCompatActivity {

    SubsamplingScaleImageView imageView;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_layout);

        imageView = findViewById(R.id.image_view);
        Intent intent = getIntent();
        String id = intent.getStringExtra(MapsActivity.EXTRA_TEXT);
        String name = intent.getStringExtra(MapsActivity.EXTRA_NAME);

        GetImageLink getImageLink = new GetImageLink();
        getImageLink.execute("{\"id\": \"" + id + "\"}");

        this.setTitle(name);
    }

    private class LoadImage extends AsyncTask<String, Void, Bitmap> {
        SubsamplingScaleImageView imageView;

        public LoadImage(SubsamplingScaleImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bm = null;
            try {
                InputStream inputStream = new URL(url).openStream();
                bm = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                imageView.setImage(ImageSource.bitmap(bitmap));
            } catch (Exception e) {
                Toast.makeText(PlanLayout.this, R.string.error_loading_image, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(PlanLayout.this, R.string.loading, Toast.LENGTH_SHORT).show();
        }
    }

    private class GetImageLink extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = getResponseFromURL("api/shop/get", strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String imageUrl = jsonObject.getString("image");
                String description = jsonObject.getString("description");

                info = findViewById(R.id.infoView);
                info.setText(description);

                LoadImage loadImage = new LoadImage(imageView);
                loadImage.execute(imageUrl);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
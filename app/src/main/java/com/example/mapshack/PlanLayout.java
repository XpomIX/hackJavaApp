package com.example.mapshack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

        LoadData loadData = new LoadData();
        loadData.execute("{\"id\": \"" + id + "\"}");

        this.setTitle(R.string.loading);
    }

    private Bitmap loadImageBitmap(String imageUrl) {
        Bitmap bm = null;
        try {
            InputStream inputStream = new URL(imageUrl).openStream();
            bm = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    private class LoadImage extends AsyncTask<String, Void, Bitmap> {
        SubsamplingScaleImageView imageView;

        public LoadImage(SubsamplingScaleImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String response = strings[0];
            String imageUrl = null;
            try {
                JSONObject jsonObject = new JSONObject(response);
                imageUrl = jsonObject.getString("image");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Bitmap bm = loadImageBitmap(imageUrl);
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                imageView.setImage(ImageSource.bitmap(bitmap));
            } catch (Exception e) {
                Toast.makeText(PlanLayout.this, R.string.error_loading_image, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            try {
                response = getResponseFromURL("api/shop/get", strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                PlanLayout.this.setTitle(name);
                info = findViewById(R.id.infoView);
                info.setText(description);
                LoadImage loadImage = new LoadImage(imageView);
                loadImage.execute(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class PlanLayout extends AppCompatActivity {

    SubsamplingScaleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_layout);

        imageView = findViewById(R.id.image_view);
        Intent intent = getIntent();
        String position = intent.getStringExtra(MapsActivity.EXTRA_TEXT);
        LoadImage loadImage = new LoadImage(imageView);
        loadImage.execute("https://media.sproutsocial.com/uploads/2017/02/10x-featured-social-media-image-size.png");

        TextView info = findViewById(R.id.infoView);
        this.setTitle(position);
        info.setText(position);
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
            imageView.setImage(ImageSource.bitmap(bitmap));
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(PlanLayout.this, R.string.loading, Toast.LENGTH_LONG).show();
        }
    }
}
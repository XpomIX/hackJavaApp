package com.example.mapshack;

import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PlanLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_layout);

        Intent intent = getIntent();
        String position = intent.getStringExtra(MapsActivity.EXTRA_TEXT);

        TextView info = findViewById(R.id.infoView);
        info.setText(position);
    }
}
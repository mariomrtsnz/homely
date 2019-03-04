package com.mario.homely.ui.photos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mario.homely.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class FullscreenPhoto extends AppCompatActivity {
    List<String> gallery;
    ImageView image;
    ImageSwitcher imageSwitcher;
    Button next, prev;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gallery = getIntent().getStringArrayListExtra("gallery");
        url = getIntent().getStringExtra("selectedPhoto");
//        imageSwitcher = findViewById(R.id.imageSwitcher);
//        image = findViewById(R.id.fullscreen_image);
        Glide.with(this).load(url).into(image);
    }


}

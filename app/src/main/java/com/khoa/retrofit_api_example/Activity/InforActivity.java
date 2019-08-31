package com.khoa.retrofit_api_example.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.khoa.retrofit_api_example.R;

public class InforActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_infor);
        getSupportActionBar().hide();

//        showImage();
    }

    public void showImage(){
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Log.e("Loi",url);
    }
}

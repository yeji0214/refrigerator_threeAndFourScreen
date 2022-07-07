package com.example.textrecognitionex;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

// 음식 추가 화면
public class AddFoodActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        getSupportActionBar().setTitle("음식 추가하기");
    }
}
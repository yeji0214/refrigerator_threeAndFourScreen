package com.example.textrecognitionex;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ThemeActivity extends AppCompatActivity {
    Button button_theme_purple, button_theme_teal, button_theme_black, button_theme_white, button_return_from_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("테마"); // AppBar Title 변경

        button_theme_purple = findViewById(R.id.button_theme_purple);
        button_theme_teal = findViewById(R.id.button_theme_teal);
        button_theme_black = findViewById(R.id.button_theme_black);
        button_theme_white = findViewById(R.id.button_theme_white);
        button_return_from_setting = findViewById(R.id.button_return_from_setting);

        button_theme_purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF5F00FF));
                getSupportActionBar().setSplitBackgroundDrawable(new ColorDrawable(0xFF5F00FF));
            }
        });

        button_theme_teal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF00D8FF));
                //getSupportActionBar().setSplitBackgroundDrawable(new ColorDrawable(0xFF00D8FF));
                getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(0xFF00D8FF));
            }
        });

        button_theme_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
            }
        });

        button_theme_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
                // 글씨 색을 검은색으로 바꿔야함
            }
        });

        button_return_from_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

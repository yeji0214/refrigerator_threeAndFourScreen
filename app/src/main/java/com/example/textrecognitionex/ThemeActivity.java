package com.example.textrecognitionex;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ThemeActivity extends AppCompatActivity {
    Button button_theme_purple, button_theme_teal, button_theme_black, button_theme_pink, button_return_from_setting;
    int color = ((MainActivity)MainActivity.mContext).color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        if (color == 1)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFBB86FC));
        else if (color == 2)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF03DAC5));
        else if (color == 3)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
        else if (color == 4)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFB2D9));

        ActionBar ab = getSupportActionBar();
        ab.setTitle("테마"); // AppBar Title 변경

        button_theme_purple = findViewById(R.id.button_theme_purple);
        button_theme_teal = findViewById(R.id.button_theme_teal);
        button_theme_black = findViewById(R.id.button_theme_black);
        button_theme_pink = findViewById(R.id.button_theme_pink);
        button_return_from_setting = findViewById(R.id.button_return_from_setting);

        button_theme_purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFBB86FC));
                //getSupportActionBar().setSplitBackgroundDrawable(new ColorDrawable(0xFF5F00FF));
                ((MainActivity)MainActivity.mContext).color = 1;
            }
        });

        button_theme_teal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF03DAC5));
                //getSupportActionBar().setSplitBackgroundDrawable(new ColorDrawable(0xFF00D8FF));
                //getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(0xFF00D8FF));
                ((MainActivity)MainActivity.mContext).color = 2;
            }
        });

        button_theme_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
                ((MainActivity)MainActivity.mContext).color = 3;
            }
        });

        button_theme_pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFB2D9));
                ((MainActivity)MainActivity.mContext).color = 4;
            }
        });

        button_return_from_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 현재 선택된 색상을 리턴하는 함수 만들 것 -> 다른 액티비티에서 받아 app bar 색상 변경하도록.
    public int getColor() {
        return color;
    }
}

package com.example.textrecognitionex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ThemeActivity extends AppCompatActivity {
    Button button_theme_purple, button_theme_teal, button_theme_black, button_theme_pink, button_return_from_setting;
    int color = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        Intent intent = getIntent();
        color = intent.getExtras().getInt("theme");

        if (color == 1) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFBB86FC));
            getWindow().setStatusBarColor(0xFFA566FF);
        }
        else if (color == 2) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF03DAC5));
            getWindow().setStatusBarColor(0xFF3DB7CC);
        }
        else if (color == 3) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
            getWindow().setStatusBarColor(0xFF4C4C4C);
        }
        else if (color == 4) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFB2D9));
            getWindow().setStatusBarColor(0xFFEDA0C7);
        }

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
                getWindow().setStatusBarColor(0xFFA566FF);
                color = 1;
            }
        });

        button_theme_teal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF03DAC5));
                getWindow().setStatusBarColor(0xFF3DB7CC);
                color = 2;
            }
        });

        button_theme_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
                getWindow().setStatusBarColor(0xFF4C4C4C);
                color = 3;
            }
        });

        button_theme_pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFB2D9));
                getWindow().setStatusBarColor(0xFFEDA0C7);
                color = 4;
            }
        });

        button_return_from_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 문제 : 뒤로가기의 기능이 아님 단순히 새 액티비티 생성
                //finish();
                Intent intent = new Intent();
                intent.putExtra("theme", color);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    // 현재 선택된 색상을 리턴하는 함수 만들 것 -> 다른 액티비티에서 받아 app bar 색상 변경하도록.
//    public static int getColor() {
//        return color;
//    }
}

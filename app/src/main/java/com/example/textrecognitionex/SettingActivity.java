package com.example.textrecognitionex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

// 설정 버튼 눌렀을 때 화면
public class SettingActivity extends AppCompatActivity {
    Button button_theme, button_expirationdate_setting, button_notification, button_return;
    int color = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 문제 : ThemeActivity가 실행되기 전에 SettingActivity가 먼저 실행됨.
        //color = ((ThemeActivity)ThemeActivity.mContext).getColor();
        color = ((MainActivity)MainActivity.mContext).color;

        //Toast.makeText(getApplicationContext(),((MainActivity)MainActivity.mContext).color,Toast.LENGTH_SHORT).show();

        if (color == 1)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFBB86FC));
        else if (color == 2)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF03DAC5));
        else if (color == 3)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
        else if (color == 4)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFB2D9));

        ActionBar ab = getSupportActionBar();
        ab.setTitle("설정"); // AppBar Title 변경

        button_theme = findViewById(R.id.button_theme);
        button_expirationdate_setting = findViewById(R.id.button_expirationdate_setting);
        button_notification = findViewById(R.id.button_notification);
        button_return = findViewById(R.id.button_return);

        // 테마 바꾸기 (오류)
        button_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_theme = new Intent(v.getContext(), ThemeActivity.class);
                intent_theme.putExtra("state", "kill");
                intent_theme.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_theme);
            }
        });

        // 유통기한 알림 설정 (스위치 이용)
        button_expirationdate_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_theme = new Intent(v.getContext(), ExpirationDateSettingActivity.class);
                intent_theme.putExtra("state", "kill");
                intent_theme.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_theme);
            }
        });

        // 알림 끄기
        button_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        // 돌아가기
        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림 설정");
        builder.setMessage("알림을 끌까요?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"푸시 알림이 꺼졌습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"푸시 알림을 끄지 않습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}

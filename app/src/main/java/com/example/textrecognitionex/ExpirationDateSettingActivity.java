package com.example.textrecognitionex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExpirationDateSettingActivity extends AppCompatActivity {
    Switch one_day_ago, three_day_ago, five_day_ago, seven_day_ago, all;
    Button button_return_from_expiration_date_setting;
    int color, switch_one_day_ago, switch_three_day_ago, switch_five_day_ago, switch_seven_day_ago, switch_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiration_date_setting);
        getSupportActionBar().setTitle("알림 설정");

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

        one_day_ago = findViewById(R.id.one_day_ago_alarm_switch);
        three_day_ago = findViewById(R.id.three_day_ago_alarm_switch);
        five_day_ago = findViewById(R.id.five_day_ago_alarm_switch);
        seven_day_ago = findViewById(R.id.seven_day_ago_alarm_switch);
        all = findViewById(R.id.all_alarm_switch);
        button_return_from_expiration_date_setting = findViewById(R.id.button_return_from_expiration_date_setting);

        switch_one_day_ago = intent.getExtras().getInt("alarm_one_day_age");
        switch_three_day_ago = intent.getExtras().getInt("alarm_three_day_age");
        switch_five_day_ago = intent.getExtras().getInt("alarm_five_day_age");
        switch_seven_day_ago = intent.getExtras().getInt("alarm_seven_day_age");
        switch_all = intent.getExtras().getInt("alarm_all");

        // 스위치를 켠 상태로 저장했다면 나갔다 들어와도 켜져있도록
        if (switch_one_day_ago == 1)
            one_day_ago.setChecked(true);
        if (switch_three_day_ago == 1)
            three_day_ago.setChecked(true);
        if (switch_five_day_ago == 1)
            five_day_ago.setChecked(true);
        if (switch_seven_day_ago == 1)
            seven_day_ago.setChecked(true);
//        if (switch_all == 0)
//            all.setChecked(false);
        if (switch_all == 1)
            all.setChecked(true);

        one_day_ago.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    switch_one_day_ago = 1;
                }else {
                    switch_one_day_ago = 0;
                    if (switch_all == 1) {
                        switch_all = 2; // all 스위치만 꺼지도록
                        all.setChecked(false);
                    }
                }
            }
        });

        three_day_ago.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    switch_three_day_ago = 1;
                }else {
                    switch_three_day_ago = 0;
                    if (switch_all == 1) {
                        switch_all = 2;
                        all.setChecked(false);
                    }
                }
            }
        });

        five_day_ago.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    switch_five_day_ago = 1;
                }else {
                    switch_five_day_ago = 0;
                    if (switch_all == 1) {
                        switch_all = 2;
                        all.setChecked(false);
                    }
                }
            }
        });

        seven_day_ago.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    switch_seven_day_ago = 1;
                }else {
                    switch_seven_day_ago = 0;
                    if (switch_all == 1) {
                        switch_all = 2;
                        all.setChecked(false);
                    }
                }
            }
        });

        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    switch_all = 1;
                    if (switch_one_day_ago == 0) {
                        switch_one_day_ago = 1;
                        one_day_ago.setChecked(true);
                    }
                    if (switch_three_day_ago == 0) {
                        switch_three_day_ago = 1;
                        three_day_ago.setChecked(true);
                    }
                    if (switch_five_day_ago == 0) {
                        switch_five_day_ago = 1;
                        five_day_ago.setChecked(true);
                    }
                    if (switch_seven_day_ago == 0){
                        switch_seven_day_ago = 1;
                        seven_day_ago.setChecked(true);
                    }
                }else {
                    if (switch_all != 2) {
                        switch_all = 0;
                        if (switch_one_day_ago == 1) {
                            switch_one_day_ago = 0;
                            one_day_ago.setChecked(false);
                        }
                        if (switch_three_day_ago == 1) {
                            switch_three_day_ago = 0;
                            three_day_ago.setChecked(false);
                        }
                        if (switch_five_day_ago == 1) {
                            switch_five_day_ago = 0;
                            five_day_ago.setChecked(false);
                        }
                        if (switch_seven_day_ago == 1) {
                            switch_seven_day_ago = 0;
                            seven_day_ago.setChecked(false);
                        }
                    }
                }
            }
        });

        button_return_from_expiration_date_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("alarm_one_day_age", switch_one_day_ago);
                intent.putExtra("alarm_three_day_age", switch_three_day_ago);
                intent.putExtra("alarm_five_day_age", switch_five_day_ago);
                intent.putExtra("alarm_seven_day_age", switch_seven_day_ago);
                intent.putExtra("alarm_all", switch_all);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}

package com.example.cardview;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * creation date: 2020/3/31 上午9:24
 * description ：
 */
public class TestOneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testone_card);
        final View testName = findViewById(R.id.tv_text);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testName.setVisibility(View.VISIBLE);
                findViewById(R.id.btn).setVisibility(View.GONE);
                findViewById(R.id.btn_colle).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.btn_colle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testName.setVisibility(View.GONE);
            }
        });
    }
}

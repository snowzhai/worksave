package com.android.znh.worksave.lambda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.znh.worksave.R;

/**
 * author:znh
 * time:2017年5月17日
 * description：jdk1.8新特性之lambda表达式及在Android Studio中的使用举例
 */
public class LambdaActivity extends AppCompatActivity {

    private View btn_lam_an;
    private View btn_lam_antwo;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG = getPackageName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lambda);
        btn_lam_an = findViewById(R.id.btn_lam_an);
        btn_lam_antwo = findViewById(R.id.btn_lam_antwo);

        //lambda表达式使用之前
        btn_lam_an.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LambdaActivity.this, "hello word!", Toast.LENGTH_SHORT);
            }
        });
        //lambda表达式使用之后
        btn_lam_antwo.setOnClickListener(view -> Toast.makeText(LambdaActivity.this, "hello word!", Toast.LENGTH_SHORT));


        //lambda表达式使用之前
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "hello word!");
            }
        }).start();
        //lambda表达式使用之后
        new Thread(() -> {
            Log.i(TAG, "hello word!");
        }).start();


    }
}

package com.android.znh.worksave;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.znh.worksave.priactice.Retrofit.RetrofitActivity;
import com.android.znh.worksave.priactice.RxJavaActivity;

public class MainActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_lam_lam = (Button) findViewById(R.id.btn_main_lam);
        btn_lam_lam.setOnClickListener(this);
        Button btn_main_Retrofit = (Button) findViewById(R.id.btn_main_Retrofit);
        btn_main_Retrofit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_main_lam:
                startActivity(new Intent(MainActivity.this, RxJavaActivity.class));
                break;
            case R.id.btn_main_Retrofit:
                startActivity(new Intent(MainActivity.this, RetrofitActivity.class));
                break;
        }

    }
}

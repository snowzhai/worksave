package com.android.znh.worksave;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.znh.worksave.lambda.RxJavaActivity;

public class MainActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_lam_lam = (Button) findViewById(R.id.btn_lam_lam);
        btn_lam_lam.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_lam_lam:
                startActivity(new Intent(MainActivity.this, RxJavaActivity.class));
                break;
        }

    }
}

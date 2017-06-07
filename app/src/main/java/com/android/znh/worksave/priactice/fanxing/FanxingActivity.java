package com.android.znh.worksave.priactice.fanxing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.znh.worksave.BaseActivity;
import com.android.znh.worksave.R;
//泛型测试页面
public class FanxingActivity extends BaseActivity  {

    private Fanxing fanxing;
    private Button btn_fanxing_sshow;
    private Button btn_fanxing_ishow;
    private Button btn_fanxing_bshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanxing);
        fanxing = new Fanxing(1234,this);
        initView();
    }

    private void initView() {
        btn_fanxing_sshow = (Button) findViewById(R.id.btn_fanxing_sshow);
        btn_fanxing_sshow.setOnClickListener(this);
        btn_fanxing_ishow = (Button) findViewById(R.id.btn_fanxing_ishow);
        btn_fanxing_ishow.setOnClickListener(this);
        btn_fanxing_bshow = (Button) findViewById(R.id.btn_fanxing_bshow);
        btn_fanxing_bshow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fanxing_sshow:
                fanxing.show("sting");
                break;
            case R.id.btn_fanxing_ishow:
                fanxing.show(111222);
                break;
            case R.id.btn_fanxing_bshow:
                fanxing.show(true);
                break;
        }
    }
}

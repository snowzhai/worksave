package com.android.znh.worksave.priactice.plug;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.znh.worksave.BaseActivity;
import com.android.znh.worksave.R;
/**
 * author:znh
 * time:2017年6月6日
 * plug:LayoutCreator
 * description：测试使用BorePlugin-给不想用butterknife又不想写findviewbyid的人-高效的生成findViewById
 */
public class BorePlugin extends BaseActivity  {

    private Button btn_bore_send;
    private TextView tv_bore_showm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bore_plugin);
        initView();
    }

    private void initView() {
        btn_bore_send = (Button) findViewById(R.id.btn_bore_send);
        tv_bore_showm = (TextView) findViewById(R.id.tv_bore_showm);

        btn_bore_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bore_send:

                break;
        }
    }
}

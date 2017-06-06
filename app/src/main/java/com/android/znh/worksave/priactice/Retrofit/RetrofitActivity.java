package com.android.znh.worksave.priactice.Retrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.znh.worksave.BaseActivity;
import com.android.znh.worksave.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends BaseActivity {

    private Button btn_retro_getdata;
    private TextView tv_retro_showdata;
    private MyRetrofit.BlogService blogService;
    private Call<ResponseBody> call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        btn_retro_getdata = (Button) findViewById(R.id.btn_retro_getdata);
        btn_retro_getdata.setOnClickListener(this);
        tv_retro_showdata = (TextView) findViewById(R.id.tv_retro_showdata);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_retro_getdata:
                getdata();
                break;

        }
    }

    public void getdata() {
        blogService = new MyRetrofit(null).createRetrofit();
        call = blogService.getBlog();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    tv_retro_showdata.setText(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}

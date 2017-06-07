package com.android.znh.worksave.priactice.fanxing;

import android.content.Context;
import android.widget.Toast;

/**
 * author:znh
 * time:2017年6月7日
 * description：泛型的使用操作
 */

//泛型类：
public class Fanxing<T> implements inter{
    Context con;
    T mvalue;
    public Fanxing(T value,Context context) {
        con=context;
        mvalue=value;
    }

    //泛型方法：
    public <T> void show(T t){
        Toast.makeText(con,t+"-"+mvalue,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showmes(Object o) {
        Toast.makeText(con,o+"-"+mvalue,Toast.LENGTH_LONG).show();

    }
}
    //泛型接口：
     interface inter<T>{
         abstract void showmes(T t);
    }

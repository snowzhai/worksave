package com.android.znh.worksave.lambda;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.znh.worksave.R;

import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * author:  znh
 * time:    2017/5/18.
 * despcraption: RxJava 详解——简洁的异步操作 类似于 AsyncTask / Handler  等的异步操作
 */

/*
API 介绍和原理简析——
    1. 概念：扩展的观察者模式
        RxJava 的观察者模式
        RxJava 有四个基本概念：Observable (可观察者，即被观察者)、 Observer (观察者)、 subscribe (订阅)、事件。
    2. 基本实现
        基于以上的概念， RxJava 的基本实现主要有三点：
            1) 创建 Observer
            2) 创建 Observable
            3) Subscribe (订阅)
* */
public class RxJavaActivity extends AppCompatActivity {

    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        tag = getPackageName();
        //1) 创建 Observer
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {
                Log.i(tag, "complete");
            }

            @Override
            public void onError(Throwable throwable) {
                Log.i(tag, "onError");
            }

            @Override
            public void onNext(String s) {
                Log.i(tag, "onNext" + s);
            }

        };
        //除了 Observer 接口之外，RxJava 还内置了一个实现了 Observer 的抽象类：Subscriber。
        // Subscriber 对 Observer 接口进行了一些扩展，但他们的基本使用方式是完全一样的：
        Subscriber<String> stringSubscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(String s) {

            }

            //onStart(): 这是 Subscriber 增加的方法。它会在 subscribe 刚开始，而事件还未发送之前被调用，可以用于做一些准备工作，
            // 例如数据的清零或重置。这是一个可选方法，默认情况下它的实现为空。需要注意的是，如果对准备工作的线程有要求（例如弹出一个显示进
            // 度的对话框，这必须在主线程执行）， onStart() 就不适用了，因为它总是在 subscribe 所发生的线程被调用，而不能指定线程。
            // 要在指定的线程来做准备工作，可以使用 doOnSubscribe() 方法，具体可以在后面的文中看到。
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void setProducer(Producer p) {
                super.setProducer(p);
            }

        };

        //2) 创建 Observable
        //Observable 即被观察者，它决定什么时候触发事件以及触发怎样的事件。 RxJava 使用 create() 方法来创建一个 Observable ，并为它定义事件触发规则：
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello word");
                subscriber.onNext("Hi");
                subscriber.onNext("bob");
                subscriber.onCompleted();
            }
        });
        //3) Subscribe (订阅)
        observable.subscribe(observer);
        // 或者：
        observable.subscribe(stringSubscriber);


        //除了 subscribe(Observer) 和 subscribe(Subscriber) ，subscribe() 还支持不完整定义的回调，RxJava 会自动根据定义创建出 Subscriber 。形式如下：
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {

            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        };
        Action0 onComplateAction = new Action0() {
            @Override
            public void call() {

            }
        };
        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);
        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onComplateAction);

        //这段代码中出现的 Action1 和 Action0。 Action0 是 RxJava 的一个接口，它只有一个方法 call()，这个方法是无参无返回值的；
        // 由于 onCompleted() 方法也是无参无返回值的，因此 Action0 可以被当成一个包装对象，将 onCompleted() 的内容打包起来将自己
        // 作为一个参数传入 subscribe() 以实现不完整定义的回调。这样其实也可以看做将 onCompleted() 方法作为参数传进了 subscribe()，
        // 相当于其他某些语言中的『闭包』。 Action1 也是一个接口，它同样只有一个方法 call(T param)，这个方法也无返回值，但有一个参数；
        // 与 Action0 同理，由于 onNext(T obj) 和 onError(Throwable error) 也是单参数无返回值的，因此 Action1 可以将 onNext(obj)
        // 和 onError(error) 打包起来传入 subscribe() 以实现不完整定义的回调。事实上，虽然 Action0 和 Action1 在 API 中使用最广泛，
        // 但 RxJava 是提供了多个 ActionX 形式的接口 (例如 Action2, Action3) 的，它们可以被用以包装不同的无返回值的方法。

        //a. 打印字符串数组
        //将字符串数组 names 中的所有字符串依次打印出来：
        String[] names ={"1","2","3","4","5","6","7","8"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        Log.d(tag, name);
                    }
                });
        //b. 由 id 取得图片并显示
        //由指定的一个 drawable 文件 id drawableRes 取得图片，并显示在 ImageView 中，并在出现异常的时候打印 Toast 报错：
        int drawableRes = R.mipmap.ic_launcher;
        ImageView imageView = (ImageView) findViewById(R.id.img_rx_show);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

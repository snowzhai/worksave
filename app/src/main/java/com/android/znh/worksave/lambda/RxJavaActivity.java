package com.android.znh.worksave.lambda;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.znh.worksave.BaseActivity;
import com.android.znh.worksave.R;

import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
    3. 线程控制 —— Scheduler
     Observable.from(names)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
    4. 变换
* */
public class RxJavaActivity extends BaseActivity {

    private String tag;
    private TextView tv_rx_show;
    private Button btn_rx_shownumber;
    private Button btn_rx_changeimage;
    String finalname="";
    private ImageView img_rx_show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        tag = getPackageName();

        tv_rx_show = (TextView) findViewById(R.id.tv_rx_show);
        btn_rx_shownumber = (Button) findViewById(R.id.btn_rx_shownumber);
        btn_rx_shownumber.setOnClickListener(this);
        btn_rx_changeimage = (Button) findViewById(R.id.btn_rx_changeimage);
        btn_rx_changeimage.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_rx_shownumber:
                Log.i(tag,"printzifu");
                printzifu();
                break;
            case R.id.btn_rx_changeimage:
                changepic();
                break;
        }
    }

    //实例
    public void functionone(){
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

    }

    //打印字符串
    public void printzifu(){

//        Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
//        Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
//        Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
//        Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
//        另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。

        //a. 打印字符串数组
        //将字符串数组 names 中的所有字符相加串依次打印出来：
        String[] names ={"1","2","3","4","5","6","7","8","9","10","11"};
        Observable.from(names)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        Log.i(tag, name);
                        finalname+=name;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        tv_rx_show.setText(finalname);
                    }
                });
    }
    //利用异步线程 更换图片
    private void changepic() {
        //b. 由 id 取得图片并显示
        //由指定的一个 drawable 文件 id drawableRes 取得图片，并显示在 ImageView 中，并在出现异常的时候打印 Toast 报错：
        img_rx_show = (ImageView) findViewById(R.id.img_rx_show);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(R.mipmap.woailuo);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onNext(Drawable drawable) {
                        img_rx_show.setImageDrawable(drawable);
                    }

                    @Override
                    public void onCompleted() {
                        Toast.makeText(RxJavaActivity.this,"完事了", Toast.LENGTH_SHORT).show();;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //变换  输入一个字符串  输出一个bitmap
    public void change(){
//        1) API
//        首先看一个 map() 的例子：

        Observable.just("images/logo.png")
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        return getBitmapFromPath(s);
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        showbitmap(bitmap);
                    }
        });
    }
    public void showbitmap( Bitmap bitmap) {
        img_rx_show.setImageBitmap(bitmap);
    }
    public Bitmap getBitmapFromPath(String s) {
        Bitmap bmp = Bitmap.createBitmap(null);
        return bmp;
    }

//    public String changstudent(){
//        Student[] students = ...;
//        Subscriber<String> subscriber = new Subscriber<String>() {
//            @Override
//            public void onNext(String name) {
//                Log.d(tag, name);
//            }
//    ...
//        };
//        Observable.from(students)
//                .map(new Func1<Student, String>() {
//                    @Override
//                    public String call(Student student) {
//                        return student.getName();
//                    }
//                })
//                .subscribe(subscriber);
//
//
//
//        Student[] students = ...;
//        Subscriber<Course> subscriber = new Subscriber<Course>() {
//            @Override
//            public void onNext(Course course) {
//                Log.d(tag, course.getName());
//            }
//    ...
//        };
//        Observable.from(students)
//                .flatMap(new Func1<Student, Observable<Course>>() {
//                    @Override
//                    public Observable<Course> call(Student student) {
//                        return Observable.from(student.getCourses());
//                    }
//                })
//                .subscribe(subscriber);
//
//
//        Student[] students = ...;
//        Subscriber<Student> subscriber = new Subscriber<Student>() {
//            @Override
//            public void onNext(Student student) {
//                List<Course> courses = student.getCourses();
//                for (int i = 0; i < courses.size(); i++) {
//                    Course course = courses.get(i);
//                    Log.d(tag, course.getName());
//                }
//            }
//    ...
//        };
//        Observable.from(students)
//                .subscribe(subscriber);
//    }

}

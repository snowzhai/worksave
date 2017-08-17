package com.zjxd.functions.task.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zjxd.functions.R;
import com.zjxd.functions.task.TaskFragment;
import com.zjxd.functions.task.Taskdata;
import com.zjxd.functions.task.help.mSqliteHelp;
import com.zjxd.functions.utils.ShowUtils;
import com.zjxd.functions.utils.StringUtils;

/**
 * 后台服务 用来监听 时间的变化  从而进行任务没有完成的提醒
 */

public class MyService extends Service {

    private mSqliteHelp mHelp;

    @Override
    public void onCreate() {
        super.onCreate();
        ShowUtils.i("wordsave"," onCreate");
        createBrocastReceive();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        ShowUtils.i("wordsave"," onBind");

        return null;
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        ShowUtils.i("wordsave"," onStartCommand");

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
//                耗时的操作
            }
        }).run();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShowUtils.i("wordsave"," onDestroy");
    }

    private void createBrocastReceive() {
        //接收时间变化的时间广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);//系统每分钟会发出该广播
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(mbroadcastReceiver, filter);
    }

    private BroadcastReceiver mbroadcastReceiver = new BroadcastReceiver() {

        private Taskdata[] data;

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.ACTION_TIME_TICK.equals(intent.getAction())){
                ShowUtils.i("wordsave"," 时间变化了222233334444455556666666677778888822299911112222333311");
                mHelp = new mSqliteHelp(context);
                data = mHelp.queryTask(StringUtils.formatDateNow("yyyyMMdd"),1,2);
                ShowNotifition(context,data.length);

            }
        }

        private void ShowNotifition(Context context,int number) {
            Intent notificationIntent = new Intent(context, TaskFragment.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            Notification noti = new Notification.Builder(context)
                    .setContentTitle("还有"+number+"个重要的任务没有完成。")
                    .setContentText("Message")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setLights(0xffff0000,0,2000)
                    .setVibrate(new long[]{0,1000,0,1000})
                    .setSound( RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .build();
            startForeground(12346, noti);
        }
    };
}


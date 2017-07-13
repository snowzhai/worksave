package com.zjxd.functions.task.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zjxd.functions.task.service.MyService;

/**
 * 开机启动的广播  用来开启服务进行  时间的监听
 */

public class TurnOnReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MyService.class));
    }
}

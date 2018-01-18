package com.yima.testnotification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by loi on 2018/1/18.
 */

public class VVServer extends Service{
    private Timer timer;
    private int curLeftTime;
    public static int wakeMainActivityTime;//全局变量

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        curLeftTime = wakeMainActivityTime;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               if(curLeftTime<=0)
               {
                   curLeftTime = wakeMainActivityTime;
//                   Intent intent = new Intent(VVServer.this,****.class);
//                   VVServer.this.startActivity(intent);
               }
                curLeftTime --;
            }
        },1000);
    }
}

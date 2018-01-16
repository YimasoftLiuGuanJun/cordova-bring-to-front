package com.yima.testnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by loi on 2018/1/8.
 */


public class OnePixelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {    //屏幕关闭启动1像素Activity
            Toast.makeText(context,"OnePixelReceiver:屏幕关闭启动1像素Activity ", Toast.LENGTH_SHORT).show();
            Log.e("OnePixelReceiver","屏幕关闭启动1像素Activity");
            Intent it = new Intent(context, OnePiexlActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {   //屏幕打开 结束1像素
            Log.e("OnePixelReceiver","屏幕打开结束1像素");
            Toast.makeText(context,"OnePixelReceiver:屏幕打开结束1像素", Toast.LENGTH_SHORT).show();
            context.sendBroadcast(new Intent("finish"));
            Intent main = new Intent(Intent.ACTION_MAIN);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            main.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(main);
        }
    }
}
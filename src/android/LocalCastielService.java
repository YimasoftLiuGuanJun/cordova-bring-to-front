package com.yima.testnotification;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class LocalCastielService extends Service {

    MyBinder myBinder;
    private PendingIntent pintent;
    MyServiceConnection myServiceConnection;
    private int i = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("LocalCastielService", "onCreate");
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        myServiceConnection = new MyServiceConnection();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                i++;
                if(i>=360){
                    startActivity(new Intent(LocalCastielService.this,MainActivity.class));
                    i=0;
                }
                Message message = new Message();
                message.what = i;
                handler.sendMessage(message);
            }
        }, 0, 10000);
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.e("LocalCastielService", String.valueOf(msg.what));
           // Toast.makeText(LocalCastielService.this, "LocalCastielService: "+String.valueOf(msg.what), Toast.LENGTH_SHORT).show();
            return true;
        }
    });

    /**
     * 显示一个普通的通知
     *
     * @param context 上下文
     */
    public void showNotification(Context context,int startId) {
        Log.e("LocalCastielService", "显示一个普通的通知");
        Notification notification = new NotificationCompat.Builder(context)
                /**设置通知左边的大图标**/
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                /**设置通知右边的小图标**/
                .setSmallIcon(R.mipmap.ic_launcher)
                /**通知首次出现在通知栏，带上升动画效果的**/
                .setTicker("保活服务1")
                /**设置通知的标题**/
                .setContentTitle("保活服务")
                /**设置通知的内容**/
                .setContentText("点击跳转到MainActivity")
                /**通知产生的时间，会在通知信息里显示**/
                .setWhen(System.currentTimeMillis())
                /**设置该通知优先级**/
                .setPriority(Notification.PRIORITY_DEFAULT)
                /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                .setAutoCancel(true)
                /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                .setOngoing(false)
                /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(context, 2, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
        //NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        /**发起通知**/
        //notificationManager.notify(0, notification);
        Log.e("LocalCastielService","notifyId"+String.valueOf(startId));
        startForeground(startId, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this, RemoteCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);
        Log.e("LocalCastielService", "绑定RemoteCastielService服务");
        showNotification(this,startId );
        return START_STICKY;
    }

    public  class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            Log.e("LocalCastielService", "远程服务连接成功");
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.e("LocalCastielService", "远程服务Remote被干掉");
            // 连接出现了异常断开了，RemoteService被杀掉了
            Toast.makeText(LocalCastielService.this, "远程服务Remote被干掉", Toast.LENGTH_LONG).show();
            // 启动RemoteCastielService
            LocalCastielService.this.startService(new Intent(LocalCastielService.this, RemoteCastielService.class));
            LocalCastielService.this.bindService(new Intent(LocalCastielService.this, RemoteCastielService.class),
                    myServiceConnection, Context.BIND_IMPORTANT);
        }

    }

    static class MyBinder extends CastielProgressConnection.Stub {
        @Override
        public String getProName() throws RemoteException {
            return "Local";
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return myBinder;
    }

    @Override
    public void onDestroy() {
        this.unbindService(myServiceConnection);
        super.onDestroy();
    }
}
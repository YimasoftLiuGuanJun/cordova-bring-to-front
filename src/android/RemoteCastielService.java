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

/**
 *
 * @ClassName: RemoteCastielService
 * @Description: 远程服务
 * @author 猴子搬来的救兵 http://blog.csdn.net/mynameishuangshuai
 */
public class RemoteCastielService extends Service {
    MyBinder myBinder;
    private PendingIntent pintent;
    MyServiceConnection myServiceConnection;
    private final static int GRAY_SERVICE_ID = 1001;
    private int i =0;
    @Override
    public void onCreate() {
        super.onCreate();
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        myServiceConnection = new MyServiceConnection();
        Log.e("RemoteCastielService", "onCreate");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e("RemoteCastielService", "");
                Message message = new Message();
                message.what = i;
                handler.sendMessage(message);
                i++;
            }
        }, 0, 15000);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.e("LocalCastielService", String.valueOf(msg.what));
            Toast.makeText(RemoteCastielService.this,"RemoteCastielService: "+ String.valueOf(msg.what), Toast.LENGTH_SHORT).show();
            return true;
        }
    });

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("RemoteCastielService", "启动LocalCastielService服务");
        Toast.makeText(RemoteCastielService.this, "启动LocalCastielService服务", Toast.LENGTH_LONG).show();
        this.bindService(new Intent(this,LocalCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);

        showNotification(RemoteCastielService.this,startId);

        //设置service为前台进程，避免手机休眠时系统自动杀掉该服务
//        startForeground(startId, notification);
        return START_STICKY;
    }

    /**
     * 显示一个普通的通知
     *
     * @param context 上下文
     */
    public void showNotification(Context context,int startId) {
        Log.e("RemoteCastielService", "显示一个普通的通知");
        Notification notification = new NotificationCompat.Builder(context)
                /**设置通知左边的大图标**/
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                /**设置通知右边的小图标**/
                .setSmallIcon(R.mipmap.ic_launcher)
                /**通知首次出现在通知栏，带上升动画效果的**/
                .setTicker("远程服务")
                /**设置通知的标题**/
                .setContentTitle("守护服务")
                /**设置通知的内容**/
                .setContentText("点击也跳转到MaintActivity")
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
                .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//        /**发起通知**/
//        notificationManager.notify(0, notification);

        Log.e("RemoteCastielService","notifyId"+String.valueOf(GRAY_SERVICE_ID));
        startForeground(GRAY_SERVICE_ID, notification);

    }


    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            Log.e("RemoteCastielService", "本地服务连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.e("RemoteCastielService", "本地服务Local被干掉");
            // 连接出现了异常断开了，LocalCastielService被杀死了
            Toast.makeText(RemoteCastielService.this, "本地服务Local被干掉", Toast.LENGTH_LONG).show();
            // 启动LocalCastielService
            RemoteCastielService.this.startService(new Intent(RemoteCastielService.this,LocalCastielService.class));
            RemoteCastielService.this.bindService(new Intent(RemoteCastielService.this,LocalCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);
        }

    }

        class MyBinder extends CastielProgressConnection.Stub {

            @Override
            public String getProName() throws RemoteException {
                return "";
            }

        }

        @Override
        public IBinder onBind(Intent arg0) {
            return myBinder;
        }
}
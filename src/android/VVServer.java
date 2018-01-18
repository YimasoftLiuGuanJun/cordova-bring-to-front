package in.lucasdup.bringtofront;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by loi on 2018/1/18.
 */

public class VVServer extends Service{
    private Timer timer;
    private int curLeftTime;
    public static int wakeMainActivityTime = 60;//全局变量

  @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(VVServer.this,"VVServer-onBind",Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(VVServer.this,"VVServer-onStartCommand",Toast.LENGTH_LONG).show();

        if(timer!=null){
            curLeftTime = wakeMainActivityTime;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(curLeftTime<=0)
                    {
                        Toast.makeText(VVServer.this,"时间到了",Toast.LENGTH_LONG).show();
                        curLeftTime = wakeMainActivityTime;
//                   Intent intent = new Intent(VVServer.this,****.class);
//                   VVServer.this.startActivity(intent);
                    }
                    curLeftTime --;
                }
            },1000);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(VVServer.this,"VVServer-onDestroy",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(VVServer.this,"VVServer-onCreate",Toast.LENGTH_LONG).show();

        if(timer!=null){
            curLeftTime = wakeMainActivityTime;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(curLeftTime<=0)
                    {
                        Toast.makeText(VVServer.this,"时间到了",Toast.LENGTH_LONG).show();
                        curLeftTime = wakeMainActivityTime;
//                   Intent intent = new Intent(VVServer.this,****.class);
//                   VVServer.this.startActivity(intent);
                    }
                    curLeftTime --;
                }
            },1000);
        }

    }
}

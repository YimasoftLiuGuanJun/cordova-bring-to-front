package in.lucasdup.bringtofront;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
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

    
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Log.e("LocalCastielService", String.valueOf(msg.what));
                    Toast.makeText(VVServer.this,"时间到了",Toast.LENGTH_LONG).show();
                    break;
                case 2:
//                     Log.e("LocalCastielService", String.valueOf(msg.what));
//                     Toast.makeText(VVServer.this,"定时器。。。。",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    BringToFront.executeGlobalJavascript("alert('你好啊')");
                    break;
            }
            return true;
        }
    });
    
  @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(VVServer.this,"VVServer-onBind",Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(VVServer.this,"VVServer-onStartCommand",Toast.LENGTH_LONG).show();

        if(timer == null){
            curLeftTime = wakeMainActivityTime;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message0 = new Message();
                    message0.what = 2;
                    handler.sendMessage(message0);

                    if(curLeftTime<=0)
                    {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        curLeftTime = wakeMainActivityTime;
//                         Intent intent = new Intent(VVServer.this,com.phonegap.helloworld.tn.class);
//                         VVServer.this.startActivity(intent);
                    }
                    curLeftTime --;
                }
            },0,1000);
        }

        return START_STICKY;
//         return super.onStartCommand(intent, flags, startId);
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
        
     
        if(timer == null){
            curLeftTime = wakeMainActivityTime;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message0 = new Message();
                    message0.what = 2;
                    handler.sendMessage(message0);

                    if(curLeftTime<=0)
                    {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        curLeftTime = wakeMainActivityTime;
//                         Intent intent = new Intent(VVServer.this,com.phonegap.helloworld.tn.class);
//                         VVServer.this.startActivity(intent);
                        
                        
                          Message messages = new Message();
                          messages.what = 3;
                          handler.sendMessage(messages);
                    }
                    curLeftTime --;
                }
            },0,1000);
        }

    }
}

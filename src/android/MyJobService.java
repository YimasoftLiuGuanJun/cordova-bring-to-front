package in.lucasdup.bringtofront;

import android.app.ActivityManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by loi on 2018/1/8.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(MyJobService.this, "MyJobService", Toast.LENGTH_LONG).show();
            JobParameters param = (JobParameters) msg.obj;
            jobFinished(param, true);

            /*
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            */
            Log.e("MyJobService", "JobServer---启动服务");
            
//             Toast.makeText(MyJobService.this,"MyJobService》》》》》》啥都没干", Toast.LENGTH_SHORT).show();
            
            //启动一个服务
            if(!isServiceWork(getApplicationContext(),"in.lucasdup.bringtofront.VVServer")){
               Intent i = new Intent(getApplicationContext(), VVServer.class);
               startService(i);
                Log.e("MyJobService", "开始启动服务");
            }else {
                Log.e("MyJobService", "服务已启动");
            }
            return true;
        }
    });

    // 判断服务是否正在运行
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e("MyJobService", "onStartJob");
        Message m = Message.obtain();
        m.obj = jobParameters;
        handler.sendMessage(m);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e("MyJobService", "onStopJob");
        handler.removeCallbacksAndMessages(null);
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("MyJobService", "onStartCommand");
        return START_STICKY;
    }
}

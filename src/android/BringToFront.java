package in.lucasdup.bringtofront;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.PendingIntent;
import android.util.Log;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;

import in.lucasdup.bringtofront.OnePixelReceiver;

/**
 * This class echoes a string called from JavaScript.
 */
public class BringToFront extends CordovaPlugin {
      private Context mContext  = cordova.getActivity();
  
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    Log.d("Bring", "action is:" + action);
    Log.e("jumpStartInterface" , "Test");
    
    if (action.equals("GotoAutoStartPage")){
       jumpStartInterface();
       return true;
    }else if(action.equals("StartJobServers")){
      StartJobServer();
    }else if(action.equals("OnePixelToKeepAlive")){
      registerScnOnAndOffBroadcast();
    }else if(action.equals("GetActivityName")){
        Intent i = new Intent(cordova.getActivity(), VVServer.class);
        cordova.getActivity().startService(i);
        Toast.makeText(cordova.getActivity(),cordova.getActivity().getComponentName().getClassName(),Toast.LENGTH_LONG).show();
    }
    else if (action.equals("bringToFront")) {
       executeGlobalJavascript("alert('你好啊')");
      
      Log.d("Bring", "I see you baby");
      Intent notificationIntent = new Intent(cordova.getActivity(), cordova.getActivity().getClass());
      notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      PendingIntent pendingIntent = PendingIntent.getActivity(cordova.getActivity(), 0, notificationIntent, 0);
      try 
      {
        pendingIntent.send();
      }
      catch (PendingIntent.CanceledException e) 
      {
        e.printStackTrace();
      }
      return true;
    }
    return false;
  }
  
   public static void executeGlobalJavascript(final String jsString){
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + jsString);
            }
        });
    }
  
  public static OnePixelReceiver mOnepxReceiver;
  //注册监听屏幕的广播
  public void registerScnOnAndOffBroadcast(){
        mOnepxReceiver = new OnePixelReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        cordova.getActivity().registerReceiver(mOnepxReceiver, intentFilter);
  }
  
  
  public void StartJobServer(){
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
              JobScheduler jobScheduler = (JobScheduler) cordova.getActivity().getSystemService("jobscheduler");
              JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(cordova.getActivity().getPackageName(), MyJobService.class.getName()))
                      .setPeriodic(10000)
                      .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                      .setPersisted(true)
                      .build();
              jobScheduler.schedule(jobInfo);
      }
  }
  
      /**
     * Get Mobile Type
     *
     * @return
     */
    private static String getMobileType() {
        return Build.MANUFACTURER;
    }
  
  public void jumpStartInterface(){
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("HLQ_Struggle", "******************当前手机型号为：" + getMobileType());
            ComponentName componentName = null;
            if (getMobileType().equals("Xiaomi")) { // 红米Note4测试通过
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (getMobileType().equals("Letv")) { // 乐视2测试通过
                intent.setAction("com.letv.android.permissionautoboot");
            } else if (getMobileType().equals("samsung")) { // 三星Note5测试通过
                componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram.AutoRunActivity");
            } else if (getMobileType().equals("HUAWEI")) { // 华为测试通过
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            } else if (getMobileType().equals("vivo")) { // VIVO测试通过
                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.MainActivity");//这个可以跳转到i管家
//                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.PurviewTabActivity");
            } else if (getMobileType().equals("Meizu")) { //万恶的魅族
                componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");
            } else if (getMobileType().equals("OPPO")) { // OPPO R8205测试通过
                componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
            } else if (getMobileType().equals("ulong")) { // 360手机 未测试
                componentName = new ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity");
            } else if(getMobileType().equals("nubia")){//中兴nubia z11Minis测试成功
                componentName = new ComponentName("cn.nubia.security2", "cn.nubia.security.appmanage.selfstart.ui.SelfStartActivity");
            }else if(getMobileType().equals("ZUK")){//联想zuk z2 pro测试通过
                componentName = new ComponentName("com.zui.safecenter", "com.lenovo.safecenter.MainTab.LeSafeMainActivity");
            }else {
                // 以上只是市面上主流机型，由于公司你懂的，所以很不容易才凑齐以上设备
                // 针对于其他设备，我们只能调整当前系统app查看详情界面
                // 在此根据用户手机当前版本跳转系统设置界面
                if (Build.VERSION.SDK_INT >= 9) {
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", cordova.getActivity().getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName", cordova.getActivity().getPackageName());
                }
            }
            intent.setComponent(componentName);
            cordova.getActivity().startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            cordova.getActivity().startActivity(intent);
        }
  }
}

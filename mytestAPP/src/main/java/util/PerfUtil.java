package util;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by zhangzhiwei01 on 2016/4/19.
 */
public class PerfUtil extends Service{
    private String pkg = "";
    private static int power = 888;
    private BroadcastReceiver br;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        try{
            Bundle bundle = intent.getExtras();
            if(bundle == null)
                return;

            this.br = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
                        int level = intent.getIntExtra("level", 0);
                        int scale = intent.getIntExtra("scale", 100);
                        PerfUtil.power = level;
                        Log.i("tag", "当前电量信息广播："+String.valueOf(level));
                    }
                }
            };
            this.registerReceiver(this.br, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

            String pkg = bundle.getString("pkg");
            this.pkg = pkg;
            Log.i("tag", "下行流量信息："+String.valueOf(this.getUidRxKBytes()));
            Log.i("tag", "当前内存信息："+String.valueOf(getMemByPkg()));
            Log.i("tag", "当前CPU信息："+String.valueOf(getProcessCpuRate()));
            Log.i("tag", "当前电量信息："+String.valueOf(this.power));
        }catch(Exception e){

        }
    }

    private float getProcessCpuRate() {
        float totalCpuTime1 = getTotalCpuTime();
        Log.i("tag", String.valueOf(totalCpuTime1));
        float processCpuTime1 = getCpuByPkg();
        Log.i("tag", String.valueOf(processCpuTime1));
        try {
            Thread.sleep(360);

        } catch (Exception e) {
        }

        float totalCpuTime2 = getTotalCpuTime();
        Log.i("tag", String.valueOf(totalCpuTime2));
        float processCpuTime2 = getCpuByPkg();
        Log.i("tag", String.valueOf(processCpuTime2));

        float cpuRate = 100 * (processCpuTime2 - processCpuTime1)
                / (totalCpuTime2 - totalCpuTime1);
        Log.i("tag", String.valueOf(cpuRate));
        if(cpuRate>=0.0&&cpuRate<=100.0)
            return cpuRate;
        else
            return 0.0f;
    }

    private long getCpuByPkg() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mRunningProcess = am.getRunningAppProcesses();
        int i = 1;
        for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess) {
            if (amProcess.processName.equals(this.pkg)) {
                return this.getMyAppCPUStat(amProcess.pid);
            }
        }
        return 0;
    }

    private long getMyAppCPUStat(int myPid) {
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader("/proc/" + myPid + "/stat");
            BufferedReader br = new BufferedReader(fr, 8192);
            String line;
            while (null != (line = br.readLine())) {
                sb.append(line).append("\n");
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            // BLog.e("getMyAppCPUStat fail.",e);
            return 0;
        }
        String[] cpuInfos = sb.toString().split(" ");
        long appCpuTime = Long.parseLong(cpuInfos[13])
                + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
                + Long.parseLong(cpuInfos[16]);
        return appCpuTime;
    }

    public static long getTotalCpuTime() { // 获取系统总CPU使用时间
        String[] cpuInfos = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long totalCpu = Long.parseLong(cpuInfos[2])
                + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
        return totalCpu;
    }

    private int getMemByPkg(){
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mRunningProcess = am.getRunningAppProcesses();
        int i = 1;
        for(ActivityManager.RunningAppProcessInfo amprocess:mRunningProcess){
            if(amprocess.processName.equals(this.pkg)){
                int pids[] = {amprocess.pid};
                Debug.MemoryInfo[] memoryInfoArray = am.getProcessMemoryInfo(pids);
                Debug.MemoryInfo pidMemoryInfo=memoryInfoArray[0];
                return pidMemoryInfo.getTotalPss();
            }
        }
        return 0;
    }
    private long getUidRxKBytes(){
        int uid = this.getDefaultUid();
        if(uid==0)
            return 0;
        else
            return TrafficStats.getUidRxBytes(uid);
    }

    private int getDefaultUid(){
        PackageManager pm = this.getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo(this.pkg, PackageManager.GET_META_DATA);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ai==null?0:ai.uid;
    }
}

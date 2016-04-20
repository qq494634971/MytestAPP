package util;

import java.util.Arrays;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.os.Build;

public class APPManage {

	/**
	 * 
	 * @Description : 这个包名的程序是否在运行
	 * @Method_Name : isRunningApp
	 * @param context 上下文
	 * @param packageName 判断程序的包名
	 * @return 必须加载的权限 <uses-permission
	 *         android:name="android.permission.GET_TASKS">
	 * @return : boolean
	 * @Creation Date : 2014-10-31 下午1:14:15
	 * @version : v1.00
	 * @Author : JiaBin
	 * 
	 * @Update Date :
	 * @Update Author : JiaBin
	 */
	public static boolean isRunningApp(Context context, String packageName) {
		boolean isAppRunning = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT > 20) {
			java.util.Set<String> activePackages = new java.util.HashSet<String>();
			final List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
				if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					activePackages.addAll(Arrays.asList(processInfo.pkgList));
				}
			}
			for (String info : activePackages) {
				if (info.equals(packageName)) {
					isAppRunning = true;
					// find it, break
					break;
				}
			}
		} else {
			List<RunningTaskInfo> list = am.getRunningTasks(100);
			for (RunningTaskInfo info : list) {
				if (info.topActivity.getPackageName().equals(packageName)
						&& info.baseActivity.getPackageName().equals(packageName)) {
					isAppRunning = true;
					// find it, break
					break;
				}
			}
		}
		return isAppRunning;
	}
}

package com.GJTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.StringUtils;

import com.ly.perf.util.FileUtil;
import com.zzw.test.caseinfo.Caseinfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class GanjiLifeCaserunnerTask extends AsyncTask {
	private StringBuffer mStringBuffer = new StringBuffer();
	private long timestamp = 0;
	private boolean isCanceled = false;
	private Context mContext;
	private Button startbutton;
	private Button endButton;
	private boolean caseisgood = true;

	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	public GanjiLifeCaserunnerTask(Context mContext, Button startbutton, Button endButton) {
		super();
		this.mContext = mContext;
		this.startbutton = startbutton;
		this.endButton = endButton;
	}

	@Override
	protected Object doInBackground(Object... params) {
		publishProgress("展示停止按钮");
		// TODO Auto-generated method stub
		timestamp = System.currentTimeMillis();
		File allResultFile = new File("/mnt/sdcard/caseinfo/UIResult_" + timestamp + ".xml");
		File caseFile = new File("/mnt/sdcard/caseinfo/testcase.txt");
		try {
			if (!allResultFile.exists()) {
				FileUtil.createFile(allResultFile);
			}
			InputStreamReader mReader = new InputStreamReader(new FileInputStream(caseFile));
			BufferedReader mBufferedReader = new BufferedReader(mReader);
			//按行读取，读取testcase.txt文件中的所有要执行的case,并按业务线归类//
			ArrayList<String> all_testcase_temp = readFileByLines("/mnt/sdcard/caseinfo/testcase.txt");
			//看读取到的case是否有重复
			ArrayList<String> repetitive_cases = new ArrayList<String>();
			for (int i = 0; i < all_testcase_temp.size(); i++) {
				for (int j = i + 1; j < all_testcase_temp.size(); j++) {
					if (all_testcase_temp.get(i).equals(all_testcase_temp.get(j))) {
						repetitive_cases.add(all_testcase_temp.get(i));
						break;
					}
				}
			}
			//看读取到的case是否有不在全量case里的
			ArrayList<String> not_exist_cases = new ArrayList<String>();
			for (String string : all_testcase_temp) {
				boolean isexist = false;
				for (String[] strings : Caseinfo.ganjilife_android_allcase) {
					if (strings[1].equals(string)) {
						isexist = true;
						break;
					}
				}
				if (!isexist) {
					not_exist_cases.add(string);
				}
			}
			String[][] all_testcase = new String[all_testcase_temp.size()][2];
			for (int i = 0, j = 0; i < Caseinfo.ganjilife_android_allcase.length; i++) {
				for (String string : all_testcase_temp) {
					if (Caseinfo.ganjilife_android_allcase[i][1].equals(string)) {
						all_testcase[j] = Caseinfo.ganjilife_android_allcase[i];
						j++;
					}
				}
			}
			if (all_testcase[all_testcase_temp.size() - 1][0] == null
					|| all_testcase[all_testcase_temp.size() - 1][1] == null || not_exist_cases.size() > 0) {
				isCanceled = true;
				caseisgood = false;
			}
			//查找case所在类
			for (int i = 0; i < all_testcase.length; i++) {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress("正在执行第" + (i + 1) + "/" + all_testcase.length + "条case");
				if (isCanceled) {
					break;
				}
				File resultFile = new File("/data/data/com.ganji.android/files/junit-report.xml");
				
				//清除执行结果
//				deleteFile(resultFile);  
				FileUtil.deleteFile(new File("/data/data/com.ganji.android/files"));
				
				String classname = all_testcase[i][0];
				String casename = all_testcase[i][1];
				runc("am instrument --user 0 -e class com.baidu.recordreplay.test." + classname + "#"
						+ casename + " com.ganji.android.test/com.baidu.cafe.CafeTestRunner");
				FileWriter fileWritter = new FileWriter(allResultFile, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(casename + "执行结果 ----" + System.currentTimeMillis() + "\n");
				bufferWritter.close();
				//8S后判断赶集生活是否已经启动
				long starttimestamp = System.currentTimeMillis() / 1000;
				boolean appisrunning = false;
				boolean isexistResult = false;
				while (!appisrunning && System.currentTimeMillis()/1000 < starttimestamp + 20) {
					appisrunning = isRunningApp(mContext, "com.ganji.android");
				}
				if (!appisrunning) {
					fileWritter = new FileWriter(allResultFile, true);
					bufferWritter = new BufferedWriter(fileWritter);
					bufferWritter.write("case执行8S后赶集生活没有启动或者已经退出\n\n\n\n");
					bufferWritter.close();
					isexistResult = true;
				}
				//15分钟后没有结果，执行下一条case
				boolean runresult = true;
				while (!isexistResult && System.currentTimeMillis() / 1000 < starttimestamp + 15 * 60) {
						if (isCanceled) {
							break;
						}
						//检查赶集生活是否已经退出,退出后等待3秒，查看是否有结果输出，有结果 - 记录，无结果 - 异常退出
						if (!isRunningApp(mContext, "com.ganji.android")) {
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (resultFile.exists() && readFileByLines(resultFile.toString()).size() != 0) {
								fileWritter = new FileWriter(allResultFile, true);
								bufferWritter = new BufferedWriter(fileWritter);
								for (String string2 : readFileByLines(resultFile.toString())) {
									bufferWritter.write(string2);
									if (string2.contains("error") || string2.contains("failure")) {
										runresult = false;
									}
								}
								bufferWritter.write("\n\n\n\n");
								bufferWritter.close();
								isexistResult = true;
								break;
							}else {
								fileWritter = new FileWriter(allResultFile, true);
								bufferWritter = new BufferedWriter(fileWritter);
								bufferWritter.write("case执行过程中异常退出\n\n\n\n");
								bufferWritter.close();
								isexistResult = true;
								break;
							}
						}
				}
				if (!isexistResult) {
					fileWritter = new FileWriter(allResultFile, true);
					bufferWritter = new BufferedWriter(fileWritter);
					bufferWritter.write("case执行15分钟后依然没有执行结果输出\n\n\n\n");
					bufferWritter.close();
					break;
				}
				//如果case执行失败保留截图到/mnt/sdcard/caseinfo/+timestamp
				if (!runresult) {
					runc("mkdir /sdcard/caseinfo/"+timestamp);
					runc("mkdir /sdcard/caseinfo/"+timestamp+"/"+casename);
					File[] children = new File("/data/data/com.ganji.android/files").listFiles();
					if (children != null) {
						for (File mFile : children) {
							try {
								if (mFile.getName().contains(".jpg")) {
									FileUtil.copyFile(mFile, new File("/sdcard/caseinfo/"+timestamp+"/"+casename+"/"+mFile.getName()));
								}
							} catch (Exception e2) {
								// TODO: handle exception
							}
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		// TODO Auto-generated method stub
		try {
			if (values[0].toString().equals("展示停止按钮")) {
				endButton.setVisibility(View.VISIBLE);
			} else {
				this.startbutton.setText((CharSequence) values[0]);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		if (!caseisgood) {
			Toast.makeText(mContext, "case不完整，未执行", Toast.LENGTH_SHORT).show();
		}
		startbutton.setText("点击开始执行");
		endButton.setVisibility(View.GONE);
		GanjiLifeCaserunnerActivity.setTaskid(this.timestamp+".xml");
		super.onPostExecute(result);
	}
	
	

	public void runc(String exec) {
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(exec);
			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			this.mStringBuffer.append("<ERROR>");
			while ((line = br.readLine()) != null) {
				Log.i("tag", line);
				this.mStringBuffer.append(line);
			}
			this.mStringBuffer.append("</ERROR>");
			int exitVal = proc.waitFor();
			this.mStringBuffer.append("Process exitValue: " + exitVal);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath 被删除文件的文件名
	 */
	public void deleteFile(File file) {
		// 路径为文件且不为空则进行删除
		boolean ss = file.isFile();
		boolean ss1 = file.exists();
		if (file.isFile() && file.exists()) {
			file.delete();
		}
	}

	/**
	 * 以行为单位读取文件
	 */
	public static ArrayList<String> readFileByLines(String fileName) {
		ArrayList<String> readStrings = new ArrayList<String>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			//            System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				if (!tempString.equals("")) {
					readStrings.add(tempString);
				}
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return readStrings;
	}

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

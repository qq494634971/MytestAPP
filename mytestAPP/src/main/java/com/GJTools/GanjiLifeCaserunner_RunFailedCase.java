package com.GJTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import util.APPManage;
import util.RunCommand;

import com.ly.perf.util.FileUtil;
import com.zzw.test.caseinfo.Caseinfo;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class GanjiLifeCaserunner_RunFailedCase extends AsyncTask {
	private String resultFileName;
	private long timestamp = 0;
	private Context mContext;
	private Button startbutton;
	private Button endButton;

	public GanjiLifeCaserunner_RunFailedCase(String resultFileName, Context mContext, Button startbutton, Button endButton) {
		super();
		this.resultFileName = resultFileName;
		this.startbutton = startbutton;
		this.endButton = endButton;
	}

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		try {
			while (GanjiLifeCaserunnerActivity.getTaskid().isEmpty());
		} catch (Exception e) {
			// TODO: handle exception
		}
		String ssssdfString = GanjiLifeCaserunnerActivity.getTaskid();
		this.resultFileName = GanjiLifeCaserunnerActivity.getTaskid();
		publishProgress("展示停止按钮");
		// TODO Auto-generated method stub
		File allResultFile = new File("/mnt/sdcard/caseinfo/UIResult_"
				+ resultFileName.substring(0, resultFileName.indexOf(".xml")) + "失败case再执行.xml");
		File caseFile = new File("/mnt/sdcard/caseinfo/UIResult_" + resultFileName);
		try {
			if (!allResultFile.exists()) {
				FileUtil.createFile(allResultFile);
			}
			if (!caseFile.exists()) {
				FileUtil.createFile(caseFile);
			}
			InputStreamReader mReader = new InputStreamReader(new FileInputStream(caseFile));
			BufferedReader mBufferedReader = new BufferedReader(mReader);
			//按行读取，读取执行结果文件中的失败case,并按业务线归类//
			ArrayList<String> all_failedcase = GanjiLife_RunnerResultAnalyze.Get_FailedCases(caseFile);
			//查找case所在类
			for (int i = 0; i < all_failedcase.size(); i++) {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress("正在执行所有失败的case的第" + (i + 1) + "/" + all_failedcase.size() + "条case");

				//清除执行结果
				File resultFile = new File("/data/data/com.ganji.android/files/junit-report.xml");
				FileUtil.deleteFile(new File("/data/data/com.ganji.android/files"));

				String classname = all_failedcase.get(i).substring(0, all_failedcase.get(i).indexOf("/"));
				String casename = all_failedcase.get(i).substring(all_failedcase.get(i).indexOf("/") + 1);
				RunCommand.runc("am instrument --user 0 -e class com.baidu.recordreplay.test." + classname
						+ "#" + casename + " com.ganji.android.test/com.baidu.cafe.CafeTestRunner");
				FileWriter fileWritter = new FileWriter(allResultFile, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(casename + "执行结果 ----" + System.currentTimeMillis() + "\n");
				bufferWritter.close();
				//8S后判断赶集生活是否已经启动
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long starttimestamp = System.currentTimeMillis() / 1000;
				boolean appisrunning = false;
				boolean isexistResult = false;
				while (!appisrunning && System.currentTimeMillis() < starttimestamp + 15) {
					appisrunning = APPManage.isRunningApp(mContext, "com.ganji.android");
				}
				if (!appisrunning) {
					fileWritter = new FileWriter(allResultFile, true);
					bufferWritter = new BufferedWriter(fileWritter);
					bufferWritter.write("case执行8S后赶集生活没有启动或者已经退出\n\n\n\n");
					bufferWritter.close();
					isexistResult = true;
				}
				//15分钟后没有结果，或者5S后赶集生活还没有运行，执行下一条case
				boolean runresult = true;
				while (!isexistResult && System.currentTimeMillis() / 1000 < starttimestamp + 15 * 60) {
					//检查是否有执行结果
					if (resultFile.exists() && util.FileUtil.readFileByLines(resultFile).size() != 0) {
						fileWritter = new FileWriter(allResultFile, true);
						bufferWritter = new BufferedWriter(fileWritter);
						for (String string2 : util.FileUtil.readFileByLines(resultFile)) {
							bufferWritter.write(string2);
							if (string2.contains("error") || string2.contains("failure")) {
								runresult = false;
							}
						}
						bufferWritter.write("\n\n\n\n");
						bufferWritter.close();
						isexistResult = true;
						break;
					}
					if (!APPManage.isRunningApp(mContext, "com.ganji.android")) {
						fileWritter = new FileWriter(allResultFile, true);
						bufferWritter = new BufferedWriter(fileWritter);
						bufferWritter.write("case执行过程中异常退出\n\n\n\n");
						bufferWritter.close();
						isexistResult = true;
						break;
					}
				}
				if (!isexistResult) {
					fileWritter = new FileWriter(allResultFile, true);
					bufferWritter = new BufferedWriter(fileWritter);
					bufferWritter.close();
					bufferWritter.write("case执行15分钟后依然没有执行结果输出\n\n\n\n");
					break;
				}
				//如果case执行失败保留截图到/mnt/sdcard/caseinfo/+timestamp
				if (!runresult) {
					RunCommand.runc("mkdir /sdcard/caseinfo/"
							+ resultFileName.substring(0, resultFileName.indexOf(".xml")) + "失败case再执行");
					RunCommand.runc("mkdir /sdcard/caseinfo/"
							+ resultFileName.substring(0, resultFileName.indexOf(".xml")) + "失败case再执行" + "/"
							+ casename);
					File[] children = new File("/data/data/com.ganji.android/files").listFiles();
					if (children != null) {
						for (File mFile : children) {
							try {
								if (mFile.getName().contains(".jpg")) {
									FileUtil.copyFile(
											mFile,
											new File("/sdcard/caseinfo/"
													+ resultFileName.substring(0,
															resultFileName.indexOf(".xml")) + "失败case再执行"
													+ "/" + casename + "/" + mFile.getName()));
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
		startbutton.setText("点击开始执行");
		endButton.setVisibility(View.GONE);
		super.onPostExecute(result);
	}

}

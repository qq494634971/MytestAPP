package com.zzw.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;

import com.ly.perf.util.FileUtil;
import com.ly.perf.util.FileUtil.PathType;
import com.zzw.test.caseinfo.ApplyReport;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class CaserunnerTask extends AsyncTask{
	private StringBuffer mStringBuffer = new StringBuffer();
	private Context mContext;
	private ArrayList<String> mIDs = new ArrayList<String>();
	public CaserunnerTask(Context mContext, String... IDs) {
		super();
		this.mContext = mContext;
		for (String string : IDs) {
			this.mIDs.add(string);
		}
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
			while ((line = br.readLine()) != null){
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

	@Override
	protected Object doInBackground(Object... params) {
		ArrayList<String> mjobids = new ArrayList<String>();
		//删除jobid.txt文件
		String perfpath = FileUtil.getPathSDCardFirst(this.mContext, "aperf");
		File saveFile = new File(perfpath + File.separator + "jobid");
		deleteFile(saveFile);
		//执行
		for (int i = 0; i < mIDs.size(); i++) {
			runc("am instrument --user 0 -e class com.baidu.recordreplay.test.IMTest#test_"
					+ mIDs.get(i) + " com.ganji.android.test/com.baidu.cafe.CafeTestRunner");
			//等待case执行完成
			while (true) {
				if (readFileByLines(saveFile.toString()).size() > mjobids.size()) {
					mjobids = readFileByLines(saveFile.toString());
					break;
				}else {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		//请求报表
		String[] jobidStrings = new String[mjobids.size()];
		for (int i = 0; i < mjobids.size(); i++) {
			jobidStrings[i] = mjobids.get(i);
		}
		ApplyReport.SendPost(jobidStrings);
		return null;
	}
	
	@Override
	protected void onCancelled(Object result) {
		// TODO Auto-generated method stub
		super.onCancelled(result);
	}
	
	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
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
}

package com.GJTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ly.perf.util.FileUtil;
import com.zzw.test.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GanjiLife_RunnerResultActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.runner_result);
		ListView runner_result＿lv = (ListView) findViewById(R.id.runner_result_list);
		runner_result＿lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, GetResultList()));
	
	
	}
	
	private List<String> GetResultList() {
		List<String> mResultList = new ArrayList<String>();
		ArrayList<String> allfiles = FileUtil.getChildFiles(new File("/sdcard/caseinfo"));
		for (String string : allfiles) {
			if (string.equals("没有获取到任何执行结果")) {
				mResultList.add(string);
				break;
			}else if(string.endsWith(".xml")) {
				long timestamp = Long.parseLong((String) string.subSequence(9, 22));
				String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(timestamp));
				mResultList.add("结果生成时间："+date);
			}
		}
		return mResultList;
	}
}

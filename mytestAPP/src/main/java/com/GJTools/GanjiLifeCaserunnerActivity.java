package com.GJTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import util.StringUtils;

import com.ly.perf.util.FileUtil;
import com.zzw.test.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GanjiLifeCaserunnerActivity extends Activity {
	private TextView topTextView;
	private ImageView topButton;
	public static Button startButton;
	public static Button endButton;
	GanjiLifeCaserunnerTask mRunner = null;
	private static String mTaskid = new String();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.case_runner);
		topTextView = (TextView) findViewById(R.id.topTv);
		topButton = (ImageView) findViewById(R.id.topButton);
		startButton = (Button) findViewById(R.id.start);
		endButton = (Button) findViewById(R.id.end);

		startButton.setText("点击开始执行case");
		endButton.setVisibility(View.GONE);
		topTextView.setText("赶集生活CaseRunner");
		// topButton.setVisibility(View.GONE);

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "开始执行",
						Toast.LENGTH_SHORT).show();
				if (mRunner != null) {
					mRunner.setCanceled(true);
					mRunner = null;
				}
				mRunner = new GanjiLifeCaserunnerTask(getApplicationContext(),
						startButton, endButton);
				mRunner.execute();
				GanjiLifeCaserunner_RunFailedCase mFailedCase = new GanjiLifeCaserunner_RunFailedCase(
						"", getApplicationContext(), startButton, endButton);
				mFailedCase.execute();
				// bindService(new Intent(getApplicationContext(),
				// GanjiLifeCaserunnerServer.class), conn, flags)
			}
		});

		endButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mRunner.setCanceled(true);
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public static void setTaskid(String taskid) {
		mTaskid = taskid;
	}

	public static String getTaskid() {
		return mTaskid;
	}
}

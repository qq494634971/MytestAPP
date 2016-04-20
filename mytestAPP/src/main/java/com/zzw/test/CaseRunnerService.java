package com.zzw.test;

import com.zzw.test.caseinfo.Caseinfo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class CaseRunnerService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();
		String pkg = bundle.getString("pkg");
		CaserunnerTask mCaserunner = new CaserunnerTask(this, pkg);
		mCaserunner.execute();
		return null;
	}

}

package com.GJTools;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GanjiLifeCaserunnerServer extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		long startstamp = System.currentTimeMillis()/1000;
		while (true && System.currentTimeMillis()/1000 < startstamp+1000*60*1000) {
//			if (GanjiLifeCaserunnerTask.allcase != 0) {
//				GanjiLifeCaserunnerActivity.startButton.setEnabled(false);
//			}
//			if (GanjiLifeCaserunnerTask.allcase != 0 && GanjiLifeCaserunnerTask.allcase == GanjiLifeCaserunnerTask.finishcase) {
//				GanjiLifeCaserunnerActivity.startButton.setEnabled(true);
//				break;
//			}
		}
		return null;
	}
	
}

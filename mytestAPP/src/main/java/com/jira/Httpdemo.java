package com.jira;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import android.content.Context;

public class Httpdemo {
	Context mContext;
	static String url = "http://bw-vm-qatech.dns.ganji.com:8008/UiInput/jira/get_para.php";
	
	public static void getv(Context mContext) {
		// TODO Auto-generated method stub
		AsyncHttpClient mClient = new AsyncHttpClient();
		mClient.get(mContext, url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String response = new String(arg2);
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				String response1 = "asd";
			}
		});
	}
	
}

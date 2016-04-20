package com.zzw.test.caseinfo;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import android.test.SyncBaseInstrumentation;

public class ApplyReport {
	public static JSONObject mJsonObject;
	public static JSONArray mJsonArray = new JSONArray();

	public static void SendPost(String... IDs) {
		RequestParams params = new RequestParams();
		SyncHttpClient client = new SyncHttpClient();
//		try {
			for (int i = 0; i < IDs.length; i++) {
//				mJsonObject = new JSONObject();
//				mJsonObject.put("jobid", IDs[i]);
				mJsonArray.put(IDs[i]);
			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		params.put("jobid", mJsonArray);
		params.put("action", "mReport");
		client.post(Caseinfo.applyReport, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});
	}
}

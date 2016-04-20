package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class RunCommand {
	
	public static void runc(String exec) {
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(exec);
			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
//			this.mStringBuffer.append("<ERROR>");
//			while ((line = br.readLine()) != null) {
//				Log.i("tag", line);
//				this.mStringBuffer.append(line);
//			}
//			this.mStringBuffer.append("</ERROR>");
//			int exitVal = proc.waitFor();
//			this.mStringBuffer.append("Process exitValue: " + exitVal);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
}

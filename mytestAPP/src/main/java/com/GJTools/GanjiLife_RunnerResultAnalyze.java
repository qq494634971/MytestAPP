package com.GJTools;

import java.io.File;
import java.util.ArrayList;

import util.FileUtil;

public class GanjiLife_RunnerResultAnalyze {
	
	public static ArrayList<String> Get_FailedCases(File resultFile) {
		String case_classname = "classname=\"com.baidu.recordreplay.test.";
		String case_name = "\" name=\"test";
		ArrayList<String> failedCases = new ArrayList<String>();
		ArrayList<String> runner_Results = FileUtil.readFileByLines(resultFile);
		for (String string : runner_Results) {
				if (string.contains("error") || string.contains("failure")) {
					String failedCaseNameAndClass = new String();
					failedCaseNameAndClass = failedCaseNameAndClass + string.subSequence(string.indexOf(case_classname)+case_classname.length(), string.indexOf(case_name));
					failedCaseNameAndClass = failedCaseNameAndClass + "/" + string.subSequence(string.indexOf(case_name)+8, string.indexOf("\" time="));
					failedCases.add(failedCaseNameAndClass);
			}
		}
		return failedCases;
	}
}

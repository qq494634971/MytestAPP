package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	
	/**
	 * 去除字符串中的所有空格换行符
	 */
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
	
	/**
	 * 获得src中的字符串find的个数
	 */
	public static int getOccur(String src, String find) {
		int o = 0;
		int index = -1;
		while ((index = src.indexOf(find, index)) > -1) {
			++index;
			++o;
		}
		return o;
	}
	
	/**
	 * 将字符串通过分隔符find转换成二维字符串
	 */
	public static String[] getStrsByMarker(String src, String find) {
		if (src.equals("")) {
			return null;
		}
		src = src + find;
		String[] strs = new String[getOccur(src, find)];
		for (int i = 0; i < strs.length; i++) {
			strs[i] = src.substring(0, src.indexOf(find));
			src = src.substring(src.indexOf(find) + 1);
		}
		return strs;
	}
}

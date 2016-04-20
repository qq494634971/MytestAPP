package util;

import java.lang.reflect.Field;
import android.content.Context;

public class ResourceUtil {
	
	/**
	 * 本地文件资源类型
	 */
	public enum ResourceType {
		xml, image, html, audio, vedio, raw, unkown, route
	}
	
	/**
	 * Android文件资源类型
	 */
	public enum AndroidResourceType {
		anim, animator, array, attr, bool, color, dimen, drawable, fraction, id, integer, interpolator, layout, menu, mipmap, plurals, raw, string, style, styleable, xml
	}
	
	/**
	 * Android Manifest文件资源类型
	 */
	public enum AndroidManifestResourceType {
		permission, permission_group
	}
	
	public static String getManifestString(Context context, AndroidManifestResourceType resourceType, String resourceName) {
		try {
			Class<?> localClass = Class.forName(context.getPackageName() + ".Manifest$" + resourceType.name());
			Field localField = localClass.getField(resourceName);
			String s = localField.get(localField.getName()).toString();
			return s;
		} catch (Exception localException) {
		}
		return null;
	}
	
	public static int getResourceID(Context context, AndroidResourceType resourceType, String resourceName) {
		try {
			Class<?> localClass = Class.forName(context.getPackageName() + ".R$" + resourceType.name());
			Field localField = localClass.getField(resourceName);
			int i = Integer.parseInt(localField.get(localField.getName()).toString());
			return i;
		} catch (Exception localException) {
		}
		return 0;
	}
}

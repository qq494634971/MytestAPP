package util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import util.ResourceUtil;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * 工具类，文件相关操作
 * 
 * @author 
 */
public final class FileUtil {
	
	
	
	/**
	 * 存储路径方式
	 */
	public enum PathType {
		/**
		 * 存储在SDCard中
		 */
		SDCARD,
		/**
		 * 存储在软件Cache中
		 */
		CACHE,
		/**
		 * 存储在软件DATA目录下的app_photos中
		 */
		APP_PHOTOS,
		/**
		 * 存储在多媒体文件夹下
		 */
		SDCARD_PHOTOS,
		/**
		 * 存储在多媒体文件夹下
		 */
		MEDIA_DIR,
		/**
		 * 存储在软件DATA目录下的app_xmls中
		 */
		APP_XML
	}
	
	/**
	 * 客户端发帖最大照片文件大小，30kb，如果大于，就需要压缩
	 */
	public static final int MAX_POST_PHOTO_FILE_SIZE = 30720;
	
	/**
	 * 图片最大边像素
	 */
	public static final int MAX_POST_PHOTO_PX = 600;
	
	/**
	 * 图片质量
	 */
	public static final int PHOTO_QUANLITY_CAMER = 80;
	public static final int PHOTO_QUANLITY_META = 50;
	public static final int PHOTO_QUANLITY_SMALLMETA = 70;
	/**
	 * 图片格式
	 */
	public static final String PHOTO_FORMATE = "jpg";
	
	/**
	 * 赶集 照片子路径
	 */
	public static final String PHOTO_DIR = "photos";
	
	/**
	 * XML文件路径
	 */
	public static final String XML_DIR = "xmls";
	
	/**
	 * Image jpg文件后缀
	 */
	public static final String EXE_JPG = "jpg";
	
	/**
	 * 获得文件的路径，如果有SD卡，那么首先获得SD卡的路径
	 */
	public static String getPathSDCardFirst(Context context, String dir) {
		String absolutePath = "";
		if (sdcardAvailable()) {
			absolutePath = createSavePath(context, PathType.SDCARD);
			absolutePath += File.separator + dir;
		} else {
			absolutePath = context.getDir(dir, Context.MODE_PRIVATE).getPath();
		}
		return absolutePath;
	}
	
	/**
	 * 判断是否有SD卡
	 */
	public static boolean sdcardAvailable() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getDefaultSavePath(Context context) {
		
		PathType pathType;
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			pathType = PathType.CACHE;
		} else {
			pathType = PathType.SDCARD;
		}
		return FileUtil.createSavePath(context.getApplicationContext(), pathType);
	}
	
	/**
	 * 获得文件的路径
	 */
	public static String createSavePath(Context context, PathType pathType) {
		String path;
		switch (pathType) {
		case CACHE:
			path = context.getCacheDir().getPath();
			break;
		case APP_PHOTOS:
			path = context.getDir(PHOTO_DIR, Context.MODE_WORLD_WRITEABLE).getPath();
			break;
		case APP_XML:
			path = context.getDir(XML_DIR, Context.MODE_WORLD_WRITEABLE).getPath();
			break;
		case SDCARD:
			path = Environment.getExternalStorageDirectory().getPath();
			break;
		case SDCARD_PHOTOS:
			path = Environment.getExternalStorageDirectory().getPath() + "/" + PHOTO_DIR;
			File fileDir = new File(path);
			if (!fileDir.exists()) {
				fileDir.mkdir();
			}
			break;
		default:
			path = Environment.getExternalStorageDirectory().getPath();
			break;
		}
		return path;
	}
	
	/**
	 * 创建指定路径的文件夹
	 */
	public static File createDir(String path) throws SecurityException {
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		return fileDir;
	}
	
	/**
	 * 创建文件
	 */
	public static boolean createFile(File file) throws IOException {
		if (file.exists()) {
			deleteFile(file);
		}
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		return file.createNewFile();
	}
	
	/**
	 * 创建缓存文件
	 */
	public static File createCacheFile(Context context, ResourceUtil.ResourceType type, String url) throws IOException {
		File cacheDir = context.getCacheDir();
		
		String baseName = "";
		try {
			Uri uri = Uri.parse(url);
			baseName = uri.getLastPathSegment();
		} catch (Exception e) {
		}
		
		File tempCache = new File(cacheDir, type.name() + "-" + System.currentTimeMillis() + "-" + baseName);
		createFile(tempCache);
		return tempCache;
	}
	
	/**
	 * 创建临时文件
	 */
	public static File createTempFile(Context context, PathType pathType, ResourceUtil.ResourceType resType, String url) throws IOException {
		String path = createSavePath(context, pathType);
		return createTempFile(context, path, resType, url);
	}
	
	/**
	 * 创建临时文件
	 */
	public static File createTempFile(Context context, String path, ResourceUtil.ResourceType resType, String url) throws IOException {
		File saveDir = createDir(path + "/" + resType.name());
		
		String baseName = "";
		try {
			Uri uri = Uri.parse(url);
			baseName = uri.getLastPathSegment();
		} catch (Exception e) {
		}
		
		File imageFile = new File(saveDir, resType.name() + "-" + System.currentTimeMillis() + "-" + baseName);
		createFile(imageFile);
		return imageFile;
	}
	
	/**
	 * 创建临时文件
	 */
	public static File createTempFile(String path, ResourceUtil.ResourceType resType, String exe) throws IOException {
		File saveDir = createDir(path + "/" + resType.name());
		
		String baseName = "";
		if (exe.startsWith(".")) {
			baseName = exe;
		} else {
			baseName = "." + exe;
		}
		
		File imageFile = new File(saveDir, resType.name() + "-" + System.currentTimeMillis() + baseName);
		createFile(imageFile);
		return imageFile;
	}
	
	/**
	 * 创建临时文件
	 */
	public static File createJobPhotoTempFile(String path, ResourceUtil.ResourceType resType, String exe) throws IOException {
		File saveDir = createDir(path + "/" + resType.name());
		
		String baseName = "";
		if (exe.startsWith(".")) {
			baseName = exe;
		} else {
			baseName = "." + exe;
		}
		
		File imageFile = new File(saveDir, resType.name() + baseName);
		//createFile(imageFile);
		return imageFile;
	}
	
	/**
	 * 保存临时图片文件
	 */
	public static File createTempFile(String path, ResourceUtil.ResourceType resType, String exe, String string) throws IOException {
		File saveDir = createDir(path + "/" + resType.name());
		
		String baseName = "";
		if (exe.startsWith(".")) {
			baseName = exe;
		} else {
			baseName = "." + exe;
		}
		
		File imageFile = new File(saveDir, string + resType.name() + "-" + System.currentTimeMillis() + baseName);
		createFile(imageFile);
		return imageFile;
	}
	
	/**
	 * 存储Bitmap到本地文件
	 */
	public static Uri saveBitmap(Context context, Bitmap bitmap, PathType pathType, ResourceUtil.ResourceType resType, String url) {
		
		Uri result = null;
		if (bitmap == null) {
			return null;
		}
		try {
			File imageFile = createTempFile(context, pathType, resType, url);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imageFile));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
			result = Uri.fromFile(imageFile);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return result;
	}
	
	/**
	 * 文件Uri来自于Media Provider
	 */
	public static final int FROM_TYPE_MEDIA = 1;
	
	/**
	 * 文件Uri来自于存储设备或是指定的存储路径
	 */
	public static final int FROM_TYPE_STORE = 2;
	
	/**
	 * 从文件Uri获取该文件的容量大小
	 */
	public static long getFileSize(Context context, Uri fileUri, int fromType) {
		long fileSizeinByte = 0;
		String mediaFilePath = null;
		switch (fromType) {
		case FROM_TYPE_MEDIA:
			mediaFilePath = getMediaRealPath(context, fileUri);
			break;
		case FROM_TYPE_STORE:
			mediaFilePath = fileUri.getEncodedPath();
			break;
		default:
			break;
		}
		try {
			if (mediaFilePath != null) {
				File mediaFile = new File(mediaFilePath);
				fileSizeinByte = mediaFile.length();
			}
		} catch (Exception e) {
		}
		return fileSizeinByte;
	}
	
	/**
	 * 获取媒体文件真实文件路径
	 */
	public static String getMediaRealPath(Context context, Uri contentUri) {
		try {
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = context.getContentResolver().query(contentUri, projection, // Which columns to return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null); // Order-by clause (ascending by name)
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String ret = cursor.getString(column_index);
			cursor.close();
			return ret;
		} catch (Exception e) {
		}
		
		return null;
	}
	
	/**
	 * 将源文件拷贝到目标文件
	 */
	public static boolean copyFile(File src, File dst) {
		try {
			InputStream in = new FileInputStream(src);
			if (!dst.exists()) {
				dst.createNewFile();
			}
			OutputStream out = new FileOutputStream(dst);
			StreamUtil.copyStream(in, out);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 从文件的Uri(带有file://开头的Uri)删除该文件
	 */
	public static final boolean deleteFile(Uri fileUri) {
		File tempFile = new File(fileUri.getEncodedPath());
		return deleteFile(tempFile);
	}
	
	/**
	 * 删除指定文件
	 */
	public static final boolean deleteFile(File file) {
		boolean result = true;
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					result &= deleteFile(children[i]);
				}
			}
		}
		result &= file.delete();
		return result;
	}
	
	/**
	 * 根据文件的最后修改时间来删除旧的文件，解决可能出现空指针的问题
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void deleOldFile(String dirPtah, int fileNums) throws Exception {
		File file = new File(dirPtah);
		File[] listFiles = file.listFiles();
		if (listFiles == null || listFiles.length < 1) {
			return;
		}
		ArrayList<File> list = new ArrayList<File>();
		for (File file0 : listFiles) {
			list.add(file0);
		}
		
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				if (((File) o1).lastModified() > ((File) o2).lastModified()) {
					return 1;
				} else if (((File) o1).lastModified() == ((File) o2).lastModified()) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		while (list.size() > fileNums) {
			File f = list.get(0);
			if (f.exists()) {
				f.delete();
			}
			list.remove(0);
		}
	}
	
	/**
	 * 删除2天之前的文件，如果文件夹为空，则删除之
	 */
	public static void deleteOldFile(File dir) {
		deleteOldFile(dir, 0);
	}
	
	private static void deleteOldFile(File dir, int depth) {
		try {
			long now = System.currentTimeMillis();
			long expire = 2 * 24 * 3600 * 1000; // 2天
			for (File f : dir.listFiles()) {
				if (f.isDirectory() && depth < 10) {
					deleteOldFile(f, depth + 1);
				} else if ((now - f.lastModified()) > expire) {
					f.delete();
				}
			}
			
			// 如果文件夹已经为空，删除
			if (dir.listFiles().length == 0) {
				dir.delete();
			}
		} catch (Throwable e) {
		}
	}
	
	/**
	 * 移除文件
	 */
	public static void remmoveFile(String filePathName) throws FileNotFoundException, IOException {
		File f = new File(filePathName);
		if (f.exists()) {
			FileUtil.deletE(f);
		}
	}
	
	/**
	 * 删除文件夹或文件下面的文件
	 */
	public static void deletE(File f) {
		File[] ff;
		int length;
		if (f.isFile()) {
			f.delete();
			return;
		} else if (f.isDirectory()) {
			ff = f.listFiles();
			length = ff.length;
			int i = 0;
			while (length != 0) {
				deletE(ff[i]);
				i++;
				length--;
			}
			if (length == 0) {
				f.delete();
				return;
			}
		}
	}
	
	/**
	 * 以行为单位读取文件
	 */
	public static ArrayList<String> readFileByLines(File file) {
		ArrayList<String> readStrings = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			//            System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				if (!tempString.equals("")) {
					readStrings.add(tempString);
				}
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return readStrings;
	}
//	public static boolean removeStrangePreferenceDir() {
//		Context context = GJApplication.getContext();
//		File dir = new File("/dbdata/databases/" + context.getPackageName() + "/shared_prefs/");
//		try {
//			if (dir.exists()) {
//				deletE(dir);
//				return true;
//			}
//		} catch (Exception e) {
//		}
//		
//		return false;
//	}
}

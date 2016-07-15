package com.stats.chinastats.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;

public class SDHelper {
	public static String SDPATH;

	/**
	 * �Ƿ���SD��
	 */
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	static {
		if (hasSDCard()) {
			SDPATH = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/";// filePath:/sdcard/
		} else {
			SDPATH = Environment.getDataDirectory().getAbsolutePath()
					+ "/data/"; // filePath: /data/data/
		}
	}

	public static String getSDPATH() {

		return SDPATH;
	}

	/**
	 * �ж��ļ��Ƿ��Ѿ�����;
	 * 
	 * @param filepath
	 * @return
	 */
	public static boolean checkFileExists(String filepath) {

		File file = new File(filepath);

		return file.exists();

	}

	public static void deletefilePath(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {

			if (file.isDirectory()) {
				File[] f = file.listFiles();
				if (f != null) {
					for (int i = 0; i < f.length; i++) {
						if (f[i].isDirectory())
							deletefilePath(f[i].getName());
						else
							f[i].delete();
					}
				}
			}
			file.delete();

		}
	}

	/**
	 * ��SD���ϴ���Ŀ¼��
	 * 
	 * @param dirpath
	 * @return
	 */
	public static File createDIR(String dirpath) {

		File dir = new File(SDPATH + dirpath);
		dir.mkdirs();
		return dir;

	}

	public static String getPath(String path) {
		if (!checkFileExists(path)) {
			createDIR(path);
		}
		return SDPATH + path;

	}

	public static String getSdCardRootPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				.toString();
	}

	public static String getAppDataPath() {
		String appDataPath = getSdCardRootPath() + File.separator
				+ Const.CACHE_PATH;
		File file = new File(appDataPath);
		if (!file.exists()) {
			file.mkdir();
		}
		return appDataPath;
	}

	public static void CopyAssets(Context context, String assetDir, String dir) {
		String[] files;
		try {
			files = context.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File mWorkingPath = new File(dir);
		// if this directory does not exists, make one.
		if (!mWorkingPath.exists()) {
			if (!mWorkingPath.mkdirs()) {

			}
		}

		for (int i = 0; i < files.length; i++) {
			try {
				String fileName = files[i];
				// we make sure file name not contains '.' to be a folder.
				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						CopyAssets(context, fileName, dir + fileName + "/");
					} else {
						CopyAssets(context, assetDir + "/" + fileName, dir
								+ fileName + "/");
					}
					continue;
				}
				File outFile = new File(mWorkingPath, fileName);
				if (outFile.exists())
					outFile.delete();
				InputStream in = null;
				if (0 != assetDir.length())
					in = context.getAssets().open(assetDir + "/" + fileName);
				else
					in = context.getAssets().open(fileName);
				OutputStream out = new FileOutputStream(outFile);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}

package com.stats.chinastats.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpDownloader {
	/**
	 * ���URL�����ļ���ǰ��������ļ����е��������ı�������ķ���ֵ�����ļ����е�����
	 * 1.����һ��URL����
	 * 2.ͨ��URL���󣬴���һ��HttpURLConnection����
	 * 3.�õ�InputStram
	 * 4.��InputStream���ж�ȡ���
	 * @param urlStr
	 * @return
	 */
	public String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			// ����һ��URL����
			URL url = new URL(urlStr);
			// ����һ��Httpl��
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			// ʹ��IO���ȡ���
			buffer = new BufferedReader(new InputStreamReader(urlConn
					.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**���������Ǵ���������������ʽ���ļ�
	 * 1.�ú��������-1����������ļ����
	 * 2.�ú��������0����������ļ��ɹ�
	 * 3.�ú��������1������ļ��Ѿ�����
	 */
	public int downFile(String urlStr, String path, String fileName){
		InputStream input = null;
		try {
			FileUtils fileUtils = new FileUtils();
			if(fileUtils.isFileExist(fileName, path)){
				return 1;
			}else{
				input = getInputStreamFromUrl(urlStr);
				File resultFile = fileUtils.write2SDFromInput(path, fileName, input);
				if(resultFile == null){
					return -1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally{
			try {
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return 0;
	}
		public InputStream getInputStreamFromUrl(String urlStr) throws MalformedURLException, IOException{
		URL url = new URL(urlStr);
		HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
		InputStream input = urlConn.getInputStream();
		return input;
	}
	
	
	
}

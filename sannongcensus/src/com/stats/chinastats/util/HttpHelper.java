package com.stats.chinastats.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HttpHelper {
	// ��־���
	private static String TAG = "HTTPHELPER";
	// ��ʱʱ��
	private static int TIMEOUT = 5 * 10000;

	/**
	 * http get����,�ɹ����ؽ��ʧ�ܷ��ؿ�
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String get(String url) throws Exception {
		String result = "";

		HttpGet httpGet = new HttpGet(url);

		HttpClient client = new DefaultHttpClient();

		HttpResponse response = client.execute(httpGet);

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			result = EntityUtils.toString(response.getEntity());
		}
		return result;
	}

	/**
	 * http post����,�ɹ����ؽ��ʧ�ܷ��ؿ�
	 * 
	 * @param url
	 * @param map
	 *            ����
	 * @throws Exception
	 */
	public static String post(String url, HashMap<String, String> map)
			throws Exception {

		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIMEOUT);
		HttpPost post = new HttpPost(url);
		// post.setHeaders(headers);
		String result = "";
		ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				BasicNameValuePair pair = new BasicNameValuePair(
						entry.getKey(), entry.getValue());
				pairList.add(pair);
			}

		}

		HttpEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			Log.i(TAG, "result =>" + result);
		}
		return result;
	}

	/**
	 * post��ʽ�����ֽ�
	 * 
	 * @param url
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String PostBytes(String url, byte[] bytes) throws Exception {

		URL murl = new URL(url);
		final HttpURLConnection con = (HttpURLConnection) murl.openConnection();

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);

		con.setRequestMethod("POST");
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		con.setRequestProperty("Content-Type", "text/html");
		// con.setRequestProperty("cookie", cookie);

		con.setReadTimeout(TIMEOUT);
		con.setConnectTimeout(TIMEOUT);
		Log.i(TAG, url);
		DataOutputStream dsDataOutputStream = new DataOutputStream(
				con.getOutputStream());
		dsDataOutputStream.write(bytes, 0, bytes.length);

		dsDataOutputStream.close();
		if (con.getResponseCode() == HttpStatus.SC_OK) {
			InputStream isInputStream = con.getInputStream();
			int ch;
			StringBuffer buffer = new StringBuffer();
			while ((ch = isInputStream.read()) != -1) {
				buffer.append((char) ch);
			}

			Log.i(TAG, "GetDataFromServer>" + buffer.toString());

			return buffer.toString();
		} else {
			return "";
		}

	}

	/**
	 * �ж��Ƿ��п�������
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			return false;
		}
		return true;
	}

	public boolean isNetworkAvailable(Context context) {
		int i = 1;
		int j = 0;
		ConnectivityManager localConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo.State localState1 = localConnectivityManager
				.getNetworkInfo(j).getState();
		NetworkInfo.State localState2 = localConnectivityManager
				.getNetworkInfo(i).getState();
		if ((NetworkInfo.State.CONNECTED.equals(localState1))
				|| (NetworkInfo.State.CONNECTED.equals(localState2)))
			return true;
		else
			return false;
	}

	/**
	 * �����ļ�
	 * 
	 * @param url
	 * @param savaPath
	 * @return
	 * @throws Exception
	 */
	public static boolean downloadFile(String url, String savaPath)
			throws Exception {
		InputStream inputstream = null;
		URL downloadUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) downloadUrl
				.openConnection();
		conn.setConnectTimeout(TIMEOUT);
		if (conn.getResponseCode() == HttpStatus.SC_OK) {
			inputstream = conn.getInputStream();
			File file = new File(savaPath);
			file.createNewFile();
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			byte[] buf = new byte[1024];
			int length = 0;
			while ((length = inputstream.read(buf, 0, 1024)) != -1) {
				raf.write(buf, 0, length);
			}
			inputstream.close();
			raf.close();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �?��ʽ�ϴ��ļ�,ʧ�ܷ��ؿ�
	 * 
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public static String postFile(String actionUrl, Map<String, String> params,
			Map<String, File> files) throws Exception {
		String BOUNDARY = "---------------------------"
				+ System.currentTimeMillis();// �ָ��
		String PREFIX = "--"; // ǰ׺
		String LINEND = "\r\n"; // ���з�
		String MULTIPART_FROM_DATA = "multipart/form-data";// �������
		String CHARSET = "UTF-8";// �ַ����

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(TIMEOUT); // ������ʱ��
		conn.setDoInput(true);// ��������
		conn.setDoOutput(true);// �������
		conn.setUseCaches(false); // ������ʹ�û���
		// ����ͷ��Ϣ
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// ������ƴ�ı����͵Ĳ���
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());

		InputStream in = null;
		// �����ļ����
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				// name��post�д��εļ� filename���ļ������
				sb1.append("Content-Disposition: form-data; name=\""
						+ file.getKey() + "\"; filename=\"" + file.getValue()
						+ "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(file.getValue());

				int bytesAvailable;
				while ((bytesAvailable = is.available()) > 0) {
					int bufferSize = Math.min(bytesAvailable, 4096);
					byte[] buffer = new byte[bufferSize];
					int bytesRead = is.read(buffer, 0, bufferSize);
					outStream.write(buffer, 0, bytesRead);
				}

				is.close();
				outStream.write(LINEND.getBytes());
			}

			// ��������־

		}
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		outStream.close();
		// �õ���Ӧ��
		StringBuilder sb2 = null;
		int res = conn.getResponseCode();
		if (res == 200) {
			in = conn.getInputStream();
			int ch;
			sb2 = new StringBuilder();
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
		} else {
			return "";
		}
		conn.disconnect();
		return sb2.toString();
	}

	public static String downLoadText(String urlStr) {

		String resultString = null;
		InputStream in = null;
		BufferedReader bufferedReader = null;

		StringBuffer sbBuffer = new StringBuffer();

		try {

			// �Ȼ�ȡURL����

			URL url = new URL(urlStr);

			// ��bһ��Httpl�ӵĶ���
             System.out.println("urlstr:"+urlStr);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setConnectTimeout(TIMEOUT);
			if (httpConn.getResponseCode() == HttpStatus.SC_OK) {
	             System.out.println("ok:");
			in = httpConn.getInputStream();
				// ʹ��IO���ȡ��ݣ�
				bufferedReader = new BufferedReader(new InputStreamReader(in));

				// ѭ�����ÿһ�У�
				while ((resultString = bufferedReader.readLine()) != null) {

					sbBuffer.append(resultString);
				}
				bufferedReader.close();
				in.close();
		}
		 httpConn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();

		} 
	return sbBuffer.toString();
	}

	public static String downloadXML(String urlStr) {
		HttpDownloader httpDownloader = new HttpDownloader();
		String result = httpDownloader.download(urlStr);
		return result;
	}
}
package com.yulebaby.teacher.net;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.BaseModel;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.model.Login;
import com.yulebaby.teacher.net.analysis.ListDataAnalysis;
import com.yulebaby.teacher.net.analysis.TextDataAnalysis;

public class HttpHelper {

	public static class NetworkType {
		public static int NotAvailable = -100;
	}

	public static boolean checkNetWorkInfo(Context context) {
		ConnectivityManager cManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}

	public static ModelData<Login> login(Context context, String url,
			ArrayList<NameValuePair> params, Json2DataInterface<Login> analyze)
			throws JSONException, ParseException, IOException {
		ModelData<Login> data = null;
		if (!checkNetWorkInfo(context)) {
			// data.setResultCode(NetworkType.NotAvailable);
			return data;
		}
		HttpParams httpParameters = new BasicHttpParams();
		// HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
		// HttpConnectionParams.setSoTimeout(httpParameters, 20000);
		HttpClient client = new DefaultHttpClient(httpParameters);
		HttpPost request = new HttpPost(url);
		try {
			if (params != null)
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			String strResult = java.net.URLDecoder.decode(
					EntityUtils.toString(response.getEntity()), "UTF-8");
			System.out.println("strResult------>" + strResult);
			JSONObject json = new JSONObject(strResult);
			data = analyze.analyzeJson(context, json);
			List<Cookie> cookies = ((DefaultHttpClient) client)
					.getCookieStore().getCookies();
			for (Cookie cookie : cookies) {
				Log.i("Cookie", cookie.toString());
				if ("jsessionid".equals(cookie.getName().trim().toLowerCase())) {
					App.getInstance().getUserInfo()
							.setCookie(cookie.getValue());
				}
			}
		}
		client.getConnectionManager().shutdown();
		return data;
	}

	public static <T extends BaseModel> ModelData<T> getInfo(Context context,
			String url, ArrayList<NameValuePair> params,
			Json2DataInterface<T> analyze) throws JSONException,
			ParseException, IOException {
		Log.d(App.TAG, "Httphelper Url = " + url);
		ModelData<T> data = null;/* new ModelData<T>(); */
		if (!checkNetWorkInfo(context)) {
			// data.setResultCode(NetworkType.NotAvailable);
			return data;
		}
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance(App.Name);
		HttpPost request = new HttpPost(url);
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10000);
		request.setParams(httpParameters);
		// BasicHttpContext mHttpContext = new BasicHttpContext();
		// CookieStore mCookieStore = new BasicCookieStore();
		// Cookie cookie = new BasicClientCookie("JSESSIONID",
		// UserInfo.getCookie());
		// mCookieStore.addCookie(cookie);
		// mHttpContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
		request.setHeader("Cookie", "JSESSIONID="
				+ App.getInstance().getUserInfo().getCookie());

		try {
			if (params != null)
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpResponse response = httpClient.execute(request/* , mHttpContext */);
		try {

			if (response.getStatusLine().getStatusCode() == 200) {
				String strResult = java.net.URLDecoder.decode(
						EntityUtils.toString(response.getEntity()), "UTF-8");
				System.out.println("strResult------>" + strResult);
				JSONObject json = new JSONObject(strResult);
				data = analyze.analyzeJson(context, json);
				if (url.contains(Url.host_url + Url.Lesson.lesson)) {
					NameValuePair typeParam = params.get(0);
					String type = typeParam.getValue();
					String lastUpdateTime = "";
					if (json.has("data")) {
						JSONObject dataJson = json.getJSONObject("data");
						if (dataJson.has("lastUpdateTime")) {
							lastUpdateTime = dataJson
									.getString("lastUpdateTime");
						}
					}
					System.out.println("save lesson:" + type
							+ " to preferences");
					SharedPreferences sp = context.getSharedPreferences(
							Commons.TEACHER_PREFERE, Context.MODE_PRIVATE);

					if (type.equals(Commons.TYPE_CHINESE)) {
						sp.edit()
								.putString(Commons.chineseTime, lastUpdateTime)
								.commit();
					}
					if (type.equals(Commons.TYPE_ENGLISH)) {
						sp.edit()
								.putString(Commons.englishTime, lastUpdateTime)
								.commit();
					}
					if (type.equals(Commons.TYPE_WATER)) {
						sp.edit().putString(Commons.waterTime, lastUpdateTime)
								.commit();
					}

					sp.edit().putString("lesson" + type, strResult).commit();

				}

			}
		} catch (Exception e) {
			System.out.println("HttpHelper　analyzeJson　error:"+e);
		} finally {
			//System.out.println("HttpClient close!!!");
			httpClient.close();
		}
		return data;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends BaseModel> ModelData<T> setInfo(Context context,
			String url, ArrayList<NameValuePair> params) throws JSONException,
			ParseException, IOException {
		ModelData<T> data = null;/* new ModelData<T>(); */
		if (!checkNetWorkInfo(context)) {
			// data.setResultCode(NetworkType.NotAvailable);
			return data;
		}
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance(App.Name);
		HttpPost request = new HttpPost(url);
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
		HttpConnectionParams.setSoTimeout(httpParameters, 5000);
		request.setParams(httpParameters);
		request.setHeader("Cookie", "JSESSIONID="
				+ App.getInstance().getUserInfo().getCookie());
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			String strResult = java.net.URLDecoder.decode(
					EntityUtils.toString(response.getEntity()), "UTF-8");
			System.out.println("strResult------>" + strResult);
			JSONObject json = new JSONObject(strResult);
			data = new TextDataAnalysis().analyzeJson(context, json);
		}
		httpClient.close();
		return data;
	}

	// public static Object uploadFile(Context context, String url,
	// String fileName, String filePath, AnalyzeJsonInterface analyze)
	// throws Exception, IOException {
	// if (!checkNetWorkInfo(context)) {
	// return null;
	// }
	// Object object = null;
	// HttpClient httpclient = new DefaultHttpClient();
	// httpclient.getParams().setParameter(
	// CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	// HttpPost httppost = new HttpPost(url);
	// httppost.addHeader("filename", fileName);
	// httppost.addHeader("user", fileName);
	// httppost.addHeader("detail", "1");
	// File file = new File(filePath);
	// FileEntity reqEntity = new FileEntity(file, "binary/octet-stream");
	// httppost.setEntity(reqEntity);
	// reqEntity.setContentType("binary/octet-stream");
	//
	// System.out.println("executing request " + httppost.getRequestLine());
	// HttpResponse response = httpclient.execute(httppost);
	// HttpEntity resEntity = response.getEntity();
	//
	// System.out.println(response.getStatusLine());
	// if (resEntity != null) {
	// if (response.getStatusLine().getStatusCode() == 200) {
	// String strResult = java.net.URLDecoder.decode(
	// EntityUtils.toString(resEntity), "UTF-8");
	// System.out.println("strResult------>" + strResult);
	// JSONObject json = new JSONObject(strResult);
	// object = analyze.analyzeJson(context, json);
	// }
	// }
	// if (resEntity != null) {
	// resEntity.consumeContent();
	// }
	// httpclient.getConnectionManager().shutdown();
	// return object;
	// }
	//
	public static <T extends BaseModel> ModelData<T> uploadFile(
			Context context, String url, String id, File file)
			throws Exception, IOException {
		ModelData<T> data = null;/* new ModelData<T>(); */
		if (!checkNetWorkInfo(context)) {
			// data.setResultCode(NetworkType.NotAvailable);
			return data;
		}
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// AndroidHttpClient httpClient =
		// AndroidHttpClient.newInstance(App.Name);
		HttpPost request = new HttpPost(url);
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 30 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, 30 * 1000);
		request.setParams(httpParameters);
		request.setHeader("Cookie", "JSESSIONID="
				+ App.getInstance().getUserInfo().getCookie());
		// try {
		// //request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }

		// JSONObject params = new JSONObject();
		// params.put("id", id);
		if (null != file) {
			MultipartEntity mpEntity = new MultipartEntity();
			FileBody cbFile = new FileBody(file, "image/*", "UTF-8");
			// /StringBody cbMessage = new
			// StringBody(params.toString(),"application/json",
			// Charset.forName("UTF-8"));
			mpEntity.addPart("file", cbFile);// 上传文件
			mpEntity.addPart("id", new StringBody(id));// 上传信息
			// httppost.setEntity(mpEntity);
			request.setEntity(mpEntity);
		}

		System.out.println("executing request " + request.getRequestLine());
		HttpResponse response = httpClient.execute(request);
		HttpEntity resEntity = response.getEntity();
		//
		// System.out.println(response.getStatusLine());
		// if (resEntity != null) {
		// System.out.println(EntityUtils.toString(resEntity));
		// }
		// if (resEntity != null) {
		// resEntity.consumeContent();
		// }

		if (response.getStatusLine().getStatusCode() == 200) {
			String strResult = java.net.URLDecoder.decode(
					EntityUtils.toString(response.getEntity()), "UTF-8");
			System.out.println("strResult------>" + strResult);
			JSONObject json = new JSONObject(strResult);
			data = new TextDataAnalysis().analyzeJson(context, json);
		}

		httpClient.getConnectionManager().shutdown();
		// httpClient.close();

		return data;

	}

}

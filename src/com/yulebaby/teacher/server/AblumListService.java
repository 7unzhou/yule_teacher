package com.yulebaby.teacher.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.TextUtils;

import com.yulebaby.teacher.model.AblumModel;
import com.yulebaby.teacher.model.OrderCustom;
import com.yulebaby.teacher.net.HttpHelper;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.net.analysis.Data4ListAnalysis;
import com.yulebaby.teacher.net.analysis.DataAnalysis;
import com.yulebaby.teacher.net.analysis.ListDataAnalysis;
import com.yulebaby.teacher.net.analysis.adapter.AblumListAdapter;
import com.yulebaby.teacher.net.analysis.adapter.OrderListAdapter;
import com.yulebaby.teacher.net.analysis.adapter.SearchAListAdapter;

public class AblumListService {
	public static ModelData<AblumModel> albumList(Context context, String page) {
		ModelData<AblumModel> result = null;
		String url = Url.host_url + Url.AblumList.albumList;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(page)) {
			params.add(new BasicNameValuePair("page", page));
		}
		try {
			result = HttpHelper.getInfo(context, url, params,
					new Data4ListAnalysis<AblumModel>(new AblumListAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public static ModelData<AblumModel> searchAlbumList(Context context,
			String key) {
		ModelData<AblumModel> result = null;
		String url = Url.host_url + Url.AblumList.search;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		// if(!TextUtils.isEmpty(page)){
		params.add(new BasicNameValuePair("code", key));
		// }
		try {
			result = HttpHelper.getInfo(context, url, params,
					new ListDataAnalysis<AblumModel>(new SearchAListAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	/**
	 * 提交参数里有文件的数据
	 * 
	 * @param url
	 *            服务器地址
	 * @param param
	 *            参数
	 * @return 服务器返回结果
	 * @throws Exception
	 */
	public static ModelData uploadSubmit(Context context, String id, String monthage,
			File file)  {
		ModelData result = null;
		String url = Url.host_url + Url.AblumList.postPhoto;
		try {
			result = HttpHelper.uploadFile(context ,url, id, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

		// ModelData result = null;
		// HttpPost post = new HttpPost(url);
		//
		// MultipartEntity entity = new MultipartEntity();
		// entity.addPart("id", new StringBody(id));
		// if (!TextUtils.isEmpty(monthage)) {
		// entity.addPart("monthAge", new StringBody(monthage));
		// }
		// // if (param != null && !param.isEmpty()) {
		// // for (Map.Entry<String, String> entry : param.entrySet()) {
		// // entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
		// // }
		// // }
		// // 添加文件参数
		// if (file != null && file.exists()) {
		// entity.addPart("file", new FileBody(file));
		// }
		// post.setEntity(entity);
		// DefaultHttpClient httpClient = new DefaultHttpClient();
		// HttpResponse response = httpClient.execute(post);
		// int stateCode = response.getStatusLine().getStatusCode();
		// StringBuffer sb = new StringBuffer();
		// if (stateCode == HttpStatus.SC_OK) {
		// HttpEntity result = response.getEntity();
		// if (result != null) {
		// InputStream is = result.getContent();
		// BufferedReader br = new BufferedReader(
		// new InputStreamReader(is));
		// String tempLine;
		// while ((tempLine = br.readLine()) != null) {
		// sb.append(tempLine);
		// }
		// }
		// }
		// post.abort();
		// return sb.toString();
	}

}

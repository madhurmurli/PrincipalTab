package in.principaltab.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FirstTimeSyncParser {
	private static InputStream isf = null;
	private static JSONObject jObjf;
	private static String jsonf = null;
	private Boolean flag=false;

	public JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		try {
			if(method == "POST"){
				isf = null;
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
			//	Log.d("statusf", httpResponse.getStatusLine().getStatusCode()+"");
			//	statusCode = httpResponse.getStatusLine().getStatusCode();
				HttpEntity httpEntity = httpResponse.getEntity();
				isf = httpEntity.getContent();
			}else if(method == "GET"){
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				isf = httpEntity.getContent();
			}			
			

		} catch(HttpHostConnectException e){
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
		//	BufferedReader reader = new BufferedReader(new InputStreamReader(isf, "iso-8859-1"), 8);
			BufferedReader reader = new BufferedReader(new InputStreamReader(isf));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			isf.close();
			reader.close();
			if(sb!=null && sb.toString()!=""){
				jsonf = sb.toString();
			}
			
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		
		try {
			if(jsonf!=null && jsonf!=""){
				jObjf= new JSONObject(jsonf);
			}else{
				jObjf = new JSONObject();
				jObjf.put("success", 0);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			flag = true;
		} 
		
		finally{
			if(flag){
				try{
					jObjf = new JSONObject();
					jObjf.put("success", 0);
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
		}

		return jObjf;
	}

}

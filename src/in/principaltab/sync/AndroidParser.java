package in.principaltab.sync;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class AndroidParser {
	static HttpURLConnection connection;
	
	public static JSONObject makeRequest(String url_request, String urlParameters) throws JSONException{
		StringBuilder total = new StringBuilder();
		URL url;
		try {
			url = new URL(url_request);
			connection = (HttpURLConnection) url.openConnection();           
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches (false);

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			 BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			 String line;
			 while ((line = reader.readLine()) != null) {
			     total.append(line);
			 }
			 reader.close();
		}
		 catch (MalformedURLException e) {
				e.printStackTrace();
			}
		catch(IOException e){
			e.printStackTrace();
		}
		    finally {
		     connection.disconnect();	     
		   }
		
		return new JSONObject(total.toString());
	}

}

package in.principaltab.sync;

import in.schoolcom.adapter.SyncTime;
import in.schoolcom.sqlite.Section;
import in.schoolcom.sqlite.SqlDbHelper;
import in.schoolcom.sqlite.Temp;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class DownloadSection implements SyncUrl{
	static private ProgressDialog pDialog;
	static private SqlDbHelper sqlHandler;
	static private Context context;
	static private String deviceId;
	static private List<Temp> tList;
	static private int secSuccess = 0;
	static private List<Section> sList;
	static private int reqCall=1,reqAgain=1;
	static private JSONObject jsonReceived;
	
	public DownloadSection(Context con_text){
		context = con_text;
	//	sqlHandler = new SqlDbHelper(context);
		sqlHandler = SqlDbHelper.getInstance(context);
		pDialog = new ProgressDialog(context);
		tList = sqlHandler.selectTemp();
		for(Temp t:tList){
			deviceId = t.getDeviceId();
		}
	}
	
	static class CalledDownloadSection extends AsyncTask<String, String, String>{
		FirstTimeSyncParser jsonParser = new FirstTimeSyncParser();
		
		protected void onPreExecute(){
			super.onPreExecute();
			SyncTime.updateSyncProgress(context);	
			pDialog.setMessage("Downloading 6 of 19 ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			jsonReceived = new JSONObject();
			sList = new ArrayList<Section>();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("tab_id", deviceId));
			param.add(new BasicNameValuePair("table_name", "section"));
			param.add(new BasicNameValuePair("req_call",reqCall+""));
			jsonReceived = jsonParser.makeHttpRequest(urlSync, "POST", param);

			if(jsonReceived!=null){
				JSONArray sync = null;
				try{
					secSuccess = jsonReceived.getInt(TAG_SUCCESS);
					if(secSuccess == 1){
						sync = jsonReceived.getJSONArray("Sync");
						reqAgain = jsonReceived.getInt("req_again");
						reqCall = reqAgain;
						for(int i=0; i<sync.length(); i++){
							JSONObject j = sync.getJSONObject(i);
							Section s = new Section();
							s.setClassId(j.getInt("ClassId"));
							s.setClassTeacherId(j.getInt("ClassTeacherId"));
							s.setSchoolId(j.getInt("SchoolId"));
							s.setSectionId(j.getInt("SectionId"));
							s.setSectionName(j.getString("SectionName"));
							sList.add(s);				
						}
						sqlHandler.initSection(sList);
					}

				}
				catch(JSONException e){
					e.printStackTrace();
					reqAgain=0;
				}
			}
				
			return null;
		}
		
		protected void onPostExecute(String s){
			super.onPostExecute(s);
			pDialog.dismiss();
			if(secSuccess == 0){
				SyncTime.updateSyncFailure(context);
				Intent i = new Intent();
				i.setClassName("in.schoolcom", "in.schoolcom.MainActivity");
				context.startActivity(i);
			}else{
				SyncTime.updateSyncProgress(context);
				if(reqAgain!=0){
					new CalledDownloadSection().execute();
				}else{
					new DownloadStudents(context).DownStudents();
				}
				
			}
			
		}
		
	}
	
	public void DownSection(){
		sqlHandler.deleteTable("section");
		new CalledDownloadSection().execute();
	}

}

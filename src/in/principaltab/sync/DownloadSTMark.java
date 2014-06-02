package in.principaltab.sync;

import in.schoolcom.adapter.SchoolId;
import in.schoolcom.adapter.SyncTime;
import in.schoolcom.sqlite.SlipTestMark;
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

public class DownloadSTMark implements SyncUrl{	
	static private ProgressDialog pDialog;
	static private SqlDbHelper sqlHandler;
	static private Context context;
	static private String deviceId;
	static private List<Temp> tList;
	static private int stmarkSuccess = 0,schoolId;
	static private List<SlipTestMark> mList;
	static private int reqCall=1,reqAgain=1;
	static private JSONObject jsonReceived;
	
	public DownloadSTMark(Context con_text){
		context = con_text;
	//	sqlHandler = new SqlDbHelper(context);
		sqlHandler = SqlDbHelper.getInstance(context);
		pDialog = new ProgressDialog(context);
		tList = sqlHandler.selectTemp();
		for(Temp t:tList){
			deviceId = t.getDeviceId();
			schoolId = t.getSchoolId();
		}
	}
	
	static class CalledDownloadSTMark extends AsyncTask<String, String, String>{
		FirstTimeSyncParser jsonParser = new FirstTimeSyncParser();

		protected void onPreExecute(){
			super.onPreExecute();
			SyncTime.updateSyncProgress(context);
			pDialog.setMessage("Downloading 13 of 19 ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {		
			jsonReceived = new JSONObject();
			mList = new ArrayList<SlipTestMark>();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("tab_id", deviceId));
			param.add(new BasicNameValuePair("table_name", "sliptestmark_"+schoolId));
			param.add(new BasicNameValuePair("req_call",reqCall+""));
			jsonReceived = jsonParser.makeHttpRequest(urlSync, "POST", param);

			if(jsonReceived!=null){
				JSONArray sync = null;
				try{
					stmarkSuccess = jsonReceived.getInt(TAG_SUCCESS);
					if(stmarkSuccess == 1){
						sync = jsonReceived.getJSONArray("Sync");
						reqAgain = jsonReceived.getInt("req_again");
						reqCall = reqAgain;
						for(int i=0; i<sync.length(); i++){
							JSONObject j = sync.getJSONObject(i);
							SlipTestMark m = new SlipTestMark();
							m.setMark(j.getString("Mark"));
							m.setSchoolId(j.getInt("SchoolId"));
							m.setClassId(j.getInt("ClassId"));
							m.setSectionId(j.getInt("SectionId"));
							m.setStudentId(j.getInt("StudentId"));
							m.setSubjectId(j.getInt("SubjectId"));
							m.setNewSubjectId(j.getInt("NewSubjectId"));
							m.setSlipTestId(j.getInt("SlipTestId"));
							mList.add(m);
						}
						sqlHandler.initSlipTestMark(mList);
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
			if(stmarkSuccess == 0){
				SyncTime.updateSyncFailure(context);
				Intent i = new Intent();
				i.setClassName("in.schoolcom", "in.schoolcom.MainActivity");
				context.startActivity(i);
			}else{
				SyncTime.updateSyncProgress(context);
				if(reqAgain!=0){
					new CalledDownloadSTMark().execute();
				}else{
					new DownloadActivityMark(context).DownActivityMark();
				}
				
			}
			
		}
		
	}
	
	public void DownSTMark(){
		sqlHandler.deleteTable("sliptestmark_"+SchoolId.getSchoolId(context));
		new CalledDownloadSTMark().execute();
	}

}

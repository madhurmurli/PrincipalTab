package in.principaltab.sync;
import in.schoolcom.adapter.SyncTime;
import in.schoolcom.sqlite.SqlDbHelper;
import in.schoolcom.sqlite.StudentAttendance;
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

public class DownloadAttendance implements SyncUrl{	
	static private ProgressDialog pDialog;
	static private SqlDbHelper sqlHandler;
	static private Context context;
	static private String deviceId;
	static private List<Temp> tList;
	static private int hwSuccess = 0;
	static private List<StudentAttendance> saList;
	static private int reqCall=1,reqAgain=1;
	static private JSONObject jsonReceived;
	
	public DownloadAttendance(Context con_text){
		context = con_text;
	//	sqlHandler = new SqlDbHelper(context);
		sqlHandler = SqlDbHelper.getInstance(context);
		pDialog = new ProgressDialog(context);
		tList = sqlHandler.selectTemp();
		for(Temp t:tList){
			deviceId = t.getDeviceId();
		}
	}
	
	private static class CalledDownloadAttendance extends AsyncTask<String, String, String>{
		FirstTimeSyncParser jsonParser = new FirstTimeSyncParser();
		
		protected void onPreExecute(){
			super.onPreExecute();
			SyncTime.updateSyncProgress(context);	
			pDialog.setMessage("Downloading 17 of 19...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			jsonReceived = new JSONObject();
			saList = new ArrayList<StudentAttendance>();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("tab_id", deviceId));
			param.add(new BasicNameValuePair("table_name", "studentattendance"));
			param.add(new BasicNameValuePair("req_call",reqCall+""));
			jsonReceived = jsonParser.makeHttpRequest(urlSync, "POST", param);
			
			if(jsonReceived!=null){
				JSONArray sync = null;
				try{
					hwSuccess = jsonReceived.getInt(TAG_SUCCESS);
					reqAgain = jsonReceived.getInt("req_again");
					reqCall = reqAgain;
					if(hwSuccess == 1){
						sync = jsonReceived.getJSONArray("Sync");
						reqAgain = jsonReceived.getInt("req_again");
						reqCall = reqAgain;
						for(int i=0; i<sync.length(); i++){
							JSONObject j = sync.getJSONObject(i);
							StudentAttendance sa = new StudentAttendance();
							sa.setClassId(j.getInt("ClassId"));
							sa.setDateAttendance(j.getString("DateAttendance"));
							sa.setSchoolId(j.getInt("SchoolId"));
							sa.setSectionId(j.getInt("SectionId"));
							sa.setStudentId(j.getInt("StudentId"));
							sa.setTypeOfLeave(j.getString("TypeOfLeave"));
							saList.add(sa);		
						}
						sqlHandler.initStudentAttendance(saList);
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
			if(hwSuccess == 0){
				SyncTime.updateSyncFailure(context);
				Intent i = new Intent();
				i.setClassName("in.schoolcom", "in.schoolcom.MainActivity");
				context.startActivity(i);
			}else{
				SyncTime.updateSyncProgress(context);
				if(reqAgain!=0){
					new CalledDownloadAttendance().execute();
				}else{
					new DownloadSubjectExams(context).DownSubExm();
				}
				
			}
			
		}
		
	}
	
	public void DownAttendance(){
		sqlHandler.deleteTable("studentattendance");
		new CalledDownloadAttendance().execute();
	}

}

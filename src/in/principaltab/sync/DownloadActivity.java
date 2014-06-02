package in.principaltab.sync;

import in.schoolcom.adapter.SyncTime;
import in.schoolcom.sqlite.Activiti;
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

public class DownloadActivity implements SyncUrl{
	static private ProgressDialog pDialog;
	static private SqlDbHelper sqlHandler;
	static private Context context;
	static private String deviceId;
	static private List<Temp> tList;
	static private int actSuccess = 0, schoolId;
	static private List<Activiti> aList;
	static private int reqCall = 1,reqAgain = 1;
	static private JSONObject jsonReceived;
	
	public DownloadActivity(Context con_text){
		context = con_text;
	//	sqlHandler = new SqlDbHelper(context);
		sqlHandler = SqlDbHelper.getInstance(context);
		pDialog = new ProgressDialog(context);
		tList = sqlHandler.selectTemp();
		for(Temp t:tList){
			deviceId = t.getDeviceId();
		}
	}
	
	private static class CalledDownloadActivity extends AsyncTask<String, String, String>{
		FirstTimeSyncParser jsonParser = new FirstTimeSyncParser();
		
		protected void onPreExecute(){
			super.onPreExecute();
			SyncTime.updateSyncProgress(context);
			pDialog.setMessage("Downloading 1 of 19 ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {		
			jsonReceived = new JSONObject();
			aList = new ArrayList<Activiti>();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("tab_id", deviceId));
			param.add(new BasicNameValuePair("table_name", "activity"));
			param.add(new BasicNameValuePair("req_call",reqCall+""));
			jsonReceived = jsonParser.makeHttpRequest(urlSync, "POST", param);

			if(jsonReceived!=null){
				JSONArray sync = null;
				try{
					actSuccess = jsonReceived.getInt(TAG_SUCCESS);
					if(actSuccess == 1){
						sync = jsonReceived.getJSONArray("Sync");
						reqAgain = jsonReceived.getInt("req_again");
						reqCall = reqAgain;
						for(int i=0; i<sync.length(); i++){
							JSONObject syc = sync.getJSONObject(i);	
							Activiti a = new Activiti();
							a.setActivityId(syc.getInt("ActivityId"));
							a.setActivityName(syc.getString("ActivityName"));
							a.setCalculation(syc.getInt("Calculation"));
							a.setClassId(syc.getInt("ClassId"));
							a.setExamId(syc.getInt("ExamId"));
							a.setMaximumMark(syc.getInt("MaximumMark"));
							a.setSectionId(syc.getInt("SectionId"));
							a.setSubActivity(syc.getInt("SubActivity"));
							a.setSubjectId(syc.getInt("SubjectId"));
							a.setSchoolId(syc.getInt("SchoolId"));
							a.setRubrixId(syc.getInt("RubrixId"));
							a.setWeightage(syc.getDouble("Weightage"));
							schoolId = syc.getInt("SchoolId");
							aList.add(a);						
						}
						sqlHandler.initActivity(aList);
						sqlHandler.updateSchoolId(schoolId);
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
			if(actSuccess == 0){
				SyncTime.updateSyncFailure(context);
				Intent i = new Intent();
				i.setClassName("in.schoolcom", "in.schoolcom.MainActivity");
				context.startActivity(i);
			}else{
				SyncTime.updateSyncProgress(context);
				if(reqAgain!=0){
					new CalledDownloadActivity().execute();
				}else{
					new DownloadClass(context).DownClass();
				}
				
			}
		}
		
	}
	
	public void DownActivity(){
		sqlHandler.deleteOtherTables();
		sqlHandler.deleteTable("activity");
		sqlHandler.deleteTable("teacher");
		new CalledDownloadActivity().execute();
		
	}

}

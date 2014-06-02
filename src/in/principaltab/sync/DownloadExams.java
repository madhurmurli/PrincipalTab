package in.principaltab.sync;

import in.schoolcom.adapter.SyncTime;
import in.schoolcom.sqlite.Exams;
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

public class DownloadExams implements SyncUrl{
	static private ProgressDialog pDialog;
	static private SqlDbHelper sqlHandler;
	static private Context context;
	static private String deviceId;
	static private List<Temp> tList;
	static private int exmSuccess = 0;
	static private List<Exams> eList;
	static private int reqCall=1,reqAgain=1;
	static private JSONObject jsonReceived;
	
	public DownloadExams(Context con_text){
		context = con_text;
	//	sqlHandler = new SqlDbHelper(context);
		sqlHandler = SqlDbHelper.getInstance(context);
		pDialog = new ProgressDialog(context);
		tList = sqlHandler.selectTemp();
		for(Temp t:tList){
			deviceId = t.getDeviceId();
		}
	}
	
	static class CalledDownloadExams extends AsyncTask<String, String, String>{
		FirstTimeSyncParser jsonParser = new FirstTimeSyncParser();
		
		protected void onPreExecute(){
			super.onPreExecute();
			SyncTime.updateSyncProgress(context);	
			pDialog.setMessage("Downloading 3 of 19 ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			jsonReceived = new JSONObject();
			eList = new ArrayList<Exams>();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("tab_id", deviceId));
			param.add(new BasicNameValuePair("table_name", "exams"));
			param.add(new BasicNameValuePair("req_call",reqCall+""));
			jsonReceived = jsonParser.makeHttpRequest(urlSync, "POST", param);
	
			if(jsonReceived!=null){
				JSONArray sync = null;
				try{
					exmSuccess = jsonReceived.getInt(TAG_SUCCESS);
					if(exmSuccess == 1){
						sync = jsonReceived.getJSONArray("Sync");
						reqAgain = jsonReceived.getInt("req_again");
						reqCall = reqAgain;
						for(int i=0; i<sync.length(); i++){
							JSONObject j = sync.getJSONObject(i);
							Exams e = new Exams();
							e.setClassId(j.getInt("ClassId"));
							e.setExamId(j.getInt("ExamId"));
							e.setExamName(j.getString("ExamName"));
							e.setFileName(j.getString("FileName"));
							e.setGradeSystem(j.getInt("GradeSystem"));
							e.setMarkUploaded(j.getInt("MarkUploaded"));
							e.setOrderId(j.getInt("OrderId"));
							e.setPercentage(j.getString("Percentage"));
							e.setPortions(j.getString("Portions"));
							e.setSchoolId(j.getInt("SchoolId"));
							e.setSubjectIDs(j.getString("SubjectIDs"));
							e.setTerm(j.getInt("Term"));
							e.setTimeTable(j.getString("TimeTable"));
							eList.add(e);		
						}
						sqlHandler.initExams(eList);
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
			if(exmSuccess == 0){
				SyncTime.updateSyncFailure(context);
				Intent i = new Intent();
				i.setClassName("in.schoolcom", "in.schoolcom.MainActivity");
				context.startActivity(i);
			}else{
				SyncTime.updateSyncProgress(context);
				if(reqAgain!=0){
					new CalledDownloadExams().execute();
				}else{
					new DownloadPortion(context).DownPortion();
				}
				
			}
			
		}
		
	}
	
	public void DownExams(){
		sqlHandler.deleteTable("exams");
		new CalledDownloadExams().execute();
	}

}

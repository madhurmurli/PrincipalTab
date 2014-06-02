package in.principaltab.sync;

import in.schoolcom.adapter.SyncTime;
import in.schoolcom.sqlite.SlipTestt;
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

public class DownloadSlipTestF implements SyncUrl{
	static private ProgressDialog pDialog;
	static private SqlDbHelper sqlHandler;
	static private Context context;
	static private String deviceId;
	static private List<Temp> tList;
	static private int stSuccess = 0;
	static private List<SlipTestt> sList;
	static private int reqCall=1,reqAgain=1;
	static private JSONObject jsonReceived;
	
	public DownloadSlipTestF(Context con_text){
		context = con_text;
	//	sqlHandler = new SqlDbHelper(context);
		sqlHandler = SqlDbHelper.getInstance(context);
		pDialog = new ProgressDialog(context);
		tList = sqlHandler.selectTemp();
		for(Temp t:tList){
			deviceId = t.getDeviceId();
		}
	}
	
	static class CalledDownloadSlipTest extends AsyncTask<String, String, String>{
		FirstTimeSyncParser jsonParser = new FirstTimeSyncParser();
		
		protected void onPreExecute(){
			super.onPreExecute();
			SyncTime.updateSyncProgress(context);
			pDialog.setMessage("Downloading 12 of 19 ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			jsonReceived = new JSONObject();
			sList = new ArrayList<SlipTestt>();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("tab_id", deviceId));
			param.add(new BasicNameValuePair("table_name", "sliptest"));
			param.add(new BasicNameValuePair("req_call",reqCall+""));
			jsonReceived = jsonParser.makeHttpRequest(urlSync, "POST", param);
	
			if(jsonReceived!=null){
				JSONArray sync = null;
				try{
					stSuccess = jsonReceived.getInt(TAG_SUCCESS);
					if(stSuccess == 1){
						sync = jsonReceived.getJSONArray("Sync");
						reqAgain = jsonReceived.getInt("req_again");
						reqCall = reqAgain;
						for(int i=0; i<sync.length(); i++){
							JSONObject j = sync.getJSONObject(i);
							SlipTestt s = new SlipTestt();
							s.setAverageMark(j.getDouble("AverageMark"));
							s.setClassId(j.getInt("ClassId"));
							s.setExtraPortion(j.getString("ExtraPortion"));
							s.setMaximumMark(j.getInt("MaximumMark"));
							s.setPortion(j.getInt("Portion"));
							s.setPortionName(j.getString("PortionName"));
							s.setSchoolId(j.getInt("SchoolId"));
							s.setSectionId(j.getInt("SectionId"));
							s.setTestDate(j.getString("TestDate"));
							s.setMarkEntered(j.getInt("MarkEntered"));
							s.setSubjectId(j.getInt("SubjectId"));
							s.setNewSubjectId(j.getInt("NewSubjectId"));
							s.setSlipTestId(j.getInt("SlipTestId"));
							s.setSlipTestName(j.getString("SlipTestName"));
							s.setIsActivity(j.getInt("IsActivity"));
							s.setGrade(j.getInt("Grade"));
							s.setCount(j.getInt("Count"));
							s.setEmployeeId(j.getInt("EmployeeId"));
							s.setWeightage(j.getDouble("Weightage"));
							sList.add(s);
						}
						sqlHandler.initSlipTest(sList);
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
			if(stSuccess == 0){
				SyncTime.updateSyncFailure(context);
				Intent i = new Intent();
				i.setClassName("in.schoolcom", "in.schoolcom.MainActivity");
				context.startActivity(i);
			}else{
				SyncTime.updateSyncProgress(context);
				if(reqAgain!=0){
					new CalledDownloadSlipTest().execute();
				}else{
					new DownloadSTMark(context).DownSTMark();
				}
				
			}
			
		}
		
	}
	
	public void DownSlipTest(){
		sqlHandler.deleteTable("sliptest");
		new CalledDownloadSlipTest().execute();
	}

}

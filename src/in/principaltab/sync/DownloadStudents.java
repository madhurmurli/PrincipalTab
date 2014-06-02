package in.principaltab.sync;

import in.schoolcom.adapter.SyncTime;
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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;

public class DownloadStudents implements SyncUrl{
	static private ProgressDialog pDialog;
	static private SqlDbHelper sqlHandler;
	static private SQLiteDatabase sqliteDatabase;
	static private Context context;
	static private String deviceId;
	static private List<Temp> tList;
	static private int studSuccess = 0;
	static private int reqCall=1,reqAgain=1;
	static private JSONObject jsonReceived;
	
	public DownloadStudents(Context con_text){
		context = con_text;
	//	sqlHandler = new SqlDbHelper(context);
		sqlHandler = SqlDbHelper.getInstance(context);
		pDialog = new ProgressDialog(context);
		tList = sqlHandler.selectTemp();
		for(Temp t:tList){
			deviceId = t.getDeviceId();
		}
	}
	
	static class CalledDownloadStudents extends AsyncTask<String, String, String>{
		FirstTimeSyncParser jsonParser = new FirstTimeSyncParser();
		
		protected void onPreExecute(){
			super.onPreExecute();
			SyncTime.updateSyncProgress(context);
			pDialog.setMessage("Downloading 7 of 19 ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			jsonReceived = new JSONObject();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("tab_id", deviceId));
			param.add(new BasicNameValuePair("table_name", "students"));
			param.add(new BasicNameValuePair("req_call",reqCall+""));
			jsonReceived = jsonParser.makeHttpRequest(urlSync, "POST", param);
	
			if(jsonReceived!=null){
				JSONArray sync = null;
				try{
					studSuccess = jsonReceived.getInt(TAG_SUCCESS);
					if(studSuccess == 1){
						sync = jsonReceived.getJSONArray("Sync");
						reqAgain = jsonReceived.getInt("req_again");
						reqCall = reqAgain;
						sqliteDatabase = sqlHandler.getWritableDatabase();
						String sql = "INSERT INTO students (SOWSID,StudentId,SchoolId,ClassId,SectionId,AdmissionNo,RollNoInClass,Username,Password,Image,Name,FatherName,MotherName,"+
							"DateOfBirth,Gender,Email,Mobile1,Mobile2,Pincode,Address,TransportationTypeId,Community,Income,IsLoggedIn,Locked,NoSms,CreationDateTime,LastLoginTime,"+
							"LoginCount,FeedbackSkip,NotificationsRead,IPAddress) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						sqliteDatabase.beginTransaction();
						SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
						for(int i=0; i<sync.length(); i++){
							JSONObject j = sync.getJSONObject(i);
							stmt.bindString(1, j.getString("SOWSID"));
							stmt.bindLong(2, j.getInt("StudentId"));
							stmt.bindLong(3, j.getInt("SchoolId"));
							stmt.bindLong(4, j.getInt("ClassId"));
							stmt.bindLong(5, j.getInt("SectionId"));
							stmt.bindString(6, j.getString("AdmissionNo"));
							stmt.bindLong(7, j.getInt("RollNoInClass"));
							stmt.bindString(8, j.getString("Username"));
							stmt.bindString(9, j.getString("Password"));
							stmt.bindString(10, j.getString("Image"));
							stmt.bindString(11, j.getString("Name"));
							stmt.bindString(12, j.getString("FatherName"));
							stmt.bindString(13, j.getString("MotherName"));
							stmt.bindString(14, j.getString("DateOfBirth"));
							stmt.bindString(15, j.getString("Gender"));
							stmt.bindString(16, j.getString("Email"));
							stmt.bindString(17, j.getString("Mobile1"));
							stmt.bindString(18, j.getString("Mobile2"));
							stmt.bindString(19, j.getString("Pincode"));
							stmt.bindString(20, j.getString("Address"));
							stmt.bindLong(21, j.getInt("TransportationTypeId"));
							stmt.bindString(22, j.getString("Community"));
							stmt.bindLong(23, j.getInt("Income"));
							stmt.bindLong(24, j.getInt("IsLoggedIn"));
							stmt.bindLong(25, j.getInt("Locked"));
							stmt.bindLong(26, j.getInt("NoSms"));
							stmt.bindString(27, j.getString("CreationDateTime"));
							stmt.bindString(28, j.getString("LastLoginTime"));
							stmt.bindLong(29, j.getInt("LoginCount"));
							stmt.bindLong(30, j.getInt("FeedbackSkip"));
							stmt.bindString(31, j.getString("NotificationsRead"));
							stmt.bindString(32, j.getString("IPAddress"));
							stmt.execute();
							stmt.clearBindings();
						}
						sqliteDatabase.setTransactionSuccessful();
						sqliteDatabase.endTransaction();
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
			if(studSuccess == 0){
				SyncTime.updateSyncFailure(context);
				Intent i = new Intent();
				i.setClassName("in.schoolcom", "in.schoolcom.MainActivity");
				context.startActivity(i);
			}else{
				SyncTime.updateSyncProgress(context);
				if(reqAgain!=0){
					new CalledDownloadStudents().execute();
				}else{
					new DownloadSubActivity(context).DownSubActivity();
				}
				
			}
			
		}
		
	}
	
	public void DownStudents(){
		sqlHandler.deleteTable("students");
		new CalledDownloadStudents().execute();
	}

}

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

public class DownloadTeachers implements SyncUrl{
	static private ProgressDialog pDialog;
	static private SqlDbHelper sqlHandler;
	static private SQLiteDatabase sqliteDatabase;
	static private Context context;
	static private String deviceId;
	static private List<Temp> tList;
	static private int tSuccess = 0;
	static private int reqCall=1, reqAgain=1;
	static private JSONObject jsonReceived;
	
	public DownloadTeachers(Context con_text){
		context = con_text;
	//	sqlHandler = new SqlDbHelper(context);
		sqlHandler = SqlDbHelper.getInstance(context);
		pDialog = new ProgressDialog(context);
		tList = sqlHandler.selectTemp();
		for(Temp t:tList){
			deviceId = t.getDeviceId();
		}
	}
	
	static class CalledDownloadTeachers extends AsyncTask<String, String, String>{
		FirstTimeSyncParser jsonParser = new FirstTimeSyncParser();
		
		protected void onPreExecute(){
			super.onPreExecute();
			SyncTime.updateSyncProgress(context);
			pDialog.setMessage("Downloading 19 of 19 ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			jsonReceived = new JSONObject();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("tab_id", deviceId));
			param.add(new BasicNameValuePair("table_name", "teacher"));
			param.add(new BasicNameValuePair("req_call",reqCall+""));
			jsonReceived = jsonParser.makeHttpRequest(urlSync, "POST", param);
	
			if(jsonReceived!=null){
				JSONArray sync = null;
				try{
					tSuccess = jsonReceived.getInt(TAG_SUCCESS);
					if(tSuccess == 1){
						sync = jsonReceived.getJSONArray("Sync");
						reqAgain = jsonReceived.getInt("req_again");
						reqCall = reqAgain;
						sqliteDatabase = sqlHandler.getWritableDatabase();
						String sql = "INSERT INTO teacher (SOWTID,TeacherId,Image,Username,Password,SchoolId,Name,Mobile,Qualification,Address,DateOfJoining,Gender,Email,"+
							"Pincode,TransportationTypeId,Community,CreationDateTime,LastLoginTime,IPAddress,Locked,TabUser,TabPass) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						sqliteDatabase.beginTransaction();
						SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
						for(int i=0; i<sync.length(); i++){
							JSONObject j = sync.getJSONObject(i);
							stmt.bindString(1, j.getString("SOWTID"));
							stmt.bindLong(2, j.getInt("TeacherId"));
							stmt.bindString(3, j.getString("Image"));
							stmt.bindString(4, j.getString("Username"));
							stmt.bindString(5, j.getString("Password"));
							stmt.bindLong(6, j.getInt("SchoolId"));
							stmt.bindString(7, j.getString("Name"));
							stmt.bindString(8, j.getString("Mobile"));
							stmt.bindString(9, j.getString("Qualification"));
							stmt.bindString(10, j.getString("Address"));
							stmt.bindString(11, j.getString("DateOfJoining"));
							stmt.bindString(12, j.getString("Gender"));
							stmt.bindString(13, j.getString("Email"));
							stmt.bindLong(14, j.getInt("Pincode"));
							stmt.bindLong(15, j.getInt("TransportationTypeId"));
							stmt.bindString(16, j.getString("Community"));
							stmt.bindString(17, j.getString("CreationDateTime"));
							stmt.bindString(18, j.getString("LastLoginTime"));
							stmt.bindString(19, j.getString("IPAddress"));
							stmt.bindLong(20, j.getInt("Locked"));
							stmt.bindLong(21, j.getInt("TabUser"));
							stmt.bindLong(22, j.getInt("TabPass"));						
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
			if(tSuccess == 0){
				SyncTime.updateSyncFailure(context);
				Intent i = new Intent();
				i.setClassName("in.schoolcom", "in.schoolcom.MainActivity");
				context.startActivity(i);
			}else{
				SyncTime.updateSyncProgress(context);
				if(reqAgain!=0){
					new CalledDownloadTeachers().execute();
				}else{
					new SubActivityProgress(context).findSubActProgress();
				}
				
			}
			
		}
		
	}
	
	public void DownTeachers(){
		sqlHandler.deleteTable("teacher");
		new CalledDownloadTeachers().execute();
	}

}

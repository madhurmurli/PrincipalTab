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
import android.util.Log;

public class DownloadSchool implements SyncUrl{
	static private ProgressDialog pDialog;
	static private SqlDbHelper sqlHandler;
	static private SQLiteDatabase sqliteDatabase;
	static private Context context;
	static private String deviceId;
	static private List<Temp> tList;
	static private int schoolSuccess = 0;
	static private int reqCall=1,reqAgain=1;
	static private JSONObject jsonReceived;
	
	public DownloadSchool(Context con_text){
		context = con_text;
	//	sqlHandler = new SqlDbHelper(context);
		sqlHandler = SqlDbHelper.getInstance(context);
		pDialog = new ProgressDialog(context);
		tList = sqlHandler.selectTemp();
		for(Temp t:tList){
			deviceId = t.getDeviceId();
		}
	}
	
	static class CalledDownloadSchool extends AsyncTask<String, String, String>{
		FirstTimeSyncParser jsonParser = new FirstTimeSyncParser();
		
		protected void onPreExecute(){
			super.onPreExecute();
			SyncTime.updateSyncProgress(context);
			pDialog.setMessage("Downloading 5 of 19 ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {

			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("tab_id", deviceId));
			param.add(new BasicNameValuePair("table_name", "school"));
			param.add(new BasicNameValuePair("req_call",reqCall+""));
			jsonReceived = jsonParser.makeHttpRequest(urlSync, "POST", param);

			if(jsonReceived!=null){
				JSONArray sync = null;
				try{
					schoolSuccess = jsonReceived.getInt(TAG_SUCCESS);
					Log.d("data", jsonReceived+"");
					if(schoolSuccess == 1){
						sync = jsonReceived.getJSONArray("Sync");
						reqAgain = jsonReceived.getInt("req_again");
						reqCall = reqAgain;
						sqliteDatabase = sqlHandler.getWritableDatabase();
						String sql = "INSERT INTO school (SchoolId,SchoolName,Website,ShortenedSchoolName,SenderID,ContactPersonName,SchoolAdminUsername,SchoolAdminPassword,"+
							"Address,address_short,Landline,Mobile,Mobile2,Email,City,State,District,Pincode,CreationDateTime,LastLoginTime,IPAddress,NumberofMobiles,RouteId,Locked,"+
							" PrincipalTeacherId,PathtoOpen,Syllabus,NumberofStudents,Launched,ClassIDs,CCEClassIDs) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+
							"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						sqliteDatabase.beginTransaction();
						SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
						for(int i=0; i<sync.length(); i++){
							JSONObject j = sync.getJSONObject(i);
							stmt.bindLong(1, j.getInt("SchoolId"));
							stmt.bindString(2, j.getString("SchoolName"));
							stmt.bindString(3, j.getString("Website"));
							stmt.bindString(4, j.getString("ShortenedSchoolName"));
							stmt.bindString(5, j.getString("SenderID"));
							stmt.bindString(6, j.getString("ContactPersonName"));
							stmt.bindString(7, j.getString("SchoolAdminUsername"));
							stmt.bindString(8, j.getString("SchoolAdminPassword"));
							stmt.bindString(9, j.getString("Address"));
							stmt.bindString(10, j.getString("address_short"));
							stmt.bindString(11, j.getString("Landline"));
							stmt.bindString(12, j.getString("Mobile"));
							stmt.bindString(13, j.getString("Mobile2"));
							stmt.bindString(14, j.getString("Email"));
							stmt.bindString(15, j.getString("City"));
							stmt.bindString(16, j.getString("State"));
							stmt.bindString(17, j.getString("District"));
							stmt.bindString(18, j.getString("Pincode"));
							stmt.bindString(19, j.getString("CreationDateTime"));
							stmt.bindString(20, j.getString("LastLoginTime"));
							stmt.bindString(21, j.getString("IPAddress"));
							stmt.bindLong(22, j.getInt("NumberofMobiles"));
							stmt.bindLong(23, j.getInt("RouteId"));
							stmt.bindLong(24, j.getInt("Locked"));
							stmt.bindLong(25, j.getInt("PrincipalTeacherId"));
							stmt.bindString(26, j.getString("PathtoOpen"));
							stmt.bindString(27, j.getString("Syllabus"));
							stmt.bindLong(28, j.getInt("NumberofStudents"));
							stmt.bindLong(29, j.getInt("Launched"));
							stmt.bindString(30, j.getString("ClassIDs"));
							stmt.bindString(31, j.getString("CCEClassIDs"));
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
			if(schoolSuccess == 0){
				SyncTime.updateSyncFailure(context);
				Intent i = new Intent();
				i.setClassName("in.schoolcom", "in.schoolcom.MainActivity");
				context.startActivity(i);
			}else{
				SyncTime.updateSyncProgress(context);
				if(reqAgain!=0){
					new CalledDownloadSchool().execute();
				}else{
					new DownloadSection(context).DownSection();
				}
				
			}
			
		}
		
	}
	
	public void DownSchool(){
		sqlHandler.deleteTable("school");
		new CalledDownloadSchool().execute();
	}


}

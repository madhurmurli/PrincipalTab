package in.principaltab.sync;

import java.util.List;

import in.schoolcom.adapter.SyncTime;
import in.schoolcom.model.PercentageSlipTest;
import in.schoolcom.sqlite.SqlDbHelper;
import in.schoolcom.sqlite.StAvg;
import in.schoolcom.sqlite.SubjectTeachers;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class SlipTestProgress {
	private static Context context;
	private static SqlDbHelper sqlHandler;
	private static ProgressDialog pDialog;
	
	public SlipTestProgress(Context con_text){
		context = con_text;
	}
	
	static class CalledStProgress extends AsyncTask<String, String, String>{
		private int avg;
		
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Preparing data (SlipTest Progress)...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
		//	sqlHandler = new SqlDbHelper(context);
			sqlHandler = SqlDbHelper.getInstance(context);
				List<SubjectTeachers> stList = sqlHandler.selectSubjectTeachers();
				for(SubjectTeachers st: stList){
					avg = PercentageSlipTest.findSlipTestPercentage(context, st.getSectionId(), st.getSubjectId(), st.getSchoolId());
					StAvg sav = new StAvg();
					sav.setSectionId(st.getSectionId());
					sav.setSubjectId(st.getSubjectId());
					sav.setSlipTestAvg(avg);
					sqlHandler.insertStAvg(sav);
				}
				
			return null;
		}
		
		protected void onPostExecute(String s){
			super.onPostExecute(s);
			pDialog.dismiss();
			SyncTime.updateSyncComplete(context);
			
			Intent intent = new Intent(context, in.schoolcom.adapter.SyncService.class);
			context.startService(intent);
			
			SharedPreferences sharedPref = context.getSharedPreferences("db_access", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.remove("is_sync");
			editor.commit();
			editor.putInt("is_sync", 0);
			editor.commit();
			
			SharedPreferences pref = context.getSharedPreferences("db_bck", Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = pref.edit();
			edit.remove("is_bck");
			edit.commit();
			edit.putInt("is_bck", 1);
			edit.commit();
			
			Intent i = new Intent();
			i.setClassName("in.schoolcom", "in.schoolcom.MainActivity");
			context.startActivity(i);
		}
		
	}
	
	public void findStProgress(){
		new CalledStProgress().execute();
	}

}

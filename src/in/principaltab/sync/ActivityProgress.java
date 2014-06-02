package in.principaltab.sync;

import in.principaltab.sqlite.SqlDbHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ActivityProgress {
	private static Context context;
	static private SqlDbHelper sqlHandler;
	private static ProgressDialog pDialog;
	
	public ActivityProgress(Context con_text){
		context = con_text;
	}
	
	static class CalledActivityProgress extends AsyncTask<String, String, String>{
		
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Preparing data (Activity Progress)...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
		//	sqlHandler = new SqlDbHelper(context);
			sqlHandler = SqlDbHelper.getInstance(context);
			sqlHandler.updateActivityAvg();
			sqlHandler.updateSubactActAvg();
			
			sqlHandler.checkActivityIsMark();
			sqlHandler.checkActivityMarkEmpty();
			
			sqlHandler.checkActSubActIsMark();
			
			return null;
		}
		
		protected void onPostExecute(String s){
			super.onPostExecute(s);
			pDialog.dismiss();
			new ExamProgress(context).findExmProgress();
		}
		
	}
	
	public void findActProgress(){
		new CalledActivityProgress().execute();
	}

}

package in.principaltab.sync;

import in.principaltab.sqlite.SqlDbHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class SubActivityProgress {
	private static Context context;
	private static SqlDbHelper sqlHandler;
	private static ProgressDialog pDialog;
	
	public SubActivityProgress(Context con_text){
		context = con_text;
	}

	static class CalledSubActivityProgress extends AsyncTask<String, String, String>{
		
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Preparing data (SubActivities Progress)...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
		//	sqlHandler = new SqlDbHelper(context);
			sqlHandler = SqlDbHelper.getInstance(context);
			sqlHandler.updateSubActivityAvg();
			sqlHandler.checkSubActivityIsMark();
			sqlHandler.checkSubActivityMarkEmpty();
			
			return null;
		}
		
		protected void onPostExecute(String s){
			super.onPostExecute(s);
			pDialog.dismiss();
			new ActivityProgress(context).findActProgress();
		}
		
	}
	
	public void findSubActProgress(){
		new CalledSubActivityProgress().execute();	
	}

}

package in.principaltab.sync;

import in.principaltab.sqlite.SqlDbHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ExamProgress {
	private static Context context;
	private static SqlDbHelper sqlHandler;
	private static ProgressDialog pDialog;
	
	public ExamProgress(Context con_text){
		context = con_text;
	}
	
	static class CalledExamProgress extends AsyncTask<String, String, String>{
		
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Preparing data (Exam Progress)...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
		//	sqlHandler = new SqlDbHelper(context);
			sqlHandler = SqlDbHelper.getInstance(context);
			
			sqlHandler.insertExmAvg();
			sqlHandler.insertExmActAvg();

			sqlHandler.checkExamIsMark();
			sqlHandler.checkExamMarkEmpty();
			
			sqlHandler.checkExmActIsMark();
			sqlHandler.checkExmActMarkEmpty();
			
			sqlHandler.checkExmSubActIsMark();
			sqlHandler.checkExmSubActMarkEmpty();

			return null;
		}
		protected void onPostExecute(String s){
			super.onPostExecute(s);
			pDialog.dismiss();
			new SlipTestProgress(context).findStProgress();
		}
	}
	
	
	
	public void findExmProgress(){
		new CalledExamProgress().execute();
	}

}

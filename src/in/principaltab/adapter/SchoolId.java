package in.principaltab.adapter;

import in.principaltab.sqlite.SqlDbHelper;
import in.principaltab.sqlite.Temp;

import java.util.List;

import android.content.Context;

public class SchoolId {
	
	public static int getSchoolId(Context context){
		int schoolId = 0;
	//	SqlDbHelper sqlHandler = new SqlDbHelper(context);
		SqlDbHelper sqlHandler = SqlDbHelper.getInstance(context);
		List<Temp> tList = sqlHandler.selectTemp();
		for(Temp t: tList){
			schoolId = t.getSchoolId();
		}
		return schoolId;
	}

}

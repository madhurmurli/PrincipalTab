package in.principaltab.sqlite;

import java.util.ArrayList;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class SqlDbHelper extends SQLiteOpenHelper implements SqlConstant {
	private static SqlDbHelper dbHelper;
	public SQLiteDatabase sqliteDatabase;
		
		private SqlDbHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);	
		}
	
		public static SqlDbHelper getInstance(Context context){
			if(dbHelper==null){
				dbHelper = new SqlDbHelper(context.getApplicationContext());
			}
			return dbHelper;
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_CLASS);
			db.execSQL(CREATE_SCHOOL);
			db.execSQL(CREATE_SECTION);
			db.execSQL(CREATE_STUDENTS);
			db.execSQL(CREATE_STUDENT_ATTENDANCE);
			db.execSQL(CREATE_TEMP_ATTENDANCE);
			db.execSQL(CREATE_SUBJECTS);
			db.execSQL(CREATE_SUBJECT_TEACHERS);
			db.execSQL(CREATE_TEACHER);
			db.execSQL(CREATE_EXAMS);
			db.execSQL(CREATE_MARKS);
			db.execSQL(CREATE_TEMP);
			db.execSQL(CREATE_TEMP_DAY);
			db.execSQL(CREATE_ACTIVITY);
			db.execSQL(CREATE_ACTIVITY_MARK);
			db.execSQL(CREATE_SUB_ACTIVITY);
			db.execSQL(CREATE_SUB_ACTIVITY_MARK);
			db.execSQL(CREATE_DOWNLOAD_SQL);
			db.execSQL(CREATE_DOWNLOAD_AUTO_ID);
			db.execSQL(CREATE_UPLOAD_SQL);
			db.execSQL(CREATE_UPLOAD_ST);
			db.execSQL(CREATE_HOMEWORK_ST);
			db.execSQL(CREATE_PORTION);
			db.execSQL(CREATE_SLIPTEST);
			db.execSQL(CREATE_SLIPTEST_T);
			db.execSQL(CREATE_SLIPTEST_MARK);
			db.execSQL(CREATE_SLIPTEST_M);
			db.execSQL(CREATE_STMAPPING);
			db.execSQL(CREATE_HOMEWORK);
			db.execSQL(CREATE_HW);
			db.execSQL(CREATE_HWMAPPING);
			db.execSQL(CREATE_TEMP_HW);
			db.execSQL(CREATE_EXMAVG);
			db.execSQL(CREATE_STAVG);
			db.execSQL(CREATE_SUBJECT_EXAMS);
			db.execSQL("insert into temp(id,DeviceId, SchoolId, ClassId, SectionId, SectionName, TeacherId, CurrentSection, CurrentSubject, "
					+ "CurrentClass, ExamId, ActivityId, SubActivityId, SlipTestId, SyncTime, IsSync) "+
						"values(1,0,0,0,0,'A',0,0,0,0,0,0,0,0,'Not Yet Synced',0)");
			db.execSQL("insert into tempday(id, OtherDate, Today, Yesterday, Otherday) values(1,'otherday',0,0,0)");
			db.execSQL("insert into temphw(id, OtherDate, Today, Yesterday, Otherday) values(1,'otherday',0,0,0)");
			db.execSQL("CREATE INDEX marks_index ON marks(SchoolId,ExamId,SubjectId,StudentId)");
			db.execSQL("CREATE INDEX act_index ON activitymark(SchoolId,ExamId,SubjectId,StudentId,ActivityId)");
			db.execSQL("CREATE INDEX subact_index ON subactivitymark(SchoolId,ExamId,SubjectId,StudentId,ActivityId,SubActivityId)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS class");
			db.execSQL("DROP TABLE IF EXISTS school");
			db.execSQL("DROP TABLE IF EXISTS section");
			db.execSQL("DROP TABLE IF EXISTS studentattendance");
			db.execSQL("DROP TABLE IF EXISTS tempattendance");
			db.execSQL("DROP TABLE IF EXISTS students");
			db.execSQL("DROP TABLE IF EXISTS subjectteachers");
			db.execSQL("DROP TABLE IF EXISTS subjects");
			db.execSQL("DROP TABLE IF EXISTS teacher");
			db.execSQL("DROP TABLE IF EXISTS exams");
			db.execSQL("DROP TABLE IF EXISTS marks");
			db.execSQL("DROP TABLE IF EXISTS temp");
			db.execSQL("DROP TABLE IF EXISTS tempday");
			db.execSQL("DROP TABLE IF EXISTS activity");
			db.execSQL("DROP TABLE IF EXISTS activitymark");
			db.execSQL("DROP TABLE IF EXISTS subactivity");
			db.execSQL("DROP TABLE IF EXISTS subactivitymark");
			db.execSQL("DROP TABLE IF EXISTS uploadsql");
			db.execSQL("DROP TABLE IF EXISTS sliptestsql");
			db.execSQL("DROP TABLE IF EXISTS homeworksql");
			db.execSQL("DROP TABLE IF EXISTS downloadsql");
			db.execSQL("DROP TABLE IF EXISTS dwnautoid");
			db.execSQL("DROP TABLE IF EXISTS portion");
			db.execSQL("DROP TABLE IF EXISTS stmapping");
			db.execSQL("DROP TABLE IF EXISTS sliptest");
			db.execSQL("DROP TABLE IF EXISTS sliptesttemp");
			db.execSQL("DROP TABLE IF EXISTS sliptestmark_22");
			db.execSQL("DROP TABLE IF EXISTS sliptestmark");
			db.execSQL("DROP TABLE IF EXISTS homeworkmessage");
			db.execSQL("DROP TABLE IF EXISTS hwmessage");
			db.execSQL("DROP TABLE IF EXISTS hwmapping");
			db.execSQL("DROP TABLE IF EXISTS temphw");
			db.execSQL("DROP TABLE IF EXISTS exmavg");
			db.execSQL("DROP TABLE IF EXISTS stavg");
			db.execSQL("DROP TABLE IF EXISTS subjectexams");
			onCreate(db);
		}

	
	public void deleteSlipTestSql(int id){
		sqliteDatabase = dbHelper.getWritableDatabase();
		sqliteDatabase.delete("sliptestsql", "SlipTestId="+id, null);
		//sqliteDatabase.close();
	}
	public void deleteHomeworkSql(int id){
		sqliteDatabase = dbHelper.getWritableDatabase();
		sqliteDatabase.delete("homeworksql", "HomeworkId="+id, null);
		//sqliteDatabase.close();
	}
	
	public void deleteTable(String tableName){
		sqliteDatabase = dbHelper.getWritableDatabase();
	//	sqliteDatabase.delete(tableName, null, null);
		sqliteDatabase.execSQL("delete from "+tableName);
		//sqliteDatabase.close();
	}
	public int runDownloadedSql(String sql){
		int ack = 0;
		sqliteDatabase = dbHelper.getWritableDatabase();
		try{
			sqliteDatabase.execSQL(sql);
			ack = 1;
		}
		catch(SQLException e){
			ack = 0;
		}
		//sqliteDatabase.close();
		return ack;
	}
	public List<Integer> runDownloadedSql(List<DownloadSql> sqlList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		List<Integer> ackIdList = new ArrayList<Integer>();
		try{
			for(DownloadSql sql: sqlList){
				sqliteDatabase.execSQL(sql.getQuery());	
				ackIdList.add(sql.getAckId());
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		//sqliteDatabase.close();
		return ackIdList;
	}
	public List<Integer> runSql(List<DownloadSql> sqlList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		List<Integer> ackIdList = new ArrayList<Integer>();	
		sqliteDatabase.beginTransaction();
		try{
		for(DownloadSql sql: sqlList){
			sqliteDatabase.execSQL(sql.getQuery());
			ackIdList.add(sql.getAckId());
			sqliteDatabase.yieldIfContendedSafely();
		}
		sqliteDatabase.setTransactionSuccessful();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			sqliteDatabase.endTransaction();
		}
		//sqliteDatabase.close();
		return ackIdList;
	}
	public void deleteSTTemp(int id){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptesttemp", null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			if(c.getInt(c.getColumnIndex("SlipTestId"))==id){
				sqliteDatabase.execSQL("delete from sliptesttemp where SlipTestId="+id);
				ContentValues cv = new ContentValues();
				cv.put("MarksEntered", 0);
				sqliteDatabase.update("stmapping", cv, "SlipTestIdLocal="+id, null);
			}
			c.moveToNext();
		}
		c.close();		
		//sqliteDatabase.close();
	}
	public void deleteHWTemp(int id){
		sqliteDatabase = dbHelper.getWritableDatabase();
		sqliteDatabase.delete("hwmessage", "HomeworkId="+id, null);
		//sqliteDatabase.close();
	}
	
	public void deleteSTTemp(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from stmapping", null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			if(c.getInt(c.getColumnIndex("Sync"))==0 && c.getInt(c.getColumnIndex("MarksEntered"))==0){
				sqliteDatabase.execSQL("delete from sliptesttemp where SlipTestId="+c.getInt(c.getColumnIndex("SlipTestIdLocal")));
			}
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
	}
	
	public void deleteSlipTest(int slipTestId, int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "delete from sliptest where SlipTestId="+slipTestId;
		Cursor c = sqliteDatabase.rawQuery("select * from sliptest", null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			if(c.getInt(c.getColumnIndex("SlipTestId"))==slipTestId){
				sqliteDatabase.execSQL(sql);
				ContentValues cv = new ContentValues();
				cv.put("SchoolId",schoolId);
				cv.put("Action", "delete");
				cv.put("TableName", "sliptest");
				cv.put("Query", sql);
				sqliteDatabase.insert("uploadsql", null, cv);
			}
			c.moveToNext();
		}
		c.close();
	//	sqliteDatabase.delete("sliptest", "SlipTestId="+slipTestId, null);
		//sqliteDatabase.close();
	}
	
	public void insertSync(String sql){
		sqliteDatabase = dbHelper.getWritableDatabase();
		sqliteDatabase.execSQL(sql);
		//sqliteDatabase.close();
	}
	public void initActivity(List<Activiti> aList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO activity (ActivityId,SchoolId,ClassId,SectionId,ExamId,SubjectId,RubrixId,ActivityName,MaximumMark,"+
				"Weightage,SubActivity,Calculation) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(Activiti a: aList){
			stmt.bindLong(1, a.getActivityId());
			stmt.bindLong(2, a.getSchoolId());
			stmt.bindLong(3, a.getClassId());
			stmt.bindLong(4, a.getSectionId());
			stmt.bindLong(5, a.getExamId());
			stmt.bindLong(6, a.getSubjectId());
			stmt.bindLong(7, a.getRubrixId());
			stmt.bindString(8, a.getActivityName());
			stmt.bindLong(9, a.getMaximumMark());
			stmt.bindDouble(10, a.getWeightage());
			stmt.bindLong(11, a.getSubActivity());
			stmt.bindLong(12, a.getCalculation());
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void initStudentAttendance(List<StudentAttendance> saList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO studentattendance (SchoolId, ClassId, SectionId, StudentId, TypeOfLeave, DateAttendance) VALUES (?,?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(StudentAttendance sa: saList){
			stmt.bindLong(1, sa.getSchoolId());
			stmt.bindLong(2, sa.getClassId());
			stmt.bindLong(3, sa.getSectionId());
			stmt.bindLong(4, sa.getStudentId());
			stmt.bindString(5, sa.getTypeOfLeave());
			stmt.bindString(6, sa.getDateAttendance());
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void initSubjectExams(List<SubjectExams> seList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO subjectExams (SchoolId, ClassId, ExamId, SubjectId, MaximumMark, FailMark) VALUES (?,?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(SubjectExams se: seList){
			stmt.bindLong(1, se.getSchoolId());
			stmt.bindLong(2, se.getClassId());
			stmt.bindLong(3, se.getExamId());
			stmt.bindLong(4, se.getSubjectId());
			stmt.bindLong(5, se.getMaximumMark());
			stmt.bindLong(6, se.getFailMark());
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void initSubActivity(List<SubActivity> sList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(SubActivity a: sList){
			ContentValues cv = new ContentValues();
			cv.put("ActivityId", a.getActivityId());
			cv.put("SubActivityName", a.getSubActivityName());
			cv.put("Calculation", a.getCalculation());
			cv.put("ClassId", a.getClassId());
			cv.put("ExamId", a.getExamId());
			cv.put("MaximumMark", a.getMaximumMark());
			cv.put("SectionId", a.getSectionId());
			cv.put("SubActivityId", a.getSubActivityId());
			cv.put("SubjectId", a.getSubjectId());
			cv.put("Weightage", a.getWeightage());
			cv.put("SchoolId", a.getSchoolId());
			sqliteDatabase.insert("subactivity", null, cv);
		}
		//sqliteDatabase.close();
	}
	
	public void initClass(List<Clas> cList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(Clas c: cList){
			ContentValues cv = new ContentValues();
			cv.put("ClassName", c.getClassName());
			cv.put("ClassType", c.getClassType());
			cv.put("ClassId", c.getClassId());
			cv.put("SchoolId", c.getSchoolId());
			sqliteDatabase.insert("class", null, cv);
		}
		//sqliteDatabase.close();
	}
	
	public void initExams(List<Exams> sList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(Exams s: sList){
			ContentValues cv = new ContentValues();
			cv.put("ExamName", s.getExamName());
			cv.put("FileName", s.getFileName());
			cv.put("Percentage", s.getPercentage());
			cv.put("Portions", s.getPortions());
			cv.put("SubjectIDs", s.getSubjectIDs());
			cv.put("TimeTable", s.getTimeTable());
			cv.put("ClassId", s.getClassId());
			cv.put("ExamId", s.getExamId());
			cv.put("GradeSystem", s.getGradeSystem());
			cv.put("MarkUploaded", s.getMarkUploaded());
			cv.put("OrderId", s.getOrderId());
			cv.put("SchoolId", s.getSchoolId());
			cv.put("Term", s.getTerm());
			sqliteDatabase.insert("exams", null, cv);
		}
		//sqliteDatabase.close();
	}
	public void initMarks(List<Marks> mList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO marks (SchoolId, ExamId, SubjectId, StudentId, Mark, Grade) VALUES (?,?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(Marks m: mList){
			stmt.bindLong(1, m.getSchoolId());
			stmt.bindLong(2, m.getExamId());
			stmt.bindLong(3, m.getSubjectId());
			stmt.bindLong(4, m.getStudentId());
			stmt.bindString(5, m.getMark());
			stmt.bindString(6, m.getGrade());
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void initSlipTest(List<SlipTestt> stList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO sliptest (SlipTestId, SchoolId, ClassId, SectionId, SlipTestName, IsActivity, Grade, Count, SubjectId, NewSubjectId, Portion, ExtraPortion, PortionName, MaximumMark,"+
				"AverageMark, TestDate, MarkEntered, EmployeeId, Weightage) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(SlipTestt st: stList){
			stmt.bindLong(1, st.getSlipTestId());
			stmt.bindLong(2, st.getSchoolId());
			stmt.bindLong(3, st.getClassId());
			stmt.bindLong(4, st.getSectionId());
			stmt.bindString(5, st.getSlipTestName());
			stmt.bindLong(6, st.getIsActivity());
			stmt.bindLong(7, st.getGrade());
			stmt.bindLong(8, st.getCount());
			stmt.bindLong(9, st.getSubjectId());
			stmt.bindLong(10, st.getNewSubjectId());
			stmt.bindLong(11, st.getPortion());
			stmt.bindString(12, st.getExtraPortion());
			stmt.bindString(13, st.getPortionName());
			stmt.bindLong(14, st.getMaximumMark());
			stmt.bindDouble(15, st.getAverageMark());
			stmt.bindString(16, st.getTestDate());
			stmt.bindLong(17, st.getMarkEntered());
			stmt.bindLong(18, st.getEmployeeId());
			stmt.bindDouble(19, st.getWeightage());
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void initSlipTestMark(List<SlipTestMark> stList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO sliptestmark_22(SchoolId,ClassId,SectionId,SubjectId,NewSubjectId,SlipTestId,StudentId,Mark) VALUES (?,?,?,?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(SlipTestMark st: stList){
			stmt.bindLong(1, st.getSchoolId());
			stmt.bindLong(2, st.getClassId());
			stmt.bindLong(3, st.getSectionId());
			stmt.bindLong(4, st.getSubjectId());
			stmt.bindLong(5, st.getNewSubjectId());
			stmt.bindLong(6, st.getSlipTestId());
			stmt.bindLong(7, st.getStudentId());
			stmt.bindString(8, st.getMark());
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void initializeActivityMark(ActivityMark am){
		sqliteDatabase = dbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("ActivityId", am.getActivityId());
			cv.put("ExamId", am.getExamId());
			cv.put("SchoolId", am.getSchoolId());
			cv.put("SubjectId", am.getSubjectId());
			cv.put("StudentId", am.getStudentId());
			cv.put("Mark", am.getMark());
			sqliteDatabase.insert("activitymark", null, cv);
		//sqliteDatabase.close();
	}
	public void initActivityMark(List<ActivityMark> amList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO activitymark (SchoolId, ExamId, SubjectId, StudentId, ActivityId, Mark) VALUES (?,?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(ActivityMark am: amList){
			stmt.bindLong(1, am.getSchoolId());
			stmt.bindLong(2, am.getExamId());
			stmt.bindLong(3, am.getSubjectId());
			stmt.bindLong(4, am.getStudentId());
			stmt.bindLong(5, am.getActivityId());
			stmt.bindString(6, am.getMark());
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	
	public void initSubActMark(List<SubActivityMark> sList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO subactivitymark (SchoolId,ExamId,SubjectId,StudentId,ActivityId,SubActivityId,Mark,Description) VALUES (?,?,?,?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(SubActivityMark s: sList){
			stmt.bindLong(1, s.getSchoolId());
			stmt.bindLong(2, s.getExamId());
			stmt.bindLong(3, s.getSubjectId());
			stmt.bindLong(4, s.getStudentId());
			stmt.bindLong(5, s.getActivityId());
			stmt.bindLong(6, s.getSubActivityId());
			stmt.bindString(7, s.getMark());
			stmt.bindString(8, s.getDescription());
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void initPortion(List<Portion> pList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO portion (PortionId, SchoolId, ClassId, SubjectId, NewSubjectId, Portion) VALUES (?,?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(Portion p: pList){
			stmt.bindLong(1, p.getPortionId());
			stmt.bindLong(2, p.getSchoolId());
			stmt.bindLong(3, p.getClassId());
			stmt.bindLong(4, p.getSubjectId());
			stmt.bindLong(5, p.getNewSubjectId());
			stmt.bindString(6, p.getPortion());
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	
	public void initSection(List<Section> sList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(Section s: sList){
			ContentValues cv = new ContentValues();
			cv.put("SectionName", s.getSectionName());
			cv.put("ClassId", s.getClassId());
			cv.put("ClassTeacherId", s.getClassTeacherId());
			cv.put("SchoolId", s.getSchoolId());
			cv.put("SectionId", s.getSectionId());
			sqliteDatabase.insert("section", null, cv);
		}
		//sqliteDatabase.close();
	}
	
	public void insertSubjects(List<Subjects> sList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(Subjects s: sList){
			ContentValues cv = new ContentValues();
			cv.put("SubjectName", s.getSubjectName());
			cv.put("SubjectId", s.getSubjectId());
			sqliteDatabase.insert("subjects", null, cv);
		}
		//sqliteDatabase.close();
	}
	public void initHomework(List<Homework> hList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO homeworkmessage (HomeworkId,SchoolId,ClassId,SectionId,TeacherId,SubjectIDs,Homework,HomeworkDate) VALUES(?,?,?,?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(Homework h: hList){
			stmt.bindLong(1, h.getHomeworkId());
			stmt.bindLong(2, h.getSchoolId());
			stmt.bindLong(3, h.getClassId());
			stmt.bindLong(4, h.getSectionId());
			stmt.bindLong(5, h.getTeacherId());
			stmt.bindString(6, h.getSubjectIDs());
			stmt.bindString(7, h.getHomework());
			stmt.bindString(8, h.getHomeworkDate());
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	
	public void insertHW(Homework h){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "insert into hwmessage(SchoolId,ClassId,SectionId,TeacherId,SubjectIDs,Homework,HomeworkDate) values("+
				h.getSchoolId()+","+h.getClassId()+","+h.getSectionId()+","+h.getTeacherId()+",'"+h.getSubjectIDs()+"','"+h.getHomework()+"','"+
				h.getHomeworkDate()+"')";
		String sql2 = "insert into homeworkmessage(SchoolId,ClassId,SectionId,TeacherId,SubjectIDs,Homework,HomeworkDate) values("+
				h.getSchoolId()+","+h.getClassId()+","+h.getSectionId()+","+h.getTeacherId()+",'"+h.getSubjectIDs()+"','"+h.getHomework()+"','"+
				h.getHomeworkDate()+"')";
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", h.getSchoolId());
			cv.put("Action", "insert");
			cv.put("TableName", "homeworkmessage");
			cv.put("Query", sql2);
			cv.put("SectionId", h.getSectionId());
			cv.put("HomeworkDate", h.getHomeworkDate());
			sqliteDatabase.insert("homeworksql", null, cv);
			ContentValues cv2 = new ContentValues();
			cv2.put("Sync", 0);
			cv2.put("HomeworkId", 0);
			cv2.put("Opened", 0);
			sqliteDatabase.insert("hwmapping", null, cv2);
		//sqliteDatabase.close();
	}
	
	public void initSubjectTeachers(List<SubjectTeachers> sList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(SubjectTeachers s: sList){
			ContentValues cv = new ContentValues();
			cv.put("ClassId", s.getClassId());
			cv.put("SchoolId", s.getSchoolId());
			cv.put("SectionId", s.getSectionId());
			cv.put("SubjectId", s.getSubjectId());
			cv.put("SubjectGroupId", s.getSubjectGroupId());
			cv.put("TeacherId", s.getTeacherId());
			sqliteDatabase.insert("subjectteachers", null, cv);
		}
		//sqliteDatabase.close();
	}
	public void insertExmAvg(List<ExmAvg> evList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO exmavg (ClassId,SectionId,SubjectId,ExamId,ExamAvg,CompleteEntry) VALUES(?,?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(ExmAvg ev: evList){
			stmt.bindLong(1, ev.getClassId());
			stmt.bindLong(2, ev.getSectionId());
			stmt.bindLong(3, ev.getSubjectId());
			stmt.bindLong(4, ev.getExamId());
			stmt.bindLong(5, 0);
			stmt.bindLong(6, 0);
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	
	public void insertStAvg(StAvg sv){
		sqliteDatabase = dbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("SectionId", sv.getSectionId());
			cv.put("SlipTestAvg", sv.getSlipTestAvg());
			cv.put("SubjectId", sv.getSubjectId());
			sqliteDatabase.insert("stavg", null, cv);
		//sqliteDatabase.close();
	}
	
	public List<ExmAvg> selectExmAvg(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from exmavg", null);
		List<ExmAvg> eaList = new ArrayList<ExmAvg>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			ExmAvg ea = new ExmAvg();
			ea.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			ea.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			ea.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			ea.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			eaList.add(ea);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return eaList;
	}
	
	public int selectStAvg(int sectionId, int subjectId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from stavg where SectionId="+sectionId+" and SubjectId="+subjectId, null);
		c.moveToFirst();
		int avg = 0;
		while(!c.isAfterLast()){
			avg = c.getInt(c.getColumnIndex("SlipTestAvg"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return avg;
	}
	
	public int selectedExmComplete(int sectionId, int subjectId, int examId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from exmavg where SectionId="+sectionId+" and SubjectId="+subjectId+" and ExamId="+examId, null);
		int i = 0;
		c.moveToFirst();
		while(!c.isAfterLast()){
			i = c.getInt(c.getColumnIndex("CompleteEntry"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return i;
	}
	public int selectedExmAvg(int sectionId, int subjectId, int examId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from exmavg where SectionId="+sectionId+" and SubjectId="+subjectId+" and ExamId="+examId, null);
		int i = 0;
		c.moveToFirst();
		while(!c.isAfterLast()){
			i = c.getInt(c.getColumnIndex("ExamAvg"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return i;
	}
	
	public List<String> selectColumnType(String tableName, List<String> columnNames){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("PRAGMA table_info("+tableName+")", null);
		List<ColumnType> columnType = new ArrayList<ColumnType>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			ColumnType ct = new ColumnType();
			ct.setType(c.getString(c.getColumnIndex("type")));
			ct.setName(c.getString(c.getColumnIndex("name")));
			columnType.add(ct);
			c.moveToNext();
		}
		c.close();
		Log.d("len", columnNames.size()+"");
		List<String> type = new ArrayList<String>();
		for(String cn: columnNames){
			for(ColumnType ct: columnType){
				if(cn.trim().equals(ct.getName())){
					if(ct.getType().equals("INT")){
						type.add("Long");
					}else if(ct.getType().equals("TEXT")){
						type.add("String");
					}else if(ct.getType().equals("REAL")){
						type.add("Double");
					}else if(ct.getType().equals("DATETIME")){
						type.add("String");
					}else if(ct.getType().equals("INTEGER")){
						type.add("Long");
					}
				}
			}
		}
		//sqliteDatabase.close();
		return type;
	}
	
	public String className(int classId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		String name = null;
		Cursor c = sqliteDatabase.rawQuery("select ClassName from class where ClassId="+classId, null);
		c.moveToFirst();
			name = c.getString(c.getColumnIndex("ClassName"));
		c.close();
		//sqliteDatabase.close();
		return name;
	}
	public String sectionName(int sectionId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		String name = null;
		Cursor c = sqliteDatabase.rawQuery("select SectionName from section where SectionId="+sectionId, null);
		c.moveToFirst();
			name = c.getString(c.getColumnIndex("SectionName"));
		c.close();
		//sqliteDatabase.close();
		return name;
	}

	public List<Clas> selectClas(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from class", null);
		List<Clas> cList = new ArrayList<Clas>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Clas clas = new Clas();
			clas.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			clas.setClassName(c.getString(c.getColumnIndex("ClassName")));
			cList.add(clas);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return cList;
	}
	
	public List<Section> selectSection(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from section", null);
		List<Section> sList = new ArrayList<Section>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Section sec = new Section();
			sec.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			sec.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			sec.setSectionName(c.getString(c.getColumnIndex("SectionName")));
			sec.setClassTeacherId(c.getInt(c.getColumnIndex("ClassTeacherId")));
			sList.add(sec);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return sList;
	}
	
	public List<Students> selectStudents(String sectionid){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from students where SectionId="+sectionid+" group by RollNoInClass", null);
		List<Students> sList = new ArrayList<Students>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Students stud = new Students();
			stud.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			stud.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			stud.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			stud.setRollNoInClass(c.getInt(c.getColumnIndex("RollNoInClass")));
			stud.setName(c.getString(c.getColumnIndex("Name")));
			sList.add(stud);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return sList;
	}
	
	public List<Integer> selectStudentsId(int sectionId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select StudentId from students where SectionId="+sectionId, null);
		List<Integer> idList = new ArrayList<Integer>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			idList.add(c.getInt(c.getColumnIndex("StudentId")));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return idList;
	}
	
	public List<Students> selectAbsentStudents(List<Integer> ids){
		sqliteDatabase = dbHelper.getReadableDatabase();
		List<Students> sList = new ArrayList<Students>();
		for(Integer id: ids){
			Cursor c = sqliteDatabase.rawQuery("select * from students where StudentId="+id, null);
			c.moveToFirst();
			while(!c.isAfterLast()){
				Students stud = new Students();
				stud.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
				stud.setClassId(c.getInt(c.getColumnIndex("ClassId")));
				stud.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
				stud.setRollNoInClass(c.getInt(c.getColumnIndex("RollNoInClass")));
				stud.setName(c.getString(c.getColumnIndex("Name")));
				sList.add(stud);
				c.moveToNext();
			}
			c.close();
		}		
		//sqliteDatabase.close();
		return sList;
	}
	
	public List<Subjects> selectSubjects(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from subjects", null);
		List<Subjects> sList = new ArrayList<Subjects>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Subjects sub = new Subjects();
			sub.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			sub.setSubjectName(c.getString(c.getColumnIndex("SubjectName")));
			sList.add(sub);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return sList;
	}
	public String selectSubjectName(int subjectId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		String subjectName = null;
		Cursor c = sqliteDatabase.rawQuery("select * from subjects where SubjectId="+subjectId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			subjectName = c.getString(c.getColumnIndex("SubjectName"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return subjectName;
	}
	
	public List<SubjectTeachers> selectSubjectTeachers(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from subjectteachers", null);
		List<SubjectTeachers> sList = new ArrayList<SubjectTeachers>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SubjectTeachers subTec = new SubjectTeachers();
			subTec.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			subTec.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			subTec.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			subTec.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			subTec.setTeacherId(c.getInt(c.getColumnIndex("TeacherId")));
			sList.add(subTec);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return sList;
	}
	
	public List<SubjectTeachers> selectSubTeachers(int classId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from subjectteachers where ClassId="+classId, null);
		List<SubjectTeachers> sList = new ArrayList<SubjectTeachers>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SubjectTeachers subTec = new SubjectTeachers();
			subTec.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			subTec.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			subTec.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			sList.add(subTec);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return sList;
	}
	
	public List<Teacher> selectTeacher(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from teacher", null);
		List<Teacher> tList = new ArrayList<Teacher>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Teacher t = new Teacher();
			t.setTeacherId(c.getInt(c.getColumnIndex("TeacherId")));
			t.setUsername(c.getString(c.getColumnIndex("Username")));
			t.setPassword(c.getString(c.getColumnIndex("Password")));
			t.setName(c.getString(c.getColumnIndex("Name")));
			t.setMobile(c.getString(c.getColumnIndex("Mobile")));
			t.setTabUser(c.getString(c.getColumnIndex("TabUser")));
			t.setTabPass(c.getString(c.getColumnIndex("TabPass")));
			tList.add(t);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return tList;
	}
	
	public String selectTeacherName(int teacherId){
		String s = "";
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from teacher where TeacherId="+teacherId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			s = c.getString(c.getColumnIndex("Name"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return s;
	}
	
	public List<TeacherIncharge> selectTeacherIncharge(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from teacherincharge", null);
		List<TeacherIncharge> tList = new ArrayList<TeacherIncharge>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			TeacherIncharge t = new TeacherIncharge();
			t.setTeacherInchargeId(c.getInt(c.getColumnIndex("TeacherInchargeId")));
			t.setClassIdsUnderControl(c.getString(c.getColumnIndex("ClassIdsUnderControl")));
			tList.add(t);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return tList;
	}
	
	public List<Exams> selectExams(int classId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from exams where ClassId="+classId, null);
		List<Exams> eList = new ArrayList<Exams>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Exams e = new Exams();
			e.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			e.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			e.setSubjectIDs(c.getString(c.getColumnIndex("SubjectIDs")));
			e.setExamName(c.getString(c.getColumnIndex("ExamName")));
			e.setMarkUploaded(c.getInt(c.getColumnIndex("MarkUploaded")));
			e.setGradeSystem(c.getInt(c.getColumnIndex("GradeSystem")));
			eList.add(e);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return eList;
	}
	
	public String selectExamName(int examId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		String s = null;
		Cursor c = sqliteDatabase.rawQuery("select * from exams where ExamId="+examId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			s = c.getString(c.getColumnIndex("ExamName"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return s;
	}
	
	public List<Marks> selectMarks(int examId, int subjectId, List<Integer> studentId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		List<Marks> mList = new ArrayList<Marks>();
		for(Integer i: studentId){		
		Cursor c = sqliteDatabase.rawQuery("select * from marks where ExamId="+examId+" AND SubjectId="+subjectId+" AND StudentId="+i, null);
		if(c.getCount()!=0){
			c.moveToFirst();
			while(!c.isAfterLast()){
				Marks m = new Marks();
				m.setExamId(c.getInt(c.getColumnIndex("ExamId")));
				m.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
				m.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
				m.setMark(c.getString(c.getColumnIndex("Mark")));
				m.setGrade(c.getString(c.getColumnIndex("Grade")));
				mList.add(m);
				c.moveToNext();
			}
		}else{
			Marks m = new Marks();
			m.setExamId(examId);
			m.setSubjectId(subjectId);
			m.setStudentId(i);
			m.setMark("0");
			mList.add(m);
			}
		c.close();
		}
		//sqliteDatabase.close();
		return mList;
	}
	public List<Marks> selectMarks(int examId, int sectionId, int subjectId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("SELECT A.StudentId,ExamId,B.SectionId,SubjectId,Mark FROM marks A, Students B"+
				" where A.ExamId="+examId+" and B.SectionId="+sectionId+" and A.SubjectId="+subjectId+" and A.StudentId=B.StudentId group by B.RollNoInClass", null);
		List<Marks> mList = new ArrayList<Marks>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Marks m = new Marks();
			m.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			m.setMark(c.getString(c.getColumnIndex("Mark")));
			mList.add(m);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return mList;
	}
	public int isThereHomework(int sectionId, String date){
		int isDelete = 1;
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from homeworkmessage where SectionId="+sectionId+" and HomeworkDate='"+date+"' LIMIT 1", null);
		if(c.getCount()>0){
			isDelete = 0;
		}
		c.close();
		//sqliteDatabase.close();
		return isDelete;
	}
	public int isThereExamMark(int examId, int sectionId, int subjectId){
		int isThere = 0;
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("SELECT A.SchoolId from marks A, students B where A.ExamId="+examId+" and A.StudentId=B.StudentId and B.SectionId="+sectionId
				+" and A.SubjectId="+subjectId+" LIMIT 1", null);
		if(c.getCount()>0){
			isThere = 1;
		}
		c.close();
		//sqliteDatabase.close();
		return isThere;
	}
	public int isThereActMark(int actId, int subjectId){
		int isThere = 0;
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from activitymark where ActivityId="+actId+" and SubjectId="+subjectId+" LIMIT 1", null);
		if(c.getCount()>0){
			isThere = 1;
		}
		c.close();
		//sqliteDatabase.close();
		return isThere;
	}
	public int isThereSubActMark(int subActId, int subjectId){
		int isThere = 0;
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from subactivitymark where SubActivityId="+subActId+" and SubjectId="+subjectId+" LIMIT 1", null);
		if(c.getCount()>0){
			isThere = 1;
		}
		c.close();
		//sqliteDatabase.close();
		return isThere;
	}
	public List<StudentAttendance> selectStudentAttendance(String date){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from studentattendance where DateAttendance='"+date+"'", null);
		List<StudentAttendance> sList = new ArrayList<StudentAttendance>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			StudentAttendance s = new StudentAttendance();
			s.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			s.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			s.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			s.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			s.setDateAttendance(c.getString(c.getColumnIndex("DateAttendance")));
			s.setTypeOfLeave(c.getString(c.getColumnIndex("TypeOfLeave")));
			sList.add(s);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return sList;
	}
	public int isStudentAttendanceMarked(int sectionId, String date){
		int marked=0;
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from studentattendance where SectionId="+sectionId+" and DateAttendance='"+date+"'", null);
		if(c.getCount()>0){
			marked = 1;
		}
		c.close();
		return marked;
	}
	public List<StudentAttendance> selectStudentAttendance(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from studentattendance", null);
		List<StudentAttendance> sList = new ArrayList<StudentAttendance>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			StudentAttendance s = new StudentAttendance();
			s.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			s.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			s.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			s.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			s.setDateAttendance(c.getString(c.getColumnIndex("DateAttendance")));
			s.setTypeOfLeave(c.getString(c.getColumnIndex("TypeOfLeave")));
			sList.add(s);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return sList;
	}
	
	public List<Integer> selectStudentIds(String date, int sectionId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select StudentId from studentattendance where DateAttendance='"+date+"' and SectionId="+sectionId, null);
		List<Integer> sList = new ArrayList<Integer>();
		if(c.getCount()>0){
		c.moveToFirst();
		while(!c.isAfterLast()){
			Integer i = c.getInt(c.getColumnIndex("StudentId"));
			sList.add(i);
			c.moveToNext();
		}
		}
		c.close();
		//sqliteDatabase.close();
		return sList;
	}
	
	public List<Students> selectTempAttendance(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from tempattendance", null);
		List<Students> sList = new ArrayList<Students>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Students s = new Students();
			s.setClassId(c.getInt(c.getColumnIndex("StudentId")));
			s.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			s.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			s.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			s.setName(c.getString(c.getColumnIndex("Name")));
			s.setRollNoInClass(c.getInt(c.getColumnIndex("RollNoInClass")));			
			sList.add(s);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return sList;
	}
	
	public List<Temp> selectTemp(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from temp where id = 1", null);
		List<Temp> tList = new ArrayList<Temp>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Temp t = new Temp();
			t.setId(c.getInt(c.getColumnIndex("id")));
			t.setDeviceId(c.getString(c.getColumnIndex("DeviceId")));
			t.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			t.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			t.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			t.setSectionName(c.getString(c.getColumnIndex("SectionName")));
			t.setTeacherId(c.getInt(c.getColumnIndex("TeacherId")));
			t.setCurrentSection(c.getInt(c.getColumnIndex("CurrentSection")));
			t.setCurrentSubject(c.getInt(c.getColumnIndex("CurrentSubject")));
			t.setCurrentClass(c.getInt(c.getColumnIndex("CurrentClass")));
			t.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			t.setActivityId(c.getInt(c.getColumnIndex("ActivityId")));
			t.setSubActivityId(c.getInt(c.getColumnIndex("SubActivityId")));
			t.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			t.setSyncTime(c.getString(c.getColumnIndex("SyncTime")));
			t.setIsSync(c.getInt(c.getColumnIndex("IsSync")));
			tList.add(t);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return tList;
	}
	
	public List<TempDay> selectTempDay(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from tempday where id = 1", null);
		List<TempDay> tList = new ArrayList<TempDay>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			TempDay t = new TempDay();
			t.setOtherDate(c.getString(c.getColumnIndex("OtherDate")));
			t.setToday(c.getInt(c.getColumnIndex("Today")));
			t.setYesterday(c.getInt(c.getColumnIndex("Yesterday")));
			t.setOtherday(c.getInt(c.getColumnIndex("Otherday")));
			tList.add(t);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return tList;
	}
	
	public List<TempHW> selectTempHW(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from temphw where id = 1", null);
		List<TempHW> tList = new ArrayList<TempHW>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			TempHW t = new TempHW();
			t.setOtherDate(c.getString(c.getColumnIndex("OtherDate")));
			t.setToday(c.getInt(c.getColumnIndex("Today")));
			t.setYesterday(c.getInt(c.getColumnIndex("Yesterday")));
			t.setOtherday(c.getInt(c.getColumnIndex("Otherday")));
			tList.add(t);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return tList;
	}
	public List<Homework> selectHomework(int sectionId,int subjectId,String homeworkDate){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from homeworkmessage where SectionId="+sectionId+" and SubjectIDs='"+subjectId+"' and HomeworkDate='"+homeworkDate+"'", null);
		List<Homework> hList = new ArrayList<Homework>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Homework h = new Homework();
			h.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			h.setHomework(c.getString(c.getColumnIndex("Homework")));
			h.setHomeworkDate(c.getString(c.getColumnIndex("HomeworkDate")));
			h.setHomeworkId(c.getInt(c.getColumnIndex("HomeworkId")));
			h.setMessageFrom(c.getString(c.getColumnIndex("MessageFrom")));
			h.setMessageVia(c.getString(c.getColumnIndex("MessageVia")));
			h.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			h.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			h.setSubjectIDs(c.getString(c.getColumnIndex("SubjectIDs")));
			h.setTeacherId(c.getInt(c.getColumnIndex("TeacherId")));
			hList.add(h);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return hList;
	}
	public List<Homework> selectHW(int sectionId,int subjectId,String homeworkDate){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from hwmessage where SectionId="+sectionId+" and SubjectIDs='"+subjectId+"' and HomeworkDate='"+homeworkDate+"'", null);
		List<Homework> hList = new ArrayList<Homework>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Homework h = new Homework();
			h.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			h.setHomework(c.getString(c.getColumnIndex("Homework")));
			h.setHomeworkDate(c.getString(c.getColumnIndex("HomeworkDate")));
			h.setHomeworkId(c.getInt(c.getColumnIndex("HomeworkId")));
			h.setMessageFrom(c.getString(c.getColumnIndex("MessageFrom")));
			h.setMessageVia(c.getString(c.getColumnIndex("MessageVia")));
			h.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			h.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			h.setSubjectIDs(c.getString(c.getColumnIndex("SubjectIDs")));
			h.setTeacherId(c.getInt(c.getColumnIndex("TeacherId")));
			hList.add(h);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return hList;
	}
	
	public List<Activiti> selectActiviti(int examId, int subjectId, int sectionId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from activity  where ExamId="+examId+" and SubjectId="+subjectId+" and SectionId="+sectionId, null);
		List<Activiti> aList = new ArrayList<Activiti>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Activiti a = new Activiti();
			a.setActivityId(c.getInt(c.getColumnIndex("ActivityId")));
			a.setActivityName(c.getString(c.getColumnIndex("ActivityName")));
			a.setCalculation(c.getInt(c.getColumnIndex("Calculation")));
			a.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			a.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			a.setMaximumMark(c.getInt(c.getColumnIndex("MaximumMark")));
			a.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			a.setSubActivity(c.getInt(c.getColumnIndex("SubActivity")));
			a.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			a.setWeightage(c.getInt(c.getColumnIndex("Weightage")));
			a.setActivityAvg(c.getInt(c.getColumnIndex("ActivityAvg")));
			a.setCompleteEntry(c.getInt(c.getColumnIndex("CompleteEntry")));
			aList.add(a);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return aList;
	}
	public List<Integer> selectIsThereAct(int examId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select ActivityId from activity where ExamId="+examId, null);
		List<Integer> idList = new ArrayList<Integer>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			idList.add(c.getInt(c.getColumnIndex("ActivityId")));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return idList;
	}
	public List<Integer> selectActivityIds(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select ActivityId from activity", null);
		List<Integer> idList = new ArrayList<Integer>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			idList.add(c.getInt(c.getColumnIndex("ActivityId")));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return idList;
	}
	public List<Integer> selectSubActivityIds(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select SubActivityId from subactivity", null);
		List<Integer> idList = new ArrayList<Integer>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			idList.add(c.getInt(c.getColumnIndex("SubActivityId")));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return idList;
	}
	public void updateSubExmMaxMark(int classId, int examId, int subjectId, int maximumMark){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("MaximumMark", maximumMark);
		sqliteDatabase.update("subjectexams", cv, "ClassId="+classId+" and ExamId="+examId+" and SubjectId="+subjectId, null);
		//sqliteDatabase.close();
	}
	public void insertExmAvg(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "SELECT A.ExamId, A.SubjectId, B.SectionId, (AVG(Mark)/C.MaximumMark)*360 as Average FROM marks A, students B, subjectexams C WHERE A.StudentId = B.StudentId"+
				" and C.ExamId=A.ExamId and C.SubjectId=A.SubjectId GROUP BY A.ExamId, A.SubjectId, B.SectionId";
		Cursor c = sqliteDatabase.rawQuery(sql,null);
	//	String sql2 = "update exmavg set ExamAvg=? where ExamId=? and SectionId=? and SubjectId=?";
		String sql2 = "insert into exmavg (SectionId,SubjectId,ExamId,ExamAvg) Values(?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql2);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(4, c.getLong(c.getColumnIndex("Average")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		c.close();
		//sqliteDatabase.close();
	}
	public void insertExmAvg(int examId, int subjectId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "SELECT A.ExamId, A.SubjectId, B.SectionId, (AVG(Mark)/C.MaximumMark)*360 as Average FROM marks A, students B, subjectexams C WHERE A.StudentId = B.StudentId"+
				" and A.ExamId="+examId+" and C.ExamId=A.ExamId and A.SubjectId="+subjectId+" and C.SubjectId=A.SubjectId GROUP BY A.ExamId, A.SubjectId, B.SectionId";
		Cursor c = sqliteDatabase.rawQuery(sql,null);
	//	String sql2 = "update exmavg set ExamAvg=? where ExamId=? and SectionId=? and SubjectId=?";
		String sql2 = "insert into exmavg (SectionId,SubjectId,ExamId,ExamAvg,CompleteEntry) Values(?,?,?,?,1)";
		Log.d("sql", sql2);
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql2);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(4, c.getLong(c.getColumnIndex("Average")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		c.close();
	}
	public void updateExmAvg(int examId, int subjectId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "SELECT A.ExamId, A.SubjectId, B.SectionId, (AVG(Mark)/C.MaximumMark)*360 as Average FROM marks A, students B, subjectexams C WHERE A.StudentId=B.StudentId"+
				" and A.ExamId="+examId+" and C.ExamId=A.ExamId and A.SubjectId="+subjectId+" and C.SubjectId=A.SubjectId GROUP BY A.ExamId, A.SubjectId, B.SectionId";
		Cursor c = sqliteDatabase.rawQuery(sql,null);
		String sql2 = "update exmavg set ExamAvg=?,CompleteEntry=1 where ExamId=? and SectionId=? and SubjectId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql2);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getLong(c.getColumnIndex("Average")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(4, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		c.close();
	}
	public void insertAvgIntoExmAvg(int sectionId, int subjectId, int examId, int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "SELECT A.ExamId, A.SubjectId, B.SectionId, (AVG(Mark)/C.MaximumMark)*360 as Average FROM marks A, students B, subjectexams C WHERE A.StudentId=B.StudentId"+
				" and A.ExamId="+examId+" and C.ExamId="+examId+" and C.SubjectId="+subjectId+" and A.SubjectId="+subjectId+" and B.SectionId="+sectionId+" GROUP BY A.ExamId, A.SubjectId, B.SectionId";
		Cursor c = sqliteDatabase.rawQuery(sql,null);
		c.moveToFirst();
		String sql2 = "update exmavg set ExamAvg="+c.getInt(c.getColumnIndex("Average"))+" where SectionId="+sectionId+" and SubjectId="+subjectId+" and ExamId="+examId;
		sqliteDatabase.execSQL(sql2);
		Log.d("exmavg", sql2);
		c.close();
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", schoolId);
		cv.put("Action", "update");
		cv.put("TableName", "exmavg");
		cv.put("Query", sql2);
		sqliteDatabase.insert("uploadsql", null, cv);
		//sqliteDatabase.close();
	}
	public void insertIntoExmAvg(int sectionId, int subjectId, int examId, int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "insert into exmavg(SectionId,SubjectId,ExamId) values("+sectionId+","+subjectId+","+examId+")";
		sqliteDatabase.execSQL(sql);
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", schoolId);
		cv.put("Action", "insert");
		cv.put("TableName", "exmavg");
		cv.put("Query", sql);
		sqliteDatabase.insert("uploadsql", null, cv);
		//sqliteDatabase.close();
	}
	public int checkExmEntry(int sectionId, int subjectId, int examId){
		int entry = 0;
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select ExamAvg from exmavg where SectionId="+sectionId+" and SubjectId="+subjectId+" and ExamId="+examId, null);
		if(c.getCount()>0){
			entry = 1;
		}
		c.close();
		//sqliteDatabase.close();
		return entry;
	}
	public void insertExmActAvg(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql_query = "Select AB.ExamId,AB.SectionId,AB.SubjectId,AVG(B)*360 as Average from (SELECT A.ExamId,B.SectionId,A.ActivityId,A.SubjectId,"+
				"C.MaximumMark ,AVG(mark)/C.MaximumMark B FROM activitymark A,students B,activity C  WHERE A.StudentId=B.StudentId and A.ActivityId=C.ActivityId"+
				" group by A.ExamId,A.ActivityId,A.SubjectId) AB group by AB.ExamId,AB.SectionId,AB.SubjectId";
		Cursor c = sqliteDatabase.rawQuery(sql_query,null);
	//	String sql_q = "update exmavg set ExamAvg=? where ExamId=? and SectionId=? and SubjectId=?";
		String sql_q = "insert into exmavg (SectionId,SubjectId,ExamId,ExamAvg) Values(?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql_q);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(4, c.getLong(c.getColumnIndex("Average")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void insertExmActAvg(int examId, int subjectId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql_query = "Select AB.ExamId,AB.SectionId,AB.SubjectId,AVG(B)*360 as Average from (SELECT A.ExamId,B.SectionId,A.ActivityId,A.SubjectId,"+
				"C.MaximumMark ,AVG(mark)/C.MaximumMark B FROM activitymark A,students B,activity C  WHERE A.StudentId=B.StudentId and A.ActivityId=C.ActivityId"+
				" and A.ExamId="+examId+" and A.SubjectId="+subjectId+" group by A.ExamId,A.ActivityId,A.SubjectId) AB group by AB.ExamId,AB.SectionId,AB.SubjectId";
		Cursor c = sqliteDatabase.rawQuery(sql_query,null);
		String sql_q = "insert into exmavg (SectionId,SubjectId,ExamId,ExamAvg,CompleteEntry) Values(?,?,?,?,1)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql_q);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(4, c.getLong(c.getColumnIndex("Average")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void updateExmActAvg(int examId, int subjectId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql_query = "Select AB.ExamId,AB.SectionId,AB.SubjectId,AVG(B)*360 as Average from (SELECT A.ExamId,B.SectionId,A.ActivityId,A.SubjectId,"+
				"C.MaximumMark ,AVG(mark)/C.MaximumMark B FROM activitymark A,students B,activity C  WHERE A.StudentId=B.StudentId and A.ActivityId=C.ActivityId"+
				" and A.ExamId="+examId+" and A.SubjectId="+subjectId+" group by A.ExamId,A.ActivityId,A.SubjectId) AB group by AB.ExamId,AB.SectionId,AB.SubjectId";
		Cursor c = sqliteDatabase.rawQuery(sql_query,null);
		String sql_q = "update exmavg set ExamAvg=? where ExamId=? and SectionId=? and SubjectId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql_q);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getLong(c.getColumnIndex("Average")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(4, c.getInt(c.getColumnIndex("SubjectId")));		
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void updateActivityAvg(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "SELECT A.ActivityId, (AVG(Mark)/A.MaximumMark)*360 as Average FROM activity A, activitymark B WHERE A.ActivityId = B.ActivityId GROUP BY A.ActivityId,B.ActivityId";
		Cursor c = sqliteDatabase.rawQuery(sql,null);
		String sql2 = "update activity set ActivityAvg=? where ActivityId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql2);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("Average")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("ActivityId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void updateActivityAvg(List<Integer> actList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(Integer act: actList){
			Log.d("act", act+"");
			String sql = "SELECT A.ActivityId, (AVG(Mark)/A.MaximumMark)*360 as Average FROM activity A, activitymark B WHERE A.ActivityId = B.ActivityId "+
					" and A.ActivityId="+act+" GROUP BY A.ActivityId,B.ActivityId";
			Cursor c = sqliteDatabase.rawQuery(sql,null);
			String sql2 = "update activity set ActivityAvg=?,CompleteEntry=1 where ActivityId=?";
			sqliteDatabase.beginTransaction();
			SQLiteStatement stmt = sqliteDatabase.compileStatement(sql2);
			c.moveToFirst();
			while(!c.isAfterLast()){
				stmt.bindLong(1, c.getInt(c.getColumnIndex("Average")));
				stmt.bindLong(2, c.getInt(c.getColumnIndex("ActivityId")));
				stmt.execute();
				stmt.clearBindings();
				c.moveToNext();
			}
			c.close();
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
		}
	}
	public void updateSubactActAvg(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "SELECT A.SectionId, A.ExamId, A.SubjectId, A.ActivityId, AVG(subactivityavg) as Average FROM subactivity A group by A.SectionId, A.ExamId, A.SubjectId, A.ActivityId";
		Cursor c = sqliteDatabase.rawQuery(sql,null);
		String sql2 = "update activity set ActivityAvg=? where ActivityId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql2);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("Average")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("ActivityId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void updateSubactActAvg(int actId, int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "SELECT A.SectionId, A.ExamId, A.SubjectId, A.ActivityId, AVG(subactivityavg) as Average FROM subactivity A WHERE A.ActivityId="+actId+" group by A.SectionId, A.ExamId, A.SubjectId, A.ActivityId";
		Cursor c = sqliteDatabase.rawQuery(sql,null);
		c.moveToFirst();
		String sql2 = "update activity set ActivityAvg="+c.getInt(c.getColumnIndex("Average"))+" where ActivityId="+actId;
		sqliteDatabase.execSQL(sql2);
		c.close();
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", schoolId);
		cv.put("Action", "update");
		cv.put("TableName", "activity");
		cv.put("Query", sql2);
		sqliteDatabase.insert("uploadsql", null, cv);
		//sqliteDatabase.close();
	}
	public void updateActExmAvg(int sectionId,int subjectId,int examId,int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "SELECT A.SectionId, A.ExamId, A.SubjectId, AVG(activityavg) as Average FROM activity A WHERE A.SectionId="+sectionId+" and A.SubjectId="+subjectId+" and A.ExamId="+examId+" group by A.SectionId, A.ExamId, A.SubjectId";
		Cursor c = sqliteDatabase.rawQuery(sql,null);
		c.moveToFirst();
		String sql2 = "update exmavg set ExamAvg="+c.getInt(c.getColumnIndex("Average"))+" where SectionId="+sectionId+" and SubjectId="+subjectId+" and ExamId="+examId;
		sqliteDatabase.execSQL(sql2);
		c.close();
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", schoolId);
		cv.put("Action", "update");
		cv.put("TableName", "exmavg");
		cv.put("Query", sql2);
		sqliteDatabase.insert("uploadsql", null, cv);
		//sqliteDatabase.close();
	}
	public void updateActivityAvg(int activityId,int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "SELECT A.ActivityId, (AVG(Mark)/A.MaximumMark)*360 as Average FROM activity A, activitymark B WHERE A.ActivityId = B.ActivityId and A.ActivityId = "+activityId+" GROUP BY A.ActivityId,B.ActivityId";
		Cursor c = sqliteDatabase.rawQuery(sql,null);
		c.moveToFirst();
		String sql2 = "update activity set ActivityAvg="+c.getInt(c.getColumnIndex("Average"))+" where ActivityId="+activityId;
		sqliteDatabase.execSQL(sql2);
		c.close();
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", schoolId);
		cv.put("Action", "update");
		cv.put("TableName", "activity");
		cv.put("Query", sql2);
		sqliteDatabase.insert("uploadsql", null, cv);
		//sqliteDatabase.close();
	}
	public void updateSubActivityAvg(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "SELECT A.SubActivityId, (AVG(Mark)/A.MaximumMark)*360 as Average FROM subactivity A, subactivitymark B WHERE A.SubActivityId = B.SubActivityId GROUP BY A.SubActivityId,B.SubActivityId";
		Cursor c = sqliteDatabase.rawQuery(sql,null);
		String sql2 = "update subactivity set SubActivityAvg=? where SubActivityId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql2);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("Average")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SubActivityId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	public void updateSubActivityAvg(int subActivityId, int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "SELECT A.SubActivityId, (AVG(Mark)/A.MaximumMark)*360 as Average FROM subactivity A, subactivitymark B WHERE A.SubActivityId = B.SubActivityId and A.SubActivityId="+subActivityId+" GROUP BY A.SubActivityId,B.SubActivityId";
		Cursor c = sqliteDatabase.rawQuery(sql,null);
		c.moveToFirst();
		String sql2 = "update subactivity set SubActivityAvg="+c.getInt(c.getColumnIndex("SubActivityAvg"))+" where SubActivityId="+subActivityId;
		sqliteDatabase.execSQL(sql2);
		c.close();
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", schoolId);
		cv.put("Action", "update");
		cv.put("TableName", "subactivity");
		cv.put("Query", sql2);
		sqliteDatabase.insert("uploadsql", null, cv);
		//sqliteDatabase.close();
	}
	public void updateSlipTestAvg(int sectionId, int subjectId, int avg, int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "update stavg set SlipTestAvg="+avg+" where SectionId="+sectionId+" and SubjectId="+subjectId;
		sqliteDatabase.execSQL(sql);
		ContentValues cv = new ContentValues();
		cv.put("SchoolId",schoolId);
		cv.put("Action", "update");
		cv.put("TableName", "stavg");
		cv.put("Query", sql);
		sqliteDatabase.insert("uploadsql", null, cv);
		//sqliteDatabase.close();
	}
	
	public String selectActivityName(int activityId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		String s = null;
		Cursor c = sqliteDatabase.rawQuery("select * from activity where ActivityId="+activityId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			s = c.getString(c.getColumnIndex("ActivityName"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return s;
	}
	
	public int selectActMaxMark(int activityId){
		int maxMark = 0;
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from activity where ActivityId="+activityId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			maxMark = c.getInt(c.getColumnIndex("MaximumMark"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return maxMark;
	}
	
	public List<ActivityMark> selectActivityMark(int activityId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from activitymark where ActivityId="+activityId, null);
		List<ActivityMark> aList = new ArrayList<ActivityMark>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			ActivityMark a = new ActivityMark();
			a.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			a.setActivityId(c.getInt(c.getColumnIndex("ActivityId")));
			a.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			a.setMark(c.getString(c.getColumnIndex("Mark")));
			a.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			a.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			aList.add(a);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return aList;
	}
	public void checkExamIsMark(){
		sqliteDatabase = dbHelper.getWritableDatabase();
	//	Cursor c = sqliteDatabase.rawQuery("SELECT A.ExamId,B.SectionId,C.SubjectId FROM marks A,students B,exmavg C"+
	//			" WHERE A.StudentId=B.StudentId and A.ExamId=C.ExamId and B.SectionId=C.SectionId group by A.ExamId,B.SectionId,C.SubjectId", null);
		Cursor c = sqliteDatabase.rawQuery("Select A.ExamId,B.SectionId,A.SubjectId,Count(*) From Marks A,Students B Where A.StudentId=B.StudentId group by A.ExamId,B.SectionId,A.SubjectId", null);
		String sql = "update exmavg set CompleteEntry=1 where ExamId=? and SectionId=? and SubjectId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);	
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkExamMarkEmpty(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery("Select A.ExamId,B.SectionId,A.SubjectId,Count(*) From Marks A,Students B Where A.StudentId=B.StudentId AND (A.Mark=0"+
				" OR A.Mark='') group by A.ExamId,B.SectionId,A.SubjectId", null);
		String sql = "update exmavg set CompleteEntry=0 where ExamId=? and SectionId=? and SubjectId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);	
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkExamMarkEmpty(int examId, int subjectId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery("Select A.ExamId,B.SectionId,A.SubjectId,Count(*) From Marks A,Students B Where A.ExamId="+examId+" AND A.StudentId=B.StudentId AND (A.Mark=0"+
				" OR A.Mark='') and A.SubjectId="+subjectId+" group by A.ExamId,B.SectionId,A.SubjectId", null);
		String sql = "update exmavg set CompleteEntry=0 where ExamId=? and SectionId=? and SubjectId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);	
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkExmActIsMark(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "Select A.ExamId,B.SectionId,A.SubjectId,Count(*) From activitymark A,students B Where A.StudentId=B.StudentId"+
				" group by A.ExamId,B.SectionId,A.SubjectId",null);
		String sql = "update exmavg set CompleteEntry=1 where ExamId=? and SectionId=? and SubjectId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);	
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkExmActMarkEmpty(){
		sqliteDatabase = dbHelper.getWritableDatabase();
	//	Cursor c = sqliteDatabase.rawQuery( "Select AB.ExamId,AB.SectionId,AB.SubjectId from (SELECT A.ExamId,B.SectionId,A.ActivityId,A.SubjectId"+
	//			" FROM activitymark A,students B,Activity C  WHERE A.StudentId=B.StudentId and A.ActivityId=C.ActivityId and A.Mark='0' group by"+
	//			" A.ExamId,A.ActivityId,A.SubjectId) AB group by AB.ExamId,AB.SectionId,AB.SubjectId",null);
		Cursor c = sqliteDatabase.rawQuery( "Select A.ExamId,B.SectionId,A.SubjectId,Count(*) From activitymark A,students B Where A.StudentId=B.StudentId"+
				" and (A.Mark=0 or A.Mark='') group by A.ExamId,B.SectionId,A.SubjectId",null);
		String sql = "update exmavg set CompleteEntry=0 where ExamId=? and SectionId=? and SubjectId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);	
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkExmActMarkEmpty(int examId, int subjectId){
		sqliteDatabase = dbHelper.getWritableDatabase();
			Cursor c = sqliteDatabase.rawQuery( "Select A.ExamId,B.SectionId,A.SubjectId,Count(*) From activitymark A,students B Where A.StudentId=B.StudentId"+
					" and A.ExamId="+examId+" and A.SubjectId="+subjectId+" and (A.Mark=0 or A.Mark='') group by A.ExamId,B.SectionId,A.SubjectId",null);
			String sql = "update exmavg set CompleteEntry=0 where ExamId=? and SectionId=? and SubjectId=?";
			sqliteDatabase.beginTransaction();
			SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);	
			c.moveToFirst();
			while(!c.isAfterLast()){
				stmt.bindLong(1, c.getInt(c.getColumnIndex("ExamId")));
				stmt.bindLong(2, c.getInt(c.getColumnIndex("SectionId")));
				stmt.bindLong(3, c.getInt(c.getColumnIndex("SubjectId")));
				stmt.execute();
				stmt.clearBindings();
				c.moveToNext();
			}
			c.close();
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
	}
	public void checkExmSubActIsMark(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "Select A.ExamId,B.SectionId,A.SubjectId,Count(*) From subactivitymark A,students B Where A.StudentId=B.StudentId"+
				" group by A.ExamId,B.SectionId,A.SubjectId",null);
		String sql = "update exmavg set CompleteEntry=1 where ExamId=? and SectionId=? and SubjectId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);	
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkExmSubActMarkEmpty(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "Select A.ExamId,B.SectionId,A.SubjectId,Count(*) From subactivitymark A,students B Where A.StudentId=B.StudentId"+
				" and (A.Mark=0 or A.Mark='') group by A.ExamId,B.SectionId,A.SubjectId",null);
		String sql = "update exmavg set CompleteEntry=0 where ExamId=? and SectionId=? and SubjectId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);	
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("ExamId")));
			stmt.bindLong(2, c.getInt(c.getColumnIndex("SectionId")));
			stmt.bindLong(3, c.getInt(c.getColumnIndex("SubjectId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkExmMarkEmpty(int examId, int sectionId, int subjectId, int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery("SELECT A.ExamId, COUNT(*) FROM marks A, students B WHERE A.ExamId="+examId+" and A.SubjectId="+subjectId+
				" and A.StudentId=B.StudentId and B.SectionId="+sectionId+" and A.Mark='' GROUP BY A.ExamId HAVING COUNT(*)>0", null);
		if(c.getCount()>0){
			String sql = "update exmavg set CompleteEntry="+0+" where ExamId="+examId+" and SectionId="+sectionId+" and SubjectId="+subjectId;
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", schoolId);
			cv.put("Action", "update");
			cv.put("TableName", "exmavg");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}else{
			String sql = "update exmavg set CompleteEntry="+1+" where ExamId="+examId+" and SectionId="+sectionId+" and SubjectId="+subjectId;
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", schoolId);
			cv.put("Action", "update");
			cv.put("TableName", "exmavg");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
		c.close();
	}
	public void checkActivityIsMark(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "SELECT A.ActivityId, COUNT(*) FROM activity A, activitymark B WHERE A.ActivityId=B.ActivityId"+
				" GROUP BY A.ActivityId HAVING COUNT(*)>0",null);
		String sql = "update activity set CompleteEntry=1 where ActivityId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("ActivityId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkActSubActIsMark(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "SELECT A.ActivityId, COUNT(*) FROM subactivity A, subactivitymark B WHERE A.ActivityId=B.ActivityId"+
				" GROUP BY A.ActivityId HAVING COUNT(*)>0",null);
		String sql = "update activity set CompleteEntry=1 where ActivityId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("ActivityId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkActivityMarkEmpty(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "SELECT A.ActivityId, COUNT(*) FROM activity A, activitymark B WHERE A.ActivityId=B.ActivityId"+
				" AND (B.Mark=0 or B.Mark='') GROUP BY A.ActivityId HAVING COUNT(*)>0",null);
		String sql = "update activity set CompleteEntry=0 where ActivityId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("ActivityId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkActivityMarkEmpty(List<Integer> actList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(Integer act: actList){
			Cursor c = sqliteDatabase.rawQuery( "SELECT A.ActivityId, COUNT(*) FROM activity A, activitymark B WHERE A.ActivityId=B.ActivityId"+
					" AND A.ActivityId="+act+" AND (B.Mark=0 or B.Mark='') GROUP BY A.ActivityId HAVING COUNT(*)>0",null);
			String sql = "update activity set CompleteEntry=0 where ActivityId=?";
			sqliteDatabase.beginTransaction();
			SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
			c.moveToFirst();
			while(!c.isAfterLast()){
				stmt.bindLong(1, c.getInt(c.getColumnIndex("ActivityId")));
				stmt.execute();
				stmt.clearBindings();
				c.moveToNext();
			}
			c.close();
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
		}
	}
	public void checkActSubActMarkEmpty(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "SELECT A.ActivityId, COUNT(*) FROM subactivity A, subactivitymark B WHERE A.ActivityId=B.ActivityId"+
				" AND (B.Mark=0 or B.Mark='') GROUP BY A.ActivityId HAVING COUNT(*)>0",null);
		String sql = "update activity set CompleteEntry=0 where ActivityId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("ActivityId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkActMarkEmpty(int actId, int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "SELECT A.ActivityId, COUNT(*) FROM activity A, activitymark B WHERE A.ActivityId=B.ActivityId AND A.ActivityId="+actId+
				" AND (B.Mark='' or B.Mark=0) GROUP BY A.ActivityId HAVING COUNT(*)>0",null);
		if(c.getCount()>0){
			String sql = "update activity set CompleteEntry="+0+" where ActivityId="+actId;
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", schoolId);
			cv.put("Action", "update");
			cv.put("TableName", "activity");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}else{
			String sql = "update activity set CompleteEntry="+1+" where ActivityId="+actId;
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", schoolId);
			cv.put("Action", "update");
			cv.put("TableName", "activity");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
		c.close();
		//sqliteDatabase.close();
	}
	public void checkExmActMarkEmpty(int examId, int sectionId, int subjectId, int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "Select A.ExamId,B.SectionId,A.SubjectId,Count(*) From activitymark A,students B Where A.StudentId=B.StudentId"+
				" and A.ExamId="+examId+" and A.SubjectId="+subjectId+" and B.SectionId="+sectionId+" and (A.Mark='' or A.Mark=0) group by A.ExamId,B.SectionId,A.SubjectId",null);
		if(c.getCount()>0){
			String sql = "update exmavg set CompleteEntry="+0+" where ExamId="+examId;
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", schoolId);
			cv.put("Action", "update");
			cv.put("TableName", "exmavg");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}else{
			String sql = "update exmavg set CompleteEntry="+1+" where ExamId="+examId;
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", schoolId);
			cv.put("Action", "update");
			cv.put("TableName", "exmavg");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
		c.close();
	}
	public void checkSubActivityIsMark(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "SELECT A.SubActivityId, COUNT(*) FROM subactivity A, subactivitymark B WHERE A.SubActivityId=B.SubActivityId"+
				" GROUP BY A.SubActivityId HAVING COUNT(*)>0",null);
		String sql = "update subactivity set CompleteEntry=1 where ActivityId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("SubActivityId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkSubActivityMarkEmpty(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "SELECT A.SubActivityId, COUNT(*) FROM subactivity A, subactivitymark B WHERE A.SubActivityId=B.SubActivityId"+
				" AND (B.Mark=0 or B.Mark='') GROUP BY A.SubActivityId HAVING COUNT(*)>0",null);
		String sql = "update subactivity set CompleteEntry=0 where ActivityId=?";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		c.moveToFirst();
		while(!c.isAfterLast()){
			stmt.bindLong(1, c.getInt(c.getColumnIndex("SubActivityId")));
			stmt.execute();
			stmt.clearBindings();
			c.moveToNext();
		}
		c.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	public void checkSubActMarkEmpty(int subActId, int schoolId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery( "SELECT A.SubActivityId, COUNT(*) FROM subactivity A, subactivitymark B WHERE A.SubActivityId=B.SubActivityId AND A.SubActivityId="+subActId+
				" AND B.Mark='' GROUP BY A.SubActivityId HAVING COUNT(*)>0",null);
		if(c.getCount()>0){
			String sql = "update subactivity set CompleteEntry="+0+" where ActivityId="+subActId;
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", schoolId);
			cv.put("Action", "update");
			cv.put("TableName", "subactivity");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}else{
			String sql = "update subactivity set CompleteEntry="+1+" where ActivityId="+subActId;
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", schoolId);
			cv.put("Action", "update");
			cv.put("TableName", "subactivity");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
		c.close();
		//sqliteDatabase.close();
	}
	public List<String> selectActivityMarc(int activityId, List<Integer> studentId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		List<String> mList = new ArrayList<String>();
		for(Integer i: studentId){
		Cursor c = sqliteDatabase.rawQuery("select Mark from activitymark where ActivityId="+activityId+" and StudentId="+i, null);
		c.moveToFirst();
		mList.add(c.getString(c.getColumnIndex("Mark")));
		c.close();
		}
		//sqliteDatabase.close();
		return mList;
	}
	public List<ActivityMark> selectActivityMarc(int activityId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		List<ActivityMark> mList = new ArrayList<ActivityMark>();
		Cursor c = sqliteDatabase.rawQuery("select * from activitymark where ActivityId="+activityId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			ActivityMark a = new ActivityMark();
			a.setMark(c.getString(c.getColumnIndex("Mark")));
			a.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			mList.add(a);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return mList;
	}
	
	public List<ActivityMark> selectActivityMark(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from activitymark", null);
		List<ActivityMark> aList = new ArrayList<ActivityMark>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			ActivityMark a = new ActivityMark();
			a.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			a.setActivityId(c.getInt(c.getColumnIndex("ActivityId")));
			a.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			a.setMark(c.getString(c.getColumnIndex("Mark")));
			a.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			a.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			aList.add(a);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return aList;
	}
	public List<ActivityMark> selectActivityMark(int examId,int subjectId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from activitymark where ExamId="+examId+" and SubjectId="+subjectId, null);
		List<ActivityMark> aList = new ArrayList<ActivityMark>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			ActivityMark a = new ActivityMark();
			a.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			a.setActivityId(c.getInt(c.getColumnIndex("ActivityId")));
			a.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			a.setMark(c.getString(c.getColumnIndex("Mark")));
			a.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			a.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			aList.add(a);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return aList;
	}
	
	public List<SubActivity> selectSubActivity(int activityId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from subactivity where ActivityId="+activityId, null);
		List<SubActivity> aList = new ArrayList<SubActivity>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SubActivity a = new SubActivity();
			a.setSubActivityId(c.getInt(c.getColumnIndex("SubActivityId")));
			a.setActivityId(c.getInt(c.getColumnIndex("ActivityId")));
			a.setSubActivityName(c.getString(c.getColumnIndex("SubActivityName")));
			a.setCalculation(c.getInt(c.getColumnIndex("Calculation")));
			a.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			a.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			a.setMaximumMark(c.getInt(c.getColumnIndex("MaximumMark")));
			a.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			a.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			a.setWeightage(c.getInt(c.getColumnIndex("Weightage")));
			a.setSubActivityAvg(c.getInt(c.getColumnIndex("SubActivityAvg")));
			a.setCompleteEntry(c.getInt(c.getColumnIndex("CompleteEntry")));
			aList.add(a);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return aList;
	}
	
	public String selectSubActivityName(int subActivityId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		String s = null;
		Cursor c = sqliteDatabase.rawQuery("select * from subactivity where SubActivityId="+subActivityId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			s = c.getString(c.getColumnIndex("SubActivityName"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return s;
	}
	
	public int selectSubActMaxMark(int activityId){
		int maxMark = 0;
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from subactivity where SubActivityId="+activityId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			maxMark = c.getInt(c.getColumnIndex("MaximumMark"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return maxMark;
	}
	
	public List<SubActivityMark> selectSubActivityMark(int subActivityId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from subactivitymark where SubActivityId="+subActivityId, null);
		List<SubActivityMark> aList = new ArrayList<SubActivityMark>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SubActivityMark a = new SubActivityMark();
			a.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			a.setActivityId(c.getInt(c.getColumnIndex("ActivityId")));
			a.setSubActivityId(c.getInt(c.getColumnIndex("SubActivityId")));
			a.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			a.setMark(c.getString(c.getColumnIndex("Mark")));
			a.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			a.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			a.setDescription(c.getString(c.getColumnIndex("Description")));
			aList.add(a);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return aList;
	}
	
	public List<SubActivityMark> selectSubActivityMarc(int subActivityId, List<Integer> studentId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		List<SubActivityMark> aList = new ArrayList<SubActivityMark>();
		for(Integer i: studentId){
		Cursor c = sqliteDatabase.rawQuery("select * from subactivitymark where SubActivityId="+subActivityId+" and StudentId="+i, null);	
		c.moveToFirst();
		while(!c.isAfterLast()){
			SubActivityMark a = new SubActivityMark();
			a.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			a.setActivityId(c.getInt(c.getColumnIndex("ActivityId")));
			a.setSubActivityId(c.getInt(c.getColumnIndex("SubActivityId")));
			a.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			a.setMark(c.getString(c.getColumnIndex("Mark")));
			a.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			a.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			a.setDescription(c.getString(c.getColumnIndex("Description")));
			aList.add(a);
			c.moveToNext();
		}
		c.close();		
		}
		//sqliteDatabase.close();
		return aList;
	}
	public List<SubActivityMark> selectSubActivityMarc(int subActivityId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		List<SubActivityMark> aList = new ArrayList<SubActivityMark>();
		Cursor c = sqliteDatabase.rawQuery("select * from subactivitymark where SubActivityId="+subActivityId, null);	
		c.moveToFirst();
		while(!c.isAfterLast()){
			SubActivityMark a = new SubActivityMark();
			a.setMark(c.getString(c.getColumnIndex("Mark")));
			a.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			aList.add(a);
			c.moveToNext();	
		}
		c.close();	
		//sqliteDatabase.close();
		return aList;
	}
	
	public List<SubActivityMark> selectSubActivityMark(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from subactivitymark", null);
		List<SubActivityMark> aList = new ArrayList<SubActivityMark>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SubActivityMark a = new SubActivityMark();
			a.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			a.setActivityId(c.getInt(c.getColumnIndex("ActivityId")));
			a.setSubActivityId(c.getInt(c.getColumnIndex("SubActivityId")));
			a.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			a.setMark(c.getString(c.getColumnIndex("Mark")));
			a.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			a.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			a.setDescription(c.getString(c.getColumnIndex("Description")));
			aList.add(a);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return aList;
	}
	public int getExmMaxMark(int classId,int examId, int subjectId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		int maxMark = 100;
		Cursor c = sqliteDatabase.rawQuery("select MaximumMark from subjectexams where ClassId="+classId+" and ExamId="+examId+
				" and SubjectId="+subjectId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			maxMark = c.getInt(c.getColumnIndex("MaximumMark"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return maxMark;
	}
	public int isExmMaxMarkDefined(int classId,int examId, int subjectId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		int isDefined = 0;
		Cursor c = sqliteDatabase.rawQuery("select MaximumMark from subjectexams where ClassId="+classId+" and ExamId="+examId+
				" and SubjectId="+subjectId, null);
		if(c.getCount()>0){
			isDefined = 1;
		}
		c.close();
		//sqliteDatabase.close();
		return isDefined;
	}
	public double getSTMaxMark(int slipTestId, int school_Id){
		sqliteDatabase = dbHelper.getReadableDatabase();
		double maxMark = 0;
		Cursor c = sqliteDatabase.rawQuery("select Mark from sliptestmark_"+school_Id+" where SlipTestId="+slipTestId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			if(Double.parseDouble(c.getString(c.getColumnIndex("Mark")))>maxMark){
				maxMark = Double.parseDouble(c.getString(c.getColumnIndex("Mark")));
			}
			c.moveToNext();
		}
		
		c.close();
		//sqliteDatabase.close();
		return maxMark;
	}
	public double getSTMaxMarc(int slipTestId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		double maxMark = 0;
		Cursor c = sqliteDatabase.rawQuery("select Mark from sliptestmark where SlipTestId="+slipTestId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			if(Double.parseDouble(c.getString(c.getColumnIndex("Mark")))>maxMark){
				maxMark = Double.parseDouble(c.getString(c.getColumnIndex("Mark")));
			}
			c.moveToNext();
		}	
		c.close();
		//sqliteDatabase.close();
		return maxMark;
	}
	
	public List<UploadSql> selectUploadSql(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from uploadsql", null);
		List<UploadSql> upList = new ArrayList<UploadSql>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			UploadSql up = new UploadSql();
			up.setAction(c.getString(c.getColumnIndex("Action")));
			up.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
			up.setQuery(c.getString(c.getColumnIndex("Query")));
			up.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			up.setTableName(c.getString(c.getColumnIndex("TableName")));
			upList.add(up);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return upList;
	}
	
	public List<SlipTestSql> selectSlipTestSql(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptestsql", null);
		List<SlipTestSql> stSqlList = new ArrayList<SlipTestSql>();
		if(c.getCount()>0){
		c.moveToFirst();
			SlipTestSql st = new SlipTestSql();
			st.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			st.setAction(c.getString(c.getColumnIndex("Action")));
			st.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
			st.setQuery(c.getString(c.getColumnIndex("Query")));
			st.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			st.setTableName(c.getString(c.getColumnIndex("TableName")));
			stSqlList.add(st);
		c.close();
		}
		//sqliteDatabase.close();
		return stSqlList;
	}
	public List<SlipTestSql> selectSTSql(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptestsql", null);
		List<SlipTestSql> stSqlList = new ArrayList<SlipTestSql>();
		if(c.getCount()>0){
		c.moveToFirst();
		while(!c.isAfterLast()){
			SlipTestSql st = new SlipTestSql();
			st.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			st.setAction(c.getString(c.getColumnIndex("Action")));
			st.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
			st.setQuery(c.getString(c.getColumnIndex("Query")));
			st.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			st.setTableName(c.getString(c.getColumnIndex("TableName")));
			stSqlList.add(st);
			c.moveToNext();
		}
		c.close();
		}
		//sqliteDatabase.close();
		return stSqlList;
	}
	public List<HomeworkSql> selectHWSql(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from homeworksql", null);
		List<HomeworkSql> stSqlList = new ArrayList<HomeworkSql>();
		if(c.getCount()>0){
		c.moveToFirst();
		while(!c.isAfterLast()){
			HomeworkSql st = new HomeworkSql();
			st.setHomeworkId(c.getInt(c.getColumnIndex("HomeworkId")));
			st.setAction(c.getString(c.getColumnIndex("Action")));
			st.setQuery(c.getString(c.getColumnIndex("Query")));
			st.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			st.setTableName(c.getString(c.getColumnIndex("TableName")));
			st.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			st.setHomeworkDate(c.getString(c.getColumnIndex("HomeworkDate")));
			stSqlList.add(st);
			c.moveToNext();
		}
		c.close();
		}
		//sqliteDatabase.close();
		return stSqlList;
	}
	public List<HomeworkSql> selectHWSql(int sectionId, String homeworkDate){
		sqliteDatabase = dbHelper.getReadableDatabase();
		List<HomeworkSql> stSqlList = new ArrayList<HomeworkSql>();
		Cursor c = sqliteDatabase.rawQuery("select * from homeworksql where SectionId="+sectionId+" and HomeworkDate='"+homeworkDate+"'", null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			HomeworkSql st = new HomeworkSql();
			st.setHomeworkId(c.getInt(c.getColumnIndex("HomeworkId")));
			st.setAction(c.getString(c.getColumnIndex("Action")));
			st.setQuery(c.getString(c.getColumnIndex("Query")));
			st.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			st.setTableName(c.getString(c.getColumnIndex("TableName")));
			st.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			st.setHomeworkDate(c.getString(c.getColumnIndex("HomeworkDate")));
			stSqlList.add(st);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return stSqlList;
	}
	public List<Integer> getUniqueSectionHW(String homeworkDate){
		sqliteDatabase = dbHelper.getReadableDatabase();
		List<Integer> secIdList = new ArrayList<Integer>();
		Cursor c = sqliteDatabase.rawQuery("select distinct SectionId from homeworksql where HomeworkDate='"+homeworkDate+"'", null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			secIdList.add(c.getInt(c.getColumnIndex("SectionId")));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return secIdList;
	}
	
	public List<DownloadSql> selectDownloadSql(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from downloadsql", null);
		List<DownloadSql> dnList = new ArrayList<DownloadSql>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			DownloadSql dn = new DownloadSql();
			dn.setAckId(c.getInt(c.getColumnIndex("AckId")));
			dn.setAction(c.getString(c.getColumnIndex("Action")));
			dn.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
			dn.setQuery(c.getString(c.getColumnIndex("Query")));
			dn.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			dn.setTableName(c.getString(c.getColumnIndex("TableName")));
			dnList.add(dn);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return dnList;
	}
	
	public List<DwnAutoId> selectDwnAutoId(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from dwnautoid", null);
		List<DwnAutoId> dnList = new ArrayList<DwnAutoId>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			DwnAutoId dn = new DwnAutoId();
			dn.setAckId(c.getInt(c.getColumnIndex("AckId")));
			dn.setAction(c.getString(c.getColumnIndex("Action")));
			dn.setQuery(c.getString(c.getColumnIndex("Query")));
			dn.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			dn.setTableName(c.getString(c.getColumnIndex("TableName")));
			dn.setIncrementId(c.getInt(c.getColumnIndex("IncrementId")));
			dnList.add(dn);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return dnList;
	}
	
	public List<SlipTestt> selectSlipTest(int sectionId, int subjectId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptest where SectionId="+sectionId+" and SubjectId="+subjectId, null);
		List<SlipTestt> stList = new ArrayList<SlipTestt>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SlipTestt st = new SlipTestt();
			st.setAverageMark(c.getDouble(c.getColumnIndex("AverageMark")));
			st.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			st.setMarkEntered(c.getInt(c.getColumnIndex("MarkEntered")));
			st.setMaximumMark(c.getInt(c.getColumnIndex("MaximumMark")));
			st.setPortion(c.getInt(c.getColumnIndex("Portion")));
			st.setExtraPortion(c.getString(c.getColumnIndex("ExtraPortion")));
			st.setPortionName(c.getString(c.getColumnIndex("PortionName")));
			st.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			st.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			st.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			st.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			st.setTestDate(c.getString(c.getColumnIndex("TestDate")));
			stList.add(st);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return stList;
	}
	public List<SlipTestt> selectSlipTest(int slipTestId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptest where SlipTestId="+slipTestId, null);
		List<SlipTestt> stList = new ArrayList<SlipTestt>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SlipTestt st = new SlipTestt();
			st.setAverageMark(c.getDouble(c.getColumnIndex("AverageMark")));
			st.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			st.setMarkEntered(c.getInt(c.getColumnIndex("MarkEntered")));
			st.setMaximumMark(c.getInt(c.getColumnIndex("MaximumMark")));
			st.setPortion(c.getInt(c.getColumnIndex("Portion")));
			st.setExtraPortion(c.getString(c.getColumnIndex("ExtraPortion")));
			st.setPortionName(c.getString(c.getColumnIndex("PortionName")));
			st.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			st.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			st.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			st.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			st.setTestDate(c.getString(c.getColumnIndex("TestDate")));
			stList.add(st);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return stList;
	}
	public List<Integer> avgSlipTest(int sectionId,int subjectId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptest where SectionId="+sectionId+" and SubjectId="+subjectId, null);
		List<Integer> stList = new ArrayList<Integer>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			stList.add(c.getInt(c.getColumnIndex("SlipTestId")));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return stList;
	}
	public List<SlipTestt> selecST(int slipTestId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptesttemp where SlipTestId="+slipTestId, null);
		List<SlipTestt> stList = new ArrayList<SlipTestt>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SlipTestt st = new SlipTestt();
			st.setAverageMark(c.getDouble(c.getColumnIndex("AverageMark")));
			st.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			st.setMarkEntered(c.getInt(c.getColumnIndex("MarkEntered")));
			st.setMaximumMark(c.getInt(c.getColumnIndex("MaximumMark")));
			st.setPortion(c.getInt(c.getColumnIndex("Portion")));
			st.setExtraPortion(c.getString(c.getColumnIndex("ExtraPortion")));
			st.setPortionName(c.getString(c.getColumnIndex("PortionName")));
			st.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			st.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			st.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			st.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			st.setTestDate(c.getString(c.getColumnIndex("TestDate")));
			stList.add(st);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return stList;
	}
	
	public String selectSlipTestName(int slipTestId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		String s = null;
		Cursor c = sqliteDatabase.rawQuery("select * from sliptest where SlipTestId="+slipTestId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			s = c.getString(c.getColumnIndex("PortionName"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return s;
	}
	
	public List<SlipTestt> selectST(int sectionId, int subjectId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptesttemp where SectionId="+sectionId+" and SubjectId="+subjectId, null);
		List<SlipTestt> stList = new ArrayList<SlipTestt>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SlipTestt st = new SlipTestt();
			st.setAverageMark(c.getDouble(c.getColumnIndex("AverageMark")));
			st.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			st.setMarkEntered(c.getInt(c.getColumnIndex("MarkEntered")));
			st.setMaximumMark(c.getInt(c.getColumnIndex("MaximumMark")));
			st.setPortion(c.getInt(c.getColumnIndex("Portion")));
			st.setPortionName(c.getString(c.getColumnIndex("PortionName")));
			st.setExtraPortion(c.getString(c.getColumnIndex("ExtraPortion")));
			st.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			st.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			st.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			st.setSlipTestName(c.getString(c.getColumnIndex("SlipTestName")));
			st.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			st.setTestDate(c.getString(c.getColumnIndex("TestDate")));
			stList.add(st);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return stList;
	}
	
	public String selectSTName(int slipTestId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		String s = null;
		Cursor c = sqliteDatabase.rawQuery("select * from sliptesttemp where SlipTestId="+slipTestId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			s = c.getString(c.getColumnIndex("PortionName"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return s;
	}
	
	public List<SlipTestt> selectSlipTest(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("SELECT * FROM sliptest ORDER BY SlipTestId ASC LIMIT 1", null);
		List<SlipTestt> stList = new ArrayList<SlipTestt>();
		c.moveToFirst();
			SlipTestt st = new SlipTestt();
			st.setMaximumMark(c.getInt(c.getColumnIndex("MaximumMark")));
			st.setPortion(c.getInt(c.getColumnIndex("Portion")));
			st.setExtraPortion(c.getString(c.getColumnIndex("ExtraPortion")));
			st.setPortionName(c.getString(c.getColumnIndex("PortionName")));
			st.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			st.setSlipTestName(c.getString(c.getColumnIndex("SlipTestName")));
			st.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			st.setTestDate(c.getString(c.getColumnIndex("TestDate")));
			stList.add(st);
		c.close();
		//sqliteDatabase.close();
		return stList;
	}
	public List<Homework> selectHomework(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("SELECT * FROM homeworkmessage ORDER BY HomeworkId ASC LIMIT 1", null);
		List<Homework> hwList = new ArrayList<Homework>();
		c.moveToFirst();
			Homework hw = new Homework();
			hw.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			hw.setHomework(c.getString(c.getColumnIndex("Homework")));
			hw.setHomeworkDate(c.getString(c.getColumnIndex("HomeworkDate")));
			hw.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			hw.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			hw.setSubjectIDs(c.getString(c.getColumnIndex("SubjectIDs")));
			hw.setTeacherId(c.getInt(c.getColumnIndex("TeacherId")));		
			hwList.add(hw);
		c.close();
		//sqliteDatabase.close();
		return hwList;
	}
	public List<Exams> selectInsertedExam(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("SELECT * FROM exams ORDER BY ExamId ASC LIMIT 1", null);
		List<Exams> eList = new ArrayList<Exams>();
		c.moveToFirst();
			Exams e = new Exams();
			e.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			e.setExamId(c.getInt(c.getColumnIndex("ExamId")));
			e.setExamName(c.getString(c.getColumnIndex("ExamName")));
			e.setGradeSystem(c.getInt(c.getColumnIndex("GradeSystem")));
			e.setMarkUploaded(c.getInt(c.getColumnIndex("MarkUploaded")));
			e.setPercentage(c.getString(c.getColumnIndex("Percentage")));
			e.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			e.setSubjectIDs(c.getString(c.getColumnIndex("SubjectIDs")));
			e.setTerm(c.getInt(c.getColumnIndex("Term")));
			eList.add(e);
		c.close();
		//sqliteDatabase.close();
		return eList;
	}
	public List<Activiti> selectInsertedActivity(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("SELECT * FROM activity ORDER BY ActivityId ASC LIMIT 1", null);
		List<Activiti> aList = new ArrayList<Activiti>();
		c.moveToFirst();
		Activiti a = new Activiti();
		a.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
		a.setActivityName(c.getString(c.getColumnIndex("ActivityName")));
		a.setCalculation(c.getInt(c.getColumnIndex("Calculation")));
		a.setClassId(c.getInt(c.getColumnIndex("ClassId")));
		a.setExamId(c.getInt(c.getColumnIndex("ExamId")));
		a.setMaximumMark(c.getInt(c.getColumnIndex("MaximumMark")));
		a.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
		a.setSubActivity(c.getInt(c.getColumnIndex("SubActivity")));
		a.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
		a.setWeightage(c.getInt(c.getColumnIndex("Weightage")));
		aList.add(a);
		c.close();
		//sqliteDatabase.close();
		return aList;
	}
	public List<SubActivity> selectInsertedSubAct(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("SELECT * FROM subactivity ORDER BY SubActivityId ASC LIMIT 1", null);
		List<SubActivity> saList = new ArrayList<SubActivity>();
		c.moveToFirst();
		SubActivity sa = new  SubActivity();
		sa.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
		sa.setActivityId(c.getInt(c.getColumnIndex("ActivityId")));
		sa.setSubActivityName(c.getString(c.getColumnIndex("SubActivityName")));
		sa.setCalculation(c.getInt(c.getColumnIndex("Calculation")));
		sa.setClassId(c.getInt(c.getColumnIndex("ClassId")));
		sa.setExamId(c.getInt(c.getColumnIndex("ExamId")));
		sa.setMaximumMark(c.getInt(c.getColumnIndex("MaximumMark")));
		sa.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
		sa.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
		sa.setWeightage(c.getInt(c.getColumnIndex("Weightage")));
		c.close();
		//sqliteDatabase.close();
		return saList;
	}
	
	public void updateSlipTestId(SlipTestt st, int id){
		sqliteDatabase = dbHelper.getWritableDatabase();
	//		String sql = "update sliptest set SlipTestId="+id+" where SectionId="+st.getSectionId()+" and SubjectId="+st.getSubjectId()+" and SlipTestName='"+st.getSlipTestName()+
	//				  "' and Portion="+st.getPortion()+" and MaximumMark="+st.getMaximumMark()+" and TestDate='"+st.getTestDate()+"'";
	//		sqliteDatabase.execSQL(sql);
		
		ContentValues cv = new ContentValues();
		cv.put("SlipTestId", id);
		sqliteDatabase.update("sliptest", cv, "SectionId="+st.getSectionId()+" and SubjectId="+st.getSubjectId()+" and PortionName='"+st.getPortionName()+
					  "' and Portion="+st.getPortion()+" and MaximumMark="+st.getMaximumMark()+" and TestDate='"+st.getTestDate()+"'", null);
		
		//sqliteDatabase.close();
	}
	public void updateHomeworkId(Homework hw, int id){
		sqliteDatabase = dbHelper.getWritableDatabase();		
		ContentValues cv = new ContentValues();
		cv.put("HomeworkId", id);
		sqliteDatabase.update("homeworkmessage", cv, "SectionId="+hw.getSectionId()+" and SubjectIDs="+hw.getSubjectIDs()+" and TeacherId="+hw.getTeacherId()+
					  " and Homework='"+hw.getHomework()+"' and HomeworkDate='"+hw.getHomeworkDate()+"'", null);
		
		//sqliteDatabase.close();
	}
	public void updateExamId(Exams e, int id){
		sqliteDatabase = dbHelper.getWritableDatabase();		
		ContentValues cv = new ContentValues();
		cv.put("ExamId", id);
		sqliteDatabase.update("exams", cv, "ClassId="+e.getClassId()+" and SubjectIDs='"+e.getSubjectIDs()+"' and ExamName='"+e.getExamName()+"'", null);
		//sqliteDatabase.close();
	}
	public void updateActivityId(Activiti a, int id){
		sqliteDatabase = dbHelper.getWritableDatabase();		
		ContentValues cv = new ContentValues();
		cv.put("ActivityId", id);
		sqliteDatabase.update("activity", cv,"SectionId="+a.getSectionId()+" and ExamId="+a.getExamId()+" and SubjectId="+a.getSubjectId()+" and ActivityName='"+a.getActivityName()+"'",null);
		//sqliteDatabase.close();
	}
	public void updateSubActivityId(SubActivity sa, int id){
		sqliteDatabase = dbHelper.getWritableDatabase();		
		ContentValues cv = new ContentValues();
		cv.put("SubActivityId", id);
		sqliteDatabase.update("subactivity", cv, "SectionId="+sa.getSectionId()+" and ActivityId="+sa.getActivityId()+" and SubjectId="+sa.getSubjectId()+
					" and SubActivityName='"+sa.getSubActivityName()+"'", null);
		//sqliteDatabase.close();
	}
	public void deleteHomework(int id, int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "delete from homeworkmessage where HomeworkId="+id;
		sqliteDatabase.execSQL(sql);
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", schoolId);
		cv.put("Action", "delete");
		cv.put("TableName", "homeworkmessage");
		cv.put("Query", sql);
		sqliteDatabase.insert("uploadsql", null, cv);
		//sqliteDatabase.close();
	}
	public void updateHomework(int id, String s, int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();		
	//	ContentValues cv = new ContentValues();
	//	cv.put("Homework", s);
	//	sqliteDatabase.update("homeworkmessage", cv, "HomeworkId="+id, null);
		String sql = "update homeworkmessage set Homework='"+s+"' where HomeworkId="+id;
		sqliteDatabase.execSQL(sql);
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", schoolId);
		cv.put("Action", "update");
		cv.put("TableName", "homeworkmessage");
		cv.put("Query", sql);
		sqliteDatabase.insert("uploadsql", null, cv);
		//sqliteDatabase.close();
	}
	
	public int selectSlipTestMaxMark(int slipTestId){
		int maxMark = 0;
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptest where SlipTestId="+slipTestId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			maxMark = c.getInt(c.getColumnIndex("MaximumMark"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return maxMark;
	}
	
	public int selectSTMaxMark(int slipTestId){
		int maxMark = 0;
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptesttemp where SlipTestId="+slipTestId, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			maxMark = c.getInt(c.getColumnIndex("MaximumMark"));
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return maxMark;
	}
	
	public List<SlipTestMark> selectSlipTestMark(int sectionId, int subjectId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptestmark_22 where SectionId="+sectionId+" and SubjectId="+subjectId, null);
		List<SlipTestMark> stMarkList = new ArrayList<SlipTestMark>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SlipTestMark stMark = new SlipTestMark();
			stMark.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			stMark.setMark(c.getString(c.getColumnIndex("Mark")));
			stMark.setMarkId(c.getInt(c.getColumnIndex("MarkId")));
			stMark.setNewSubjectId(c.getInt(c.getColumnIndex("NewSubjectId")));
			stMark.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			stMark.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			stMark.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			stMark.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			stMark.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			stMarkList.add(stMark);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return stMarkList;
	}
	public List<SlipTestMark> avgSlipTestMarks(int slipTestId, int schoolId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptestmark_"+schoolId+" where SlipTestId="+slipTestId, null);
		List<SlipTestMark> stMarkList = new ArrayList<SlipTestMark>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SlipTestMark stMark = new SlipTestMark();
			stMark.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			stMark.setMark(c.getString(c.getColumnIndex("Mark")));
			stMark.setMarkId(c.getInt(c.getColumnIndex("MarkId")));
			stMark.setNewSubjectId(c.getInt(c.getColumnIndex("NewSubjectId")));
			stMark.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			stMark.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			stMark.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			stMark.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			stMark.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			stMarkList.add(stMark);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return stMarkList;
	}
	public List<SlipTestMark> selectSlipTestMark(int slipTestId, List<Integer> studentId, int schoolId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		List<SlipTestMark> stMarkList = new ArrayList<SlipTestMark>();
		for(Integer i: studentId){
		Cursor c = sqliteDatabase.rawQuery("select * from sliptestmark_"+schoolId+" where SlipTestId="+slipTestId+" and StudentId="+i, null);	
		c.moveToFirst();
		while(!c.isAfterLast()){
			SlipTestMark stMark = new SlipTestMark();
			stMark.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			stMark.setMark(c.getString(c.getColumnIndex("Mark")));
			stMark.setMarkId(c.getInt(c.getColumnIndex("MarkId")));
			stMark.setNewSubjectId(c.getInt(c.getColumnIndex("NewSubjectId")));
			stMark.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			stMark.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			stMark.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			stMark.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			stMark.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			stMarkList.add(stMark);
			c.moveToNext();
		}
		c.close();
		}
		//sqliteDatabase.close();
		return stMarkList;
	}
	
	public List<SlipTestMark> selectSTMark(int slipTestId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptestmark where SlipTestId="+slipTestId, null);
		List<SlipTestMark> stMarkList = new ArrayList<SlipTestMark>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SlipTestMark stMark = new SlipTestMark();
			stMark.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			stMark.setMark(c.getString(c.getColumnIndex("Mark")));
			stMark.setMarkId(c.getInt(c.getColumnIndex("MarkId")));
			stMark.setNewSubjectId(c.getInt(c.getColumnIndex("NewSubjectId")));
			stMark.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			stMark.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			stMark.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			stMark.setStudentId(c.getInt(c.getColumnIndex("StudentId")));
			stMark.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			stMarkList.add(stMark);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return stMarkList;
	}
	
	public SlipTestt selectST(int slipTestId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptesttemp where SlipTestId="+slipTestId, null);
		c.moveToFirst();
			SlipTestt st = new SlipTestt();
			st.setAverageMark(c.getInt(c.getColumnIndex("AverageMark")));
			st.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			st.setMarkEntered(c.getInt(c.getColumnIndex("MarkEntered")));
			st.setMaximumMark(c.getInt(c.getColumnIndex("MaximumMark")));
			st.setPortion(c.getInt(c.getColumnIndex("Portion")));
			st.setPortionName(c.getString(c.getColumnIndex("PortionName")));
			st.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			st.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			st.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			st.setSlipTestName(c.getString(c.getColumnIndex("SlipTestName")));
			st.setTestDate(c.getString(c.getColumnIndex("TestDate")));
			st.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
		c.close();
		//sqliteDatabase.close();
		return st;
	}
	public Homework selectHW(int homeworkId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from hwmessage where HomeworkId="+homeworkId, null);
		c.moveToFirst();
			Homework h = new Homework();
			h.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			h.setHomework(c.getString(c.getColumnIndex("Homework")));
			h.setHomeworkDate(c.getString(c.getColumnIndex("HomeworkDate")));
			h.setHomeworkId(c.getInt(c.getColumnIndex("HomeworkId")));
			h.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			h.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			h.setSubjectIDs(c.getString(c.getColumnIndex("SubjectIDs")));
			h.setTeacherId(c.getInt(c.getColumnIndex("TeacherId")));
		c.close();
		//sqliteDatabase.close();
		return h;
	}
	
	public List<SlipTestt> selectST(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from sliptesttemp", null);
		List<SlipTestt> stList = new ArrayList<SlipTestt>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			SlipTestt st = new SlipTestt();
			st.setAverageMark(c.getInt(c.getColumnIndex("AverageMark")));
			st.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			st.setMarkEntered(c.getInt(c.getColumnIndex("MarkEntered")));
			st.setMaximumMark(c.getInt(c.getColumnIndex("MaximumMark")));
			st.setPortion(c.getInt(c.getColumnIndex("Portion")));
			st.setPortionName(c.getString(c.getColumnIndex("PortionName")));
			st.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			st.setSectionId(c.getInt(c.getColumnIndex("SectionId")));
			st.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			st.setSlipTestName(c.getString(c.getColumnIndex("SlipTestName")));
			st.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			stList.add(st);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return stList;
	}
	
	
	public List<Portion> selectPortion(int classId, int subjectId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from portion where ClassId="+classId+" and SubjectId="+subjectId , null);
		List<Portion> pList = new ArrayList<Portion>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			Portion p = new Portion();
			p.setClassId(c.getInt(c.getColumnIndex("ClassId")));
			p.setNewSubjectId(c.getInt(c.getColumnIndex("NewSubjectId")));
			p.setPortion(c.getString(c.getColumnIndex("Portion")));
			p.setPortionId(c.getInt(c.getColumnIndex("PortionId")));
			p.setSchoolId(c.getInt(c.getColumnIndex("SchoolId")));
			p.setSubjectId(c.getInt(c.getColumnIndex("SubjectId")));
			pList.add(p);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return pList;
	}
	
	public List<STMapping> selectSTMapping(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from stmapping", null);
		List<STMapping> stList = new ArrayList<STMapping>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			STMapping st = new STMapping();
			st.setSync(c.getInt(c.getColumnIndex("Sync")));
			st.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			st.setSlipTestIdLocal(c.getInt(c.getColumnIndex("SlipTestIdLocal")));
			st.setMarksEntered(c.getInt(c.getColumnIndex("MarksEntered")));
			stList.add(st);
			c.moveToNext();
		}
		c.close();
		//sqliteDatabase.close();
		return stList;
	}
	
	public List<STMapping> selectSTMap(){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select * from stmapping", null);
		List<STMapping> stList = new ArrayList<STMapping>();
		if(c.getCount()>0){
			c.moveToLast();
			STMapping st = new STMapping();
			st.setSync(c.getInt(c.getColumnIndex("Sync")));
			st.setSlipTestId(c.getInt(c.getColumnIndex("SlipTestId")));
			st.setSlipTestIdLocal(c.getInt(c.getColumnIndex("SlipTestIdLocal")));
			stList.add(st);
		}
		c.close();
		//sqliteDatabase.close();
		return stList;
	}
	
	public int selectSTMapEM(int slipTestId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		int eM = 0;
		Cursor c = sqliteDatabase.rawQuery("select * from stmapping where SlipTestIdLocal="+slipTestId, null);
		c.moveToFirst();
		if(!c.isAfterLast()){
			eM = c.getInt(c.getColumnIndex("MarksEntered"));
			c.moveToNext();
		}	
		c.close();
		//sqliteDatabase.close();
		return eM;
	}
	
	public void insertStudentAttendance(StudentAttendance sa){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "insert into studentattendance(SchoolId, ClassId, SectionId, StudentId, DateAttendance, TypeOfLeave) values("+
				sa.getSchoolId()+","+sa.getClassId()+","+sa.getSectionId()+","+sa.getStudentId()+",'"+sa.getDateAttendance()+"','"+sa.getTypeOfLeave()+"')";
		sqliteDatabase.execSQL(sql);

		ContentValues cv = new ContentValues();
		cv.put("SchoolId", sa.getSchoolId());
		cv.put("Action", "insert");
		cv.put("TableName", "studentattendance");
		cv.put("Query", sql);
		sqliteDatabase.insert("uploadsql", null, cv);
		//sqliteDatabase.close();
	}
	
	public void insertTempAttendance(TempAttendance ta){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("StudentId", ta.getStudentId());
		cv.put("ClassId", ta.getClassId());
		cv.put("SectionId", ta.getSectionId());
		cv.put("RollNoInClass", ta.getRollNoInClass());
		cv.put("Name", ta.getName());
		sqliteDatabase.insert("tempattendance", null, cv);
		//sqliteDatabase.close();
	}
	
	public void scoreUpdate(String examId, String subjectId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("MarkUploaded", 1);
		sqliteDatabase.update("exammarkinserted", cv, "ExamId=? AND SubjectId=?", new String[]{examId,subjectId});
		//sqliteDatabase.close();
	}
	
	public void attendanceInsert(String s){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("DateId", s);
		cv.put("AttendanceUploaded", 0);
		sqliteDatabase.insert("attendanceinserted", null, cv);
		//sqliteDatabase.close();
	}
	
	public void insertMarks(List<Marks> mList){
		sqliteDatabase = dbHelper.getWritableDatabase();	
		for(Marks m: mList){
			String sql = "insert into marks(SchoolId, ExamId, SubjectId, StudentId, Mark, Grade, SectionId) values("+
					m.getSchoolId()+","+m.getExamId()+","+m.getSubjectId()+","+m.getStudentId()+",'"+m.getMark()+"','0',"+m.getSectionId()+")";
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", m.getSchoolId());
			cv.put("Action", "insert");
			cv.put("TableName", "marks");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
		//sqliteDatabase.close();
	}
	
	public void updateMarks(List<Marks> mList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(Marks m: mList){
			String sql = "update marks set Mark='"+m.getMark()+"', Grade='"+m.getGrade()+"' where ExamId="+m.getExamId()+" and SubjectId="+m.getSubjectId()+" and StudentId="+m.getStudentId();
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", m.getSchoolId());
			cv.put("Action", "update");
			cv.put("TableName", "marks");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
	//	//sqliteDatabase.close();
	}
	
	public void insertActivityMark(List<ActivityMark> mList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(ActivityMark m: mList){
			String sql = "insert into activitymark(SchoolId, ExamId, SubjectId, StudentId, ActivityId, Mark) values("+
					m.getSchoolId()+","+m.getExamId()+","+m.getSubjectId()+","+m.getStudentId()+","+m.getActivityId()+",'"+m.getMark()+"')";
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", m.getSchoolId());
			cv.put("Action", "insert");
			cv.put("TableName", "activitymark");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
		//sqliteDatabase.close();
	}
	
	public void updateActivityMark(List<ActivityMark> amList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(ActivityMark am: amList){
			String sql = "update activitymark set Mark='"+am.getMark()+"' where ActivityId="+am.getActivityId()+" and StudentId="+am.getStudentId();
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", am.getSchoolId());
			cv.put("Action", "update");
			cv.put("TableName", "activitymark");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
		//sqliteDatabase.close();
	}
	
	public void updateSlipTestMark(List<SlipTestMark> stmList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		int slipTestId = 0;
		int schoolId = 0;
		for(SlipTestMark stm: stmList){
			slipTestId = stm.getSlipTestId();
			schoolId = stm.getSchoolId();
			String sql="update sliptestmark_"+schoolId+" set Mark='"+stm.getMark()+"' where SlipTestId="+stm.getSlipTestId()+" and StudentId="+stm.getStudentId();
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", stm.getSchoolId());
			cv.put("Action", "update");
			cv.put("TableName", "sliptestmark_"+schoolId);
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
		
		Cursor c = sqliteDatabase.rawQuery("select * from sliptestmark_"+schoolId+" where SlipTestId="+slipTestId, null);
		int minus = 0;
		int length = c.getCount();
		double totalMark = 0;
		double avgMark = 0;
		c.moveToFirst();	
		while(!c.isAfterLast()){
			if(!c.getString(c.getColumnIndex("Mark")).equals("") && Double.parseDouble(c.getString(c.getColumnIndex("Mark")))!=-1){
				totalMark = totalMark+Double.parseDouble(c.getString(c.getColumnIndex("Mark")));
			}else{
				minus+=1;				
			}
			c.moveToNext();
		}
		c.close();
		avgMark = totalMark/(length-minus);
		
		String sql2 = "update sliptest set AverageMark='"+avgMark+"' where SlipTestId="+slipTestId;
		sqliteDatabase.execSQL(sql2);
		
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", schoolId);
		cv.put("Action", "update");
		cv.put("TableName", "sliptest");
		cv.put("Query", sql2);
		sqliteDatabase.insert("uploadsql", null, cv);	
		//sqliteDatabase.close();
	}
	
	public void updateSTMark(List<SlipTestMark> stmList){
		int slipTestId = 0;
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(SlipTestMark stm: stmList){
			slipTestId = stm.getSlipTestId();
			String sql="update sliptestmark set Mark='"+stm.getMark()+"' where SlipTestId="+stm.getSlipTestId()+" and StudentId="+stm.getStudentId();
			sqliteDatabase.execSQL(sql);
		}
		Cursor c = sqliteDatabase.rawQuery("select * from sliptestmark where SlipTestId="+slipTestId, null);
		int minus = 0;
		int length = c.getCount();
		double totalMark = 0;
		double avgMark = 0;
		c.moveToFirst();	
		while(!c.isAfterLast()){
			if(!c.getString(c.getColumnIndex("Mark")).equals("") && Double.parseDouble(c.getString(c.getColumnIndex("Mark")))!=-1){
				totalMark = totalMark+Double.parseDouble(c.getString(c.getColumnIndex("Mark")));
			}else{
				minus+=1;				
			}
			c.moveToNext();
		}
		c.close();
		avgMark = totalMark/(length-minus);	
		String sql = "update sliptesttemp set AverageMark='"+avgMark+"' where SlipTestId="+slipTestId;
		sqliteDatabase.execSQL(sql);
		//sqliteDatabase.close();
	}
	
	public void insertSubActivityMark(List<SubActivityMark> mList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(SubActivityMark m: mList){
			String sql = "insert into subactivitymark(SchoolId, ExamId, SubjectId, StudentId, ActivityId, SubActivityId, Mark) values("+
					m.getSchoolId()+","+m.getExamId()+","+m.getSubjectId()+","+m.getStudentId()+","+m.getActivityId()+","+m.getSubActivityId()+",'"+m.getMark()+"')";
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", m.getSchoolId());
			cv.put("Action", "insert");
			cv.put("TableName", "subactivitymark");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
		//sqliteDatabase.close();
	}
	
	public void insertAck(int ackId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("AckId", ackId);
		sqliteDatabase.insert("ack", null, cv);
		//sqliteDatabase.close();
	}
	
	public void updateSubActivityMark(List<SubActivityMark> amList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(SubActivityMark am: amList){
			String sql = "update subactivitymark set Mark='"+am.getMark()+"' where SubActivityId="+am.getSubActivityId()+" and StudentId="+am.getStudentId();
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", am.getSchoolId());
			cv.put("Action", "update");
			cv.put("TableName", "subactivitymark");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
		//sqliteDatabase.close();
	}
	
	public void insertSlipTest(SlipTestt st,int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
			String sql = "insert into sliptest(SlipTestId,SchoolId,ClassId,SectionId,SubjectId,Portion,ExtraPortion,PortionName,MaximumMark,AverageMark,TestDate,MarkEntered)"
					+ " values("+st.getSlipTestId()+","+st.getSchoolId()+","+st.getClassId()+","+st.getSectionId()+","+st.getSubjectId()+","+st.getPortion()+",'"+
					st.getExtraPortion()+"','"+st.getPortionName()+"',"+st.getMaximumMark()+","+st.getAverageMark()+",'"+st.getTestDate()+"',"+st.getMarkEntered()+")";
			sqliteDatabase.execSQL(sql);
			
		Cursor c = sqliteDatabase.rawQuery("select * from sliptestmark_"+schoolId+" where SlipTestId="+st.getSlipTestId(), null);
		int minus = 0;
		int length = c.getCount();
		double totalMark = 0;
		double avgMark = 0;
		c.moveToFirst();	
		while(!c.isAfterLast()){
			if(!c.getString(c.getColumnIndex("Mark")).equals("") && Double.parseDouble(c.getString(c.getColumnIndex("Mark")))!=-1){
				totalMark = totalMark+Double.parseDouble(c.getString(c.getColumnIndex("Mark")));
			}else{
				minus+=1;				
			}
			c.moveToNext();
		}
		c.close();
		avgMark = totalMark/(length-minus);
		String sql2 = "update sliptest set AverageMark='"+avgMark+"' where SlipTestId="+st.getSlipTestId();
		sqliteDatabase.execSQL(sql2);
		
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", st.getSchoolId());
		cv.put("Action", "update");
		cv.put("TableName", "sliptest");
		cv.put("Query", sql2);
		sqliteDatabase.insert("uploadsql", null, cv);
		//sqliteDatabase.close();
	}
	public void editSlipTest(SlipTestt st){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "update sliptest set Portion='"+st.getPortion()+"',ExtraPortion='"+st.getExtraPortion()+"',MaximumMark="+st.getMaximumMark()+",TestDate='"+st.getTestDate()
				+ "',PortionName='"+st.getPortionName()+"' where SlipTestId="+st.getSlipTestId();
		sqliteDatabase.execSQL(sql);
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", st.getSchoolId());
		cv.put("Action", "update");
		cv.put("TableName", "sliptest");
		cv.put("Query", sql);
		sqliteDatabase.insert("uploadsql", null, cv);
	
		//sqliteDatabase.close();
	}
	public void editST(SlipTestt st){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "update sliptesttemp set Portion='"+st.getPortion()+"',ExtraPortion='"+st.getExtraPortion()+"',MaximumMark="+st.getMaximumMark()+",TestDate='"+st.getTestDate()
				+ "',PortionName='"+st.getPortionName()+"' where SlipTestId="+st.getSlipTestId();
		sqliteDatabase.execSQL(sql);
	
		//sqliteDatabase.close();
	}
	public void insertHomework(Homework h){
		sqliteDatabase = dbHelper.getWritableDatabase();
			String sql = "insert into homeworkmessage(HomeworkId,SchoolId,ClassId,SectionId,TeacherId,SubjectIDs,Homework,HomeworkDate)"
					+ " values("+h.getHomeworkId()+","+h.getSchoolId()+","+h.getClassId()+","+h.getSectionId()+","+h.getTeacherId()+",'"+h.getSubjectIDs()+
					"','"+h.getHomework()+"','"+h.getHomeworkDate()+"')";
			sqliteDatabase.execSQL(sql);
	
		//sqliteDatabase.close();
	}
	
	public void insertST(SlipTestt st){
		sqliteDatabase = dbHelper.getWritableDatabase();
			String sql = "insert into sliptesttemp(SchoolId,ClassId,SectionId,SubjectId,Portion,ExtraPortion,PortionName,MaximumMark,AverageMark,TestDate,MarkEntered)"
					+ " values("+st.getSchoolId()+","+st.getClassId()+","+st.getSectionId()+","+st.getSubjectId()+","+st.getPortion()+",'"
					+ st.getExtraPortion()+"','"+st.getPortionName()+"',"+st.getMaximumMark()+","+st.getAverageMark()+",'"+st.getTestDate()+"',"+st.getMarkEntered()+")";
			String sql2 = "insert into sliptest(SchoolId,ClassId,SectionId,SubjectId,Portion,ExtraPortion,PortionName,MaximumMark,AverageMark,TestDate,MarkEntered)"
					+ " values("+st.getSchoolId()+","+st.getClassId()+","+st.getSectionId()+","+st.getSubjectId()+","+st.getPortion()+",'"
					+st.getExtraPortion()+"','"+st.getPortionName()+"',"+st.getMaximumMark()+","+st.getAverageMark()+",'"+st.getTestDate()+"',"+st.getMarkEntered()+")";
			sqliteDatabase.execSQL(sql);
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", st.getSchoolId());
			cv.put("Action", "insert");
			cv.put("TableName", "sliptest");
			cv.put("Query", sql2);
			sqliteDatabase.insert("sliptestsql", null, cv);
			ContentValues cv2 = new ContentValues();
			cv2.put("Sync", 0);
			cv2.put("SlipTestId", 0);
			cv2.put("MarksEntered", 0);
			cv2.put("Opened", 0);
			sqliteDatabase.insert("stmapping", null, cv2);
		//sqliteDatabase.close();
	}
	
	public void insertSTMark(List<SlipTestMark> stmList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		int slipTestId = 0;
		for(SlipTestMark stm: stmList){
			slipTestId = stm.getSlipTestId();
			String sql = "insert into sliptestmark(SchoolId,ClassId,SectionId,SubjectId,SlipTestId,StudentId,Mark) values("+stm.getSchoolId()+","+stm.getClassId()+","+stm.getSectionId()+","
					+ stm.getSubjectId()+","+stm.getSlipTestId()+","+stm.getStudentId()+",'"+stm.getMark()+"')";
			sqliteDatabase.execSQL(sql);
			/*
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", stm.getSchoolId());
			cv.put("Action", "insert");
			cv.put("TableName", "sliptestmark_22");
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
			*/
		}
		Cursor c = sqliteDatabase.rawQuery("select * from sliptestmark where SlipTestId="+slipTestId, null);
		int length = c.getCount();
		int minus = 0;
		double totalMark = 0;
		double avgMark = 0;
		c.moveToFirst();	
		while(!c.isAfterLast()){
			if(!c.getString(c.getColumnIndex("Mark")).equals("") && Double.parseDouble(c.getString(c.getColumnIndex("Mark")))!=-1){
				totalMark = totalMark+Double.parseDouble(c.getString(c.getColumnIndex("Mark")));
			}else{
				minus+=1;				
			}
			c.moveToNext();
		}
		c.close();
		avgMark = totalMark/(length-minus);
		
		String sql2 = "update sliptesttemp set AverageMark='"+avgMark+"' where SlipTestId="+slipTestId;
		sqliteDatabase.execSQL(sql2);
		//sqliteDatabase.close();
	}
	
	public void insertSlipTestMark(List<SlipTestMark> stmList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		for(SlipTestMark stm: stmList){
			String sql = "insert into sliptestmark_"+stm.getSchoolId()+"(SchoolId,ClassId,SectionId,SubjectId,SlipTestId,StudentId,Mark) values("+stm.getSchoolId()+","+stm.getClassId()+","+stm.getSectionId()+","
					+ stm.getSubjectId()+","+stm.getSlipTestId()+","+stm.getStudentId()+",'"+stm.getMark()+"')";
			sqliteDatabase.execSQL(sql);
			
			ContentValues cv = new ContentValues();
			cv.put("SchoolId", stm.getSchoolId());
			cv.put("Action", "insert");
			cv.put("TableName", "sliptestmark_"+stm.getSchoolId());
			cv.put("Query", sql);
			sqliteDatabase.insert("uploadsql", null, cv);
		}
		//sqliteDatabase.close();
	}
	public void insertDownloadSql(List<DownloadSql> dwnSqlList){
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO downloadsql (AckId,Action,TableName,Query,SchoolId) VALUES (?,?,?,?,?)";
		sqliteDatabase.beginTransaction();
		SQLiteStatement stmt = sqliteDatabase.compileStatement(sql);
		for(DownloadSql dwnSql: dwnSqlList){
			stmt.bindLong(1, dwnSql.getAckId());
			stmt.bindString(2, dwnSql.getAction());
			stmt.bindString(3, dwnSql.getTableName());
			stmt.bindString(4, dwnSql.getQuery());
			stmt.bindLong(5, dwnSql.getSchoolId());
			stmt.execute();
			stmt.clearBindings();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		//sqliteDatabase.close();
	}
	
	public void insertDwnAutoId(DwnAutoId dnsql){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("AckId", dnsql.getAckId());
		cv.put("Action", dnsql.getAction());
		cv.put("TableName", dnsql.getTableName());
		cv.put("Query", dnsql.getQuery());
		cv.put("SchoolId", dnsql.getSchoolId());
		cv.put("CreatedAt", dnsql.getCreatedAt());
		cv.put("IncrementId", dnsql.getIncrementId());
		sqliteDatabase.insert("dwnautoid", null, cv);
		//sqliteDatabase.close();
	}
	
	public void updateTemp(Temp t){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("ClassId", t.getClassId());
		cv.put("SectionId", t.getSectionId());
		cv.put("SectionName", t.getSectionName());
		cv.put("TeacherId", t.getTeacherId());
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateTem(String name, int value){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(name, value);
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateDeviceId(String id){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("DeviceId", id);
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void upIsSync(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("IsSync", 1);
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void dwnIsSync(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("IsSync", 0);
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateToday(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Today", 1);
		cv.put("Yesterday", 0);
		cv.put("Otherday", 0);
		sqliteDatabase.update("tempday", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateYesterday(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Today", 0);
		cv.put("Yesterday", 1);
		cv.put("Otherday", 0);
		sqliteDatabase.update("tempday", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateOtherday(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Today", 0);
		cv.put("Yesterday", 0);
		cv.put("Otherday", 1);
		//cv.put("OtherDate", otherdate);
		sqliteDatabase.update("tempday", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateTodayHW(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Today", 1);
		cv.put("Yesterday", 0);
		cv.put("Otherday", 0);
		sqliteDatabase.update("temphw", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateYesterdayHW(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Today", 0);
		cv.put("Yesterday", 1);
		cv.put("Otherday", 0);
		sqliteDatabase.update("temphw", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateOtherdayHW(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Today", 0);
		cv.put("Yesterday", 0);
		cv.put("Otherday", 1);
	//	cv.put("OtherDate", otherdate);
		sqliteDatabase.update("temphw", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateSecSubClas(Temp t){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("CurrentSection", t.getCurrentSection());
		cv.put("CurrentSubject", t.getCurrentSubject());
		cv.put("CurrentClass", t.getCurrentClass());
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateExamId(int id){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("ExamId", id);
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateActivityId(int id){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("ActivityId", id);
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateSubActivityId(int id){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("SubActivityId", id);
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateSlipTestId(int id){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("SlipTestId", id);
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateSyncTime(String time){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("SyncTime", time);
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateSchoolId(int schoolId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("SchoolId", schoolId);
		sqliteDatabase.update("temp", cv, "id=1", null);
		//sqliteDatabase.close();
	}
	
	public void updateSTMapping(int slipTestId, int slipTestIdLocal){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("SlipTestId", slipTestId);
		cv.put("Sync", 1);
		sqliteDatabase.update("stmapping", cv, "SlipTestIdLocal="+slipTestIdLocal, null);
		//sqliteDatabase.close();
	}
	public void updateHWMapping(int homeworkId, int homeworkIdLocal){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("HomeworkId", homeworkId);
		cv.put("Sync", 1);
		sqliteDatabase.update("hwmapping", cv, "HomeworkIdLocal="+homeworkIdLocal, null);
		//sqliteDatabase.close();
	}
	
	public void updateSTMap(int slipTestId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("MarksEntered", 1);
		sqliteDatabase.update("stmapping", cv, "SlipTestIdLocal="+slipTestId, null);
		//sqliteDatabase.close();
	}
	
	public int selectSTOpened(int slipTestId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select Opened from stmapping where SlipTestIdLocal="+slipTestId, null);
		c.moveToFirst();
		int b = c.getInt(c.getColumnIndex("Opened"));
		//sqliteDatabase.close();
		return b;
	}
	public int selectHWOpened(int homeworkId){
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqliteDatabase.rawQuery("select Opened from hwmapping where HomeworkIdLocal="+homeworkId, null);
		c.moveToFirst();
		int b = c.getInt(c.getColumnIndex("Opened"));
		//sqliteDatabase.close();
		return b;
	}
	
	public void upSTOpened(int slipTestId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Opened", 1);
		sqliteDatabase.update("stmapping", cv, "SlipTestIdLocal="+slipTestId, null);
		//sqliteDatabase.close();
	}
	public void upHWOpened(int homeworkId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Opened", 1);
		sqliteDatabase.update("hwmapping", cv, "HomeworkIdLocal="+homeworkId, null);
		//sqliteDatabase.close();
	}
	
	public void dwnSTOpened(int slipTestId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Opened", 0);
		sqliteDatabase.update("stmapping", cv, "SlipTestIdLocal="+slipTestId, null);
		//sqliteDatabase.close();
	}
	public void dwnHWOpened(int homeworkId){
		sqliteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Opened", 0);
		sqliteDatabase.update("hwmapping", cv, "HomeworkIdLocal="+homeworkId, null);
		//sqliteDatabase.close();
	}
	public void clearTempAttendance(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		sqliteDatabase.execSQL("delete from tempattendance");
		//sqliteDatabase.close();
	}
	
	public void deleteOtherTables(){
		sqliteDatabase = dbHelper.getWritableDatabase();
		sqliteDatabase.delete("downloadsql", null, null);
		sqliteDatabase.delete("dwnautoid", null, null);
		sqliteDatabase.delete("exmavg", null, null);
		sqliteDatabase.delete("hwmessage", null, null);
		sqliteDatabase.delete("homeworksql", null, null);
		sqliteDatabase.delete("hwmapping", null, null);
		sqliteDatabase.delete("sliptestmark", null, null);
		sqliteDatabase.delete("sliptestsql", null, null);
		sqliteDatabase.delete("sliptesttemp", null, null);
		sqliteDatabase.delete("stavg", null, null);
		sqliteDatabase.delete("stmapping", null, null);
		sqliteDatabase.delete("tempattendance", null, null);
		sqliteDatabase.delete("uploadsql", null, null);
		//sqliteDatabase.close();
	}
	
}

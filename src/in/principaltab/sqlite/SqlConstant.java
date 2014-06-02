package in.principaltab.sqlite;

public interface SqlConstant {
	
	public static String DATABASE_NAME = "school.db";
	public static int DATABASE_VERSION = 6;
	
	public static String CREATE_CLASS = "CREATE TABLE class(SchoolId INT, ClassId INT PRIMARY KEY,"
			+ "ClassName TEXT, ClassType TEXT)";
		
	public static String CREATE_SCHOOL = "CREATE TABLE school(SchoolId INT PRIMARY KEY,"
		+ "SchoolName TEXT, Website TEXT, ShortenedSchoolName TEXT, SenderID TEXT, ContactPersonName TEXT,"
		+ "SchoolAdminUserName TEXT, SchoolAdminPassword TEXT, Address TEXT, address_short TEXT, Landline TEXT, Mobile TEXT, Mobile2 TEXT, Email TEXT, City TEXT, State TEXT,"
		+ "District TEXT, Pincode TEXT, CreationDateTime TEXT, LastLoginTime TEXT, IPAddress TEXT, NumberofMobiles INT, RouteId INT, Locked INT,"
		+ "PrincipalTeacherId INT, PathtoOpen TEXT, Syllabus TEXT, NumberofStudents INT, Launched INT, ClassIDs TEXT, CCEClassIDs TEXT, DateTimeRecordInserted DATETIME)";
		
	public static String CREATE_SECTION = "CREATE TABLE section(SchoolId INT, SectionId INT PRIMARY KEY,"
		+ "ClassId INT, SectionName TEXT, ClassTeacherId INT, DateTimeRecordInserted DATETIME)";
		
	public static String CREATE_STUDENT_ATTENDANCE = "CREATE TABLE studentattendance(SchoolId INT,"
		+ "ClassId INT, SectionId INT, StudentId INT, DateAttendance TEXT, TypeOfLeave TEXT, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_TEMP_ATTENDANCE = "CREATE TABLE tempattendance(StudentId INT, ClassId INT,"
		+  "SectionId INT, RollNoInClass INT, Name TEXT)";
		
	public static String CREATE_STUDENTS = "CREATE TABLE students(SOWSID TEXT, StudentId INT PRIMARY KEY, SchoolId INT,"
		+ "ClassId INT, SectionId INT, AdmissionNo TEXT, RollNoInClass INT, Username TEXT, Password TEXT, Image TEXT, Name TEXT,"
		+ "FatherName TEXT, MotherName TEXT, DateOfBirth TEXT, Gender TEXT, Email TEXT, Mobile1 TEXT, Mobile2 TEXT, Pincode TEXT, Address TEXT,"
		+ "TransportationTypeId INT, Community TEXT, Income INT, IsLoggedIn INT, Locked INT, NoSms INT, CreationDateTime TEXT, LastLoginTime TEXT,"
		+ "LoginCount INT, FeedbackSkip INT, NotificationsRead TEXT, IPAddress TEXT, DateTimeRecordInserted DATETIME)";
		
	public static String CREATE_SUBJECTS = "CREATE TABLE subjects(SubjectId INT, SubjectName TEXT, DateTimeRecordInserted DATETIME)";
		
	public static String CREATE_SUBJECT_TEACHERS = "CREATE TABLE subjectteachers(SchoolId INT, ClassId INT,"
		+ "SectionId INT, SubjectId INT, SubjectGroupId INT, TeacherId INT, DateTimeRecordInserted DATETIME)";
		
	public static String CREATE_TEACHER = "CREATE TABLE teacher(SOWTID TEXT, TeacherId INT PRIMARY KEY, Image TEXT,"
		+ "Username TEXT, Password TEXT,SchoolId INT, Name TEXT, Mobile TEXT, Qualification TEXT, Address TEXT, DateOfJoining TEXT, Gender TEXT, Email TEXT, Pincode INT,"
		+ "TransportationTypeId INT, Community TEXT, CreationDateTime TEXT, LastLoginTime TEXT, IPAddress TEXT, Locked INT, TabUser INT, TabPass INT, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_EXAMS = "CREATE TABLE exams(SchoolId INT, ClassId INT, ExamId INT, SubjectIDs TEXT, ExamName TEXT, OrderId INT, Percentage TEXT,"
		+ "TimeTable TEXT, Portions TEXT, FileName TEXT, GradeSystem INT, Term INT, MarkUploaded INT, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_SUBJECT_EXAMS = "CREATE TABLE subjectexams(SchoolId INT, ClassId INT, ExamId INT, SubjectId INT, TimeTable TEXT,"
		+ "Session TEXT, MaximumMark INT, FailMark INT, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_MARKS = "CREATE TABLE marks(SchoolId INT, ExamId INT, SubjectId INT, StudentId INT, Mark TEXT, Grade TEXT, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_TEMP = "CREATE TABLE temp(id INT PRIMARY KEY, DeviceId TEXT, SchoolId INT, ClassId INT, SectionId INT, SectionName TEXT,"
		+	"TeacherId INT, CurrentSection INT, CurrentSubject INT, CurrentClass INT, ExamId INT, ActivityId INT, SubActivityId INT, SlipTestId INT, SyncTime TEXT, IsSync INT)";
	
	public static String CREATE_TEMP_DAY = "CREATE TABLE tempday(id INT PRIMARY KEY, OtherDate DATE, Today INT, Yesterday INT, Otherday INT)";
	
	public static String CREATE_ACTIVITY = "CREATE TABLE activity(ActivityId INT, SchoolId INT, ClassId INT, SectionId INT, ExamId INT, SubjectId INT, RubrixId INT, ActivityName TEXT, "
			+ "MaximumMark INT, Weightage REAL, SubActivity INT, Calculation INT, ActivityAvg INT DEFAULT 0, CompleteEntry INT DEFAULT 0, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_ACTIVITY_MARK = "CREATE TABLE activitymark(SchoolId INT, ExamId INT, SubjectId INT, StudentId INT, ActivityId INT, Mark TEXT, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_SUB_ACTIVITY = "CREATE TABLE subactivity(SubActivityId INT, SchoolId INT, ClassId INT, SectionId INT, ExamId INT, SubjectId INT,"
		+ "ActivityId INT, SubActivityName TEXT, MaximumMark INT, Weightage INT, Calculation INT, SubActivityAvg INT DEFAULT 0, CompleteEntry INT DEFAULT 0, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_SUB_ACTIVITY_MARK = "CREATE TABLE subactivitymark(SchoolId INT, ExamId INT, SubjectId INT, StudentId INT, ActivityId INT, SubActivityId INT," +
			"Mark TEXT, Description TEXT, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_UPLOAD_SQL = "CREATE TABLE uploadsql(SchoolId INT, Action TEXT, TableName TEXT, Query TEXT, CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP)";
	
	public static String CREATE_UPLOAD_ST = "CREATE TABLE sliptestsql(SlipTestId INTEGER PRIMARY KEY AUTOINCREMENT, SchoolId INT, Action TEXT, TableName TEXT, Query TEXT, CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP)";
	
	public static String CREATE_HOMEWORK_ST = "CREATE TABLE homeworksql(HomeworkId INTEGER PRIMARY KEY AUTOINCREMENT, SchoolId INT, Action TEXT, TableName TEXT, Query TEXT, SectionId INT, HomeworkDate TEXT)";
	
	public static String CREATE_DOWNLOAD_SQL = "CREATE TABLE downloadsql(AckId INT, SchoolId INT, Action TEXT, TableName TEXT, Query TEXT, CreatedAt TEXT)";
	
	public static String CREATE_DOWNLOAD_AUTO_ID = "CREATE TABLE dwnautoid(AckId INT, SchoolId INT, Action TEXT, TableName TEXT, Query TEXT, CreatedAt TEXT, IncrementId INT)";
	
	public static String CREATE_PORTION = "CREATE TABLE portion(PortionId INT, SchoolId INT, ClassId INT, SubjectId INT, NewSubjectId INT, Portion TEXT, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_SLIPTEST = "CREATE TABLE sliptest(SlipTestId INT, SchoolId INT, ClassId INT, SectionId INT, SlipTestName TEXT, IsActivity INT, Grade INT, Count INT,"+
			" SubjectId INT,NewSubjectId, Portion INT, ExtraPortion TEXT, PortionName TEXT, MaximumMark INT, AverageMark REAL, TestDate TEXT, MarkEntered INT,EmployeeId INT,"+
			" Weightage REAL, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_SLIPTEST_T = "CREATE TABLE sliptesttemp(SlipTestId INTEGER PRIMARY KEY AUTOINCREMENT, SchoolId INT, ClassId INT, SectionId INT, SlipTestName TEXT,"+
			" SubjectId INT, Portion INT,ExtraPortion TEXT, PortionName TEXT, MaximumMark INT, AverageMark INT, TestDate TEXT, MarkEntered INT)";
	
	public static String CREATE_STMAPPING = "CREATE TABLE stmapping(SlipTestIdLocal INTEGER PRIMARY KEY AUTOINCREMENT, SlipTestId INT, Sync INT, MarksEntered INT, Opened INT)";
	
	public static String CREATE_SLIPTEST_MARK = "CREATE TABLE sliptestmark_22(MarkId INTEGER PRIMARY KEY, SchoolId INT, ClassId INT, SectionId INT, SubjectId INT, NewSubjectId INT, SlipTestId INT, StudentId INT, Mark TEXT, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_SLIPTEST_M = "CREATE TABLE sliptestmark(MarkId INTEGER PRIMARY KEY AUTOINCREMENT, SchoolId INT, ClassId INT, SectionId INT, SubjectId INT, NewSubjectId INT, SlipTestId INT, StudentId INT, Mark TEXT)";
	
	public static String CREATE_HOMEWORK = "CREATE TABLE homeworkmessage(HomeworkId INT, SchoolId INT, ClassId INT, SectionId INT, TeacherId INT, MessageFrom TEXT, MessageVia TEXT, SubjectIDs TEXT,"+
			"Homework TEXT, HomeworkDate TEXT, DateTimeRecordInserted DATETIME)";
	
	public static String CREATE_HW = "CREATE TABLE hwmessage(HomeworkId INTEGER PRIMARY KEY AUTOINCREMENT, SchoolId INT, ClassId INT, SectionId INT, TeacherId INT, MessageFrom TEXT, MessageVia TEXT, SubjectIDs TEXT,"+
			"Homework TEXT, HomeworkDate TEXT)";
	
	public static String CREATE_HWMAPPING = "CREATE TABLE hwmapping(HomeworkIdLocal INTEGER PRIMARY KEY AUTOINCREMENT, HomeworkId INT, Sync INT, Opened INT)";

	public static String CREATE_TEMP_HW = "CREATE TABLE temphw(id INT PRIMARY KEY, OtherDate DATE, Today INT, Yesterday INT, Otherday INT)";
	
	public static String CREATE_EXMAVG = "CREATE TABLE exmavg(ClassId INT, SectionId INT, SubjectId INT, ExamId INT, ExamAvg INT DEFAULT 0, CompleteEntry INT DEFAULT 0)";
	
	public static String CREATE_STAVG = "CREATE TABLE stavg(SectionId INT, SubjectId INT, SlipTestAvg INT)";

}

package in.principaltab.sqlite;

public class UploadSql {
	private int schoolId;
	private String action;
	private String tableName;
	private String query;
	private String createdAt;
	
	public UploadSql(){
		
	}
	
	public UploadSql(String tableName, String action, String query){
		this.tableName = tableName;
		this.action = action;
		this.query = query;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public int getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

}

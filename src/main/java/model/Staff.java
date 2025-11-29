package model;

public class Staff {
	int staffId;
	String staffName;
	String staffEmail;
	
	public Staff() {};
	
	public Staff(int staffId, String staffName, String staffEmail) {
		this.staffId = staffId;
		this.staffName = staffName;
		this.staffEmail = staffEmail;
	}

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffEmail() {
		return staffEmail;
	}

	public void setStaffEmail(String staffEmail) {
		this.staffEmail = staffEmail;
	};
}

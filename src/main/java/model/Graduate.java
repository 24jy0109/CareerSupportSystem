package model;

public class Graduate {
	private int graduateId;
	private Company comapany;
	private Staff staff;
	private Course course;
	private String graduateStudentNumber;
	private String graduateName;
	private String graduateEmail;
	private String otherInfo;
	private String graduateJobCategory;

	public Graduate() {

	}

	public Graduate(String graduateStudentNumber, Company comapany, Staff staff, Course course,
			String graduateName, String graduateEmail, String otherInfo, String graduateJobCategory) {

		this.graduateStudentNumber = graduateStudentNumber;
		this.comapany = comapany;
		this.staff = staff;
		this.course = course;
		this.graduateName = graduateName;
		this.graduateEmail = graduateEmail;
		this.otherInfo = otherInfo;
		this.graduateJobCategory = graduateJobCategory;
	}

	public String getGraduateStudentNumber() {
		return graduateStudentNumber;
	}
	
	public void setGraduateStudentNumber(String graduateStudentNumber) {
		this.graduateStudentNumber = graduateStudentNumber;
	}

	public Company getComapany() {
		return comapany;
	}

	public void setComapany(Company comapany) {
		this.comapany = comapany;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getGraduateName() {
		return graduateName;
	}

	public void setGraduateName(String graduateName) {
		this.graduateName = graduateName;
	}

	public String getGraduateEmail() {
		return graduateEmail;
	}

	public void setGraduateEmail(String graduateEmail) {
		this.graduateEmail = graduateEmail;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public String getGraduateJobCategory() {
		return graduateJobCategory;
	}

	public void setGraduateJobCategory(String graduateJobCategory) {
		this.graduateJobCategory = graduateJobCategory;
	}

}

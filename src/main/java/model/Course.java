package model;

public class Course {
	private String courseCode;
	private String courseName;
	private int courseTerm;

	public Course() {
	};

	public Course(String courseCode, String courseName, int courseTerm) {
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.courseTerm = courseTerm;
	};

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCourseTerm() {
		return courseTerm;
	}

	public void setCourseTerm(int courseTerm) {
		this.courseTerm = courseTerm;
	}
}

package model;

import java.util.ArrayList;

public class Student {
	private String studentNumber;
	private Course course;
	private ArrayList<Request> requests;
	private String studentName;
	private String studentEmail;
	
	public Student(){};
	
	public Student(String studentNumber, Course course, ArrayList<Request> requests, String studentName, String studentEmail){
		this.studentNumber = studentNumber;
		this.course = course;
		this.requests = requests;
		this.studentName = studentName;
		this.studentEmail = studentEmail;
	}
	
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public ArrayList<Request> getRequests() {
		return requests;
	}
	public void setRequests(ArrayList<Request> requests) {
		this.requests = requests;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentEmail() {
		return studentEmail;
	}
	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}
}

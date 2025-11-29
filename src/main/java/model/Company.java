package model;

import java.util.ArrayList;

public class Company {
	private int companyId;
	private String companyName;
	private ArrayList<Event> events;
	private ArrayList<Graduate> graduates;
	private ArrayList<Student> requestStudents;

	public Company() {

	}

	public Company(int companyId, String companyName, ArrayList<Event> events, ArrayList<Graduate> graduates,
			ArrayList<Student> requestStudents) {
		this.companyId = companyId;
		this.companyName = companyName;
		this.events = events;
		this.graduates = graduates;
		this.graduates = graduates;
		this.requestStudents = requestStudents;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public ArrayList<Graduate> getGraduates() {
		return graduates;
	}

	public void setGraduates(ArrayList<Graduate> graduates) {
		this.graduates = graduates;
	}

	public ArrayList<Student> getRequestStudents() {
		return requestStudents;
	}

	public void setRequestStudents(ArrayList<Student> requestStudents) {
		this.requestStudents = requestStudents;
	}

}

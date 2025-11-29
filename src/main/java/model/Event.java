package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Event {
	private int eventId;
	private Company company;
	private Staff staff;
	private ArrayList<Graduate> joinGraduates;
	private ArrayList<Student> joinstudents;
	private LocalDateTime eventStartTime;
	private LocalDateTime eventEndTime;
	private String eventPlace;
	private int eventCapacity;
	private int eventProgress;
	private String eventOtherInfo;

	public Event() {

	}

	public Event(int eventId, Company company, Staff staff, ArrayList<Graduate> joinGraduates, ArrayList<Student> joinStudents,
			LocalDateTime eventStartTime, LocalDateTime eventEndTime, String eventPlace, int eventCapacity, int eventProgress,
			String eventOtherInfo) {
		this.eventId = eventId;
		this.company = company;
		this.staff = staff;
		this.joinGraduates = joinGraduates;
		this.joinstudents = joinStudents;
		this.eventStartTime = eventStartTime;
		this.eventEndTime = eventEndTime;
		this.eventPlace = eventPlace;
		this.eventCapacity = eventCapacity;
		this.eventProgress = eventProgress;
		this.eventOtherInfo = eventOtherInfo;
	}
	
	public int getEventId() {
		return eventId;
	}
	
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public ArrayList<Graduate> getJoinGraduates() {
		return joinGraduates;
	}

	public void setJoinGraduates(ArrayList<Graduate> joinGraduates) {
		this.joinGraduates = joinGraduates;
	}

	public ArrayList<Student> getJoinstudents() {
		return joinstudents;
	}

	public void setJoinstudents(ArrayList<Student> joinstudents) {
		this.joinstudents = joinstudents;
	}

	public LocalDateTime getEventStartTime() {
		return eventStartTime;
	}

	public void setEventStartTime(LocalDateTime eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public LocalDateTime getEventEndTime() {
		return eventEndTime;
	}

	public void setEventEndTime(LocalDateTime eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public String getEventPlace() {
		return eventPlace;
	}

	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}

	public int getEventCapacity() {
		return eventCapacity;
	}

	public void setEventCapacity(int eventCapacity) {
		this.eventCapacity = eventCapacity;
	}
	
	public int getEventProgress() {
		return eventProgress;
	}

	public void setEventProgress(int eventProgress) {
		this.eventProgress = eventProgress;
	}

	public String getEventOtherInfo() {
		return eventOtherInfo;
	}

	public void setEventOtherInfo(String eventOtherInfo) {
		this.eventOtherInfo = eventOtherInfo;
	}
}

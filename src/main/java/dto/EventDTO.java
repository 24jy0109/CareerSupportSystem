package dto;

import java.util.List;

import model.Event;
import model.Graduate;
import model.Staff;
import model.Student;

public class EventDTO {
	private Event event;
	private List<Staff> staffs;
	private List<Graduate> graduates;
	private List<Student> students;
	private int joinStudentCount;
	private Boolean joinAvailability;
	private Boolean studentRequested;

	public EventDTO() {
	}

	public EventDTO(Event event, List<Staff> staffs, List<Graduate> graduates) {
		this.event = event;
		this.staffs = staffs;
		this.graduates = graduates;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<Staff> getStaffs() {
		return staffs;
	}

	public void setStaffs(List<Staff> staffs) {
		this.staffs = staffs;
	}

	public List<Graduate> getGraduates() {
		return graduates;
	}

	public void setGraduates(List<Graduate> graduates) {
		this.graduates = graduates;
	}
	
	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public int getJoinStudentCount() {
		return joinStudentCount;
	}

	public void setJoinStudentCount(int joinStudentCount) {
		this.joinStudentCount = joinStudentCount;
	}
	
	public Boolean getJoinAvailability() {
		return joinAvailability;
	}

	public void setJoinAvailability(Boolean joinAvailability) {
		this.joinAvailability = joinAvailability;
	}
	
	public Boolean getStudentRequested() {
		return studentRequested;
	}

	public void setStudentRequested(Boolean studentRequested) {
		this.studentRequested = studentRequested;
	}
}

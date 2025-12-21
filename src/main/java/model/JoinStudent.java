package model;

public class JoinStudent {
	private Student student;
	private Event event;
	private boolean joinAvailability;

	public JoinStudent() {

	}

	public JoinStudent(Student student, Event event, boolean joinAvailability) {
		this.student = student;
		this.event = event;
		this.joinAvailability = joinAvailability;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public boolean isJoinAvailability() {
		return joinAvailability;
	}

	public void setJoin_availability(boolean joinAvailability) {
		this.joinAvailability = joinAvailability;
	}

}

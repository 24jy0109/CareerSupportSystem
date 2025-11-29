package model;

public class JoinStudent {
	private Student student;
	private Event event;
	private boolean join_availability;

	public JoinStudent() {

	}

	public JoinStudent(Student student, Event event, boolean join_availability) {
		this.student = student;
		this.event = event;
		this.join_availability = join_availability;
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

	public boolean isJoin_availability() {
		return join_availability;
	}

	public void setJoin_availability(boolean join_availability) {
		this.join_availability = join_availability;
	}

}

package dto;

import java.util.List;

import model.Event;
import model.Graduate;
import model.Staff;

public class EventDTO {
	private Event event;
	private List<Staff> staffs;
	private List<Graduate> graduates;
	
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


}

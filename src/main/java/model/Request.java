package model;

import java.time.LocalDateTime;

public class Request {
	private Student student;
	private Company company;
	private LocalDateTime requestTime;
	
	public Request() {
		
	}
	
	public Request(Student student, Company company, LocalDateTime requestTime) {
		this.student = student;
		this.company = company;
		this.requestTime = requestTime;
	}

	public Student getstudent() {
		return student;
	}

	public void setstudent(Student student) {
		this.student = student;
	}

	public Company getcompany() {
		return company;
	}

	public void setcompany(Company company) {
		this.company = company;
	}

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}
	
	
}



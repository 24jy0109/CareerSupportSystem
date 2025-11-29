package model;

import java.time.LocalDateTime;

public class Request {
	private int studentId;
	private int companyId;
	private LocalDateTime requestTime;
	
	public Request() {
		
	}
	
	public Request(int studentId,LocalDateTime requestTime) {
		this.studentId = studentId;
		this.requestTime = requestTime;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}
	
	
}



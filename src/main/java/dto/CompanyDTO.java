package dto;

import java.util.List;

import model.Company;
import model.Staff;

public class CompanyDTO {

	private Company company;
	private List<Staff> staffs;
	private String eventProgress;
	private String isRequest;
	private int graduateCount;
	private int requestCount;

	public CompanyDTO() {
	}

	public CompanyDTO(Company company, List<Staff> staffs, String eventProgress, String isRequest, int graduateCount,
			int requestCount) {
		this.company = company;
		this.staffs = staffs;
		this.eventProgress = eventProgress;
		this.isRequest = isRequest;
		this.graduateCount = graduateCount;
		this.requestCount = requestCount;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<Staff> getStaffs() {
		return staffs;
	}

	public void setStaffs(List<Staff> staffs) {
		this.staffs = staffs;
	}

	public String getEventProgress() {
		return eventProgress;
	}

	public void setEventProgress(String eventProgress) {
		this.eventProgress = eventProgress;
	}

	public String getIsRequest() {
		return isRequest;
	}

	public void setIsRequest(String isRequest) {
		this.isRequest = isRequest;
	}

	public int getGraduateCount() {
		return graduateCount;
	}

	public void setGraduateCount(int graduateCount) {
		this.graduateCount = graduateCount;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}
}

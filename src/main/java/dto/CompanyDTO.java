package dto;

import model.Company;

public class CompanyDTO {

	private Company company;
    private String eventProgress;
    private String isRequest;
    private int graduateCount;

	public CompanyDTO() {
	}

    public CompanyDTO(Company company, String eventProgress, String isRequest, int graduateCount) {
		this.company = company;
        this.eventProgress = eventProgress;
        this.isRequest = isRequest;
        this.graduateCount = graduateCount;
    }

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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
}

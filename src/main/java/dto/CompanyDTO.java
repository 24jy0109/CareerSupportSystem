package dto;

public class CompanyDTO {

	private int companyId;
	private String companyName;
    private String eventProgress;
    private String isRequest;
    private int graduateCount;

	public CompanyDTO() {
	}

    public CompanyDTO(int companyId, String eventProgress, String companyName, String isRequest, int graduateCount) {
		this.companyId = companyId;
		this.companyName = companyName;
        this.eventProgress = eventProgress;
        this.isRequest = isRequest;
        this.graduateCount = graduateCount;
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

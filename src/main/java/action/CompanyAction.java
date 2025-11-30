package action;

import java.util.ArrayList;
import java.util.List;

import dao.CompanyDBAccess;
import dto.CompanyDTO;

public class CompanyAction {
	public List<CompanyDTO> execute(String[] data) throws Exception {
		String action = data[0];
		List<CompanyDTO> companies = new ArrayList<CompanyDTO>();
		switch(action) {
		case "CompanyList":
			CompanyDBAccess companyDBA = new CompanyDBAccess();
			companies = companyDBA.searchStudentCompanies("", "", data[1]);
			break;
		}
		return companies;
	}
}

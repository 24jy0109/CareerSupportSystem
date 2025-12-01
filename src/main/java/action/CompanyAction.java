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
			if (data[1].isEmpty()) {
				companies = companyDBA.searchStaffCompanies(data[2]);
				System.out.println(data[2]);
				break;
			} else {
				companies = companyDBA.searchStudentCompanies(data[2], data[1]);
				break;
			}
		}
		return companies;
	}
}

package action;

import java.util.ArrayList;
import java.util.List;

import dao.CompanyDBAccess;
import dto.CompanyDTO;

public class CompanyAction {
	public List<CompanyDTO> execute(String[] data) throws Exception {
		// data[0] アクション
		// data[1] 学籍番号
		// data[2] CompanyName, CompanyId
		
		String action = data[0];
		List<CompanyDTO> companies = new ArrayList<CompanyDTO>();
		CompanyDBAccess companyDBA = new CompanyDBAccess();
		switch(action) {
		case "CompanyList":
			if (data[1].isEmpty()) {
				companies = companyDBA.searchStaffCompanies(data[2]);
				System.out.println(data[2]);
			} else {
				companies = companyDBA.searchStudentCompanies(data[2], data[1]);
			}
			break;
		case "CompanyDetail":
			companies = companyDBA.SearchCompanyDetail(Integer.parseInt(data[2]), data[1]);
			break;
		}
		return companies;
	}
}

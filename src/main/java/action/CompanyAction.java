package action;

import java.util.ArrayList;

import dao.CompanyDBAccess;
import model.Company;

public class CompanyAction {
	public ArrayList<Company> execute(String[] data) throws Exception {
		String action = data[0];
		ArrayList<Company> companies = new ArrayList<Company>();
		switch(action) {
		case "CompanyList":
			CompanyDBAccess companyDBA = new CompanyDBAccess();
//			companyDBA.searchCompanies(name, sort, studentNumber);
			break;
		}
		return companies;
	}
}

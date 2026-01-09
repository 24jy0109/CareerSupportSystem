package action;

import java.util.ArrayList;
import java.util.List;

import dao.CompanyDBAccess;
import dao.StaffDBAcess;
import dto.CompanyDTO;
import model.Staff;

public class CompanyAction {
	public List<CompanyDTO> execute(String[] data) throws Exception {
		// data[0] アクション
		// data[1] 学籍番号
		// data[2] CompanyName, CompanyId

		String action = data[0];
		List<CompanyDTO> companies = new ArrayList<CompanyDTO>();
		CompanyDBAccess companyDBA = new CompanyDBAccess();
		switch (action) {
		case "CompanyList":
			if (data[1].isEmpty()) {
				companies = companyDBA.searchStaffCompanies(data[2]);
			} else {
				companies = companyDBA.searchStudentCompanies(data[2], data[1]);
			}
			break;
		case "CompanyDetail":
			companies = companyDBA.SearchCompanyDetail(Integer.parseInt(data[2]), data[1]);
			break;
		case "CompanyRegister":
			String companyName = data[2];

			CompanyDBAccess companyDBA2 = new CompanyDBAccess();
			companyDBA2.insertCompany(companyName);
			break;
		case "RegistEvent":
			companies = companyDBA.SearchCompanyWithGraduates(Integer.parseInt(data[2]));
			List<Staff> staffs = new StaffDBAcess().getAllStaffs();
			companies.getFirst().setStaffs(staffs);
			break;
		case "CompanyUpdate":
			int id = Integer.parseInt(data[1]);
			String name = data[2];
			companyDBA.updateCompany(id, name);
			break;

		case "CompanyName":
			companies.add(companyDBA.SearchCompanById(Integer.parseInt(data[2])));
			break;
		case "findCompanyName":
			CompanyDTO dto = companyDBA.SearchCompanById(Integer.parseInt(data[1]));
			if (dto != null) {
				companies.add(dto);
			}
			break;
		}
		return companies;
	}
}

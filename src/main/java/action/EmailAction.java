package action;

import java.util.ArrayList;
import java.util.List;

import dao.CompanyDBAccess;
import dao.GraduateDBAccess;
import dao.StaffDBAcess;
import dto.EmailDTO;
import model.Company;
import model.Email;
import model.Graduate;
import model.Staff;

public class EmailAction {
	public List<EmailDTO> execute(String[] data) throws Exception {
		String action = data[0];
		EmailDTO emailDTO = new EmailDTO();
		List<EmailDTO> list = new ArrayList<EmailDTO>();

		Graduate graduate;
		Staff staff;
		Company company;
		Email email;

		GraduateDBAccess graduateDBA = new GraduateDBAccess();
		StaffDBAcess staffDBA = new StaffDBAcess();
		CompanyDBAccess companyDBA = new CompanyDBAccess();

		boolean result;
		String title;
		String body;
		StringBuilder sb = new StringBuilder();

		switch (action) {
		case "ScheduleArrangeEmailConfirm":
			//			data[0]=コマンド
			//			data[1]=空文字
			//			data[2]=卒業生学籍番号
			//			data[3]=スタッフID
			//			data[4]=件名
			//			data[5]=本文
			//			data[6]=companyId
			graduate = graduateDBA.searchGraduateByGraduateStudentNumber(data[2]);
			staff = staffDBA.searchStaffById(Integer.parseInt(data[3]));
			company = companyDBA.SearchCompanById(Integer.parseInt(data[6])).getCompany();

			email = new Email();
			email.setSubject(data[4]);
			body = data[5];
			body += "\n\n回答URL:" + "http://○○○○△△△△";
			body += "\n\n\n担当者名" + staff.getStaffName();
			body += "\n担当者メールアドレス" + staff.getStaffEmail();
			email.setBody(body);

			emailDTO.setInputBody(data[5]);
			emailDTO.setGraduate(graduate);
			emailDTO.setStaff(staff);
			emailDTO.setCompany(company);
			emailDTO.setEmail(email);

			list = new ArrayList<>();
			list.add(emailDTO);
			break;
		case "ScheduleArrangeEmailBack":
			//			data[0]=コマンド
			//			data[1]=空文字
			//			data[2]=卒業生学籍番号
			//			data[3]=スタッフID
			//			data[4]=件名
			//			data[5]=本文
			//			data[6]=companyId
			graduate = graduateDBA.searchGraduateByGraduateStudentNumber(data[2]);
			staff = staffDBA.searchStaffById(Integer.parseInt(data[3]));
			company = companyDBA.SearchCompanById(Integer.parseInt(data[6])).getCompany();

			email = new Email();
			email.setSubject(data[4]);

			emailDTO.setInputBody(data[5]);
			emailDTO.setGraduate(graduate);
			emailDTO.setStaff(staff);
			emailDTO.setCompany(company);
			emailDTO.setEmail(email);

			list = new ArrayList<>();
			list.add(emailDTO);
			break;
		}
		return list;
	}
}

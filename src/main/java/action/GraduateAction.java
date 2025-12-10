package action;

import java.util.ArrayList;
import java.util.List;

import dao.AnswerDBAccess;
import dao.CompanyDBAccess;
import dao.GraduateDBAccess;
import model.Answer;
import model.Company;
import model.Course;
import model.Email;
import model.Graduate;
import model.Staff;

public class GraduateAction {
	public List<Graduate> execute(String[] data) throws Exception {
		// data[0] アクション
		// data[1] 学籍番号
		List<Graduate> list = new ArrayList<Graduate>();
		
		String action = data[0];
		GraduateDBAccess GraduateDBA = new GraduateDBAccess();
		CompanyDBAccess CompanyDBA = new CompanyDBAccess();
		Graduate graduate = new Graduate();
		
		
		Company company = new Company();
		Course course = new Course();

		
		switch (action) {
		case "AssignStaff":
			// data[2] graduateStudentNumber
			// data[3] staffId

			graduate = GraduateDBA.searchGraduateByGraduateStudentNumber(data[2]);

			//			graduate.setGraduateStudentNumber(data[2]);
			Staff staff = new Staff();
			staff.setStaffId(Integer.parseInt(data[3]));
			graduate.setStaff(staff);
			GraduateDBA.setStaff(graduate);

			AnswerDBAccess AnswerDBA = new AnswerDBAccess();
			Answer answer = new Answer();
			answer.setGraduateStudentNumber(graduate.getGraduateStudentNumber());
			answer = AnswerDBA.insertAnswer(answer);

			// メールの件名・本文はあなたのデータに合わせて
			String subject = "【イベントのお願い】";
			String body = "http://localhost:8080/CareerSupportSystem/answer?command=AnswerForm&answerId="
					+ answer.getAnswerId();

			Email mail = new Email(); // 1通ずつ新しく作る
			mail.setTo("24jy0109@jec.ac.jp");
			mail.setSubject(subject);
			mail.setBody(body + "\n" + graduate.getGraduateEmail());

			boolean result = mail.send();

			if (!result) {
				System.out.println("送信失敗: " + graduate.getGraduateEmail());
			} else {
				System.out.println("送信成功: " + graduate.getGraduateEmail());
			}
			break;
		case "RegisterGraduate":
			//			Graduate graduate = new Graduate();

			// ① 会社ID
			int companyId = Integer.parseInt(data[1]);
			company.setCompanyId(companyId);
			graduate.setComapany(company);

			// jobType
			graduate.setGraduateJobCategory(data[2]);

			// 氏名
			graduate.setGraduateName(data[3]);

			// 学科CODE
			course.setCourseCode(data[4]);
			graduate.setCourse(course);

			// 学籍番号
			graduate.setGraduateStudentNumber(data[5]);

			// メール
			graduate.setGraduateEmail(data[6]);

			// その他
			graduate.setOtherInfo(data[7]);

			GraduateDBAccess gdb = new GraduateDBAccess();
			gdb.insertGraduate(graduate);
			break;
		case "findCompanyName":
			companyId = Integer.parseInt(data[1]);
			
			String companyName = CompanyDBA.searchCompanyNameById(String.valueOf(companyId));
			company.setCompanyName(companyName);
			graduate.setComapany(company);
			list.add(graduate);

			break;
		case "findCourseName":
			course = new Course();

			break;
		}
		return list;
	}
}

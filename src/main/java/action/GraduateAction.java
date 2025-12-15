package action;

import java.util.ArrayList;
import java.util.List;

import dao.AnswerDBAccess;
import dao.CompanyDBAccess;
import dao.CourseDBAccess;
import dao.GraduateDBAccess;
import model.Answer;
import model.Company;
import model.Course;
import model.Email;
import model.Graduate;

public class GraduateAction {
	public List<Graduate> execute(String[] data) throws Exception {
		// data[0] アクション
		// data[1] 学籍番号
		List<Graduate> list = new ArrayList<Graduate>();

		String action = data[0];
		GraduateDBAccess GraduateDBA = new GraduateDBAccess();
		CompanyDBAccess CompanyDBA = new CompanyDBAccess();
		CourseDBAccess CourseDBA = new CourseDBAccess();

		Graduate graduate = new Graduate();
		Company company = new Company();
		Course course = new Course();
		Email mail = new Email();

		AnswerDBAccess AnswerDBA = new AnswerDBAccess();
		Answer answer = new Answer();

		String title;
		String body;
		switch (action) {
		/*		case "SendScheduleArrangeEmail":
					// data[2] graduateStudentNumber
					// data[3] staffId
		
					graduate = GraduateDBA.searchGraduateByGraduateStudentNumber(data[2]);
		
					//			graduate.setGraduateStudentNumber(data[2]);
					Staff staff = new Staff();
					staff.setStaffId(Integer.parseInt(data[3]));
					graduate.setStaff(staff);
					GraduateDBA.setStaff(graduate);
					break;*/
		case "findGraduateStudentNumber":
			boolean exists = GraduateDBA.findGraduateStudentNumber(data[1]);

			if (exists) {
				// ダミーでもOK、とにかく1件入れる
				list.add(new Graduate());
			}

			break;
		case "RegisterGraduate":
			//			Graduate graduate = new Graduate();

			// ① 会社ID
			int companyId = Integer.parseInt(data[1]);
			company.setCompanyId(companyId);
			graduate.setCompany(company);

			// jobType
			graduate.setGraduateJobCategory(data[2]);
			String jobType = data[2];

			// 氏名
			graduate.setGraduateName(data[3]);
			String name = data[3];

			// 学科CODE
			course.setCourseCode(data[4]);
			graduate.setCourse(course);

			// 学籍番号
			graduate.setGraduateStudentNumber(data[5]);
			String graduateStudentNumber = data[5];

			// メール
			graduate.setGraduateEmail(data[6]);
			String email = data[6];

			// その他
			graduate.setOtherInfo(data[7]);
			String otherInfo = data[7];

			//メール送信
			title = "【連絡先登録完了】";
			body = "内容をご確認ください。";
			body += "\n職種：" + jobType;
			body += "\n氏名：" + name;
			body += "\n学籍番号：" + graduateStudentNumber;
			body += "\nその他：" + otherInfo;

			mail.setTo(email);
			mail.setSubject(title);
			mail.setBody(body);

			boolean result = mail.send();

			if (!result) {
				System.out.println("送信失敗: " + graduate.getGraduateEmail());
			} else {
				System.out.println("送信成功: " + graduate.getGraduateEmail());
			}

			GraduateDBAccess gdb = new GraduateDBAccess();
			gdb.insertGraduate(graduate);
			break;

		case "findCompanyName":
			companyId = Integer.parseInt(data[1]);
			graduate.setCompany(company);

			String companyName = CompanyDBA.searchCompanyNameById(String.valueOf(companyId));
			company.setCompanyName(companyName);
			graduate.setCompany(company);
			list.add(graduate);

			break;
		case "findCourseName":
			String courseCode = data[1];
			graduate.setCourse(course);

			String courseName = CourseDBA.findCourseNameById(String.valueOf(courseCode));
			course.setCourseName(courseName);
			graduate.setCourse(course);
			list.add(graduate);
			break;
		}
		return list;
	}
}

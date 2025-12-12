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
		CourseDBAccess CourseDBA = new CourseDBAccess();
		
		Graduate graduate = new Graduate();
		Company company = new Company();
		Course course = new Course();

		AnswerDBAccess AnswerDBA = new AnswerDBAccess();
		Answer answer = new Answer();

		switch (action) {
		case "SendScheduleArrangeEmail":
			// data[2] graduateStudentNumber
			// data[3] staffId

			graduate = GraduateDBA.searchGraduateByGraduateStudentNumber(data[2]);

			//			graduate.setGraduateStudentNumber(data[2]);
			Staff staff = new Staff();
			staff.setStaffId(Integer.parseInt(data[3]));
			graduate.setStaff(staff);
			GraduateDBA.setStaff(graduate);
			break;
		case "RegisterGraduate":
			//			Graduate graduate = new Graduate();

			// ① 会社ID
			int companyId = Integer.parseInt(data[1]);
			company.setCompanyId(companyId);
			graduate.setCompany(company);

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

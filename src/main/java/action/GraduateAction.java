package action;

import java.util.ArrayList;
import java.util.List;

import dao.AnswerDBAccess;
import dao.CompanyDBAccess;
import dao.CourseDBAccess;
import dao.GraduateDBAccess;
import dto.CompanyDTO;
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
			//企業名を取得
			String companyName = data[2];
			// jobType
			graduate.setGraduateJobCategory(data[3]);
			String jobType = data[3];

			// 氏名
			graduate.setGraduateName(data[4]);
			String name = data[4];

			// 学科CODE
			course.setCourseCode(data[5]);
			graduate.setCourse(course);
			//学科名取得
			String courseName = data[6];

			// 学籍番号
			graduate.setGraduateStudentNumber(data[7]);
			String graduateStudentNumber = data[7];

			// メール
			graduate.setGraduateEmail(data[8]);
			String email = data[8];

			// その他
			graduate.setOtherInfo(data[9]);
			String otherInfo = data[9];

			// メール送信（登録完了）
			title = "【連絡先登録完了】" + name + " 様";

			body = "";
			body += courseName + "\n";
			body += name + " 様\n\n";

			body += "この度は連絡先登録してくださり誠にありがとうございます。\n";
			body += "下記の内容で連絡先情報が登録されましたのでご確認ください。\n\n";

			body += "｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ\n";
			body += "企業名　　：" + companyName + "\n";
			body += "職種　　　：" + jobType + "\n";
			body += "氏名　　　：" + name + "\n";
			body += "学籍番号　：" + graduateStudentNumber + "\n";
			body += "その他　　：" + (otherInfo == null || otherInfo.isEmpty() ? "なし" : otherInfo) + "\n";
			body += "｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ\n\n";

			body += "内容を変更する場合は、再度システムより登録情報の変更をお願いいたします。\n\n";

			body += "本メールはシステムより自動送信されています。\n";
			body += "本メールに返信されても対応できませんのでご了承ください。";

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

		case "UpdateGraduate":
			// ① 会社ID
			companyId = Integer.parseInt(data[1]);
			company.setCompanyId(companyId);
			graduate.setCompany(company);
			companyName = data[2];

			// 職種
			graduate.setGraduateJobCategory(data[3]);
			jobType = data[3];

			// 氏名
			graduate.setGraduateName(data[4]);
			name = data[4];

			// 学科CODE
			course.setCourseCode(data[5]);
			graduate.setCourse(course);
			courseName = data[6];

			// 学籍番号
			graduate.setGraduateStudentNumber(data[7]);
			graduateStudentNumber = data[7];

			// メール
			graduate.setGraduateEmail(data[8]);
			email = data[8];

			// その他
			graduate.setOtherInfo(data[9]);
			otherInfo = data[9];

			// メール送信
			title = "【連絡先情報更新完了】" + name + " 様";
			body = "";
			body += name + " 様\n\n";
			body += "情報システム開発科\n";
			body += name + " 様\n\n";

			body += "連絡先情報の更新が完了しました。\n";
			body += "下記の内容が現在の登録情報となりますのでご確認ください。\n\n";

			body += "｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ\n";
			body += "企業名　　：" + companyName + "\n";
			body += "職種　　　：" + jobType + "\n";
			body += "学科　　　：" + courseName + "\n";
			body += "氏名　　　：" + name + "\n";
			body += "学籍番号　：" + graduateStudentNumber + "\n";
			body += "その他　　：" + (otherInfo == null || otherInfo.isEmpty() ? "なし" : otherInfo) + "\n";
			body += "｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ+｡｡.｡･.｡*ﾟ\n\n";

			body += "内容を修正する場合は、再度システムより情報の更新をお願いいたします。\n\n";

			body += "本メールはシステムより自動送信されています。\n";
			body += "本メールに返信されても対応できませんのでご了承ください。\n";

			mail.setTo(email);
			mail.setSubject(title);
			mail.setBody(body);

			result = mail.send();
			if (!result) {
				System.out.println("送信失敗: " + graduate.getGraduateEmail());
			} else {
				System.out.println("送信成功: " + graduate.getGraduateEmail());
			}

			// DB更新
			gdb = new GraduateDBAccess();
			gdb.updateGraduate(graduate); // ← insertじゃなくupdateを呼ぶ
			break;

		case "deleteGraduate":
			graduateStudentNumber = data[1];

			System.out.println("DEBUG delete studentNumber = " + graduateStudentNumber);
			GraduateDBA.deleteGraduate(graduateStudentNumber);

			break;

		case "findCompanyName":
			companyId = Integer.parseInt(data[1]);
			graduate.setCompany(company);

			companyName = CompanyDBA.searchCompanyNameById(String.valueOf(companyId));
			company.setCompanyName(companyName);
			graduate.setCompany(company);
			list.add(graduate);

			break;
		case "findCourseName":
			String courseCode = data[1];
			graduate.setCourse(course);

			courseName = CourseDBA.findCourseNameById(String.valueOf(courseCode));
			course.setCourseName(courseName);
			graduate.setCourse(course);
			list.add(graduate);
			break;

		case "findStudentNumber":
			exists = GraduateDBA.findGraduateStudentNumber(data[1]);

			if (exists) {
				// 登録済み → ダミー1件返す
				list.add(new Graduate());
			}
			break;
		case "findGraduateInfo":
			gdb = new GraduateDBAccess();
			String studentNumber = data[1];
			graduate = gdb.searchGraduateByGraduateStudentNumber(studentNumber);
			list.add(graduate);

			break;

		//editInfo
		case "graduateSearchBycompanyId":
			companyId = Integer.parseInt(data[1]);

			List<CompanyDTO> companyList = CompanyDBA.SearchCompanyWithGraduates(companyId);

			if (companyList != null && !companyList.isEmpty()) {
				CompanyDTO dto = companyList.get(0);

				// ダミーGraduateではなく、
				// company を持った Graduate を1つだけ返す
				Graduate g = new Graduate();
				g.setCompany(dto.getCompany()); // ← companyの中に graduates 全員入ってる
				list.add(g);
			}
			break;

		}

		return list;
	}
}

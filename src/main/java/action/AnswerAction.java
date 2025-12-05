package action;

import dao.GraduateDBAccess;
import model.Email;
import model.Graduate;
import model.Staff;

public class AnswerAction {
	public void execute(String[] data) throws Exception {
		// data[0] アクション
		// data[1] 学籍番号

		String action = data[0];
		GraduateDBAccess GraduateDBA = new GraduateDBAccess();
		Graduate graduate = new Graduate();
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
			

			// メールの件名・本文はあなたのデータに合わせて
			String subject = "【イベントのお願い】" ;
			String body = "開催をお願いします";

			    Email mail = new Email();   // 1通ずつ新しく作る
			    mail.setTo("24jy0109@jec.ac.jp");
			    mail.setSubject(subject);
			    mail.setBody(body + graduate.getGraduateEmail());

			    boolean result = mail.send();

			    if (!result) {
			        System.out.println("送信失敗: " + graduate.getGraduateEmail());
			    } else {
			        System.out.println("送信成功: " + graduate.getGraduateEmail());
			    }
			break;
		}
	}
}

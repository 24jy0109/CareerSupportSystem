package action;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.AnswerDBAccess;
import dao.GraduateDBAccess;
import model.Answer;
import model.Email;
import model.Graduate;
import model.Staff;

public class AnswerAction {
	public List<Answer> execute(String[] data) throws Exception {
		// data[0] アクション
		String action = data[0];
		
		List<Answer> answers = new ArrayList<>();
		
		GraduateDBAccess GraduateDBA = new GraduateDBAccess();
		Graduate graduate = new Graduate();
		
		AnswerDBAccess answerDBA = new AnswerDBAccess();
		Answer answer = new Answer();
		
		switch (action) {
		case "AssignStaff":
			// data[1] 学籍番号
			// data[2] graduateStudentNumber
			// data[3] staffId
			
			graduate = GraduateDBA.searchGraduateByGraduateStudentNumber(data[2]);
			
//			graduate.setGraduateStudentNumber(data[2]);
			Staff staff = new Staff();
			staff.setStaffId(Integer.parseInt(data[3]));
			graduate.setStaff(staff);
			GraduateDBA.setStaff(graduate);
			
			// メールの件名・本文
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
		case "registAnswer":
			// data[1] answerId
			// data[2] eventAvailability
			// data[3] firstChoice
			// data[4] secondChoice
			// data[5] thirdhoice
			answer.setAnswerId(Integer.parseInt(data[1]));
			answer.setEventAvailability(Boolean.parseBoolean(data[2]));
			answer.setFirstChoice(LocalDateTime.parse(data[3]));
			answer.setSecondChoice(LocalDateTime.parse(data[4]));
			answer.setThirdChoice(LocalDateTime.parse(data[5]));
			answerDBA.updateAnswer(answer);
			answers.add(answer);
			break;
		}
		return answers;
	}
}

package action;

import java.util.ArrayList;
import java.util.List;

import dao.AnswerDBAccess;
import exception.ValidationException;
import model.Answer;
import model.Email;
import model.Graduate;
import model.MailBuilder;
import model.Staff;
import validator.AnswerTimeValidator;

public class AnswerAction extends BaseAction {
	public List<Answer> execute(String[] data) throws ValidationException, Exception {
		// data[0] アクション
		String action = data[0];

		List<Answer> answers = new ArrayList<>();

		AnswerDBAccess answerDBA = new AnswerDBAccess();
		Answer answer = new Answer();

		Staff staff = new Staff();
		Graduate graduate = new Graduate();

		Email mail = new Email();
		boolean result;
		String title;
		String body;
		StringBuilder sb = new StringBuilder();

		switch (action) {
		case "AnsweredCheck":
			// data[1] answerId
			answers.add(answerDBA.searchAnswerById(Integer.parseInt(data[1])));
			break;
		case "AnswerConfirm":
			// data[1] answerId
			// data[2] eventAvailability
			// data[3] firstChoiceStart
			// data[4] firstChoiceEnd
			// data[5] secondChoiceStart
			// data[6] secondChoiceEnd
			// data[7] thirdChoiceStart
			// data[8] thirdChoiceEnd

			answer.setAnswerId(Integer.parseInt(data[1]));
			answer.setEventAvailability(Boolean.parseBoolean(data[2]));

			answer.setFirstChoiceStartTime(parseDateTimeOrNull(data[3]));
			answer.setFirstChoiceEndTime(parseDateTimeOrNull(data[4]));

			answer.setSecondChoiceStartTime(parseDateTimeOrNull(data[5]));
			answer.setSecondChoiceEndTime(parseDateTimeOrNull(data[6]));

			answer.setThirdChoiceStartTime(parseDateTimeOrNull(data[7]));
			answer.setThirdChoiceEndTime(parseDateTimeOrNull(data[8]));

			AnswerTimeValidator.validate(answer);
			answers.add(answer);
			break;
		case "AnswerBack":
			// data[1] answerId
			// data[2] eventAvailability
			// data[3] firstChoiceStart
			// data[4] firstChoiceEnd
			// data[5] secondChoiceStart
			// data[6] secondChoiceEnd
			// data[7] thirdChoiceStart
			// data[8] thirdChoiceEnd

			answer.setAnswerId(Integer.parseInt(data[1]));
			answer.setEventAvailability(Boolean.parseBoolean(data[2]));

			answer.setFirstChoiceStartTime(parseDateTimeOrNull(data[3]));
			answer.setFirstChoiceEndTime(parseDateTimeOrNull(data[4]));

			answer.setSecondChoiceStartTime(parseDateTimeOrNull(data[5]));
			answer.setSecondChoiceEndTime(parseDateTimeOrNull(data[6]));

			answer.setThirdChoiceStartTime(parseDateTimeOrNull(data[7]));
			answer.setThirdChoiceEndTime(parseDateTimeOrNull(data[8]));

			answers.add(answer);
			break;
		case "RegistAnswer":
		    // データ更新
		    answer.setAnswerId(Integer.parseInt(data[1]));
		    answer.setEventAvailability(Boolean.parseBoolean(data[2]));

		    answer.setFirstChoiceStartTime(parseDateTimeOrNull(data[3]));
		    answer.setFirstChoiceEndTime(parseDateTimeOrNull(data[4]));
		    answer.setSecondChoiceStartTime(parseDateTimeOrNull(data[5]));
		    answer.setSecondChoiceEndTime(parseDateTimeOrNull(data[6]));
		    answer.setThirdChoiceStartTime(parseDateTimeOrNull(data[7]));
		    answer.setThirdChoiceEndTime(parseDateTimeOrNull(data[8]));

		    AnswerTimeValidator.validate(answer);
		    answerDBA.updateAnswer(answer);
		    answer = answerDBA.searchAnswerById(answer.getAnswerId());
		    answers.add(answer);

		    // メール生成
		    title = "【イベント回答】参加可否・希望日時のご連絡";
		    body = MailBuilder.buildAnswerNotification(answer);
		    // メール送信
		    sendMail(answer.getEvent().getStaff().getStaffEmail(), title, body);
		    
		    title = "【イベント回答】参加可否・希望日時のご登録";
		    body = MailBuilder.buildAnswerSelfNotification(answer);
		    // メール送信
		    sendMail(answer.getGraduate().getGraduateEmail(), title, body);

		    break;
		case "ScheduleAnswerCheck":
			answers = answerDBA.getAllAnswers();
			break;
		case "yesAnswer":
				answers.add(answerDBA.searchAnswerById(Integer.parseInt(data[1]), Integer.parseInt(data[2])));
			break;
		case "noAnswer":
		    // Answer取得
		    answer = answerDBA.searchAnswerById(Integer.parseInt(data[1]));
		    staff = answer.getEvent().getStaff();
		    graduate = answer.getGraduate();

		    // メール本文生成
		    body = MailBuilder.buildNoAnswerNotification(graduate, staff);

		    // メール送信
		    sendMail(graduate.getGraduateEmail(), "【イベント参加についてのご連絡】", body);

		    // Answer削除
		    answerDBA.deleteAnswer(answer.getAnswerId());
		    break;
		case "deleteAnswer":
			// Answer削除
			answerDBA.deleteAnswer(Integer.parseInt(data[1]));
			break;
		}
		return answers;
	}
}

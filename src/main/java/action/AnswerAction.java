package action;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dao.AnswerDBAccess;
import exception.ValidationException;
import model.Answer;
import model.Email;
import model.Graduate;
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

			answerDBA.updateAnswer(answer);
			answer = answerDBA.searchAnswerById(answer.getAnswerId());
			answers.add(answer);

			staff = answer.getEvent().getStaff();
			graduate = answer.getGraduate();

			title = "【イベント回答】参加可否・希望日時のご連絡";

			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");

			/* ===== 冒頭 ===== */
			sb.append(graduate.getGraduateName())
					.append(" さんより、以下の開催について回答がありました。\n\n");

			/* ===== 参加可否 ===== */
			sb.append("■ 参加可否\n");
			if (Boolean.TRUE.equals(answer.getEventAvailability())) {
				sb.append("  出席する\n\n");
			} else {
				sb.append("  出席しない\n\n");
			}

			/* ===== 希望日時（出席の場合のみ） ===== */
			if (Boolean.TRUE.equals(answer.getEventAvailability())) {
				sb.append("■ 希望日時\n");

				if (answer.getFirstChoiceStartTime() != null) {
					sb.append("  【第1希望】\n");
					sb.append("    ")
							.append(answer.getFirstChoiceStartTime().format(fmt))
							.append(" ～ ")
							.append(answer.getFirstChoiceEndTime().format(fmt))
							.append("\n");
				}

				if (answer.getSecondChoiceStartTime() != null) {
					sb.append("  【第2希望】\n");
					sb.append("    ")
							.append(answer.getSecondChoiceStartTime().format(fmt))
							.append(" ～ ")
							.append(answer.getSecondChoiceEndTime().format(fmt))
							.append("\n");
				}

				if (answer.getThirdChoiceStartTime() != null) {
					sb.append("  【第3希望】\n");
					sb.append("    ")
							.append(answer.getThirdChoiceStartTime().format(fmt))
							.append(" ～ ")
							.append(answer.getThirdChoiceEndTime().format(fmt))
							.append("\n");
				}
				sb.append("\n");
			}

			/* ===== 卒業生情報 ===== */
			sb.append("■ 回答者情報\n");
			sb.append("  氏名: ").append(graduate.getGraduateName()).append("\n");
			sb.append("  メール: ").append(graduate.getGraduateEmail()).append("\n\n");

			/* ===== フッター ===== */
			sb.append("詳細は管理画面よりご確認ください。\n");
			sb.append("※本メールは自動送信です。\n\n");

			body = sb.toString();

			mail.setTo("24jy0109@jec.ac.jp");
			mail.setSubject(title);
			mail.setBody(body + "\n\n" + staff.getStaffEmail());

			result = mail.send();

			if (!result) {
				System.out.println("送信失敗" + staff.getStaffEmail());
			} else {
				System.out.println("送信成功" + staff.getStaffEmail());
			}

			break;
		case "ScheduleAnswerCheck":
			answers = answerDBA.getAllAnswers();
			break;
		case "yesAnswer":
			try {
				answers.add(answerDBA.searchAnswerById(Integer.parseInt(data[1]), Integer.parseInt(data[2])));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "noAnswer":
			// data[1] answerId
			answerDBA.deleteAnswer(Integer.parseInt(data[1]));
		}
		return answers;
	}
}

package action;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.AnswerDBAccess;
import model.Answer;
import model.Graduate;
import validator.AnswerTimeValidator;

public class AnswerAction {
	public List<Answer> execute(String[] data) throws Exception {
		// data[0] アクション
		String action = data[0];

		List<Answer> answers = new ArrayList<>();

		Graduate graduate = new Graduate();

		AnswerDBAccess answerDBA = new AnswerDBAccess();
		Answer answer = new Answer();

		switch (action) {
		case "registAnswer":
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

			System.out.println("実行チェック");

			answerDBA.updateAnswer(answer);
			answers.add(answer);
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

	private LocalDateTime parseDateTimeOrNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return LocalDateTime.parse(value);
	}

}

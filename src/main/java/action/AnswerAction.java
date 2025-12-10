package action;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.AnswerDBAccess;
import dao.GraduateDBAccess;
import model.Answer;
import model.Graduate;

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

		    answer.setFirstChoiceStartTime(LocalDateTime.parse(data[3]));
		    answer.setFirstChoiceEndTime(LocalDateTime.parse(data[4]));

		    answer.setSecondChoiceStartTime(LocalDateTime.parse(data[5]));
		    answer.setSecondChoiceEndTime(LocalDateTime.parse(data[6]));

		    answer.setThirdChoiceStartTime(LocalDateTime.parse(data[7]));
		    answer.setThirdChoiceEndTime(LocalDateTime.parse(data[8]));

		    answerDBA.updateAnswer(answer);
		    
		    answers.add(answer);
		    for(String d : data) {
		    	System.out.println(d);
		    }
		    break;
		case "ScheduleAnswerCheck":
//			answers = answerDBA
			break;
		}
		return answers;
	}
}

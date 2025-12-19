package action;

import java.util.ArrayList;
import java.util.List;

import dao.JoinStudentDBAccess;
import model.JoinStudent;

public class JoinStudentAction {
	public List<JoinStudent> execute(String[] data) throws Exception {
		// data[0] アクション
		String action = data[0];
		
		List<JoinStudent> joinStudents = new ArrayList<>();
		
		JoinStudentDBAccess joinStudentDBAccess = new JoinStudentDBAccess();
		
		switch (action) {
		case "JoinStudentList":
		    // data[1] eventId
			joinStudents = joinStudentDBAccess.searchJoinStudentsByEventId(Integer.parseInt(data[1]));
		    break;
		}
		return joinStudents;
	}
}

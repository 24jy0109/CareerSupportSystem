package action;

import java.util.List;

import dao.RequestDBAccess;
import model.Student;

public class AppointmentRequestAction {
	public List<Student> execute(String[] data) throws Exception {
		// data[0] アクション
		// data[1] 学籍番号
		// data[2] CompanyName, CompanyId
		
		String action = data[0];
		RequestDBAccess requestDBA = new RequestDBAccess();
		switch(action) {
		case "ApplyRequest":
			requestDBA.insertRequest(Integer.parseInt(data[2]), data[1]);
			break;
		case "CancelRequest":
			requestDBA.cancelRequest(Integer.parseInt(data[2]), data[1]);
			break;
		}
		return null;
	}
}

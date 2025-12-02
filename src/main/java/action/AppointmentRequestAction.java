package action;

import java.util.ArrayList;
import java.util.List;

import dao.RequestDBAccess;
import model.Request;

public class AppointmentRequestAction {
	public List<Request> execute(String[] data) throws Exception {
		// data[0] アクション
		// data[1] 学籍番号
		// data[2] CompanyName, CompanyId
		
		List<Request> list = new ArrayList<>();
		
		String action = data[0];
		RequestDBAccess requestDBA = new RequestDBAccess();
		switch(action) {
		case "RequestList":
			list = requestDBA.requestStudentList(Integer.parseInt(data[2]));
			break;
		case "ApplyRequest":
			requestDBA.insertRequest(Integer.parseInt(data[2]), data[1]);
			break;
		case "CancelRequest":
			requestDBA.cancelRequest(Integer.parseInt(data[2]), data[1]);
			break;
		}
		return list;
	}
}

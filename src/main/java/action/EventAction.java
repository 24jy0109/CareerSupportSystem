package action;

import java.util.ArrayList;
import java.util.List;

import dao.CompanyDBAccess;
import model.Event;

public class EventAction {
	public List<Event> execute(String[] data) throws Exception {
		// data[0] アクション
		// data[1] 学籍番号
		// data[2] CompanyName, CompanyId

		String action = data[0];
		List<Event> events = new ArrayList<Event>();
		CompanyDBAccess companyDBA = new CompanyDBAccess();
		switch (action) {
		case "RegistEvent":
//			events = companyDBA.searchStaffCompanies(data[2]);
			break;
		}
		return null;
	}
}

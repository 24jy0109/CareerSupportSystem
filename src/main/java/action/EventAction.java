package action;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.CompanyDBAccess;
import dao.EventDBAccess;
import model.Company;
import model.Event;
import model.Graduate;
import model.Staff;

public class EventAction {
	public List<Event> execute(String[] data) throws Exception {
		// data[0] = アクション名 ("RegistEvent")
		// data[1] = 学籍番号
		// data[2] = companyId || companyName
		// data[3] = staffId
		// data[4] = eventPlace
		// data[5] = eventStartTime
		// data[6] = eventEndTime
		// data[7] = eventCapacity
		// data[8] = eventOtherInfo

		String action = data[0];
		List<Event> list = new ArrayList<Event>();

		CompanyDBAccess companyDBA = new CompanyDBAccess();
		
		switch (action) {
		case "RegistEvent":
			Event event = new Event();
			
			Company company = new Company();
			company.setCompanyId(Integer.parseInt(data[2]));
			event.setCompany(company);
			
			Staff staff = new Staff();
			staff.setStaffId(Integer.parseInt(data[3]));
			event.setStaff(staff);

			// Event の基本情報をセット
			event.setEventPlace(data[4]);
			event.setEventStartTime(LocalDateTime.parse(data[5]));
			event.setEventEndTime(LocalDateTime.parse(data[6]));
			event.setEventCapacity(Integer.parseInt(data[7]));
			event.setEventOtherInfo(data[8]);

			// 参加させる卒業生をセット
			ArrayList<Graduate> graduates = new ArrayList<>();
			if (!data[9].isEmpty()) {
				String[] graduateNumbers = data[9].split(",");
				for (String num : graduateNumbers) {
					Graduate g = new Graduate();
					g.setGraduateStudentNumber(num);
					graduates.add(g);
				}
			}
			event.setJoinGraduates(graduates);

			// eventProgress は登録時は 0 にしておくなど
			event.setEventProgress(0);
			
			EventDBAccess eventDBA = new EventDBAccess();
			eventDBA.insertEvent(event);
		}

		return list;
	}
}

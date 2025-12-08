package action;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.CompanyDBAccess;
import dao.EventDBAccess;
import dao.GraduateDBAccess;
import dao.RequestDBAccess;
import dao.StaffDBAcess;
import dto.CompanyDTO;
import dto.EventDTO;
import model.Company;
import model.Email;
import model.Event;
import model.Graduate;
import model.Staff;

public class EventAction {
	public List<EventDTO> execute(String[] data) throws Exception {
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
		EventDTO eventDTO = new EventDTO();
		List<EventDTO> list = new ArrayList<EventDTO>();

		CompanyDBAccess companyDBA = new CompanyDBAccess();
		
		switch (action) {
		case "RegistEventForm":
			List<CompanyDTO> companies = companyDBA.SearchCompanyWithGraduates(Integer.parseInt(data[2]));
			List<Staff> staffs = new StaffDBAcess().getAllStaffs();
			Event e = new Event();
			e.setCompany(companies.getFirst().getCompany());
			eventDTO.setEvent(e);
			eventDTO.setGraduates(companies.getFirst().getCompany().getGraduates());
			eventDTO.setStaffs(staffs);
			
			list.add(eventDTO);
			break;
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

			// eventProgress は登録時は2(開催)
			event.setEventProgress(2);
			
			EventDBAccess eventDBA = new EventDBAccess();
			eventDBA.insertEvent(event);
			
			RequestDBAccess requestDBA = new RequestDBAccess();
			List<String> emails = requestDBA.searchEmailsByCompanyId(Integer.parseInt(data[2]));

			// メールの件名・本文はあなたのデータに合わせて
			String subject = "【イベント通知】" ;
			String body = "イベントに関するお知らせです。";

			for (String toEmail : emails) {

			    Email mail = new Email();   // 1通ずつ新しく作る
			    mail.setTo("24jy0109@jec.ac.jp");
			    mail.setSubject(subject);
			    mail.setBody(body + toEmail);

			    boolean result = mail.send();

			    if (!result) {
			        System.out.println("送信失敗: " + toEmail);
			    } else {
			        System.out.println("送信成功: " + toEmail);
			    }
			}

			break;
		case "ScheduleArrangeSend":
			// data[2] = graduateStudentNumber
			Graduate graduate = new GraduateDBAccess().searchGraduateByGraduateStudentNumber(data[2]);
			List<Graduate> g = new ArrayList<Graduate>(); 
			g.add(graduate);
			eventDTO.setGraduates(g);
			
			List<Staff> s = new ArrayList<>();
			s = new StaffDBAcess().getAllStaffs();
			eventDTO.setStaffs(s);
			list.add(eventDTO);
			break;
		}

		return list;
	}
}

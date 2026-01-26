package action;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dao.AnswerDBAccess;
import dao.CompanyDBAccess;
import dao.EventDBAccess;
import dao.GraduateDBAccess;
import dao.JoinStudentDBAccess;
import dao.RequestDBAccess;
import dao.StaffDBAcess;
import dto.CompanyDTO;
import dto.EventDTO;
import exception.ValidationException;
import model.Answer;
import model.Company;
import model.Email;
import model.Event;
import model.EventProgress;
import model.Graduate;
import model.JoinStudent;
import model.MailBuilder;
import model.Staff;
import model.Student;
import validator.EventValidator;

public class EventAction extends BaseAction {
	public List<EventDTO> execute(String[] data) throws ValidationException, Exception {
		// data[0] = アクション名 ("RegistEvent")
		// data[1] = 学籍番号
		// data[2] = companyId || companyName
		// data[3] = staffId
		// data[4] = eventPlace
		// data[5] = eventStartTime
		// data[6] = eventEndTime
		// data[7] = eventCapacity
		// data[8] = eventOtherInfo
		// data[9] = graduates
		// data[10] = eventId
		// data[11] = answerId
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");//
		String action = data[0];
		EventDTO eventDTO = new EventDTO();
		List<EventDTO> list = new ArrayList<EventDTO>();
		List<Graduate> graduates = new ArrayList<>();

		EventDBAccess eventDBA = new EventDBAccess();
		CompanyDBAccess companyDBA = new CompanyDBAccess();
		AnswerDBAccess answerDBA = new AnswerDBAccess();
		StaffDBAcess staffDBA = new StaffDBAcess();
		GraduateDBAccess graduateDBA = new GraduateDBAccess();
		RequestDBAccess requestDBA = new RequestDBAccess();

		Event event = new Event();
		Company company = new Company();
		Staff staff = new Staff();
		Graduate graduate = new Graduate();

		Email mail = new Email();
		boolean result;
		String title;
		String body;
		StringBuilder sb = new StringBuilder();

		switch (action) {
		case "EventList":
			// data[1] が存在する場合
			if (data.length > 1 && data[1] != null && !data[1].isEmpty()) {
				// data[1] を使った処理
				list = eventDBA.getAllEvents(data[1], data[2]);
			} else {
				// data[1] が無い場合
				list = eventDBA.getAllEvents("", data[2]);
			}

			break;

		case "EventDetail":
			// data[1] eventId
			eventDTO = eventDBA.searchEventById(Integer.parseInt(data[2]));
			if (data[1] != "") {
				eventDTO.setJoinAvailability(eventDBA.isStudentJoinedEvent(Integer.parseInt(data[2]), data[1]));
				eventDTO.setStudentRequested((requestDBA.isStudentRequestedCompany(eventDTO.getEvent().getCompany().getCompanyId(), data[1])));
			}
			list.add(eventDTO);
			break;
		case "RegistEventForm":
			List<CompanyDTO> companies = companyDBA.SearchCompanyWithGraduates(Integer.parseInt(data[2]));
			List<Staff> staffs = new StaffDBAcess().getAllStaffs();
			event.setCompany(companies.getFirst().getCompany());
			eventDTO.setEvent(event);
			eventDTO.setGraduates(companies.getFirst().getCompany().getGraduates());
			eventDTO.setStaffs(staffs);

			list.add(eventDTO);
			break;
		case "RegistEventConfirm":
			// Event の基本情報をセット
			if (data[10] != null && !data[10].isEmpty()) {
				event.setEventId(Integer.parseInt(data[10]));
			}
			event.setEventPlace(data[4]);
			event.setEventStartTime(parseDateTimeOrNull(data[5]));
			event.setEventEndTime(parseDateTimeOrNull(data[6]));
			Integer capacity = parseIntOrNull(data[7]);
			event.setEventCapacity(capacity != null ? capacity : 0);
			event.setEventOtherInfo(data[8]);

			// Company / Staff（ID 系は validation 対象外）
			company = companyDBA.SearchCompanById(Integer.parseInt(data[2])).getCompany();
			event.setCompany(company);

			staff = staffDBA.searchStaffById(Integer.parseInt(data[3]));
			event.setStaff(staff);

			// 参加させる卒業生をセット
			if (data[9] != null && !data[9].isBlank()) {
				graduates = graduateDBA.searchGraduatesByGraduateStudentNumbers(
						data[9].split(","));
				event.setJoinGraduates(graduates);
			}

			// 基本情報が揃った時点で validation
			EventValidator.validate(event);

			eventDTO.setEvent(event);
			list.add(eventDTO);
			break;
		case "RegistEventBack":
			// Event の基本情報をセット
			if (data[10] != null && !data[10].isEmpty()) {
				event.setEventId(Integer.parseInt(data[10]));
			}
			event.setEventPlace(data[4]);
			event.setEventStartTime(parseDateTimeOrNull(data[5]));
			event.setEventEndTime(parseDateTimeOrNull(data[6]));
			event.setEventCapacity(Integer.parseInt(data[7]));
			event.setEventOtherInfo(data[8]);

			company = companyDBA.SearchCompanById(Integer.parseInt(data[2])).getCompany();
			event.setCompany(company);

			staff = staffDBA.searchStaffById(Integer.parseInt(data[3]));
			event.setStaff(staff);

			// 参加させる卒業生をセット
			if (data[9] != null && !data[9].isBlank()) {
				graduates = graduateDBA.searchGraduatesByGraduateStudentNumbers(
						data[9].split(","));
				event.setJoinGraduates(graduates);
			}
			eventDTO.setEvent(event);
			list.add(eventDTO);
			break;
		case "RegistEvent":
			company = companyDBA.SearchCompanById(Integer.parseInt(data[2])).getCompany();
			event.setCompany(company);

			staff = staffDBA.searchStaffById(Integer.parseInt(data[3]));
			event.setStaff(staff);

			// Event の基本情報をセット
			event.setEventPlace(data[4]);
			event.setEventStartTime(LocalDateTime.parse(data[5]));
			event.setEventEndTime(LocalDateTime.parse(data[6]));
			event.setEventCapacity(Integer.parseInt(data[7]));
			event.setEventOtherInfo(data[8]);

			// 参加させる卒業生をセット
			if (!data[9].isEmpty()) {
				graduates = graduateDBA.searchGraduatesByGraduateStudentNumbers(data[9].split(","));
			}
			event.setJoinGraduates(graduates);

			// eventProgress は登録時は2(開催)
			event.setEventProgress(EventProgress.ONGOING);

			if (data[10] == null || data[10].isEmpty()) {
				// 回答から遷移していない
				eventDBA.insertEvent(event);
			} else {
				// 回答から遷移している
				event.setEventId(Integer.parseInt(data[10]));
				eventDBA.updateEvent(event);
				answerDBA.deleteAnswerByEventId(Integer.parseInt(data[10]));
			}

			requestDBA = new RequestDBAccess();
			List<String> studentEmails = requestDBA.searchEmailsByCompanyId(Integer.parseInt(data[2]));

			List<String> graduateEmails = new ArrayList<String>();
			for (Graduate g : event.getJoinGraduates()) {
				graduateEmails.add(g.getGraduateEmail());
			}

			title = "【イベント通知】";
			body = MailBuilder.buildEventNotice(event);

			// 申請者にメール
			for (String toEmail : studentEmails) {
				sendMail(toEmail, title, body);
			}

			// 参加卒業生にメール
			for (String toEmail : graduateEmails) {
				sendMail(toEmail, title, body);
			}

			break;
		case "ScheduleArrangeSendForm":
			// data[2] = graduateStudentNumber
			graduate = new GraduateDBAccess().searchGraduateByGraduateStudentNumber(data[2]);
			List<Graduate> g = new ArrayList<Graduate>();
			g.add(graduate);
			eventDTO.setGraduates(g);

			List<Staff> s = new ArrayList<>();
			s = new StaffDBAcess().getAllStaffs();
			eventDTO.setStaffs(s);
			list.add(eventDTO);
			break;
		case "SendScheduleArrangeEmail":
		    // 1. 空のイベント作成
		    company.setCompanyId(Integer.parseInt(data[6]));
		    staff.setStaffId(Integer.parseInt(data[3]));
		    event.setCompany(company);
		    event.setStaff(staff);
		    event.setEventProgress(EventProgress.NONE);
		    event = eventDBA.insertEvent(event);

		    // 2. Answer 作成
		    Answer answer = new Answer();
		    graduate.setGraduateStudentNumber(data[2]);
		    answer.setGraduate(graduate);
		    answer.setEvent(event);
		    answer = answerDBA.insertAnswer(answer);

		    // 3. 卒業生取得
		    graduate = new GraduateDBAccess().searchGraduateByGraduateStudentNumber(data[2]);

		    // 4. メール本文生成
		    body = MailBuilder.buildScheduleArrangeEmail(graduate, answer, data[5]);

		    // 5. メール送信
		    sendMail(graduate.getGraduateEmail(), data[4], body);

		    // 6. DTO に格納
		    graduates.add(graduate);
		    eventDTO.setGraduates(graduates);
		    list.add(eventDTO);
		    break;
		case "EventEnd":
			// data[1] eventId
			eventDBA.eventEnd(Integer.parseInt(data[1]));
			List<JoinStudent> joinStudents = new JoinStudentDBAccess()
					.searchJoinStudentsByEventId(Integer.parseInt(data[1]));
			for (JoinStudent joinStudent : joinStudents) {
				requestDBA.cancelRequest(joinStudent.getEvent().getCompany().getCompanyId(),
						joinStudent.getStudent().getStudentNumber());
			}
			eventDTO = eventDBA.searchEventById(Integer.parseInt(data[1]));
			for (Graduate gr : eventDTO.getGraduates()) {
				graduateDBA.removeStaff(gr);
			}
			break;
		case "EventCancel":
		    eventDTO = eventDBA.eventCancel(Integer.parseInt(data[1]));
		    event = eventDTO.getEvent();

		    body = MailBuilder.buildEventCancelEmail(eventDTO);

		    title = "【イベント中止のお知らせ】";
		    // 申請者にメール
		    for (Student student : eventDTO.getStudents()) {
		        sendMail(student.getStudentEmail(), title, body);
		    }

		    // 参加卒業生にメール
		    for (Graduate grad : eventDTO.getGraduates()) {
		        sendMail(grad.getGraduateEmail(), title, body);
		    }
		    break;
		case "EventJoin":
		    // 参加登録
		    eventDBA.eventJoin(data[1], Integer.parseInt(data[2]));
		    eventDTO = eventDBA.searchEventById(Integer.parseInt(data[2]));
		    event = eventDTO.getEvent();

		    // 件名・本文
		    title = "【イベント通知】";
		    body = MailBuilder.buildEventJoinEmail(event);

		    // メール送信
		    sendMail(data[1] + "@jec.ac.jp", title, body);
		    break;
		case "EventNotJoin":
			// data[1] studentNumber
			// data[2] eventId
			eventDBA.eventNotJoin(data[1], Integer.parseInt(data[2]));
			break;
		case "JoinHistory":
			// data[1] studentNumber
			list = eventDBA.joinHistoryList(data[1]);
			break;
		}

		return list;
	}
}

package action;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dao.AnswerDBAccess;
import dao.CompanyDBAccess;
import dao.EventDBAccess;
import dao.GraduateDBAccess;
import dao.RequestDBAccess;
import dao.StaffDBAcess;
import dto.CompanyDTO;
import dto.EventDTO;
import model.Answer;
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
		// data[9] = graduates
		// data[10] = eventId
		// data[11] = answerId

		String action = data[0];
		EventDTO eventDTO = new EventDTO();
		List<EventDTO> list = new ArrayList<EventDTO>();
		List<Graduate> graduates = new ArrayList<>();
		
		EventDBAccess eventDBA = new EventDBAccess();
		CompanyDBAccess companyDBA = new CompanyDBAccess();
		AnswerDBAccess answerDBA = new AnswerDBAccess();
		StaffDBAcess staffDBA = new StaffDBAcess();
		GraduateDBAccess graduateDBA = new GraduateDBAccess();

		Event event = new Event();
		Company company = new Company();
		Staff staff = new Staff();
		Graduate graduate = new Graduate();
		
		String title;
		String body;

		switch (action) {
		case "RegistEventForm":
			List<CompanyDTO> companies = companyDBA.SearchCompanyWithGraduates(Integer.parseInt(data[2]));
			List<Staff> staffs = new StaffDBAcess().getAllStaffs();
			event.setCompany(companies.getFirst().getCompany());
			eventDTO.setEvent(event);
			eventDTO.setGraduates(companies.getFirst().getCompany().getGraduates());
			eventDTO.setStaffs(staffs);

			list.add(eventDTO);
			break;
		case "RegistEvent":
			company.setCompanyId(Integer.parseInt(data[2]));
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
			event.setEventProgress(2);

			
			if (data[10] == null || data[10].isEmpty()) {
				// 回答から遷移していない
				try {
					eventDBA.insertEvent(event);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// 回答から遷移している
				try {
					event.setEventId(Integer.parseInt(data[10]));
					eventDBA.updateEvent(event);
					answerDBA.deleteAnswer(Integer.parseInt(data[11]));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			RequestDBAccess requestDBA = new RequestDBAccess();
			List<String> studentEmails = requestDBA.searchEmailsByCompanyId(Integer.parseInt(data[2]));
			
			List<String> graduateEmails = new ArrayList<String>();
			for (Graduate g : event.getJoinGraduates()) {
				graduateEmails.add(g.getGraduateEmail());
			}

			// メールの件名・本文はあなたのデータに合わせて
			title = "【イベント通知】";
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");

			StringBuilder sb = new StringBuilder();

			sb.append(event.getCompany().getCompanyName())
			  .append(" のイベントが開催されます。\n\n");

			sb.append("■ 開催日時\n");
			sb.append("  ")
			  .append(event.getEventStartTime().format(fmt))
			  .append(" ～ ")
			  .append(event.getEventEndTime().format(fmt))
			  .append("\n\n");

			sb.append("■ 開催場所\n");
			sb.append("  ").append(event.getEventPlace()).append("\n\n");

			sb.append("■ 定員\n");
			sb.append("  ").append(event.getEventCapacity()).append(" 名\n\n");

			if (event.getEventOtherInfo() != null && !event.getEventOtherInfo().isEmpty()) {
			    sb.append("■ 備考\n");
			    sb.append("  ").append(event.getEventOtherInfo()).append("\n\n");
			}

			sb.append("■ 担当スタッフ\n");
			sb.append("  ").append(event.getStaff().getStaffName()).append("\n\n");
			sb.append("  ").append(event.getStaff().getStaffEmail());
			
			sb.append("詳細はシステムをご確認ください。\n");
			sb.append("※本メールは自動送信です。");

			body = sb.toString();

			// 申請者にメール
			for (String toEmail : studentEmails) {

				Email mail = new Email(); // 1通ずつ新しく作る
				mail.setTo("24jy0109@jec.ac.jp");
				mail.setSubject(title);
				mail.setBody(body + toEmail);

				boolean result = mail.send();

				if (!result) {
					System.out.println("送信失敗: " + toEmail);
				} else {
					System.out.println("送信成功: " + toEmail);
				}
			}
			
			// 参加卒業生にメール
			for (String toEmail : graduateEmails) {

				Email mail = new Email(); // 1通ずつ新しく作る
				mail.setTo("24jy0109@jec.ac.jp");
				mail.setSubject(title);
				mail.setBody(body + toEmail);

				boolean result = mail.send();

				if (!result) {
					System.out.println("送信失敗: " + toEmail);
				} else {
					System.out.println("送信成功: " + toEmail);
				}
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
			//			data[0]=コマンド
			//			data[1]=空文字
			//			data[2]=卒業生学籍番号
			//			data[3]=スタッフID
			//			data[4]=件名
			//			data[5]=本文
			//			data[6]=companyId
			
			// 空のイベント作成
			company.setCompanyId(Integer.parseInt(data[6]));
			staff.setStaffId(Integer.parseInt(data[3]));
			event.setCompany(company);
			event.setStaff(staff);
			event = eventDBA.insertEvent(event);
			
			Answer answer = new Answer();
			graduate.setGraduateStudentNumber(data[2]);
			answer.setGraduate(graduate);
			answer.setEvent(event);
			answer = answerDBA.insertAnswer(answer);

			// メールの件名・本文はあなたのデータに合わせて
			title = data[4];
			body = data[5];
			body += "\n\n回答URL:" + "http://localhost:8080/CareerSupportSystem/answer?command=AnswerForm&answerId="
					+ answer.getAnswerId();
			
			Email mail = new Email();
			graduate = new GraduateDBAccess().searchGraduateByGraduateStudentNumber(data[2]);
			
			body += "\n\n担当者名" + graduate.getStaff().getStaffName();
			body += "\n\n担当者メールアドレス" + graduate.getStaff().getStaffEmail();
			
			mail.setTo("24jy0109@jec.ac.jp");
			mail.setSubject(title);
			mail.setBody(body + "\nテスト用：" + graduate.getGraduateEmail());

			boolean result = mail.send();

			if (!result) {
				System.out.println("送信失敗: " + graduate.getGraduateEmail());
			} else {
				System.out.println("送信成功: " + graduate.getGraduateEmail());
			}
			graduates.add(graduate);
			eventDTO.setGraduates(graduates);
			list.add(eventDTO);
			break;
		}

		return list;
	}
}

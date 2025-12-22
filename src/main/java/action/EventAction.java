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
import model.EventProgress;
import model.Graduate;
import model.Staff;
import model.Student;

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

		Email mail = new Email();
		boolean result;
		String title;
		String body;
		StringBuilder sb = new StringBuilder();

		switch (action) {
		case "EventList":
			list = eventDBA.getAllEvents();
			break;
		case "EventDetail":
			// data[1] eventId
			eventDTO = eventDBA.searchEventById(Integer.parseInt(data[1]));
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
			sb.append("  ").append(event.getStaff().getStaffEmail()).append("\n\n");

			sb.append("詳細はシステムをご確認ください。\n");
			sb.append("※本メールは自動送信です。").append("\n\n");

			body = sb.toString();

			// 申請者にメール
			for (String toEmail : studentEmails) {

				mail = new Email(); // 1通ずつ新しく作る
				mail.setTo("24jy0109@jec.ac.jp");
				mail.setSubject(title);
				mail.setBody(body + toEmail);

				result = mail.send();

				if (!result) {
					System.out.println("送信失敗: " + toEmail);
				} else {
					System.out.println("送信成功: " + toEmail);
				}
			}

			// 参加卒業生にメール
			for (String toEmail : graduateEmails) {

				mail = new Email(); // 1通ずつ新しく作る
				mail.setTo("24jy0109@jec.ac.jp");
				mail.setSubject(title);
				mail.setBody(body + toEmail);

				result = mail.send();

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

			mail = new Email();
			graduate = new GraduateDBAccess().searchGraduateByGraduateStudentNumber(data[2]);

			body += "\n\n担当者名" + graduate.getStaff().getStaffName();
			body += "\n\n担当者メールアドレス" + graduate.getStaff().getStaffEmail();

			mail.setTo("24jy0109@jec.ac.jp");
			mail.setSubject(title);
			mail.setBody(body + "\nテスト用：" + graduate.getGraduateEmail());

			result = mail.send();

			if (!result) {
				System.out.println("送信失敗: " + graduate.getGraduateEmail());
			} else {
				System.out.println("送信成功: " + graduate.getGraduateEmail());
			}
			graduates.add(graduate);
			eventDTO.setGraduates(graduates);
			list.add(eventDTO);
			break;
		case "EventEnd":
			// data[1] eventId
			eventDBA.eventEnd(Integer.parseInt(data[1]));
			break;
		case "EventCancel":
			// data[1] eventId
			eventDTO = eventDBA.eventCancel(Integer.parseInt(data[1]));
			event = eventDTO.getEvent();
			staff = eventDTO.getStaffs().getFirst();

			sb.append("関係者各位\n\n");
			sb.append("以下のイベントにつきまして、誠に勝手ながら開催を中止（キャンセル）とさせていただきます。\n\n");

			// イベント情報
			sb.append("【イベント情報】\n");
			sb.append("企業名：").append(event.getCompany().getCompanyName()).append("\n");
			sb.append("開催日時：")
					.append(event.getEventStartTime().toString().replace("T", " "))
					.append(" ～ ")
					.append(event.getEventEndTime().toString().replace("T", " "))
					.append("\n");
			sb.append("担当職員：")
					.append(staff.getStaffName())
					.append("（")
					.append(staff.getStaffEmail())
					.append("）\n\n");

			// 参加卒業生
			sb.append("【参加予定だった卒業生】\n");

			for (Graduate gra : eventDTO.getGraduates()) {
				sb.append("・")
						.append(gra.getGraduateName())
						.append("（")
						.append(gra.getGraduateJobCategory())
						.append("）\n");

				sb.append("\n");
				sb.append("ご不明な点がございましたら、上記担当職員までお問い合わせください。\n\n");
				sb.append("何卒ご理解のほど、よろしくお願いいたします。\n");

				body = sb.toString();

				// 申請者にメール
				for (Student student : eventDTO.getStudents()) {

					mail = new Email(); // 1通ずつ新しく作る
					mail.setTo("24jy0109@jec.ac.jp");
					mail.setSubject("イベント中止のお知らせ");
					mail.setBody(student.getStudentName() + "様\n\n" + body + student.getStudentEmail());

					result = mail.send();

					if (!result) {
						System.out.println("送信失敗: " + student.getStudentEmail());
					} else {
						System.out.println("送信成功: " + student.getStudentEmail());
					}
				}

				// 参加卒業生にメール
				for (Graduate grad : eventDTO.getGraduates()) {

					mail = new Email(); // 1通ずつ新しく作る
					mail.setTo("24jy0109@jec.ac.jp");
					mail.setSubject("イベント中止のお知らせ");
					mail.setBody(grad.getGraduateName() + "様\n\n" + body + grad.getGraduateEmail());

					result = mail.send();

					if (!result) {
						System.out.println("送信失敗: " + grad.getGraduateEmail());
					} else {
						System.out.println("送信成功: " + grad.getGraduateEmail());
					}
				}
			}
			break;
		case "EventJoin":
			// data[1] studentNumber
			// data[2] eventId
			eventDBA.eventJoin(data[1], Integer.parseInt(data[2]));
			eventDTO = eventDBA.searchEventById(Integer.parseInt(data[2]));
			event = eventDTO.getEvent();
			
			// メールの件名・本文はあなたのデータに合わせて
			title = "【イベント通知】";
			fmt = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");

			sb.append(event.getCompany().getCompanyName())
					.append(" のイベントに参加登録が完了しました。\n\n");

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

			// 参加者にメール
				mail = new Email(); // 1通ずつ新しく作る
				mail.setTo(data[1] + "@jec.ac.jp");
				mail.setSubject(title);
				mail.setBody(body);

				result = mail.send();

				if (!result) {
					System.out.println("送信失敗: " + data[1] + "@jec.ac.jp");
				} else {
					System.out.println("送信成功: " + data[1] + "@jec.ac.jp");
				}
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

package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import action.AnswerAction;
import action.EmailAction;
import action.EventAction;
import action.GraduateAction;
import dto.EmailDTO;
import dto.EventDTO;
import exception.ValidationException;
import model.Answer;

@WebServlet("/event")
public class EventController extends BaseController {
	private static final long serialVersionUID = 1L;

	public EventController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// リクエスト情報に対する文字コードの設定する
		request.setCharacterEncoding("UTF-8");

		// 各画面から送信されるリクエストパラメータ"command"の値を取得する
		String command = request.getParameter("command");

		// 戻り値用のArrayList<Company>
		List<EventDTO> events = new ArrayList<EventDTO>();
		List<Answer> answers = new ArrayList<Answer>();
		List<EmailDTO> emails = new ArrayList<EmailDTO>();

		// sessionから値を取得
		HttpSession session = request.getSession();
		String studentNumber = (String) session.getAttribute("studentNumber");
		String role = (String) session.getAttribute("role");

		String data[];

		String companyId;
		String[] selectedGraduates;

		// Test
		System.out.println(studentNumber);
		System.out.println(role);
		System.out.println(command);
		System.out.println();

		// リクエストパラメータ"command"の値がない場合、セッションがstudent, staffではない
		if (command == null || command.isEmpty() || (role != "student" && role != "staff")) {
			response.sendRedirect("login");
			return;
		}

		EventAction eventAction = new EventAction();
		EmailAction emailAction = new EmailAction();

		// 次画面用の変数
		String nextPage = null;
		// リクエストパラメータ"command", sessionのroleの値に対応した処理を実行する
		if (role.equals("staff")) {
			// 職員の遷移
			switch (command) {
			case "RegistEventForm":
				nextPage = "staff/RegistEventInfo.jsp";
				companyId = (String) request.getParameter("companyId");
				try {
					events = eventAction.execute(new String[] { command, "", companyId });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				break;
			case "RegistEventConfirm":
				nextPage = "staff/EventInfoConfirm.jsp";
				// 配列を作成（必要な項目分のサイズを確保）
				data = new String[12];

				data[0] = command;
				data[1] = "";
				data[2] = request.getParameter("companyId");
				data[3] = request.getParameter("staffId");
				data[4] = request.getParameter("eventPlace");
				data[5] = request.getParameter("eventStartTime");
				data[6] = request.getParameter("eventEndTime");
				data[7] = request.getParameter("eventCapacity");
				data[8] = request.getParameter("eventOtherInfo");
				// チェックボックスで選択された参加卒業生
				selectedGraduates = request.getParameterValues("graduateStudents");
				data[9] = (selectedGraduates != null) ? String.join(",", selectedGraduates) : "";
				data[10] = request.getParameter("eventId");
				data[11] = request.getParameter("answerId");
				try {
					events = eventAction.execute(data);
					request.setAttribute("event", events.getFirst());
				} catch (ValidationException e) {
					request.setAttribute("error", e.getMessage());
					request.setAttribute("inputEvent", e.getEvent());
					try {
						events = eventAction.execute(new String[] { "RegistEventForm", "", data[2] });
					} catch (Exception e1) {
						handleException(e1, request, response, "staff/AppointMenu.jsp");
						return;
					}
					nextPage = "staff/RegistEventInfo.jsp";
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				break;
			case "RegistEventBack":
				nextPage = "staff/RegistEventInfo.jsp";
				// 配列を作成（必要な項目分のサイズを確保）
				data = new String[11];
				data[0] = command;
				data[1] = "";
				data[2] = request.getParameter("companyId");
				data[3] = request.getParameter("staffId");
				data[4] = request.getParameter("eventPlace");
				data[5] = request.getParameter("eventStartTime");
				data[6] = request.getParameter("eventEndTime");
				data[7] = request.getParameter("eventCapacity");
				data[8] = request.getParameter("eventOtherInfo");

				// チェックボックスで選択された参加卒業生
				selectedGraduates = request.getParameterValues("graduateStudents");
				data[9] = (selectedGraduates != null) ? String.join(",", selectedGraduates) : "";

				data[10] = request.getParameter("eventId");
				try {
					events = eventAction.execute(data);
					request.setAttribute("inputEvent", events.getFirst().getEvent());
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}

				try {
					events = eventAction.execute(new String[] { "RegistEventForm", "", data[2] });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				break;
			case "RegistEvent":
				// 配列を作成（必要な項目分のサイズを確保）
				data = new String[12];

				data[0] = command; // "RegistEvent"
				data[1] = "";
				data[2] = request.getParameter("companyId");
				data[3] = request.getParameter("staffId");
				data[4] = request.getParameter("eventPlace");
				data[5] = request.getParameter("eventStartTime");
				data[6] = request.getParameter("eventEndTime");
				data[7] = request.getParameter("eventCapacity");
				data[8] = request.getParameter("eventOtherInfo");

				// チェックボックスで選択された参加卒業生
				selectedGraduates = request.getParameterValues("graduateStudents");
				data[9] = (selectedGraduates != null) ? String.join(",", selectedGraduates) : "";

				data[10] = request.getParameter("eventId");
				try {
					events = eventAction.execute(data);
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				setFlashMessage(request, FLASH_SUCCESS, "イベントを開催しました。");
				response.sendRedirect("event?command=EventList");
				return;
			case "ScheduleArrangeSendForm":
				nextPage = "staff/ScheduleArrangeSend.jsp";
				String graduateStudentNumber = request.getParameter("graduateStudentNumber");
				try {
					events = eventAction.execute(new String[] { command, "", graduateStudentNumber });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				break;
			case "ScheduleArrangeEmailConfirm":
				nextPage = "staff/ScheduleArrangeEmailConfirm.jsp";
				data = new String[7];
				data[0] = command;
				data[1] = "";
				data[2] = (String) request.getParameter("graduateStudentNumber");
				data[3] = (String) request.getParameter("staffId");
				data[4] = (String) request.getParameter("mailTitle");
				data[5] = (String) request.getParameter("mailBody");
				data[6] = (String) request.getParameter("companyId");

				try {
					emails = emailAction.execute(data);
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				request.setAttribute("email", emails.getFirst());
				break;
			case "ScheduleArrangeEmailBack":
				nextPage = "staff/ScheduleArrangeSend.jsp";
				data = new String[7];
				data[0] = command;
				data[1] = "";
				data[2] = (String) request.getParameter("graduateStudentNumber");
				data[3] = (String) request.getParameter("staffId");
				data[4] = (String) request.getParameter("mailTitle");
				data[5] = (String) request.getParameter("mailBody");
				data[6] = (String) request.getParameter("companyId");

				try {
					emails = emailAction.execute(data);
					request.setAttribute("email", emails.getFirst());
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}

				try {
					events = eventAction.execute(new String[] { "ScheduleArrangeSendForm", "", data[2] });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				break;
			case "SendScheduleArrangeEmail":
				data = new String[7];
				data[0] = command;
				data[1] = "";
				data[2] = (String) request.getParameter("graduateStudentNumber");
				data[3] = (String) request.getParameter("staffId");
				// 卒業生にスタッフを割り当てる
				try {
					new GraduateAction().execute(data);
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}

				data[4] = (String) request.getParameter("mailTitle");
				data[5] = (String) request.getParameter("mailBody");
				data[6] = (String) request.getParameter("companyId");

				try {
					events = eventAction.execute(data);
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				setFlashMessage(request, FLASH_SUCCESS, "メールを送信しました。");
				response.sendRedirect("event?command=RegistEventForm&companyId=" + data[6]);
				return;
			case "EventList":
				nextPage = "staff/EventList.jsp";
				try {
					events = eventAction.execute(new String[] { command, "" });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				break;
			case "EventDetail":
				nextPage = "staff/EventDetail.jsp";
				String eventId = request.getParameter("eventId");
				try {
					events = eventAction.execute(new String[] { command, "", eventId });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				request.setAttribute("event", events.getFirst());
				break;
			case "yesAnswer":
				nextPage = "staff/RegistEventInfo.jsp";
				data = new String[3];
				data[0] = command;
				data[1] = request.getParameter("answerId");
				data[2] = request.getParameter("choice");

				try {
					answers = new AnswerAction().execute(data);
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				Answer answer = answers.getFirst();
				request.setAttribute("inputEvent", answer.toInputEvent());
				companyId = String.valueOf(answer.getGraduate().getCompany().getCompanyId());
				try {
					events = eventAction.execute(new String[] { "RegistEventForm", "", companyId });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				break;
			case "EventEnd":
				eventId = request.getParameter("eventId");
				try {
					eventAction.execute(new String[] { command, eventId });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				response.sendRedirect("event?command=EventList");
				return;
			case "EventCancel":
				eventId = request.getParameter("eventId");
				try {
					eventAction.execute(new String[] { command, eventId });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				response.sendRedirect("event?command=EventList");
				return;
			}
		} else {
			// 学生の遷移
			switch (command) {
			case "EventList":
				nextPage = "student/EventList.jsp";
				try {
					events = eventAction.execute(new String[] { command, studentNumber });
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
				}
				break;
			case "EventDetail":
				nextPage = "student/EventDetail.jsp";
				String eventId = request.getParameter("eventId");
				try {
					events = eventAction.execute(new String[] { command, studentNumber, eventId });
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
				}
				request.setAttribute("event", events.getFirst());
				break;
			case "EventJoin":
				eventId = request.getParameter("eventId");
				try {
					events = eventAction.execute(new String[] { command, studentNumber, eventId });
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
				}
				setFlashMessage(request, FLASH_SUCCESS, "参加登録しました。");
				response.sendRedirect("event?command=JoinHistory");
				return;
			case "EventNotJoin":
				eventId = request.getParameter("eventId");
				try {
					eventAction.execute(new String[] { command, studentNumber, eventId });
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
				}
				setFlashMessage(request, FLASH_SUCCESS, "不参加登録しました。");
				response.sendRedirect("event?command=JoinHistory");
				return;
			case "JoinHistory":
				nextPage = "student/JoinHistory.jsp";
				try {
					events = eventAction.execute(new String[] { command, studentNumber });
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
				}
				break;
			}
		}
		request.setAttribute("events", events);
		// 次のページへの転送
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

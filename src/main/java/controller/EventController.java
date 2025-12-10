package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import action.EventAction;
import action.GraduateAction;
import dto.EventDTO;

@WebServlet("/event")
public class EventController extends HttpServlet {
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

		// sessionから値を取得
		HttpSession session = request.getSession();
		String studentNumber = (String) session.getAttribute("studentNumber");
		String role = (String) session.getAttribute("role");
		
		String data[];
		
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
		
		// 次画面用の変数
		String nextPage = null;
		// リクエストパラメータ"command", sessionのroleの値に対応した処理を実行する
		if (role.equals("staff")) {
			// 職員の遷移
			switch (command) {
			case "RegistEventForm":
				nextPage = "staff/RegistEventInfo.jsp";
				String companyId = (String) request.getParameter("companyId");
				try {
					events = eventAction.execute(new String[] { command, "",  companyId});
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "RegistEvent":
				// 配列を作成（必要な項目分のサイズを確保）
				data = new String[10];

				data[0] = command;  // "RegistEvent"
				data[1] = "";				
				data[2] = request.getParameter("companyId");
				data[3] = request.getParameter("staffId");
				data[4] = request.getParameter("eventPlace");
				data[5] = request.getParameter("eventStartTime");
				data[6] = request.getParameter("eventEndTime");
				data[7] = request.getParameter("eventCapacity");
				data[8] = request.getParameter("eventOtherInfo");

				// チェックボックスで選択された参加卒業生
				String[] selectedGraduates = request.getParameterValues("graduateStudents");
				data[9] = (selectedGraduates != null) ? String.join(",", selectedGraduates) : "";
				
				try {
					events = eventAction.execute(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
				response.sendRedirect("event?command=EventList");
				return;
			case "ScheduleArrangeSendForm":
				nextPage = "staff/ScheduleArrangeSend.jsp";
				String graduateStudentNumber = request.getParameter("graduateStudentNumber");
				try {
					events = eventAction.execute(new String[] { command, "",  graduateStudentNumber});
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "SendScheduleArrangeEmail":
				data = new String[6];
				data[0] = command;
				data[1] = "";
				data[2] = (String) request.getParameter("graduateStudentNumber");
				data[3] = (String) request.getParameter("staffId");
				try {
					new GraduateAction().execute(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				data[4] = request.getParameter("mailTitle");
				data[5] = request.getParameter("mailBody");

				try {
					events = eventAction.execute(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
				companyId = (String) request.getParameter("companyId");
				response.sendRedirect("event?command=RegistEventForm&companyId=" + companyId);
				return;
			case "EventList":
				nextPage = "staff/EventList.jsp";
				break;
			}
		} else {
			// 学生の遷移
//			switch (command) {
//			case "CompanyList":
//				nextPage = "student/CompanyList.jsp";
//				String companyName = (String) request.getParameter("companyName");
//				if (companyName == null) companyName = "";
//				try {
//					companies = companyAction.execute(new String[] { "CompanyList", studentNumber, companyName});
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				break;
//			case "CompanyDetail":
//				nextPage = "student/CompanyDetail.jsp";
//				String companyId = (String) request.getParameter("companyId");
//				try {
//					companies = companyAction.execute(new String[] { "CompanyDetail", studentNumber, companyId});
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				break;
//			}
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

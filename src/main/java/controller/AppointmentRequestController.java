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

import action.AppointmentRequestAction;
import action.CompanyAction;
import dto.CompanyDTO;
import model.Request;

@WebServlet("/appointment_request")
public class AppointmentRequestController extends BaseController {
	private static final long serialVersionUID = 1L;

	public AppointmentRequestController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// リクエスト情報に対する文字コードの設定する
		request.setCharacterEncoding("UTF-8");

		// 各画面から送信されるリクエストパラメータ"command"の値を取得する
		String command = request.getParameter("command");

		// sessionから値を取得
		HttpSession session = request.getSession();
		String studentNumber = (String) session.getAttribute("studentNumber");
		String role = (String) session.getAttribute("role");

		List<Request> list = new ArrayList<>();

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

		AppointmentRequestAction appointmentRequestAction = new AppointmentRequestAction();

		// 次画面用の変数
		String nextPage = null;
		// リクエストパラメータ"command", sessionのroleの値に対応した処理を実行する
		if (role.equals("staff")) {
			// 職員の遷移
			switch (command) {
			case "RequestList":
				nextPage = "staff/RequestList.jsp";
				String companyId = (String) request.getParameter("companyId");
				try {
					list = appointmentRequestAction.execute(new String[] { command, "", companyId });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				

				if (list == null || list.isEmpty()) {
					try {
						List<CompanyDTO> companies = new CompanyAction()
								.execute(new String[] { "CompanyName", "", companyId });
						request.setAttribute("showCompany", companies.getFirst());
					} catch (Exception e) {
						request.setAttribute("error", e.getMessage());
						nextPage = "staff/AppointMenu.jsp";
						return;
					}
				}

				break;
			}
		} else {
			// 学生の遷移
			switch (command) {
			case "ApplyRequest":
			case "CancelRequest":
				String companyId = (String) request.getParameter("companyId");
				try {
					appointmentRequestAction.execute(new String[] { command, studentNumber, companyId });
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
				}
		        if ("ApplyRequest".equals(command)) {
		            setFlashMessage(
		                request,
		                FLASH_SUCCESS,
		                "申請しました。"
		            );
		        } else if ("CancelRequest".equals(command)) {
		            setFlashMessage(
		                request,
		                FLASH_SUCCESS,
		                "申請を取り消しました。"
		            );
		        }
				response.sendRedirect("company?command=CompanyList");
				return;
			}
		}

		request.setAttribute("requests", list);
		// 次のページへの転送
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
		rd.forward(request, response);
	}

}

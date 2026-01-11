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

import action.JoinStudentAction;
import model.JoinStudent;

/**
 * Servlet implementation class GraduateController
 */
@WebServlet("/join_student")
public class JoinStudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public JoinStudentController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// リクエスト情報に対する文字コードの設定する
		request.setCharacterEncoding("UTF-8");

		// 各画面から送信されるリクエストパラメータ"command"の値を取得する
		String command = request.getParameter("command");

		//		 戻り値用のArrayList<Student>
		List<JoinStudent> joinStudents = new ArrayList<>();

		// sessionから値を取得
		HttpSession session = request.getSession();
		String studentNumber = (String) session.getAttribute("studentNumber");
		String role = (String) session.getAttribute("role");
		String[] data;

		// Test
		System.out.println(studentNumber);
		System.out.println(role);
		System.out.println(command);
		System.out.println();

		JoinStudentAction joinStudentAction = new JoinStudentAction();

		// 次画面用の変数
		String nextPage = null;

		// リクエストパラメータ"command", sessionのroleの値に対応した処理を実行する
		switch (command) {
		case "JoinStudentList":
			nextPage = "staff/JoinStudentList.jsp";
			String eventId = request.getParameter("eventId");
			try {
				joinStudents = joinStudentAction.execute(new String[] {command, eventId});
			} catch (Exception e) {
				nextPage = "staff/AppointMenu.jsp";
				request.setAttribute("error", e.getMessage());
			}
			break;
		default:
			response.sendRedirect("login");
			return;
		}

		request.setAttribute("joinStudents", joinStudents);
		// 次のページへの転送
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}

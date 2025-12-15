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

import action.AnswerAction;
import model.Answer;

/**
 * Servlet implementation class GraduateController
 */
@WebServlet("/answer")
public class AnswerController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AnswerController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// リクエスト情報に対する文字コードの設定する
		request.setCharacterEncoding("UTF-8");

		// 各画面から送信されるリクエストパラメータ"command"の値を取得する
		String command = request.getParameter("command");

		//		 戻り値用のArrayList<Company>
		List<Answer> answers = new ArrayList<>();

		// sessionから値を取得
		HttpSession session = request.getSession();
		String studentNumber = (String) session.getAttribute("studentNumber");
		String role = (String) session.getAttribute("role");

		// Test
		System.out.println(studentNumber);
		System.out.println(role);
		System.out.println(command);
		System.out.println();

		AnswerAction answerAction = new AnswerAction();

		// 次画面用の変数
		String nextPage = null;

		// リクエストパラメータ"command", sessionのroleの値に対応した処理を実行する
		switch (command) {
		case "AnswerForm":
			nextPage = "graduate/Answer.jsp";
			break;
		case "registAnswer":
			nextPage = "graduate/CompleteRegistAnswer.jsp";
			String[] data = new String[9];
			data[0] = command;
			data[1] = request.getParameter("answerId");
			data[2] = request.getParameter("eventAvailability");

			data[3] = request.getParameter("firstChoiceStart");
			data[4] = request.getParameter("firstChoiceEnd");

			data[5] = request.getParameter("secondChoiceStart");
			data[6] = request.getParameter("secondChoiceEnd");

			data[7] = request.getParameter("thirdChoiceStart");
			data[8] = request.getParameter("thirdChoiceEnd");
			
			try {
				answers = answerAction.execute(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "ScheduleAnswerCheck":
			nextPage = "staff/ScheduleAnswerCheck.jsp";
			// セッションがstaffではない
			if (role != "staff") {
				response.sendRedirect("login");
				return;
			}
			try {
				answers = answerAction.execute(new String[] { command });
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			response.sendRedirect("login");
			return;
		}

		request.setAttribute("answers", answers);
		// 次のページへの転送
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}

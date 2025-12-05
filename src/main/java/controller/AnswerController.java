package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import action.AnswerAction;

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

		// 戻り値用のArrayList<Company>
		//		List<CompanyDTO> companies = new ArrayList<CompanyDTO>();

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
				String answerId = request.getParameter("answerId");
				String eventAvailabilityStr = request.getParameter("eventAvailability");
				String firstChoiceStr = request.getParameter("firstChoice");
				String secondChoiceStr = request.getParameter("secondChoice");
				String thirdChoiceStr = request.getParameter("thirdChoice");
				break;
			case "CompanyList":
				// セッションがstaffではない
				if (role != "staff") {
					response.sendRedirect("login");
					return;
				}
				break;
			default:
				response.sendRedirect("login");
				return;
			}

		// 次のページへの転送
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
}

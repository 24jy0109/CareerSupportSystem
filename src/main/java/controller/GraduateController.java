package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import action.GraduateAction;

/**
 * Servlet implementation class GraduateController
 */
@WebServlet("/graduate")
public class GraduateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GraduateController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

		// リクエストパラメータ"command"の値がない場合、セッションがstudent, staffではない
		if (command == null || command.isEmpty() || (role != "student" && role != "staff")) {
			response.sendRedirect("login");
			return;
		}

		GraduateAction graduateAction = new GraduateAction();

		// 次画面用の変数
		String nextPage = null;
		
		// リクエストパラメータ"command", sessionのroleの値に対応した処理を実行する
		if (role.equals("staff")) {
			// 職員の遷移
			switch (command) {
			case "AssignStaff":
				String graduateStudentNumber = (String) request.getParameter("graduateStudentNumber");
				String staffId = (String) request.getParameter("staffId");
				String companyId = (String) request.getParameter("companyId");
				try {
					graduateAction.execute(new String[] { "AssignStaff", "", graduateStudentNumber, staffId });
				} catch (Exception e) {
					e.printStackTrace();
				}
				response.sendRedirect("company?command=RegistEvent&companyId=" + companyId);
				return;
			}
		} else {
			// 学生の遷移
			switch (command) {
			case "CompanyList":
				break;
			}
		}
		
//		request.setAttribute("companies", companies);
		// 次のページへの転送
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
		rd.forward(request, response);
	}

}

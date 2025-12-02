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

import action.CompanyAction;
import dto.CompanyDTO;

@WebServlet("/company")
public class CompanyController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CompanyController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// リクエスト情報に対する文字コードの設定する
		request.setCharacterEncoding("UTF-8");

		// 各画面から送信されるリクエストパラメータ"command"の値を取得する
		String command = request.getParameter("command");

		// 戻り値用のArrayList<Company>
		List<CompanyDTO> companies = new ArrayList<CompanyDTO>();

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
		
		CompanyAction companyAction = new CompanyAction();
		
		// 次画面用の変数
		String nextPage = null;
		// リクエストパラメータ"command", sessionのroleの値に対応した処理を実行する
		if (role.equals("staff")) {
			// 職員の遷移
			switch (command) {
			case "CompanyList":
				nextPage = "staff/CompanyList.jsp";
				String companyName = (String) request.getParameter("companyName");
				if (companyName == null) companyName = "";
				try {
					companies = companyAction.execute(new String[] { "CompanyList", "",  companyName});
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		} else {
			// 学生の遷移
			switch (command) {
			case "CompanyList":
				nextPage = "student/CompanyList.jsp";
				String companyName = (String) request.getParameter("companyName");
				if (companyName == null) companyName = "";
				try {
					companies = companyAction.execute(new String[] { "CompanyList", studentNumber, companyName});
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "CompanyDetail":
				nextPage = "student/CompanyDetail.jsp";
				String companyId = (String) request.getParameter("companyId");
				try {
					companies = companyAction.execute(new String[] { "CompanyDetail", studentNumber, companyId});
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "CompanyRegister":
				nextPage = "staff/CompanyRegisterConfirm.jsp";
				
			}
		}
		request.setAttribute("companies", companies);
		// 次のページへの転送
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

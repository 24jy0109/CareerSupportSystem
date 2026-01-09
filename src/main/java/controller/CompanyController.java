package controller;

import java.io.IOException;
import java.io.PrintWriter;
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
import dao.CompanyDBAccess;
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
		String companyName = null;
		// リクエストパラメータ"command", sessionのroleの値に対応した処理を実行する
		if (role.equals("staff")) {
			// 職員の遷移
			switch (command) {
			case "CompanyList":
				nextPage = "staff/CompanyList.jsp";
				companyName = (String) request.getParameter("companyName");
				if (companyName == null)
					companyName = "";
				try {
					companies = companyAction.execute(new String[] { "CompanyList", "", companyName });
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			//				企業名入力、確認
			case "CompanyRegister":
				nextPage = "staff/CompanyRegister.jsp";
				String companyId = request.getParameter("companyId");

				if (companyId != null) {
					List<CompanyDTO> result = new ArrayList<>();
					try {
						result = companyAction.execute(
								new String[] { "findCompanyName", companyId, "" });
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (!result.isEmpty()) {
						CompanyDTO dto = result.get(0);
						request.setAttribute("companyId", companyId);
						request.setAttribute(
								"companyName",
								dto.getCompany().getCompanyName());
					}
					System.out.println("companyId:" + companyId);
					System.out.println("companyName:" + companyName);
					System.out.println("result:" + result);
				}

				break;

			case "AjaxSearchCompany":
				String name = request.getParameter("companyName");
				List<CompanyDTO> list = new ArrayList<>();
				CompanyDBAccess dbAccess = new CompanyDBAccess(); // ← ここで DAO を作る
				try {
					list = dbAccess.findSimilarCompany(name); // DAOで曖昧検索
				} catch (Exception e) {
					e.printStackTrace();
				}

				// JSONに変換して返す
				response.setContentType("application/json;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("[");
				for (int i = 0; i < list.size(); i++) {
					out.print("{\"companyName\":\"" + list.get(i).getCompany().getCompanyName() + "\"}");
					if (i < list.size() - 1)
						out.print(",");
				}
				out.print("]");
				out.flush();
				return; // JSP遷移しない

			case "CompanyRegisterNext":

				companyId = null;
				System.out.println("companyId:" + companyId);
				companyId = request.getParameter("companyId");
				companyName = request.getParameter("companyName");

				CompanyDBAccess db = new CompanyDBAccess();

				System.out.println("companyId:" + companyId);
				System.out.println("companyName:" + companyName);

				if (companyId == null || companyId == "") {
					// ① 空白チェック
					if (companyName == null || companyName.replaceAll("[ 　]", "").isEmpty()) {
						request.setAttribute("error", "企業名を入力してください。");
						nextPage = "staff/CompanyRegister.jsp";
						System.out.println("空白チェック");
						break;
					}

					// (株) が含まれていないかチェック
					if (companyName.matches(".*[\\(（]株[\\)）].*")) {
						request.setAttribute("error", "(株) は使用できません。");
						request.setAttribute("companyName", companyName);
						request.setAttribute("companyId", companyId);
						nextPage = "staff/CompanyRegister.jsp";
						System.out.println("（株）チェック");
						break;
					}

					// 前後の「株式会社」を削除
					String normalizedCompanyName = companyName.trim();
					normalizedCompanyName = normalizedCompanyName.replaceAll("^株式会社", ""); // 先頭
					normalizedCompanyName = normalizedCompanyName.replaceAll("株式会社$", ""); // 末尾
					normalizedCompanyName = normalizedCompanyName.trim();

					boolean exists = false;
					try {
						exists = db.existsCompanyName(companyName);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (exists) {
						request.setAttribute("error", "この企業名はすでに登録されています。");
						request.setAttribute("companyName", companyName);
						nextPage = "staff/CompanyRegister.jsp";
						System.out.println("重複チェック（あり）");
					} else {
						// 類似企業検索
						List<CompanyDTO> similarList = new ArrayList<>();
						try {
							similarList = db.findSimilarCompany(normalizedCompanyName);
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (!similarList.isEmpty()) {
							// 類似企業があれば JSP に表示
							request.setAttribute("similarCompanies", similarList);
						}
						// 入力値をセットして JSP に戻す（確認ボタンを押すまで登録しない）
						request.setAttribute("companyName", companyName);
						request.setAttribute("companyId", companyId);
						nextPage = "staff/CompanyRegisterConfirm.jsp";
						System.out.println("重複チェック（なし）");
						break;
					}

				} else {
					// ① 空白チェック
					if (companyName == null || companyName.replaceAll("[ 　]", "").isEmpty()) {
						request.setAttribute("error", "企業名を入力してください。");
						request.setAttribute("companyId", companyId);
						nextPage = "staff/CompanyRegister.jsp";
						System.out.println("空白チェック");
						break;
					}

					boolean exists = false;
					try {
						exists = db.existsCompanyName(companyName);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (exists) {
						request.setAttribute("error", "この企業名はすでに登録されています。");
						request.setAttribute("companyName", companyName);
						nextPage = "staff/CompanyRegister.jsp";
						break;
					}

					// ② 確認画面へ
					request.setAttribute("companyId", companyId);
					request.setAttribute("companyName", companyName);
					nextPage = "staff/CompanyRegisterConfirm.jsp";
					break;
				}

				break;

			//				企業名追加
			case "CompanyRegisterConfirm":
				nextPage = "staff/AppointMenu.jsp";
				companyId = request.getParameter("companyId");
				companyName = request.getParameter("companyName");
				try {
					if (companyId == null || companyId.isEmpty()) {
						companyAction.execute(
								new String[] { "CompanyRegister", "", companyName });
					} else {
						companyAction.execute(
								new String[] { "CompanyUpdate", companyId, companyName });
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;

			case "CompanyRegisterBack":
				nextPage = "staff/CompanyRegister.jsp";
				companyName = request.getParameter("companyName");
				request.setAttribute("companyName", companyName);
				request.setAttribute("companyId", request.getParameter("companyId"));
				break;

			case "RegistEvent":
				nextPage = "staff/RegistEventInfo.jsp";
				companyId = (String) request.getParameter("companyId");
				try {
					companies = companyAction.execute(new String[] { command, "", companyId });
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
				companyName = (String) request.getParameter("companyName");
				if (companyName == null)
					companyName = "";
				try {
					companies = companyAction.execute(new String[] { "CompanyList", studentNumber, companyName });
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "CompanyDetail":
				nextPage = "student/CompanyDetail.jsp";
				String companyId = (String) request.getParameter("companyId");
				try {
					companies = companyAction.execute(new String[] { "CompanyDetail", studentNumber, companyId });
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

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

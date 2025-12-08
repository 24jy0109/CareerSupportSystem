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
import action.GraduateAction;
import dao.CompanyDBAccess;
import dao.CourseDBAccess;
import dto.CompanyDTO;
import model.Course;

/**
 * Servlet implementation class GraduateController
 */
@WebServlet("/graduate")
public class GraduateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GraduateController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

		// リクエストパラメータ"command"の値がない場合、セッションがstudent, staffではない
		if (command == null || command.isEmpty() || (role != "student" && role != "staff")) {
			response.sendRedirect("login");
			return;
		}

		GraduateAction graduateAction = new GraduateAction();
		// 戻り値用のArrayList<Company>
		List<CompanyDTO> companies = new ArrayList<CompanyDTO>();

		CompanyAction companyAction = new CompanyAction();

		// 次画面用の変数
		String nextPage = null;
		String companyName = null;
		String jobType = null;
		String graduateName = null;
		String courseName = null;
		String graduateStudentNumber = null;
		String graduateEmail = null;
		String otherInfo = null;

		String companyId = null;
		String courseCode = null;

		// リクエストパラメータ"command", sessionのroleの値に対応した処理を実行する
		if (role.equals("staff")) {
			// 職員の遷移
			switch (command) {
			case "AssignStaff":
				graduateStudentNumber = (String) request.getParameter("graduateStudentNumber");
				String staffId = (String) request.getParameter("staffId");
				companyId = (String) request.getParameter("companyId");
				try {
					graduateAction.execute(new String[] { "AssignStaff", "", graduateStudentNumber, staffId });
				} catch (Exception e) {
					e.printStackTrace();
				}
				response.sendRedirect("company?command=RegistEvent&companyId=" + companyId);
				return;

			//				連絡先登録

			//				入力画面
			case "RegistEmail":
				nextPage = "common/RegistEmail.jsp";

				//				企業名
				companyId = (String) request.getParameter("companyId");
				if (companyId == null)
					companyId = "";
				try {
					companies = companyAction.execute(new String[] { "CompanyList", "", companyId });
				} catch (Exception e) {
					e.printStackTrace();
				}
				request.setAttribute("companies", companies);
				//				学科
				List<Course> courses = new ArrayList<>();
				CourseDBAccess courseDB = new CourseDBAccess();
				try {
					courses = courseDB.getAllCourses();
				} catch (Exception e) {
					e.printStackTrace();
				}
				request.setAttribute("courses", courses);
				break;
			//				確認へ
			case "RegistEmailNext":
				nextPage = "/common/RegistEmailConfirm.jsp";
				// 入力値取得
				companyId = request.getParameter("companyId");

				jobType = request.getParameter("jobType");
				graduateName = request.getParameter("graduateName");

				courseCode = request.getParameter("courseCode");

				graduateStudentNumber = request.getParameter("graduateStudentNumber");
				graduateEmail = request.getParameter("graduateEmail");
				otherInfo = request.getParameter("otherInfo");

				// ★ ID → 名前に変換（ここ超重要）
				CompanyDBAccess cdb = new CompanyDBAccess();
				CourseDBAccess coursedb = new CourseDBAccess();

				try {
					companyName = cdb.serchCompanyNameById(companyId);
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				try {
					courseName = coursedb.findCourseNameById(courseCode);
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

				// JSPへ渡す値
				request.setAttribute("companyId", companyId); // 登録処理用
				request.setAttribute("companyName", companyName); // 表示用

				request.setAttribute("jobType", jobType);
				request.setAttribute("graduateName", graduateName);

				request.setAttribute("courseCode", courseCode); // 登録処理用
				request.setAttribute("courseName", courseName); // 表示用

				request.setAttribute("graduateStudentNumber", graduateStudentNumber);
				request.setAttribute("graduateEmail", graduateEmail);
				request.setAttribute("otherInfo", otherInfo);
				break;

			//				連絡先情報をデータベースに登録
			case "RegistEmailConfirm":
				nextPage = "common/CompleteRegistEmail.jsp";
				//				配列に挿入！
				String[] registEmailInfo = new String[8];
				registEmailInfo[0] = "RegisterGraduate";
				registEmailInfo[1] = request.getParameter("companyId");
				registEmailInfo[2] = request.getParameter("jobType");
				registEmailInfo[3] = request.getParameter("graduateName");
				registEmailInfo[4] = request.getParameter("courseCode");
				registEmailInfo[5] = request.getParameter("graduateStudentNumber");
				registEmailInfo[6] = request.getParameter("mailAddress");
				registEmailInfo[7] = request.getParameter("otherInfo");

				try {
					graduateAction.execute(registEmailInfo);
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} // ← requestを渡す！

				break;
				
			case "AppointMenu":
				nextPage = "staff/AppointMenu.jsp";
				break;
			}
		} else {
			// 学生の遷移
			switch (command) {
			case "CompanyList":
				break;
			}
		}
		// 次のページへの転送
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
		rd.forward(request, response);
	}
}
//		request.setAttribute("companies", companies);

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
import dao.CourseDBAccess;
import dto.CompanyDTO;
import model.Course;
import model.Graduate;

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
		doPost(request, response);
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
		CompanyAction companyAction = new CompanyAction();
		List<CompanyDTO> companies = new ArrayList<CompanyDTO>();

		List<Graduate> graduate = new ArrayList<Graduate>();
		//				学科
		List<Course> courses = new ArrayList<>();
//		CourseDBAccess courseDB = new CourseDBAccess();

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
			//				連絡先登録
			//				入力画面
			case "RegistEmail":
				
				nextPage = "common/RegistEmail.jsp";
				//
				// Back の場合、入力値が送られてくる
				String backCompanyId = request.getParameter("companyId");
				String backCourseCode = request.getParameter("courseCode");
				String job = request.getParameter("jobType");
				String name = request.getParameter("graduateName");
				String sn = request.getParameter("graduateStudentNumber");
				String mail = request.getParameter("graduateEmail"); // hidden名に合わせる
				String info = request.getParameter("otherInfo");

				// 会社IDと会社名を設定
				if (backCompanyId != null && !backCompanyId.isEmpty()) {
					companyId = backCompanyId; // 登録用ID
					try {
						graduate = graduateAction.execute(new String[] {"findCompanyName",companyId});
						companyName = graduate.get(0).getCompany().getCompanyName();
					} catch (Exception e) {
						e.printStackTrace();
						companyName = ""; // 念のため初期化
					}
				} else {
					// 初回表示
					companyId = "";
					companyName = "";
				}

				// 学科コードと学科を設定
				if (backCourseCode != null && !backCourseCode.isEmpty()) {
					courseCode = backCourseCode; // 登録用ID
					try {
						graduate = graduateAction.execute(new String[] {"findCourseName",courseCode});
						courseName = graduate.get(0).getCourse().getCourseName();
					} catch (Exception e) {
						e.printStackTrace();
						courseName = ""; // 念のため初期化
					}
				} else {
					// 初回表示
					courseCode = "";
					courseName = "";
				}
				
				// 企業一覧読み込み
				try {
					companies = companyAction.execute(new String[] { "CompanyList", "", "" });
				} catch (Exception e) {
					e.printStackTrace();
				}
				request.setAttribute("companies", companies);

				// 学科一覧読み込み
				try {
					courses = new CourseDBAccess().getAllCourses();
				} catch (Exception e) {
					e.printStackTrace();
				}
				request.setAttribute("courses", courses);

				// JSPへ渡す入力値
				request.setAttribute("companyId", companyId); // 登録処理用
				request.setAttribute("companyName", companyName); // 表示用
				request.setAttribute("backCompany", backCompanyId); // Back 判定用（必要なら）
				request.setAttribute("jobType", job);
				request.setAttribute("graduateName", name);
				request.setAttribute("courseCode", courseCode);
				request.setAttribute("courseName", courseName);
				request.setAttribute("graduateStudentNumber", sn);
				request.setAttribute("graduateEmail", mail);
				request.setAttribute("otherInfo", info);

				break;
			//				確認へ
			case "RegistEmailNext":
				// RegistEmailNext の冒頭付近に追加
				courseCode = request.getParameter("courseCode");
				System.out.println("DEBUG: RegistEmailNext - received courseCode -> '" + courseCode +  "'");

				nextPage = "/common/RegistEmailConfirm.jsp";
				// 入力値取得
				companyId = request.getParameter("companyId");

				jobType = request.getParameter("jobType");
				graduateName = request.getParameter("graduateName");

				courseCode = request.getParameter("courseCode");

				graduateStudentNumber = request.getParameter("graduateStudentNumber");
				graduateEmail = request.getParameter("graduateEmail");
				otherInfo = request.getParameter("otherInfo");


				try {
					graduate = graduateAction.execute(new String[] {"findCompanyName",companyId});
					companyName = graduate.get(0).getCompany().getCompanyName();
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				try {
					graduate = graduateAction.execute(new String[] {"findCourseName",courseCode});
					courseName = graduate.get(0).getCourse().getCourseName();
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
				registEmailInfo[6] = request.getParameter("graduateEmail");
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

		//		request.setAttribute("companies", companies);
		// 次のページへの転送
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
		rd.forward(request, response);
	}

}

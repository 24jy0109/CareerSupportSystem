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
public class GraduateController extends BaseController {
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
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				response.sendRedirect("company?command=RegistEvent&companyId=" + companyId);
				return;
			//				連絡先登録
			//				連絡先登録
			//				入力画面
			case "RegistEmail":
				// パラメータ取得
				graduateStudentNumber = request.getParameter("graduateStudentNumber");
				String fromConfirm = request.getParameter("fromConfirm");
				String updateMode = request.getParameter("updateMode");
				//				if (fromConfirm == null || fromConfirm.isEmpty()) {
				//					fromConfirm = "false";
				//				}
				request.setAttribute("updateMode", updateMode);
				System.out.println("fromConfirm➡" + fromConfirm);
				System.out.println("updateMode➡" + updateMode);

				// JSPに渡す変数
				//				companyId = "";
				//				courseCode = "";
				//				graduateName = "";
				//				graduateEmail = "";
				//				jobType = "";
				//				otherInfo = "";
				//				companyName = "";
				//				courseName = "";

				if (!"true".equals(updateMode)) {
					companyId = request.getParameter("companyId");
					courseCode = request.getParameter("courseCode");
					graduateName = request.getParameter("graduateName");
					graduateEmail = request.getParameter("graduateEmail");
					graduateStudentNumber = request.getParameter("graduateStudentNumber");
					jobType = request.getParameter("jobType");
					String otherJob = request.getParameter("otherJob");
					if ("other".equals(jobType)) {
						jobType = otherJob;
					}
					otherInfo = request.getParameter("otherInfo");

				} else {
					if (graduateStudentNumber != null && !graduateStudentNumber.isEmpty()) {
						try {
							List<Graduate> list = graduateAction.execute(
									new String[] { "findGraduateInfo", graduateStudentNumber });

							if (list != null && !list.isEmpty()) {
								Graduate g = list.get(0);

								companyId = String.valueOf(g.getCompany().getCompanyId());
								courseCode = g.getCourse().getCourseCode();
								graduateName = g.getGraduateName();
								graduateEmail = g.getGraduateEmail();
								jobType = g.getGraduateJobCategory();
								otherInfo = g.getOtherInfo();
							}
						} catch (Exception e) {
							handleException(e, request, response, "staff/AppointMenu.jsp");
							return;
						}
					}
				}

				// ① 確認画面から戻ってきた場合
				if ("true".equals(fromConfirm)) {
					companyId = request.getParameter("companyId");
					courseCode = request.getParameter("courseCode");
					graduateName = request.getParameter("graduateName");
					graduateEmail = request.getParameter("graduateEmail");
					graduateStudentNumber = request.getParameter("graduateStudentNumber");
					jobType = request.getParameter("jobType");
					otherInfo = request.getParameter("otherInfo");

					// 会社名・学科名を再取得
					try {
						List<Graduate> gList = graduateAction.execute(new String[] { "findCompanyName", companyId });
						companyName = gList.get(0).getCompany().getCompanyName();
					} catch (Exception e) {
						handleException(e, request, response, "staff/AppointMenu.jsp");
						return;
					}

					try {
						List<Graduate> gList = graduateAction.execute(new String[] { "findCourseName", courseCode });
						courseName = gList.get(0).getCourse().getCourseName();
					} catch (Exception e) {
						handleException(e, request, response, "staff/AppointMenu.jsp");
						return;
					}

					nextPage = "/common/RegistEmail.jsp";

				} else {
					// ② 編集ボタンから来た場合 or 新規追加
					if (graduateStudentNumber != null && !graduateStudentNumber.isEmpty()) {
						try {
							List<Graduate> list = graduateAction
									.execute(new String[] { "findGraduateInfo", graduateStudentNumber });
							if (list != null && !list.isEmpty()) {
								Graduate g = list.get(0);
								companyId = String.valueOf(g.getCompany().getCompanyId());
								courseCode = g.getCourse().getCourseCode();
								graduateName = g.getGraduateName();
								graduateEmail = g.getGraduateEmail();
								jobType = g.getGraduateJobCategory();
								otherInfo = g.getOtherInfo();

								// 会社名・学科名
								List<Graduate> tmp;
								tmp = graduateAction.execute(new String[] { "findCompanyName", companyId });
								companyName = tmp.get(0).getCompany().getCompanyName();
								tmp = graduateAction.execute(new String[] { "findCourseName", courseCode });
								courseName = tmp.get(0).getCourse().getCourseName();
							}
						} catch (Exception e) {
							handleException(e, request, response, "staff/AppointMenu.jsp");
							return;
						}
					}
					// 新規の場合は空のまま
					nextPage = "/common/RegistEmail.jsp";
				}

				// 会社名・学科名を再取得
				try {
					List<Graduate> gList = graduateAction.execute(new String[] { "findCompanyName", companyId });
					companyName = gList.get(0).getCompany().getCompanyName();
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}

				try {
					List<Graduate> gList = graduateAction.execute(new String[] { "findCourseName", courseCode });
					courseName = gList.get(0).getCourse().getCourseName();
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}

				// 企業一覧読み込み
				try {
					companies = companyAction.execute(new String[] { "CompanyList", "", "" });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				request.setAttribute("companies", companies);

				// 学科一覧読み込み
				try {
					courses = new CourseDBAccess().getAllCourses();
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				request.setAttribute("courses", courses);

				// ③ JSPに値を渡す
				request.setAttribute("companyId", companyId);
				request.setAttribute("courseCode", courseCode);
				request.setAttribute("graduateName", graduateName);
				request.setAttribute("graduateEmail", graduateEmail);
				request.setAttribute("graduateStudentNumber", graduateStudentNumber);
				request.setAttribute("jobType", jobType);
				request.setAttribute("otherInfo", otherInfo);
				request.setAttribute("companyName", companyName);
				request.setAttribute("courseName", courseName);
				request.setAttribute("fromConfirm", fromConfirm);

				break;

			//				確認へ
			case "RegistEmailNext":
				// RegistEmailNext の冒頭付近に追加
				courseCode = request.getParameter("courseCode");
				System.out.println("DEBUG: RegistEmailNext - received courseCode -> '" + courseCode + "'");

				// 入力値取得
				companyId = request.getParameter("companyId");

				jobType = request.getParameter("jobType");
				String otherJob = request.getParameter("otherJob");

				if ("other".equals(jobType)) {
					jobType = otherJob;
				}

				graduateName = request.getParameter("graduateName");

				courseCode = request.getParameter("courseCode");

				graduateStudentNumber = request.getParameter("graduateStudentNumber");
				// 学籍番号の3,4文字目（学科コード）
				String studentCourseCode = graduateStudentNumber.substring(2, 4);

				graduateEmail = request.getParameter("graduateEmail");
				otherInfo = request.getParameter("otherInfo");

				if (!studentCourseCode.equals(courseCode)) {
					request.setAttribute(
							"error",
							"学籍番号の学科コードと選択された学科が一致しません");

					try {
						companies = companyAction.execute(new String[] { "CompanyList", "", "" });
					} catch (Exception e) {
						handleException(e, request, response, "staff/AppointMenu.jsp");
						return;
					}
					try {
						courses = new CourseDBAccess().getAllCourses();
					} catch (Exception e) {
						handleException(e, request, response, "staff/AppointMenu.jsp");
						return;
					}

					// JSPへ渡す値

					request.setAttribute("companies", companies);
					request.setAttribute("courses", courses);

					request.setAttribute("companyId", companyId); // 登録処理用
					request.setAttribute("companyName", companyName); // 表示用

					request.setAttribute("jobType", jobType);
					request.setAttribute("graduateName", graduateName);

					request.setAttribute("courseCode", courseCode); // 登録処理用
					request.setAttribute("courseName", courseName); // 表示用

					request.setAttribute("graduateStudentNumber", graduateStudentNumber);
					request.setAttribute("graduateEmail", graduateEmail);
					request.setAttribute("otherInfo", otherInfo);

					nextPage = "/common/RegistEmail.jsp";
					break;
				}

				try {
					graduate = graduateAction.execute(new String[] { "findCompanyName", companyId });
					companyName = graduate.get(0).getCompany().getCompanyName();
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				try {
					graduate = graduateAction.execute(new String[] { "findCourseName", courseCode });
					courseName = graduate.get(0).getCourse().getCourseName();
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}

				//学籍番号が既にあるかチェック！
				updateMode = request.getParameter("updateMode");
				if (updateMode == null) {
					updateMode = "false";
				}
				System.out.println("updateMode➡" + updateMode);
				if (!"true".equals(updateMode)) {
					// 新規登録のときだけ重複チェック
					List<Graduate> exists = null;
					try {
						exists = graduateAction.execute(
								new String[] { "findGraduateStudentNumber", graduateStudentNumber });
					} catch (Exception e) {
						handleException(e, request, response, "staff/AppointMenu.jsp");
						return;
					}

					if (exists != null && !exists.isEmpty()) {
						request.setAttribute("error",
								"この学籍番号はすでに登録されています");
						nextPage = "/common/RegistEmail.jsp";
					} else {
						nextPage = "/common/RegistEmailConfirm.jsp";
					}

				} else {
					// 更新モードなら無条件で確認画面へ
					nextPage = "/common/RegistEmailConfirm.jsp";
				}

				// モードは必ず引き回す
				request.setAttribute("updateMode", updateMode);

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
				String[] registEmailInfo = new String[10];
				//				registEmailInfo[0] = "RegisterGraduate";
				registEmailInfo[1] = request.getParameter("companyId");
				registEmailInfo[2] = request.getParameter("companyName");
				registEmailInfo[3] = request.getParameter("jobType");
				registEmailInfo[4] = request.getParameter("graduateName");
				registEmailInfo[5] = request.getParameter("courseCode");
				registEmailInfo[6] = request.getParameter("courseName");
				registEmailInfo[7] = request.getParameter("graduateStudentNumber");
				registEmailInfo[8] = request.getParameter("graduateEmail");
				registEmailInfo[9] = request.getParameter("otherInfo");

				updateMode = request.getParameter("updateMode");

				if ("true".equals(updateMode)) {
					registEmailInfo[0] = "UpdateGraduate";
				} else {
					registEmailInfo[0] = "RegisterGraduate";
				}

				try {
					graduateAction.execute(registEmailInfo);
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				} // ← requestを渡す！

				break;

			case "AppointMenu":
				nextPage = "staff/AppointMenu.jsp";
				break;

			case "editInfo":
				nextPage = "staff/EditInfo.jsp";

				companyId = request.getParameter("companyId");

				try {
					List<Graduate> list = graduateAction.execute(
							new String[] { "graduateSearchBycompanyId", companyId });

					if (list != null && !list.isEmpty()) {
						request.setAttribute("companyDTO", list.get(0));
					}

				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				break;
			case "deleteGraduate":
				graduateStudentNumber = request.getParameter("graduateStudentNumber");
				companyId = request.getParameter("companyId");

				System.out.println("DEBUG delete studentNumber = " + graduateStudentNumber);

				try {
					graduateAction.execute(
							new String[] { "deleteGraduate", graduateStudentNumber });
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				request.setAttribute("companyId", companyId);
				try {
					List<Graduate> list = graduateAction.execute(
							new String[] { "graduateSearchBycompanyId", companyId });

					if (list != null && !list.isEmpty()) {
						request.setAttribute("companyDTO", list.get(0));
					}
				} catch (Exception e) {
					handleException(e, request, response, "staff/AppointMenu.jsp");
					return;
				}
				nextPage = "staff/EditInfo.jsp";
				break;
			}
		} else {
			// 学生の遷移
			switch (command) {
			case "CompanyList":
				break;

			case "RegistEmail":
				String fromConfirm = request.getParameter("fromConfirm");

				String backCompanyId = null;
				String job = null;
				graduateStudentNumber = (String) session.getAttribute("studentNumber");
				String backCourseCode = studentNumber.substring(2, 4);
				String name = (String) session.getAttribute("name");
				String mail = null;
				String sn = null;
				String info = null;
				// ★ 編集ボタンから戻ってきた場合
				if ("true".equals(fromConfirm)) {
					nextPage = "common/RegistEmail.jsp";

					// Confirm から送られてきた値をそのまま使う
					companyId = request.getParameter("companyId");
					job = request.getParameter("jobType");
					courseCode = request.getParameter("courseCode");
					sn = request.getParameter("graduateStudentNumber");
					mail = request.getParameter("graduateEmail");
					info = request.getParameter("otherInfo");

					// 会社名
					try {
						graduate = graduateAction.execute(
								new String[] { "findCompanyName", companyId });
					} catch (Exception e) {
						handleException(e, request, response, "student/AppointMenu.jsp");
						return;
					}
					companyName = graduate.get(0).getCompany().getCompanyName();

					// 学科名
					try {
						graduate = graduateAction.execute(
								new String[] { "findCourseName", courseCode });
					} catch (Exception e) {
						handleException(e, request, response, "student/AppointMenu.jsp");
						return;
					}
					courseName = graduate.get(0).getCourse().getCourseName();

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

					request.setAttribute("fromConfirm", fromConfirm);

					// 企業一覧読み込み
					try {
						companies = companyAction.execute(new String[] { "CompanyList", "", "" });
					} catch (Exception e) {
						handleException(e, request, response, "student/AppointMenu.jsp");
						return;
					}
					request.setAttribute("companies", companies);

					break;
				}
				List<Graduate> list = null;

				try {
					list = graduateAction.execute(new String[] { "findStudentNumber", studentNumber });
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
				}

				//				String check;
				if (list != null && !list.isEmpty()) {
					// 登録済み
					nextPage = "common/RegistEmailConfirm.jsp";
					try {
						list = graduateAction.execute(new String[] { "findGraduateInfo", studentNumber });
						Graduate graduateInfo = list.get(0);
						//データベースから値を取得
						companyId = String.valueOf(graduateInfo.getCompany().getCompanyId());
						job = graduateInfo.getGraduateJobCategory();
						name = graduateInfo.getGraduateName();
						courseCode = graduateInfo.getCourse().getCourseCode();
						sn = graduateInfo.getGraduateStudentNumber();
						mail = graduateInfo.getGraduateEmail();
						info = graduateInfo.getOtherInfo();
						fromConfirm = "true";
						request.setAttribute("fromConfirm", fromConfirm);

					} catch (Exception e) {
						handleException(e, request, response, "student/AppointMenu.jsp");
						return;
					}

					try {
						graduate = graduateAction.execute(new String[] { "findCompanyName", companyId });
						companyName = graduate.get(0).getCompany().getCompanyName();
					} catch (Exception e) {
						handleException(e, request, response, "student/AppointMenu.jsp");
						return;
					}

					try {
						graduate = graduateAction.execute(new String[] { "findCourseName", courseCode });
						courseName = graduate.get(0).getCourse().getCourseName();
					} catch (Exception e) {
//						handleException(e, request, response, "student/AppointMenu.jsp");
						courseName = ""; // 念のため初期化
						e.printStackTrace();
//						return;
					}

				} else {
					// 未登録
					nextPage = "common/RegistEmail.jsp";
					// Back の場合、入力値が送られてくる
					backCompanyId = request.getParameter("companyId");
					backCourseCode = request.getParameter("courseCode");
					job = request.getParameter("jobType");
					name = request.getParameter("graduateName");
					sn = request.getParameter("graduateStudentNumber");
					mail = request.getParameter("graduateEmail"); // hidden名に合わせる
					info = request.getParameter("otherInfo");
					// 会社IDと会社名を設定
					if (backCompanyId != null && !backCompanyId.isEmpty()) {
						companyId = backCompanyId; // 登録用ID
						try {
							graduate = graduateAction.execute(new String[] { "findCompanyName", companyId });
							companyName = graduate.get(0).getCompany().getCompanyName();
						} catch (Exception e) {
//							handleException(e, request, response, "student/AppointMenu.jsp");
							courseName = ""; // 念のため初期化
							e.printStackTrace();
//							return;
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
							graduate = graduateAction.execute(new String[] { "findCourseName", courseCode });
							courseName = graduate.get(0).getCourse().getCourseName();
						} catch (Exception e) {
//							handleException(e, request, response, "student/AppointMenu.jsp");
							courseName = ""; // 念のため初期化
							e.printStackTrace();
//							return;
						}
					} else {
						// 初回表示
						courseCode = "";
						courseName = "";
					}
				}

				// 企業一覧読み込み
				try {
					companies = companyAction.execute(new String[] { "CompanyList", "", "" });
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
				}
				request.setAttribute("companies", companies);

				// 学科一覧読み込み
				try {
					courses = new CourseDBAccess().getAllCourses();
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
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

			case "RegistEmailNext":
				// RegistEmailNext の冒頭付近に追加
				courseCode = request.getParameter("courseCode");
				System.out.println("DEBUG: RegistEmailNext - received courseCode -> '" + courseCode + "'");
				//確認画面からきてるかチェック 
				fromConfirm = request.getParameter("fromConfirm");
				if (fromConfirm == "")
					fromConfirm = "false"; // 初期化
				// 入力値取得
				companyId = request.getParameter("companyId");

				jobType = request.getParameter("jobType");
				String otherJob = request.getParameter("otherJob");

				if ("other".equals(jobType)) {
					jobType = otherJob;
				}

				graduateName = request.getParameter("graduateName");

				courseCode = request.getParameter("courseCode");

				graduateStudentNumber = request.getParameter("graduateStudentNumber");
				graduateEmail = request.getParameter("graduateEmail");
				otherInfo = request.getParameter("otherInfo");

				try {
					graduate = graduateAction.execute(new String[] { "findCompanyName", companyId });
					companyName = graduate.get(0).getCompany().getCompanyName();
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
				}
				try {
					graduate = graduateAction.execute(new String[] { "findCourseName", courseCode });
					if (graduate != null && !graduate.isEmpty()) {
						if (graduate.get(0).getCourse() != null) {
							courseName = graduate.get(0).getCourse().getCourseName();
						} else {
							courseName = ""; // Course が null の場合
						}
					} else {
						courseName = ""; // graduate が空リストの場合
					}
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
				}

				System.err.println(fromConfirm);
				if ("true".equals(fromConfirm)) {
					nextPage = "/common/RegistEmailConfirm.jsp";
				} else {
					// 学籍番号の重複チェック
					List<Graduate> exists = null;
					try {
						exists = graduateAction
								.execute(new String[] { "findGraduateStudentNumber", graduateStudentNumber });
					} catch (Exception e) {
						handleException(e, request, response, "student/AppointMenu.jsp");
						return;
					}

					if (exists != null && !exists.isEmpty()) {
						request.setAttribute("error", "この学籍番号はすでに登録されています");
						nextPage = "/common/RegistEmail.jsp";
					} else {
						nextPage = "/common/RegistEmailConfirm.jsp";
					}
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

				request.setAttribute("fromConfirm", fromConfirm);

				break;

			//				連絡先情報をデータベースに登録
			case "RegistEmailConfirm":
				nextPage = "common/CompleteRegistEmail.jsp";
				//				配列に挿入！
				String[] registEmailInfo = new String[10];
				//				registEmailInfo[0] = "RegisterGraduate";
				registEmailInfo[1] = request.getParameter("companyId");
				registEmailInfo[2] = request.getParameter("companyName");
				registEmailInfo[3] = request.getParameter("jobType");
				registEmailInfo[4] = request.getParameter("graduateName");
				registEmailInfo[5] = request.getParameter("courseCode");
				registEmailInfo[6] = request.getParameter("courseName");
				registEmailInfo[7] = request.getParameter("graduateStudentNumber");
				registEmailInfo[8] = request.getParameter("graduateEmail");
				registEmailInfo[9] = request.getParameter("otherInfo");

				fromConfirm = request.getParameter("fromConfirm");

				// 学籍番号が既にあるかで分岐
				//			    List<Graduate> exists = null;
				//			    try {
				//			        exists = graduateAction.execute(new String[] { "findGraduateStudentNumber", registEmailInfo[7] });
				//			    } catch (Exception e) {
				//			        e.printStackTrace();
				//			    }

				if ("true".equals(fromConfirm)) {
					registEmailInfo[0] = "UpdateGraduate";
				} else {
					registEmailInfo[0] = "RegisterGraduate";
				}
				try {
					graduateAction.execute(registEmailInfo);
				} catch (Exception e) {
					handleException(e, request, response, "student/AppointMenu.jsp");
					return;
				} // ← requestを渡す！

				break;

			case "AppointMenu":
				nextPage = "student/AppointMenu.jsp";
				break;
			}

		}

		//		request.setAttribute("companies", companies);
		// 次のページへの転送
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
		rd.forward(request, response);
	}

}

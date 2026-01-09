package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import action.LoginAction;

@WebServlet("/callback")
public class CallbackController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		try {
			// 認可コード取得
			String code = request.getParameter("code");
			if (code == null || code.isEmpty()) {
				throw new Exception("ログイン処理に失敗しました。もう一度お試しください。");
			}

			// ログイン処理
			LoginAction action = new LoginAction();
			String[] user = action.execute(new String[] { code });

			if (user == null || user.length == 0) {
				throw new Exception("許可されていないアカウントでログインが行われました。");
			}

			// セッション保存
			HttpSession session = request.getSession();
			session.setMaxInactiveInterval(60 * 60 * 24 * 7);
			session.setAttribute("studentNumber", user[0]);
			session.setAttribute("name", user[1]);
			session.setAttribute("email", user[2]);
			session.setAttribute("role", user[3]);

			// 正常遷移
			response.sendRedirect("mypage?command=AppointmentMenu");

		} catch (Exception e) {
			// 開発者向けログ
			e.printStackTrace();

			// ユーザー向けエラーメッセージ
			request.setAttribute("error", e.getMessage());
			RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
			rd.forward(request, response);
		}
	}
}

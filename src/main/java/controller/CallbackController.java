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
	protected void doGet(HttpServletRequest request, HttpServletResponse resppnse)
			throws ServletException, IOException {
		// リクエスト情報に対する文字コードの設定する
		request.setCharacterEncoding("UTF-8");

		// 認可コード
		String code = request.getParameter("code");
		if (code == null || code.isEmpty()) {
			System.out.println("認可コードが取得できません");
			RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
			rd.forward(request, resppnse);
			return;
		}

		try {
			LoginAction action = new LoginAction();
			String[] user = action.execute(new String[] { code });

			if (user.length == 0) {
				//　違うアカウントでログイン
				RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
				rd.forward(request, resppnse);
				return;
			}

			// セッションにユーザー情報を保存
			HttpSession session = request.getSession();
			session.setMaxInactiveInterval(60 * 60 * 24 * 7);
			session.setAttribute("studentNumber", user[0]);
			session.setAttribute("name", user[1]);
			session.setAttribute("email", user[2]);
			session.setAttribute("role", user[3]);

			resppnse.sendRedirect("mypage?command=Mypage");
		} catch (Exception e) {
			e.printStackTrace();
			resppnse.getWriter().println("Google認証エラー: " + e.getMessage());
		}


	}
}

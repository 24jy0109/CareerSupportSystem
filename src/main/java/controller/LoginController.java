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
import model.Key;

@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// リクエスト情報に対する文字コードの設定する
		request.setCharacterEncoding("UTF-8");
		// 各画面から送信されるリクエストパラメータ"command"の値を取得する
		String command = request.getParameter("command");
		// リクエストパラメータ"command"の値がない場合
		if (command == null || command.isEmpty()) {
			// 値を"Login"にする
			command = "Login";
		}
		// セッションオブジェクト格納用変数
		HttpSession session = null;
		// リクエストパラメータ"command"の値に対応した処理を実行する
		switch (command) {
		case "Login":
			Key key = new Key();
			// Google OAuth 2w.0 認証画面への URL にリダイレクト
			String url = "https://accounts.google.com/o/oauth2/v2/auth" +
					"?client_id=" + key.getClientId() +
					"&redirect_uri=" + key.getRedirectUri() +
					"&response_type=code" +
					"&scope=email%20profile";

			response.sendRedirect(url);
			break;
		// 職員ログイン検証用
		case "TestStaffLogin":
			LoginAction action = new LoginAction();
			try {
				String[] staff = action.execute(new String[] {"TestStaffLogin"});
				// セッションにユーザー情報を保存
				session = request.getSession();
				session.setMaxInactiveInterval(60 * 60 * 24 * 7);
				session.setAttribute("studentNumber", staff[0]);
				session.setAttribute("name", staff[1]);
				session.setAttribute("email", staff[2]);
				session.setAttribute("role", staff[3]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.sendRedirect("mypage?command=Mypage");
			break;
		}

	}
}

package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBAccess {
	public Connection createConnection() throws Exception {
		/*変数宣言*/
		//JDBC URL  
		String url = "jdbc:mysql://10.64.144.5:3306/24jy0109";
		//ユーザ名  
		String user = "24jy0109";
		//パスワード  
		String pass = "24jy0109";
		//データベース接続
		Connection con = null;

		try {
			//JDBCドライバをロードする
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.cj.jdbc.Driver");
			//データベース接続
			con = DriverManager.getConnection(url, user, pass);
			//自動コミットモード解除
			//con.setAutoCommit(false);
			return con;
		} catch (Exception e) {
			//DB接続に失敗でExceptionをthrow
			String errorMessage = "【エラー】<br>"
					+ "DB接続処理に失敗しました！<br>"
					+ "管理者に連絡してください。";
			throw new Exception(errorMessage);
		}
	}

	public void closeConnection(Connection con) throws Exception {
		try {
			//DB接続を切断
			con.close();
		} catch (Exception e) {
			//DB接続の切断に失敗でExceptionをthrow
			String errorMessage = "【エラー】<br>"
					+ "DB接続の切断に失敗しました！<br>"
					+ "管理者に連絡してください。";
			throw new Exception(errorMessage);
		}
	}
}

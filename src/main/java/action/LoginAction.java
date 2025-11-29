package action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import org.json.JSONObject;

import dao.StaffDBAcess;
import dao.StudentDBAcess;
import model.Course;
import model.Staff;
import model.Student;

public class LoginAction {
	public String[] execute(String[] data) throws Exception {
		// key取得
		Properties prop = new Properties();
		try (InputStream in = getClass().getClassLoader()
				.getResourceAsStream("config.properties")) {
			if (in == null) {
				System.out.println("config.properties が見つかりません");
			} else {
				prop.load(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		final String CLIENT_ID = prop.getProperty("CLIENT_ID");
		final String CLIENT_SECRET = prop.getProperty("CLIENT_SECRET");
		final String REDIRECT_URI = prop.getProperty("REDIRECT_URI");
		String code = data[0];

		// 職員ログイン検証用
		if (code == "TestStaffLogin") {
			// 職員
			Staff staff = new Staff();
			staff.setStaffName("テスト スタッフ");
			staff.setStaffEmail("TestStaff@jec.ac.jp");

			// DB登録
			StaffDBAcess staffDBA = new StaffDBAcess();
			staffDBA.insertStaff(staff);
			return new String[] { String.valueOf(staff.getStaffId()), staff.getStaffName(), staff.getStaffEmail(),
					"staff" };
		}

		// ---- ① 認可コード → アクセストークン ----
		URL url = new URL("https://oauth2.googleapis.com/token");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		String params = "code=" + URLEncoder.encode(code, "UTF-8") +
				"&client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8") +
				"&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8") +
				"&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8") +
				"&grant_type=authorization_code";

		try (OutputStream os = con.getOutputStream()) {
			os.write(params.getBytes("UTF-8"));
			os.flush();
		}

		// エラー時のレスポンスを確認
		int status = con.getResponseCode();
		InputStream is = (status < 400) ? con.getInputStream() : con.getErrorStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuilder response = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null)
			response.append(line);

		if (status >= 400) {
			throw new IOException("Token endpoint returned HTTP " + status + ": " + response);
		}

		JSONObject tokenObj = new JSONObject(response.toString());
		String accessToken = tokenObj.getString("access_token");

		// ---- ② UserInfo API で名前とメール取得 ----
		URL userUrl = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken);
		HttpURLConnection ucon = (HttpURLConnection) userUrl.openConnection();
		ucon.setRequestMethod("GET");

		BufferedReader ubr = new BufferedReader(new InputStreamReader(ucon.getInputStream(), "UTF-8"));
		StringBuilder userJson = new StringBuilder();
		while ((line = ubr.readLine()) != null)
			userJson.append(line);

		// 取得情報チェック
		JSONObject userObj = new JSONObject(userJson.toString());
		String email = userObj.getString("email");

		if (email.endsWith("@jec.ac.jp")) {
			String pattern = "^\\d{2}[a-zA-Z]{2}\\d{4}@.*";
			if (email.matches(pattern)) {
				// 生徒
				String studentNumber = userObj.getString("name").substring(0, 8);
				Course course = new Course();
				course.setCourseCode(userObj.getString("name").substring(2, 4));
				String studentName = userObj.getString("name").substring(8);

				// DB登録
				Student student = new Student(studentNumber, course, null, studentName, email);
				StudentDBAcess studentDBA = new StudentDBAcess();
				studentDBA.insertStudent(student);

				return new String[] { studentNumber, studentName, email, "student" };
			} else {
				// 職員
				String name = userObj.getString("name");
				Staff staff = new Staff();
				staff.setStaffName(name);
				staff.setStaffEmail(email);

				// DB登録
				StaffDBAcess staffDBA = new StaffDBAcess();
				staffDBA.insertStaff(staff);
				return new String[] { "", name, email, "staff" };
			}
		} else {
			// jecじゃない人は空
			return new String[] {};
		}
	}
}

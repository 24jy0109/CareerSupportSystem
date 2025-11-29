package action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import dao.StaffDBAcess;
import dao.StudentDBAcess;
import model.Course;
import model.Key;
import model.Staff;
import model.Student;

public class LoginAction {

	public String[] execute(String[] data) throws Exception {
		String code = data[0];

		// ---- テスト用職員ログイン ----
		if ("TestStaffLogin".equals(code)) {
			Staff staff = new Staff();
			staff.setStaffName("テスト スタッフ");
			staff.setStaffEmail("TestStaff@jec.ac.jp");

			StaffDBAcess staffDBA = new StaffDBAcess();
			staffDBA.insertStaff(staff);

			return new String[] { String.valueOf(staff.getStaffId()), staff.getStaffName(), staff.getStaffEmail(),
					"staff" };
		}

		// ---- Key クラスから OAuth 設定取得 ----
		Key key = new Key();

		// ---- ① 認可コード → アクセストークン ----
		String accessToken = getAccessToken(code, key);

		// ---- ② UserInfo API でユーザー情報取得 ----
		JSONObject userObj = getUserInfo(accessToken);
		String email = userObj.getString("email");

		if (!email.endsWith("@jec.ac.jp")) {
			return new String[] {}; // jec以外のメールは無視
		}

		String name = userObj.getString("name");

		// ---- 生徒か職員か判定 ----
		if (email.matches("^\\d{2}[a-zA-Z]{2}\\d{4}@.*")) {
			// 生徒
			String studentNumber = name.substring(0, 8);
			Course course = new Course();
			course.setCourseCode(name.substring(2, 4));
			String studentName = name.substring(8);

			Student student = new Student(studentNumber, course, null, studentName, email);
			StudentDBAcess studentDBA = new StudentDBAcess();
			studentDBA.insertStudent(student);

			return new String[] { studentNumber, studentName, email, "student" };
		} else {
			// 職員
			Staff staff = new Staff();
			staff.setStaffName(name);
			staff.setStaffEmail(email);

			StaffDBAcess staffDBA = new StaffDBAcess();
			staffDBA.insertStaff(staff);

			return new String[] { "", name, email, "staff" };
		}
	}

	private String getAccessToken(String code, Key key) throws IOException {
		URL url = new URL("https://oauth2.googleapis.com/token");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		String params = "code=" + URLEncoder.encode(code, "UTF-8") +
				"&client_id=" + URLEncoder.encode(key.getClientId(), "UTF-8") +
				"&client_secret=" + URLEncoder.encode(key.getClientSecret(), "UTF-8") +
				"&redirect_uri=" + URLEncoder.encode(key.getRedirectUri(), "UTF-8") +
				"&grant_type=authorization_code";

		try (OutputStream os = con.getOutputStream()) {
			os.write(params.getBytes("UTF-8"));
			os.flush();
		}

		int status = con.getResponseCode();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(status < 400) ? con.getInputStream() : con.getErrorStream(), "UTF-8"));

		StringBuilder response = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null)
			response.append(line);

		if (status >= 400) {
			throw new IOException("Token endpoint returned HTTP " + status + ": " + response);
		}

		return new JSONObject(response.toString()).getString("access_token");
	}

	private JSONObject getUserInfo(String accessToken) throws IOException {
		URL userUrl = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken);
		HttpURLConnection ucon = (HttpURLConnection) userUrl.openConnection();
		ucon.setRequestMethod("GET");

		BufferedReader br = new BufferedReader(new InputStreamReader(ucon.getInputStream(), "UTF-8"));
		StringBuilder userJson = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null)
			userJson.append(line);

		return new JSONObject(userJson.toString());
	}
}

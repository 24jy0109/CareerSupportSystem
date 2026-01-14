package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Company;
import model.Course;
import model.Event;
import model.Request;
import model.Student;

public class RequestDBAccess extends DBAccess {

	public List<Request> requestStudentList(int companyId) throws Exception {
		List<Request> list = new ArrayList<>();

		String sqlReq = "SELECT r.request_time, "
				+ "s.student_number, s.student_name, s.student_email, "
				+ "c.company_id, c.company_name, "
				+ "co.course_code, co.course_name, co.course_term "
				+ "FROM request r "
				+ "JOIN student s ON r.student_number = s.student_number "
				+ "JOIN course co ON s.course_code = co.course_code "
				+ "JOIN company c ON r.company_id = c.company_id "
				+ "WHERE r.company_id = ? "
				+ "ORDER BY r.request_time DESC";

		String sqlEvent = "SELECT event_id, event_start_time, event_end_time, event_place "
				+ "FROM event "
				+ "WHERE company_id = ? AND event_progress = 2";

		try (Connection con = createConnection()) {

			// ------------------------------
			// event_progress = 2 の Event 一覧
			// ------------------------------
			ArrayList<Event> eventList = new ArrayList<>();

			try (PreparedStatement psEv = con.prepareStatement(sqlEvent)) {
				psEv.setInt(1, companyId);

				try (ResultSet rsEv = psEv.executeQuery()) {
					while (rsEv.next()) {
						Event ev = new Event();
						ev.setEventId(rsEv.getInt("event_id"));

						Timestamp st = rsEv.getTimestamp("event_start_time");
						if (st != null)
							ev.setEventStartTime(st.toLocalDateTime());

						Timestamp et = rsEv.getTimestamp("event_end_time");
						if (et != null)
							ev.setEventEndTime(et.toLocalDateTime());

						ev.setEventPlace(rsEv.getString("event_place"));
						eventList.add(ev);
					}
				}
			}

			// ------------------------------
			// Request + Student 一覧
			// ------------------------------
			try (PreparedStatement ps = con.prepareStatement(sqlReq)) {
				ps.setInt(1, companyId);

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {

						Course course = new Course();
						course.setCourseName(rs.getString("course_name"));

						Student student = new Student();
						student.setStudentNumber(rs.getString("student_number"));
						student.setStudentName(rs.getString("student_name"));
						student.setCourse(course);

						Company company = new Company();
						company.setCompanyId(rs.getInt("company_id"));
						company.setCompanyName(rs.getString("company_name"));
						company.setEvents(eventList);

						Request req = new Request();
						req.setstudent(student);
						req.setcompany(company);
						req.setRequestTime(
								rs.getTimestamp("request_time").toLocalDateTime());

						list.add(req);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、リクエスト学生一覧の取得に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}

		return list;
	}

	public void insertRequest(int companyId, String studentNumber) throws Exception {

		String sql = "INSERT INTO request (company_id, student_number) "
				+ "VALUES (?, ?)";

		try (Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, companyId);
			ps.setString(2, studentNumber);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、リクエストの登録に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}

	public void cancelRequest(int companyId, String studentNumber) throws Exception {

		String sql = "DELETE FROM request "
				+ "WHERE company_id = ? AND student_number = ?";

		try (Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, companyId);
			ps.setString(2, studentNumber);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、リクエストの取消に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}

	public List<String> searchEmailsByCompanyId(int companyId) throws Exception {

		List<String> emails = new ArrayList<>();

		String sql = "SELECT s.student_email "
				+ "FROM request r "
				+ "JOIN student s ON r.student_number = s.student_number "
				+ "WHERE r.company_id = ?";

		try (Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, companyId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					emails.add(rs.getString("student_email"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、メールアドレスの取得に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}

		return emails;
	}

	public Boolean isStudentRequestedCompany(int companyId, String studentNumber) throws Exception {

		String sql = "SELECT 1 " +
				"FROM request " +
				"WHERE company_id = ? AND student_number = ?";

		try (Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, companyId);
			ps.setString(2, studentNumber);

			try (ResultSet rs = ps.executeQuery()) {

				// レコードが存在する = 申請済み
				if (rs.next()) {
					return true;
				}

				// レコードなし = 未申請
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、企業申請状況の取得に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}
}
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
		Connection con = createConnection();
	    List<Request> list = new ArrayList<>();

	    // ① リクエスト学生一覧を取得するSQL
	    String sqlReq =
	        "SELECT r.request_time, "
	      + "s.student_number, s.student_name, s.student_email, "
	      + "c.company_id, c.company_name, "
	      + "co.course_code, co.course_name, co.course_term "
	      + "FROM request r "
	      + "JOIN student s ON r.student_number = s.student_number "
	      + "JOIN course co ON s.course_code = co.course_code "
	      + "JOIN company c ON r.company_id = c.company_id "
	      + "WHERE r.company_id = ? "
	      + "ORDER BY r.request_time DESC";

	    // ② event_progress = 2 のイベントだけ取得するSQL
	    String sqlEvent =
	        "SELECT event_id, event_start_time, event_end_time, event_place "
	      + "FROM event "
	      + "WHERE company_id = ? AND event_progress = 2";

	        // ------------------------------
	        // ② Company の events を作る
	        // ------------------------------
	        ArrayList<Event> eventList = new ArrayList<>();

	        try (PreparedStatement psEv = con.prepareStatement(sqlEvent)) {
	            psEv.setInt(1, companyId);

	            try (ResultSet rsEv = psEv.executeQuery()) {
	                while (rsEv.next()) {
	                    Event ev = new Event();
	                    ev.setEventId(rsEv.getInt("event_id"));

	                    Timestamp st = rsEv.getTimestamp("event_start_time");
	                    if (st != null) ev.setEventStartTime(st.toLocalDateTime());

	                    Timestamp et = rsEv.getTimestamp("event_end_time");
	                    if (et != null) ev.setEventEndTime(et.toLocalDateTime());

	                    ev.setEventPlace(rsEv.getString("event_place"));

	                    eventList.add(ev);
	                }
	            }
	        }

	        // ------------------------------
	        // ③ request と student を取得
	        // ------------------------------
	        try (PreparedStatement ps = con.prepareStatement(sqlReq)) {
	            ps.setInt(1, companyId);

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    // ----- Course -----
	                    Course course = new Course();
	                    course.setCourseName(rs.getString("course_name"));

	                    // ----- Student -----
	                    Student student = new Student();
	                    student.setStudentNumber(rs.getString("student_number"));
	                    student.setStudentName(rs.getString("student_name"));
	                    student.setCourse(course);

	                    // ----- Company -----
	                    Company company = new Company();
	                    company.setCompanyId(rs.getInt("company_id"));
	                    company.setCompanyName(rs.getString("company_name"));
	                    company.setEvents(eventList);   // <--- ★ここ重要：progress=2 の event だけ

	                    // ----- Request -----
	                    Request req = new Request();
	                    req.setstudent(student);
	                    req.setcompany(company);
	                    req.setRequestTime(rs.getTimestamp("request_time").toLocalDateTime());

	                    list.add(req);
	                }
	            }
	        }
	        return list;
	    }



	public void insertRequest(int companyId, String studentNumber) throws Exception {
		Connection con = createConnection();
		try {
			String sql = "INSERT INTO request (company_id, student_number) VALUES (?, ?)";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setInt(1, companyId);
				ps.setString(2, studentNumber);
				ps.executeUpdate();
			}
		} finally {
			if (con != null)
				con.close();
		}
	}

	public void cancelRequest(int companyId, String studentNumber) throws Exception {
		Connection con = createConnection();
		try {
			String sql = "DELETE FROM request WHERE company_id = ? AND student_number = ?";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setInt(1, companyId);
				ps.setString(2, studentNumber);
				ps.executeUpdate();
			}
		} finally {
			if (con != null)
				con.close();
		}
	}

	public List<String> searchEmailsByCompanyId(int companyId) throws Exception {
	    Connection con = createConnection();
	    List<String> emails = new ArrayList<>();

	    String sql = "SELECT s.student_email FROM request r "
	               + "JOIN student s ON r.student_number = s.student_number "
	               + "WHERE r.company_id = ?";

	    try (PreparedStatement pstmt = con.prepareStatement(sql)) {

	        pstmt.setInt(1, companyId);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            emails.add(rs.getString("student_email"));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return emails;
	}
}

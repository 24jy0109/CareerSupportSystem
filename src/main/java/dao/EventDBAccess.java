package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dto.EventDTO;
import model.Company;
import model.Course;
import model.Event;
import model.Graduate;
import model.Staff;
import model.Student;

public class EventDBAccess extends DBAccess {

	public Event insertEvent(Event event) throws Exception {
		Connection con = createConnection();
		con.setAutoCommit(false);

		try {
			// --------------------------------------------------
			// 1. event テーブルへ INSERT
			// --------------------------------------------------
			String sqlEvent = "INSERT INTO event "
					+ "(staff_id, company_id, event_place, event_start_time, event_end_time, event_capacity, event_progress, event_other_info) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			int eventId = 0;

			try (PreparedStatement ps = con.prepareStatement(sqlEvent, PreparedStatement.RETURN_GENERATED_KEYS)) {

				// ① staff_id
				setNullable(ps, 1,
						event.getStaff() == null ? null : event.getStaff().getStaffId(),
						Types.INTEGER);

				// ② company_id
				setNullable(ps, 2,
						event.getCompany() == null ? null : event.getCompany().getCompanyId(),
						Types.INTEGER);

				// ③ event_place
				setNullable(ps, 3, event.getEventPlace(), Types.VARCHAR);

				// ④ event_start_time
				setNullable(ps, 4,
						event.getEventStartTime() == null ? null : Timestamp.valueOf(event.getEventStartTime()),
						Types.TIMESTAMP);

				// ⑤ event_end_time
				setNullable(ps, 5,
						event.getEventEndTime() == null ? null : Timestamp.valueOf(event.getEventEndTime()),
						Types.TIMESTAMP);

				// ⑥ event_capacity
				setNullable(ps, 6, event.getEventCapacity(), Types.INTEGER);

				// ⑦ event_progress
				setNullable(ps, 7, event.getEventProgress(), Types.INTEGER);

				// ⑧ event_other_info
				setNullable(ps, 8, event.getEventOtherInfo(), Types.VARCHAR);

				ps.executeUpdate();

				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						eventId = rs.getInt(1);
						event.setEventId(eventId); // ★ Event にセット
					}
				}
			}

			// --------------------------------------------------
			// 2. join_graduate へ参加卒業生 INSERT
			// --------------------------------------------------
			if (event.getJoinGraduates() != null && !event.getJoinGraduates().isEmpty()) {

				String sqlJoin = "INSERT INTO join_graduate (event_id, graduate_student_number) VALUES (?, ?)";

				try (PreparedStatement psJoin = con.prepareStatement(sqlJoin)) {

					for (Graduate g : event.getJoinGraduates()) {
						setNullable(psJoin, 1, eventId, Types.INTEGER);
						setNullable(psJoin, 2, g.getGraduateStudentNumber(), Types.VARCHAR);
						psJoin.addBatch();
					}

					psJoin.executeBatch();
				}
			}

			con.commit();

			return event;

		} catch (Exception e) {
			con.rollback();
			throw e;

		} finally {
			if (con != null)
				con.close();
		}
	}

	public Event updateEvent(Event event) throws Exception {
		Connection con = createConnection();
		con.setAutoCommit(false);

		try {
			// ---------------------------------
			// 1. event を UPDATE
			// ---------------------------------
			String sqlEvent = "UPDATE event SET "
					+ "staff_id = ?, "
					+ "company_id = ?, "
					+ "event_place = ?, "
					+ "event_start_time = ?, "
					+ "event_end_time = ?, "
					+ "event_capacity = ?, "
					+ "event_progress = ?, "
					+ "event_other_info = ? "
					+ "WHERE event_id = ?";

			try (PreparedStatement ps = con.prepareStatement(sqlEvent)) {

				setNullable(ps, 1,
						event.getStaff() == null ? null : event.getStaff().getStaffId(),
						Types.INTEGER);

				setNullable(ps, 2,
						event.getCompany() == null ? null : event.getCompany().getCompanyId(),
						Types.INTEGER);

				setNullable(ps, 3, event.getEventPlace(), Types.VARCHAR);

				setNullable(ps, 4,
						event.getEventStartTime() == null ? null : Timestamp.valueOf(event.getEventStartTime()),
						Types.TIMESTAMP);

				setNullable(ps, 5,
						event.getEventEndTime() == null ? null : Timestamp.valueOf(event.getEventEndTime()),
						Types.TIMESTAMP);

				setNullable(ps, 6, event.getEventCapacity(), Types.INTEGER);
				setNullable(ps, 7, event.getEventProgress(), Types.INTEGER);
				setNullable(ps, 8, event.getEventOtherInfo(), Types.VARCHAR);

				ps.setInt(9, event.getEventId());

				ps.executeUpdate();
			}

			// ---------------------------------
			// 2. join_graduate に INSERT（初回）
			// ---------------------------------
			if (event.getJoinGraduates() != null && !event.getJoinGraduates().isEmpty()) {

				String sqlJoin = "INSERT INTO join_graduate (event_id, graduate_student_number) VALUES (?, ?)";

				try (PreparedStatement psJoin = con.prepareStatement(sqlJoin)) {

					for (Graduate g : event.getJoinGraduates()) {
						psJoin.setInt(1, event.getEventId());
						psJoin.setString(2, g.getGraduateStudentNumber());
						psJoin.addBatch();
					}

					psJoin.executeBatch();
				}
			}

			con.commit();
			return event;

		} catch (Exception e) {
			con.rollback();
			throw e;

		} finally {
			if (con != null)
				con.close();
		}
	}

	public List<EventDTO> getAllEvents() throws Exception {

		List<EventDTO> list = new ArrayList<>();
		Connection con = createConnection();

		String sql = "SELECT " +
				"  e.event_id, " +
				"  e.event_progress, " +
				"  e.event_capacity, " +
				"  c.company_id, " +
				"  c.company_name, " +
				"  COUNT(js.student_number) AS join_count " +
				"FROM event e " +
				"JOIN company c " +
				"  ON e.company_id = c.company_id " +
				"LEFT JOIN join_student js " +
				"  ON e.event_id = js.event_id " +
				" AND js.join_availability = 1 " +
				"WHERE e.event_progress <> 0 " +
				"GROUP BY " +
				"  e.event_id, " +
				"  e.event_progress, " +
				"  c.company_id, " +
				"  c.company_name " +
				"ORDER BY e.event_id";

		try (PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				// -------- Event --------
				Event event = new Event();
				event.setEventId(rs.getInt("event_id"));
				event.setEventProgress(rs.getInt("event_progress"));
				event.setEventCapacity(rs.getInt("event_capacity"));

				// -------- Company --------
				Company company = new Company();
				company.setCompanyId(rs.getInt("company_id"));
				company.setCompanyName(rs.getString("company_name"));
				event.setCompany(company);

				// -------- EventDTO --------
				EventDTO dto = new EventDTO();
				dto.setEvent(event);
				dto.setJoinStudentCount(rs.getInt("join_count"));

				list.add(dto);
			}
		} finally {
			if (con != null)
				con.close();
		}

		return list;
	}

	public EventDTO searchEventById(int eventId) throws Exception {

		Connection con = createConnection();
		EventDTO dto = null;

		try {
			// ============================================
			// 1. Event / Company / Staff / 参加学生数
			// ============================================
			String sql = "SELECT "
					+ " e.event_id, e.event_place, e.event_start_time, e.event_end_time, "
					+ " e.event_capacity, e.event_progress, e.event_other_info, "
					+ " c.company_id, c.company_name, "
					+ " s.staff_id, s.staff_name, s.staff_email, "
					+ " COUNT(js.student_number) AS join_student_count "
					+ "FROM event e "
					+ "JOIN company c ON e.company_id = c.company_id "
					+ "JOIN staff s ON e.staff_id = s.staff_id "
					+ "LEFT JOIN join_student js "
					+ "  ON e.event_id = js.event_id AND js.join_availability = true "
					+ "WHERE e.event_id = ? "
					+ "GROUP BY "
					+ " e.event_id, e.event_place, e.event_start_time, e.event_end_time, "
					+ " e.event_capacity, e.event_progress, "
					+ " c.company_id, c.company_name, "
					+ " s.staff_id, s.staff_name, s.staff_email";

			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setInt(1, eventId);

				try (ResultSet rs = ps.executeQuery()) {
					if (!rs.next()) {
						return null; // 存在しない
					}

					// Event
					Event event = new Event();
					event.setEventId(rs.getInt("event_id"));
					event.setEventPlace(rs.getString("event_place"));
					event.setEventStartTime(rs.getTimestamp("event_start_time").toLocalDateTime());
					event.setEventEndTime(rs.getTimestamp("event_end_time").toLocalDateTime());
					event.setEventCapacity(rs.getInt("event_capacity"));
					event.setEventProgress(rs.getInt("event_progress"));
					event.setEventOtherInfo(rs.getString("event_other_info"));

					// Company
					Company company = new Company();
					company.setCompanyId(rs.getInt("company_id"));
					company.setCompanyName(rs.getString("company_name"));
					event.setCompany(company);

					// Staff
					Staff staff = new Staff();
					staff.setStaffId(rs.getInt("staff_id"));
					staff.setStaffName(rs.getString("staff_name"));
					staff.setStaffEmail(rs.getString("staff_email"));
					event.setStaff(staff);

					dto = new EventDTO();
					dto.setEvent(event);
					dto.setJoinStudentCount(rs.getInt("join_student_count"));
				}
			}

			// ============================================
			// 2. 参加卒業生一覧
			// ============================================
			String sqlGraduate = "SELECT "
					+ " g.graduate_student_number, g.graduate_name, g.graduate_job_category, "
					+ " g.graduate_other_info, "
					+ " co.course_name, co.course_term "
					+ "FROM join_graduate jg "
					+ "JOIN graduate g ON jg.graduate_student_number = g.graduate_student_number "
					+ "JOIN course co ON g.course_code = co.course_code "
					+ "WHERE jg.event_id = ?";

			List<Graduate> graduates = new ArrayList<>();

			try (PreparedStatement ps = con.prepareStatement(sqlGraduate)) {
				ps.setInt(1, eventId);

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Graduate g = new Graduate();
						g.setGraduateStudentNumber(rs.getString("graduate_student_number"));
						g.setGraduateName(rs.getString("graduate_name"));
						g.setGraduateJobCategory(rs.getString("graduate_job_category"));
						g.setOtherInfo(rs.getString("graduate_other_info"));

						Course course = new Course();
						course.setCourseName(rs.getString("course_name"));
						course.setCourseTerm(rs.getInt("course_term"));
						g.setCourse(course);

						graduates.add(g);
					}
				}
			}

			dto.setGraduates(graduates);

			return dto;

		} finally {
			if (con != null)
				con.close();
		}
	}

	public void eventEnd(int eventId) throws Exception {
		String sql = "UPDATE event SET event_progress = 3 WHERE event_id = ?";

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, eventId);
			ps.executeUpdate();
		}
	}

	public EventDTO eventCancel(int eventId) throws Exception {

		// ① イベントをキャンセル状態に更新
		String updateSql = "UPDATE event SET event_progress = 4 WHERE event_id = ?";

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(updateSql)) {
			ps.setInt(1, eventId);
			ps.executeUpdate();
		}

		// ② イベント基本情報 + 企業 + 担当職員を取得
		String eventSql = "SELECT e.event_start_time, e.event_end_time, " +
				"       c.company_name, " +
				"       s.staff_name, s.staff_email " +
				"FROM event e " +
				"JOIN company c ON e.company_id = c.company_id " +
				"JOIN staff s ON e.staff_id = s.staff_id " +
				"WHERE e.event_id = ?";

		Event event = null;
		Staff staff = null;

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(eventSql)) {
			ps.setInt(1, eventId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					event = new Event();
					event.setEventStartTime(rs.getTimestamp("event_start_time").toLocalDateTime());
					event.setEventEndTime(rs.getTimestamp("event_end_time").toLocalDateTime());

					Company company = new Company();
					company.setCompanyName(rs.getString("company_name"));
					event.setCompany(company);

					staff = new Staff();
					staff.setStaffName(rs.getString("staff_name"));
					staff.setStaffEmail(rs.getString("staff_email"));
				}
			}
		}

		// ③ 参加卒業生を取得
		String graduateSql = "SELECT g.graduate_name, g.graduate_email, " +
				"       g.graduate_job_category " +
				"FROM join_graduate jg " +
				"JOIN graduate g ON jg.graduate_student_number = g.graduate_student_number " +
				"WHERE jg.event_id = ?";

		List<Graduate> graduates = new ArrayList<>();

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(graduateSql)) {
			ps.setInt(1, eventId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Graduate g = new Graduate();
					g.setGraduateName(rs.getString("graduate_name"));
					g.setGraduateEmail(rs.getString("graduate_email"));
					g.setGraduateJobCategory(rs.getString("graduate_job_category"));
					graduates.add(g);
				}
			}
		}

		// ④ 参加在校生を取得
		String joinStudentSql = "SELECT s.student_number, s.student_name, s.student_email " +
				"FROM join_student js " +
				"JOIN student s ON js.student_number = s.student_number " +
				"WHERE js.event_id = ? " +
				"AND js.join_availability = 1";

		List<Student> students = new ArrayList<>();

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(joinStudentSql)) {
			ps.setInt(1, eventId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Student student = new Student();
					student.setStudentNumber(rs.getString("student_number"));
					student.setStudentName(rs.getString("student_name"));
					student.setStudentEmail(rs.getString("student_email"));

					students.add(student);
				}
			}
		}

		// ⑤ EventDTO にまとめて返却
		EventDTO dto = new EventDTO();
		dto.setEvent(event);
		dto.setStaffs(List.of(staff)); // 単一だが DTO 定義に合わせる
		dto.setGraduates(graduates);
		dto.setStudents(students);

		return dto;
	}

	public void eventJoin(String studentNumber, int eventId) throws Exception {
		String countSql = "SELECT e.event_capacity, COUNT(js.student_number) AS join_count " +
				"FROM event e " +
				"LEFT JOIN join_student js " +
				"  ON e.event_id = js.event_id " +
				" AND js.join_availability = 1 " +
				"WHERE e.event_id = ? " +
				"GROUP BY e.event_capacity";

		String existsSql = "SELECT 1 FROM join_student " +
				"WHERE event_id = ? AND student_number = ?";

		String insertSql = "INSERT INTO join_student (event_id, student_number, join_availability) " +
				"VALUES (?, ?, 1)";

		try (Connection con = createConnection()) {

			con.setAutoCommit(false);

			int capacity = 0;
			int joinCount = 0;

			/* ① 定員 & 現在の参加人数を取得 */
			try (PreparedStatement ps = con.prepareStatement(countSql)) {
				ps.setInt(1, eventId);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						capacity = rs.getInt("event_capacity");
						joinCount = rs.getInt("join_count");
					} else {
						throw new Exception("イベントが存在しません");
					}
				}
			}

			/* ② 定員チェック */
			if (joinCount >= capacity) {
				con.rollback();
				throw new Exception("定員に達しています");
			}

			/* ③ 二重参加チェック */
			try (PreparedStatement ps = con.prepareStatement(existsSql)) {
				ps.setInt(1, eventId);
				ps.setString(2, studentNumber);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						con.rollback();
						throw new Exception("すでに参加済みです");
					}
				}
			}

			/* ④ 参加登録 */
			try (PreparedStatement ps = con.prepareStatement(insertSql)) {
				ps.setInt(1, eventId);
				ps.setString(2, studentNumber);
				ps.executeUpdate();
			}

			con.commit();
		}

	}

	public void eventNotJoin(String studentNumber, int eventId) throws Exception {
		String insertSql =
			"INSERT INTO join_student (event_id, student_number, join_availability) " +
			"VALUES (?, ?, 0)";

		try (
			Connection con = createConnection();
			PreparedStatement ps = con.prepareStatement(insertSql)
		) {
			ps.setInt(1, eventId);
			ps.setString(2, studentNumber);
			ps.executeUpdate();
		}
	}

	public List<EventDTO> joinHistoryList(String studentNumber) throws Exception {
		List<EventDTO> list = new ArrayList<>();
		String sql =
			"SELECT " +
			" js.event_id, " +
			" js.join_availability, " +
			" e.event_progress, " +
			" c.company_id, " +
			" c.company_name " +
			"FROM join_student js " +
			"JOIN event e ON js.event_id = e.event_id " +
			"JOIN company c ON e.company_id = c.company_id " +
			"WHERE js.student_number = ? " +
			"ORDER BY js.event_id";

		try (
			Connection con = createConnection();
			PreparedStatement ps = con.prepareStatement(sql)
		) {
			ps.setString(1, studentNumber);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {

					// Event
					Event event = new Event();
					event.setEventId(rs.getInt("event_id"));
					event.setEventProgress(rs.getInt("event_progress"));

					// Company
					Company company = new Company();
					company.setCompanyId(rs.getInt("company_id"));
					company.setCompanyName(rs.getString("company_name"));
					event.setCompany(company);

					// DTO
					EventDTO dto = new EventDTO();
					dto.setEvent(event);

					// join_availability（int → boolean）
					dto.setJoinAvailability(
						rs.getInt("join_availability") == 1
					);

					list.add(dto);
				}
			}
		}
		return list;
	}
}
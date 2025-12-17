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
import model.Event;
import model.Graduate;

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

		String sql = "SELECT "
				+ " e.event_id, "
				+ " e.event_progress, "
				+ " c.company_id, "
				+ " c.company_name, "
				+ " COUNT(js.student_number) AS join_count "
				+ "FROM event e "
				+ "JOIN company c ON e.company_id = c.company_id "
				+ "LEFT JOIN join_student js "
				+ "  ON e.event_id = js.event_id "
				+ " AND js.join_availability = 1 "
				+ "GROUP BY "
				+ " e.event_id, "
				+ " e.event_progress, "
				+ " c.company_id, "
				+ " c.company_name "
				+ "ORDER BY e.event_id";

		try (PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				// -------- Event --------
				Event event = new Event();
				event.setEventId(rs.getInt("event_id"));
				event.setEventProgress(rs.getInt("event_progress"));

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

}
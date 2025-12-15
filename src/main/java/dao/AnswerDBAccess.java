package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Answer;
import model.Company;
import model.Event;
import model.Graduate;

public class AnswerDBAccess extends DBAccess {
	// ===================================================================================
	// INSERT
	// ===================================================================================
	public Answer insertAnswer(Answer answer) throws Exception {

		String sql = "INSERT INTO answer ("
				+ "event_id, graduate_student_number, event_availability, "
				+ "first_choice_start_time, first_choice_end_time, "
				+ "second_choice_start_time, second_choice_end_time, "
				+ "third_choice_start_time, third_choice_end_time"
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			setNullable(ps, 1, answer.getEvent().getEventId(), java.sql.Types.INTEGER);
			setNullable(ps, 2, answer.getGraduate().getGraduateStudentNumber(), java.sql.Types.CHAR);

			setNullable(ps, 3, answer.getEventAvailability(), java.sql.Types.BOOLEAN);

			setNullable(ps, 4, answer.getFirstChoiceStartTime() == null ? null
					: Timestamp.valueOf(answer.getFirstChoiceStartTime()), java.sql.Types.TIMESTAMP);
			setNullable(ps, 5,
					answer.getFirstChoiceEndTime() == null ? null : Timestamp.valueOf(answer.getFirstChoiceEndTime()),
					java.sql.Types.TIMESTAMP);

			setNullable(ps, 6, answer.getSecondChoiceStartTime() == null ? null
					: Timestamp.valueOf(answer.getSecondChoiceStartTime()), java.sql.Types.TIMESTAMP);
			setNullable(ps, 7,
					answer.getSecondChoiceEndTime() == null ? null : Timestamp.valueOf(answer.getSecondChoiceEndTime()),
					java.sql.Types.TIMESTAMP);

			setNullable(ps, 8, answer.getThirdChoiceStartTime() == null ? null
					: Timestamp.valueOf(answer.getThirdChoiceStartTime()), java.sql.Types.TIMESTAMP);
			setNullable(ps, 9,
					answer.getThirdChoiceEndTime() == null ? null : Timestamp.valueOf(answer.getThirdChoiceEndTime()),
					java.sql.Types.TIMESTAMP);

			ps.executeUpdate();

			// AUTO_INCREMENT の取得
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					answer.setAnswerId(rs.getInt(1));
				}
			}
		}

		return answer;
	}

	// ===================================================================================
	// UPDATE
	// ===================================================================================
	public void updateAnswer(Answer answer) {

		String sql = "UPDATE answer SET "
				+ "event_availability = ?, "
				+ "first_choice_start_time = ?, first_choice_end_time = ?, "
				+ "second_choice_start_time = ?, second_choice_end_time = ?, "
				+ "third_choice_start_time = ?, third_choice_end_time = ? "
				+ "WHERE answer_id = ?";

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			setNullable(ps, 1, answer.getEventAvailability(), java.sql.Types.BOOLEAN);

			setNullable(ps, 2, answer.getFirstChoiceStartTime() == null ? null
					: Timestamp.valueOf(answer.getFirstChoiceStartTime()), java.sql.Types.TIMESTAMP);
			setNullable(ps, 3,
					answer.getFirstChoiceEndTime() == null ? null : Timestamp.valueOf(answer.getFirstChoiceEndTime()),
					java.sql.Types.TIMESTAMP);

			setNullable(ps, 4, answer.getSecondChoiceStartTime() == null ? null
					: Timestamp.valueOf(answer.getSecondChoiceStartTime()), java.sql.Types.TIMESTAMP);
			setNullable(ps, 5,
					answer.getSecondChoiceEndTime() == null ? null : Timestamp.valueOf(answer.getSecondChoiceEndTime()),
					java.sql.Types.TIMESTAMP);

			setNullable(ps, 6, answer.getThirdChoiceStartTime() == null ? null
					: Timestamp.valueOf(answer.getThirdChoiceStartTime()), java.sql.Types.TIMESTAMP);
			setNullable(ps, 7,
					answer.getThirdChoiceEndTime() == null ? null : Timestamp.valueOf(answer.getThirdChoiceEndTime()),
					java.sql.Types.TIMESTAMP);

			ps.setInt(8, answer.getAnswerId());

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Answer> getAllAnswers() {
		List<Answer> list = new ArrayList<>();

		String sql = "SELECT "
				+ "a.answer_id, "
				+ "c.company_name, "
				+ "g.graduate_name, "
				+ "a.first_choice_start_time, a.first_choice_end_time, "
				+ "a.second_choice_start_time, a.second_choice_end_time, "
				+ "a.third_choice_start_time, a.third_choice_end_time "
				+ "FROM answer a "
				+ "JOIN graduate g ON a.graduate_student_number = g.graduate_student_number "
				+ "JOIN event e ON a.event_id = e.event_id "
				+ "JOIN company c ON e.company_id = c.company_id "
				+ "ORDER BY a.answer_id";

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Answer answer = new Answer();
				answer.setAnswerId(rs.getInt("answer_id"));

				// ----- Graduate -----
				Graduate graduate = new Graduate();
				graduate.setGraduateName(rs.getString("graduate_name"));
				answer.setGraduate(graduate);

				// ----- Company（Event 経由）-----
				Company company = new Company();
				company.setCompanyName(rs.getString("company_name"));

				Event event = new Event();
				event.setCompany(company);
				answer.setEvent(event);

				Timestamp ts;

				ts = rs.getTimestamp("first_choice_start_time");
				if (ts != null)
					answer.setFirstChoiceStartTime(ts.toLocalDateTime());

				ts = rs.getTimestamp("first_choice_end_time");
				if (ts != null)
					answer.setFirstChoiceEndTime(ts.toLocalDateTime());

				ts = rs.getTimestamp("second_choice_start_time");
				if (ts != null)
					answer.setSecondChoiceStartTime(ts.toLocalDateTime());

				ts = rs.getTimestamp("second_choice_end_time");
				if (ts != null)
					answer.setSecondChoiceEndTime(ts.toLocalDateTime());

				ts = rs.getTimestamp("third_choice_start_time");
				if (ts != null)
					answer.setThirdChoiceStartTime(ts.toLocalDateTime());

				ts = rs.getTimestamp("third_choice_end_time");
				if (ts != null)
					answer.setThirdChoiceEndTime(ts.toLocalDateTime());

				list.add(answer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public Answer searchAnswerById(int answerId, int choice) throws Exception {
		Connection con = createConnection();
		Answer answer = null;

		String timeSelect;
		if (choice == 1) {
			timeSelect = "a.first_choice_start_time, a.first_choice_end_time";
		} else if (choice == 2) {
			timeSelect = "a.second_choice_start_time, a.second_choice_end_time";
		} else if (choice == 3) {
			timeSelect = "a.third_choice_start_time, a.third_choice_end_time";
		} else {
			throw new IllegalArgumentException("choice は 1〜3 を指定してください");
		}

		String sql = "SELECT a.answer_id, a.graduate_student_number, " +
				timeSelect + ", " +
				"c.company_id, c.company_name " +
				"FROM answer a " +
				"JOIN graduate g ON a.graduate_student_number = g.graduate_student_number " +
				"JOIN company c ON g.company_id = c.company_id " +
				"WHERE a.answer_id = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, answerId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					answer = new Answer();
					answer.setAnswerId(rs.getInt("answer_id"));

					// 卒業生
					Graduate grad = new Graduate();
					grad.setGraduateStudentNumber(rs.getString("graduate_student_number"));
					answer.setGraduate(grad);

					// 企業
					Company company = new Company();
					company.setCompanyId(rs.getInt("company_id"));
					company.setCompanyName(rs.getString("company_name"));

					// Graduate に Company をセット
					grad.setCompany(company);

					// 希望時間（choice に応じて）
					Timestamp st;
					Timestamp et;

					if (choice == 1) {
						st = rs.getTimestamp("first_choice_start_time");
						et = rs.getTimestamp("first_choice_end_time");
						if (st != null)
							answer.setFirstChoiceStartTime(st.toLocalDateTime());
						if (et != null)
							answer.setFirstChoiceEndTime(et.toLocalDateTime());

					} else if (choice == 2) {
						st = rs.getTimestamp("second_choice_start_time");
						et = rs.getTimestamp("second_choice_end_time");
						if (st != null)
							answer.setSecondChoiceStartTime(st.toLocalDateTime());
						if (et != null)
							answer.setSecondChoiceEndTime(et.toLocalDateTime());

					} else {
						st = rs.getTimestamp("third_choice_start_time");
						et = rs.getTimestamp("third_choice_end_time");
						if (st != null)
							answer.setThirdChoiceStartTime(st.toLocalDateTime());
						if (et != null)
							answer.setThirdChoiceEndTime(et.toLocalDateTime());
					}
				}
			}
		} finally {
			if (con != null)
				con.close();
		}

		return answer;
	}

}

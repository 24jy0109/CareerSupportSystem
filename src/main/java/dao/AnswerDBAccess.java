package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import model.Answer;

public class AnswerDBAccess extends DBAccess {

	public Answer insertAnswer(Answer answer) throws Exception {

		Connection con = createConnection();

		String sql = "INSERT INTO answer ("
				+ "graduate_student_number, event_availability, "
				+ "first_choice, second_choice, third_choice"
				+ ") VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = con.prepareStatement(
				sql, Statement.RETURN_GENERATED_KEYS)) {

			// graduate_student_number
			if (answer.getGraduateStudentNumber() != null) {
				pstmt.setString(1, answer.getGraduateStudentNumber());
			} else {
				pstmt.setNull(1, java.sql.Types.VARCHAR);
			}

			// event_availability
			if (answer.getEventAvailability() != null) {
				pstmt.setBoolean(2, answer.getEventAvailability());
			} else {
				pstmt.setNull(2, java.sql.Types.BOOLEAN);
			}

			// first_choice
			if (answer.getFirstChoice() != null) {
				pstmt.setTimestamp(3, Timestamp.valueOf(answer.getFirstChoice()));
			} else {
				pstmt.setNull(3, java.sql.Types.TIMESTAMP);
			}

			// second_choice
			if (answer.getSecondChoice() != null) {
				pstmt.setTimestamp(4, Timestamp.valueOf(answer.getSecondChoice()));
			} else {
				pstmt.setNull(4, java.sql.Types.TIMESTAMP);
			}

			// third_choice
			if (answer.getThirdChoice() != null) {
				pstmt.setTimestamp(5, Timestamp.valueOf(answer.getThirdChoice()));
			} else {
				pstmt.setNull(5, java.sql.Types.TIMESTAMP);
			}

			pstmt.executeUpdate();

			// ---- ここでAUTO_INCREMENTされたIDを取得 ----
			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					int generatedId = rs.getInt(1);
					answer.setAnswerId(generatedId);
				}
			}
		}

		return answer;
	}
}

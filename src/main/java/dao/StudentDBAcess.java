package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Student;

public class StudentDBAcess extends DBAccess {

	public void insertStudent(Student student) throws Exception {

		String select_sql =
				"SELECT student_number FROM student WHERE student_number = ?";

		String insert_sql =
				"INSERT INTO student (student_number, course_code, student_name, student_email) "
				+ "VALUES (?, ?, ?, ?)";

		try (Connection con = createConnection();
			 PreparedStatement select_pstate = con.prepareStatement(select_sql)) {

			// ---- 既存学生チェック ----
			select_pstate.setString(1, student.getStudentNumber());

			try (ResultSet rs = select_pstate.executeQuery()) {

				if (!rs.next()) {

					try (PreparedStatement insert_pstate =
							con.prepareStatement(insert_sql)) {

						insert_pstate.setString(1, student.getStudentNumber());
						insert_pstate.setString(2, student.getCourse().getCourseCode());
						insert_pstate.setString(3, student.getStudentName());
						insert_pstate.setString(4, student.getStudentEmail());

						try {
							insert_pstate.executeUpdate();
						} catch (Exception e) {
							throw new Exception("学生情報の登録処理に失敗しました。", e);
						}
					}
				}
			}

		} catch (Exception e) {

			// すでに業務メッセージが設定されている場合はそのまま投げる
			if (e.getMessage() != null &&
				(e.getMessage().contains("学生情報"))) {
				throw e;
			}

			// それ以外は存在確認失敗としてまとめる
			throw new Exception("学生情報の存在確認に失敗しました。", e);
		}
	}
}

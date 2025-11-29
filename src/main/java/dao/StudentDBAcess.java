package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Student;

public class StudentDBAcess extends DBAccess {
	public void insertStudent(Student student) throws Exception {
		//データベース接続
		Connection con = createConnection();
		
		String select_sql = "SELECT student_number FROM student WHERE student_number = ?";
		PreparedStatement select_pstate = con.prepareStatement(select_sql);
		select_pstate.setString(1, student.getStudentNumber());
		ResultSet rs;
		try {
			//SQL実行
			rs = select_pstate.executeQuery();
		} catch (Exception e) {
			throw new Exception(e);
		}

		if (!rs.next()) {
			String insert_sql = "INSERT INTO student (student_number, course_code, student_name, student_email) VALUES "
					+ "(?, ?, ?, ?)";
			PreparedStatement insert_pstate = con.prepareStatement(insert_sql);
			
			insert_pstate.setString(1, student.getStudentNumber());
			insert_pstate.setString(2, student.getCourse().getCourseCode());
			insert_pstate.setString(3, student.getStudentName());
			insert_pstate.setString(4, student.getStudentEmail());
			try {
				//SQL実行
				insert_pstate.executeUpdate();
				rs.close();
				insert_pstate.close();
			} catch (Exception e) {
				throw new Exception(e);
			}
		}
		
		//DB接続を切断
		select_pstate.close();
		closeConnection(con);
	}
}

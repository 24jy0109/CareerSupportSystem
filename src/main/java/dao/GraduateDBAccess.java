package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import model.Graduate;

public class GraduateDBAccess extends DBAccess {

	// 既存卒業生の担当スタッフを変更 or 設定する
	public void setStaff(Graduate graduate) throws Exception {
		Connection con = createConnection();
		try {
			String sql = "UPDATE graduate "
					+ "SET staff_id = ? "
					+ "WHERE graduate_student_number = ?";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setInt(1, graduate.getStaff().getStaffId());
				ps.setString(2, graduate.getGraduateStudentNumber());
				ps.executeUpdate();
			}
		} finally {
			if (con != null)
				con.close();
		}
	}
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

	public Graduate searchGraduateByGraduateStudentNumber(String graduateStudentNumber) throws Exception {
	    Connection con = createConnection();

	    String sql = "SELECT graduate_student_number, graduate_name, graduate_email, graduate_other_info, graduate_job_category "
	               + "FROM graduate WHERE graduate_student_number = ?";

	    try (PreparedStatement pstmt = con.prepareStatement(sql)) {

	        pstmt.setString(1, graduateStudentNumber);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            Graduate g = new Graduate();
	            g.setGraduateStudentNumber(rs.getString("graduate_student_number"));
	            g.setGraduateName(rs.getString("graduate_name"));
	            g.setGraduateEmail(rs.getString("graduate_email"));
	            g.setOtherInfo(rs.getString("graduate_other_info"));
	            g.setGraduateJobCategory(rs.getString("graduate_job_category"));
	            return g;
	        }

	    } finally {
	        if (con != null) con.close();
	    }

	    return null; // 見つからなかった場合
	}


}

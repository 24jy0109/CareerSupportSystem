package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Company;
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

	    // company_id と company_name のみ JOIN
	    String sql = "SELECT g.graduate_student_number, g.graduate_name, g.graduate_email, " +
	                 "g.graduate_other_info, g.graduate_job_category, g.company_id, " +
	                 "c.company_name " +
	                 "FROM graduate g " +
	                 "LEFT JOIN company c ON g.company_id = c.company_id " +
	                 "WHERE g.graduate_student_number = ?";

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

	            // --- Company セット（company_id と company_name のみ） ---
	            int companyId = rs.getInt("company_id");
	            if (!rs.wasNull()) { // company_id が NULL でない場合
	                Company c = new Company();
	                c.setCompanyId(companyId);
	                c.setCompanyName(rs.getString("company_name"));
	                g.setCompany(c);
	            }

	            return g;
	        }

	    } finally {
	        if (con != null) con.close();
	    }

	    return null;
	}


	
	public List<Graduate> findAll() throws Exception {

	    List<Graduate> list = new ArrayList<>();

	    Connection con = createConnection();

	    String sql = "SELECT * FROM graduate";
	    PreparedStatement ps = con.prepareStatement(sql);
	    ResultSet rs = ps.executeQuery();

	    while (rs.next()) {
	        Graduate g = new Graduate();
//	        g.setGraduateId(rs.getInt("graduate_id"));
	        g.setGraduateName(rs.getString("graduate_name"));
	        list.add(g);
	    }

	    rs.close();
	    ps.close();
	    con.close();

	    return list;
	}

	public void insertGraduate(Graduate graduate) {
		String sql = "INSERT INTO graduate (company_id, staff_id, course_code, graduate_student_number, graduate_name, graduate_email, graduate_other_info, graduate_job_category) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = createConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, graduate.getCompany().getCompanyId());
			ps.setNull(2, java.sql.Types.INTEGER); // staff_id を NULL
			ps.setString(3, graduate.getCourse().getCourseCode());
			ps.setString(4, graduate.getGraduateStudentNumber());
			ps.setString(5, graduate.getGraduateName());
			ps.setString(6, graduate.getGraduateEmail());
			ps.setString(7, graduate.getOtherInfo());
			ps.setString(8, graduate.getGraduateJobCategory());

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB登録中にエラーが発生しました：" + e.getMessage());

		} finally {
			try {
				closeConnection(con);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}

	public void updateGraduate(Graduate graduate) {

	}

	public void deleteGraduate(int graduateId) {

	}

	public void searchGraduate(int graduate) {

	}

}

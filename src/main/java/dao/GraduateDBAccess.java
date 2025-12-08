package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Graduate;

public class GraduateDBAccess extends DBAccess {
	public void setStaffId(int staffId) {

	}

	public void insertGraduate(Graduate graduate) {
		String sql = "INSERT INTO graduate (company_id, staff_id, course_code, graduate_student_number, graduate_name, graduate_email, other_info, graduate_job_category) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		 Connection con = null;
		    PreparedStatement ps = null;

		    try {
		        con = createConnection();
		        ps = con.prepareStatement(sql);

		        ps.setInt(1, graduate.getComapany().getCompanyId());
		        ps.setString(2, graduate.getCourse().getCourseCode());
		        ps.setString(3, graduate.getGraduateStudentNumber());
		        ps.setString(4, graduate.getGraduateName());
		        ps.setString(5, graduate.getGraduateEmail());
		        ps.setString(6, graduate.getOtherInfo());
		        ps.setString(7, graduate.getGraduateJobCategory());

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
		/*Connection con = createConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		
		ps.setInt(1, graduate.getComapany().getCompanyId());
		ps.setInt(2, graduate.getStaff().getStaffId());
		ps.setInt(3, graduate.getCourse().getCourseCode());
		ps.setString(4, graduate.getGraduateStudentNumber());
		ps.setString(5, graduate.getGraduateName());
		ps.setString(6, graduate.getGraduateEmail());
		ps.setString(7, graduate.getOtherInfo());
		ps.setString(8, graduate.getGraduateJobCategory());
		
		ps.executeUpdate();
		closeConnection(con);*/
	}

	public void updateGraduate(Graduate graduate) {

	}

	public void deleteGraduate(int graduateId) {

	}

	public void searchGraduate(int graduate) {

	}
	
	
	public List<Graduate> findAll() throws Exception {

	    List<Graduate> list = new ArrayList<>();

	    Connection con = createConnection();

	    String sql = "SELECT * FROM graduate";
	    PreparedStatement ps = con.prepareStatement(sql);
	    ResultSet rs = ps.executeQuery();

	    while (rs.next()) {
	        Graduate g = new Graduate();
	        g.setGraduateId(rs.getInt("graduate_id"));
	        g.setGraduateName(rs.getString("graduate_name"));
	        list.add(g);
	    }

	    rs.close();
	    ps.close();
	    con.close();

	    return list;
	}
}

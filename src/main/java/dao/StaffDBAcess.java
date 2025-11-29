package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Staff;

public class StaffDBAcess extends DBAccess {
	public void insertStaff(Staff staff) throws Exception {
		//データベース接続
		Connection con = createConnection();
		
		String select_sql = "SELECT staff_email FROM staff WHERE staff_email = ?";
		PreparedStatement select_pstate = con.prepareStatement(select_sql);
		select_pstate.setString(1, staff.getStaffEmail());
		ResultSet rs;
		try {
			//SQL実行
			rs = select_pstate.executeQuery();
		} catch (Exception e) {
			throw new Exception(e);
		}

		if (!rs.next()) {
			String insert_sql = "INSERT INTO staff (staff_name, staff_email) VALUES "
					+ "(?, ?)";
			PreparedStatement insert_pstate = con.prepareStatement(insert_sql);
			
			insert_pstate.setString(1, staff.getStaffName());
			insert_pstate.setString(2, staff.getStaffEmail());
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

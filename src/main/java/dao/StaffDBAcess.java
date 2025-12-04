package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

	public List<Staff> getAllStaffs() throws Exception {
		List<Staff> staffList = new ArrayList<>();

		String sql = "SELECT staff_id, staff_name, staff_email FROM staff";

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();) {

			while (rs.next()) {
				Staff staff = new Staff(
						rs.getInt("staff_id"),
						rs.getString("staff_name"),
						rs.getString("staff_email"));
				staffList.add(staff);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return staffList;
	}

}

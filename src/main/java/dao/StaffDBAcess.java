package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Staff;

public class StaffDBAcess extends DBAccess {
	public void insertStaff(Staff staff) throws Exception {

		String selectSql = "SELECT staff_email FROM staff WHERE staff_email = ?";
		String insertSql = "INSERT INTO staff (staff_name, staff_email) VALUES (?, ?)";

		try (Connection con = createConnection()) {

			try (
					PreparedStatement selectPs = con.prepareStatement(selectSql)) {

				selectPs.setString(1, staff.getStaffEmail());

				try (ResultSet rs = selectPs.executeQuery()) {

					if (!rs.next()) {

						try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
							insertPs.setString(1, staff.getStaffName());
							insertPs.setString(2, staff.getStaffEmail());
							insertPs.executeUpdate();
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、職員情報の登録に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}

	public List<Staff> getAllStaffs() throws Exception {

		List<Staff> staffList = new ArrayList<>();

		String sql = "SELECT staff_id, staff_name, staff_email FROM staff";

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Staff staff = new Staff(
						rs.getInt("staff_id"),
						rs.getString("staff_name"),
						rs.getString("staff_email"));
				staffList.add(staff);
			}

			return staffList;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、職員一覧の取得に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}

	public Staff searchStaffById(int staffId) throws Exception {

		Staff staff = null;
		String sql = "SELECT staff_id, staff_name, staff_email FROM staff WHERE staff_id = ?";

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, staffId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					staff = new Staff();
					staff.setStaffId(rs.getInt("staff_id"));
					staff.setStaffName(rs.getString("staff_name"));
					staff.setStaffEmail(rs.getString("staff_email"));
				}
			}

			return staff;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、職員情報の取得に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}
}

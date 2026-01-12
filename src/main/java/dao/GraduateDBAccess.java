package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Company;
import model.Course;
import model.Graduate;
import model.Staff;

public class GraduateDBAccess extends DBAccess {
	// 既存卒業生の担当スタッフを変更 or 設定する
	public void setStaff(Graduate graduate) throws Exception {
		try (Connection con = createConnection()) {
			String sql = "UPDATE graduate "
					+ "SET staff_id = ? "
					+ "WHERE graduate_student_number = ?";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setInt(1, graduate.getStaff().getStaffId());
				ps.setString(2, graduate.getGraduateStudentNumber());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"卒業生の担当スタッフ設定処理中にエラーが発生しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}

	// 卒業生の担当スタッフを外す（staff_id を NULL にする）
	public void removeStaff(Graduate graduate) throws Exception {
		try (Connection con = createConnection()) {
			String sql = "UPDATE graduate "
					+ "SET staff_id = NULL "
					+ "WHERE graduate_student_number = ?";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, graduate.getGraduateStudentNumber());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"卒業生の担当スタッフ解除処理中にエラーが発生しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}

	public Graduate searchGraduateByGraduateStudentNumber(String graduateStudentNumber) throws Exception {
		// company と staff を JOIN
		String sql = "SELECT g.graduate_student_number, g.graduate_name, g.graduate_email, "
				+ "g.graduate_other_info, g.graduate_job_category, "
				+ "g.company_id, c.company_name, "
				+ "g.staff_id, s.staff_name, s.staff_email, "
				+ "g.course_code, co.course_name "
				+ "FROM graduate g "
				+ "LEFT JOIN company c ON g.company_id = c.company_id "
				+ "LEFT JOIN staff s ON g.staff_id = s.staff_id "
				+ "LEFT JOIN course co ON g.course_code = co.course_code "
				+ "WHERE g.graduate_student_number = ?";

		try (Connection con = createConnection();
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, graduateStudentNumber);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				Graduate g = new Graduate();
				g.setGraduateStudentNumber(rs.getString("graduate_student_number"));
				g.setGraduateName(rs.getString("graduate_name"));
				g.setGraduateEmail(rs.getString("graduate_email"));
				g.setOtherInfo(rs.getString("graduate_other_info"));
				g.setGraduateJobCategory(rs.getString("graduate_job_category"));

				// --- Company セット ---
				int companyId = rs.getInt("company_id");
				if (!rs.wasNull()) {
					Company c = new Company();
					c.setCompanyId(companyId);
					c.setCompanyName(rs.getString("company_name"));
					g.setCompany(c);
				}

				// --- Staff セット ---
				int staffId = rs.getInt("staff_id");
				if (!rs.wasNull()) {
					Staff s = new Staff();
					s.setStaffId(staffId);
					s.setStaffName(rs.getString("staff_name"));
					s.setStaffEmail(rs.getString("staff_email"));
					g.setStaff(s);
				}

				// --- Course セット ---
				String courseCode = rs.getString("course_code");
				if (courseCode != null) {
					Course co = new Course();
					co.setCourseCode(courseCode);
					co.setCourseName(rs.getString("course_name"));
					g.setCourse(co);
				}

				return g;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"卒業生情報の取得処理中にエラーが発生しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
		return null;
	}

	public List<Graduate> searchGraduatesByGraduateStudentNumbers(
			String[] graduateStudentNumbers) throws Exception {

		List<Graduate> list = new ArrayList<>();

		// 空チェック（IN () 防止）
		if (graduateStudentNumbers == null || graduateStudentNumbers.length == 0) {
			return list;
		}

		// ?,?,? 動的生成
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < graduateStudentNumbers.length; i++) {
			sb.append("?");
			if (i < graduateStudentNumbers.length - 1) {
				sb.append(",");
			}
		}

		String sql = "SELECT " +
				" g.graduate_student_number, " +
				" g.graduate_name, " +
				" g.graduate_email, " +
				" g.graduate_job_category, " +
				" c.course_code, " +
				" c.course_name, " +
				" c.course_term " +
				"FROM graduate g " +
				"JOIN course c ON g.course_code = c.course_code " +
				"WHERE g.graduate_student_number IN (" + sb + ")";

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			for (int i = 0; i < graduateStudentNumbers.length; i++) {
				ps.setString(i + 1, graduateStudentNumbers[i]);
			}

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {

					// Course
					Course course = new Course();
					course.setCourseCode(rs.getString("course_code"));
					course.setCourseName(rs.getString("course_name"));
					course.setCourseTerm(rs.getInt("course_term"));

					// Graduate
					Graduate g = new Graduate();
					g.setGraduateStudentNumber(
							rs.getString("graduate_student_number"));
					g.setGraduateName(
							rs.getString("graduate_name"));
					g.setGraduateEmail(
							rs.getString("graduate_email"));
					g.setGraduateJobCategory(
							rs.getString("graduate_job_category"));
					g.setCourse(course);

					list.add(g);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"卒業生情報の取得処理中にエラーが発生しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}

		return list;
	}

	public List<Graduate> findAll() throws Exception {

		List<Graduate> list = new ArrayList<>();

		try {
			Connection con = createConnection();

			try {
				String sql = "SELECT * FROM graduate";
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();

				try {
					while (rs.next()) {
						Graduate g = new Graduate();
						// g.setGraduateId(rs.getInt("graduate_id"));
						g.setGraduateName(rs.getString("graduate_name"));
						list.add(g);
					}
				} finally {
					rs.close();
					ps.close();
				}

			} finally {
				con.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"卒業生一覧の取得処理中にエラーが発生しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}

		return list;
	}

	public void insertGraduate(Graduate graduate) throws Exception {

		String sql = "INSERT INTO graduate "
				+ "(company_id, staff_id, course_code, graduate_student_number, "
				+ " graduate_name, graduate_email, graduate_other_info, graduate_job_category) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			Connection con = createConnection();

			try {
				PreparedStatement ps = con.prepareStatement(sql);

				try {
					ps.setInt(1, graduate.getCompany().getCompanyId());
					ps.setNull(2, java.sql.Types.INTEGER); // staff_id を NULL
					ps.setString(3, graduate.getCourse().getCourseCode());
					ps.setString(4, graduate.getGraduateStudentNumber());
					ps.setString(5, graduate.getGraduateName());
					ps.setString(6, graduate.getGraduateEmail());
					ps.setString(7, graduate.getOtherInfo());
					ps.setString(8, graduate.getGraduateJobCategory());

					ps.executeUpdate();

				} finally {
					ps.close();
				}

			} finally {
				con.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"卒業生情報の登録処理中にエラーが発生しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}

	public boolean updateGraduate(Graduate graduate) throws Exception {

		String sql = "UPDATE graduate SET "
				+ "company_id = ?, "
				+ "course_code = ?, "
				+ "graduate_name = ?, "
				+ "graduate_email = ?, "
				+ "graduate_other_info = ?, "
				+ "graduate_job_category = ? "
				+ "WHERE graduate_student_number = ?";

		try {
			Connection con = createConnection();

			try {
				PreparedStatement ps = con.prepareStatement(sql);

				try {
					ps.setInt(1, graduate.getCompany().getCompanyId());
					ps.setString(2, graduate.getCourse().getCourseCode());
					ps.setString(3, graduate.getGraduateName());
					ps.setString(4, graduate.getGraduateEmail());
					ps.setString(5, graduate.getOtherInfo());
					ps.setString(6, graduate.getGraduateJobCategory());
					ps.setString(7, graduate.getGraduateStudentNumber());

					int count = ps.executeUpdate();
					return count > 0;

				} finally {
					ps.close();
				}

			} finally {
				con.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"卒業生情報の更新処理中にエラーが発生しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}

	public void deleteGraduate(String graduateStudentNumber) throws Exception {

		System.out.println(
				"DELETE対象=[" + graduateStudentNumber + "] length=" + graduateStudentNumber.length());

		try {
			Connection con = createConnection();

			try {
				String sql = "DELETE FROM graduate WHERE graduate_student_number = ?";
				PreparedStatement ps = con.prepareStatement(sql);

				try {
					ps.setString(1, graduateStudentNumber);
					ps.executeUpdate();
				} finally {
					ps.close();
				}

			} finally {
				con.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"卒業生情報の削除処理中にエラーが発生しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}

	public void searchGraduate(int graduate) {

	}

	//学籍番号からGraduateテーブルの卒業生学籍番号があるか確認
	public boolean findGraduateStudentNumber(String graduateStudentNumber) throws Exception {

		Graduate g = searchGraduateByGraduateStudentNumber(graduateStudentNumber);
		return g != null;
	}

}

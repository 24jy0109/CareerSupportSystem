package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Course;

public class CourseDBAccess extends DBAccess {
	public List<Course> getAllCourses() throws Exception {

		List<Course> list = new ArrayList<>();

		String sql = "SELECT course_code, course_name FROM course ORDER BY course_code";

		try (Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Course c = new Course();
				c.setCourseCode(rs.getString("course_code"));
				c.setCourseName(rs.getString("course_name"));
				list.add(c);
			}

			return list;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、学科一覧の取得に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}
	
	public String findCourseNameById(String courseCode) throws Exception {
		String courseName = null;

		String sql = "SELECT course_name FROM course WHERE course_code = ?";

		try (Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, courseCode);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					courseName = rs.getString("course_name");
				}
			}

			return courseName;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、学科情報の取得に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}
}
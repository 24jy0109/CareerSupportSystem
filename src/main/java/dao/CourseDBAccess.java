package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Course;

public class CourseDBAccess extends DBAccess {
	public List<Course> getAllCourses() throws Exception {
		Connection con = createConnection();
		List<Course> list = new ArrayList<>();

		try {
			String sql = "SELECT course_code, course_name FROM course ORDER BY course_code";
			try (PreparedStatement ps = con.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					Course c = new Course();
					c.setCourseCode(rs.getString("course_code"));
					c.setCourseName(rs.getString("course_name"));
					list.add(c);
				}
			}
		} finally {
			if (con != null)
				con.close();
		}

		return list;
	}
	public String findCourseNameById(String courseCode) throws Exception {
		Connection con = createConnection();
		String courseName = null;

		try {
			String sql = "SELECT course_name FROM course WHERE course_code = ?";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, courseCode);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						courseName = rs.getString("course_name");
					}
				}
			}
		} finally {
			if (con != null) {
				con.close();
			}
		}

		return courseName;
	}
}
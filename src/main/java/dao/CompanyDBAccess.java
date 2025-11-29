package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Company;
import model.Event;
import model.Graduate;
import model.Student;

public class CompanyDBAccess extends DBAccess {
	public ArrayList<Company> searchCompanies(String name, String sort, String studentNumber) throws Exception {
		//データベース接続
		Connection con = createConnection();

		ArrayList<Company> list = new ArrayList<>();

		// Company 全件
		String sqlCompany = "SELECT company_id, company_name FROM company";
		Map<Integer, Company> map = new HashMap<>();

		try (PreparedStatement ps = con.prepareStatement(sqlCompany);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("company_id");

				Company c = new Company();
				c.setCompanyId(id);
				c.setCompanyName(rs.getString("company_name"));
				c.setEvents(new ArrayList<>());
				c.setGraduates(new ArrayList<>());
				c.setRequestStudents(new ArrayList<>());
				map.put(id, c);
			}
		}

		// イベント一覧を company に追加
		String sqlEvent = "SELECT event_id, company_id, event_progress FROM event";
		try (PreparedStatement ps = con.prepareStatement(sqlEvent);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int companyId = rs.getInt("company_id");
				Company c = map.get(companyId);
				if (c == null) {
					continue;
				}
				Event e = new Event();
				e.setEventId(rs.getInt("event_id"));
				e.setEventProgress(rs.getInt("event_progress"));
				c.getEvents().add(e);
			}
		}

		// 卒業生一覧を company に追加
		String sqlGraduate = "SELECT graduate_student_number, company_id FROM graduate";
		try (PreparedStatement ps = con.prepareStatement(sqlGraduate);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int companyId = rs.getInt("company_id");
				Company c = map.get(companyId);
				if (c == null) {
					continue;
				}

				Graduate g = new Graduate();
				g.setGraduateStudentNumber(rs.getString("graduate_student_number"));
				c.getGraduates().add(g);
			}
		}

		// request → student JOIN 一覧を company に追加
		String sqlRequest = "SELECT company_id " +
				"FROM request " +
				"WHERE student_number = ?";

		try (PreparedStatement ps = con.prepareStatement(sqlRequest)) {
		    ps.setString(1, studentNumber);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int companyId = rs.getInt("company_id");

					Company c = map.get(companyId);
					if (c == null) {
						continue;
					}
					
					Student s = new Student();
					s.setStudentNumber(studentNumber);
					c.getRequestStudents().add(s);
				}
			}
		}

		// Map → ArrayList に変換
		list.addAll(map.values());
		return list;
	}
}

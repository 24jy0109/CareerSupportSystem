
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.CompanyDTO;
import model.Company;
import model.Course;
import model.Event;
import model.Graduate;
import model.Staff;

public class CompanyDBAccess extends DBAccess {

	public void insertCompany(String companyName) throws Exception {
		String sql = "INSERT INTO company(company_name) VALUES(?)";

		Connection con = createConnection();

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, companyName);
			ps.executeUpdate(); // ← 登録実行
		} finally {
			if (con != null)
				con.close();
		}
	}
	public boolean existsCompanyName(String companyName) throws Exception {
	    String sql = "SELECT COUNT(*) FROM company WHERE company_name = ?";
	    Connection con = createConnection();

	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, companyName);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } finally {
	        if (con != null) con.close();
	    }
	    return false;
	}

	public List<CompanyDTO> searchStudentCompanies(String companyName, String studentNumber)
			throws Exception {
		Connection con = createConnection();
		List<CompanyDTO> list = new ArrayList<>();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT c.company_id, c.company_name, ");
			sql.append("CASE WHEN COUNT(DISTINCT e.event_id) > 0 THEN '開催' ELSE '' END AS eventProgress, ");
			sql.append("CASE WHEN COUNT(DISTINCT r.student_number) > 0 THEN '申請済み' ELSE '' END AS isRequest, ");
			sql.append("COUNT(DISTINCT g.graduate_student_number) AS graduateCount ");
			sql.append("FROM company c ");
			sql.append("LEFT JOIN event e ON c.company_id = e.company_id AND e.event_progress = 2 ");
			sql.append("LEFT JOIN request r ON c.company_id = r.company_id AND r.student_number = ? ");
			sql.append("LEFT JOIN graduate g ON c.company_id = g.company_id ");
			sql.append("WHERE 1=1 ");

			if (companyName != null && !companyName.isEmpty()) {
				sql.append("AND c.company_name LIKE ? ");
			}

			sql.append("GROUP BY c.company_id, c.company_name ");
			// 開催がある会社を上に、さらに会社名で昇順ソート
			sql.append("ORDER BY CASE WHEN COUNT(DISTINCT e.event_id) > 0 THEN 0 ELSE 1 END, c.company_name ASC ");

			try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
				ps.setString(1, studentNumber);
				if (companyName != null && !companyName.isEmpty()) {
					ps.setString(2, "%" + companyName + "%");
				}

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Company company = new Company();
						company.setCompanyId(rs.getInt("company_id"));
						company.setCompanyName(rs.getString("company_name"));
						String eventProgress = rs.getString("eventProgress"); // "開催" または ""
						String isRequest = rs.getString("isRequest"); // "申請済み" または ""
						int graduateCount = rs.getInt("graduateCount");

						CompanyDTO dto = new CompanyDTO();
						dto.setCompany(company);
						dto.setEventProgress(eventProgress);
						dto.setIsRequest(isRequest);
						dto.setGraduateCount(graduateCount);

						list.add(dto);
					}
				}
			}

		} finally {
			if (con != null)
				con.close();
		}

		return list;
	}

	public List<CompanyDTO> searchStaffCompanies(String companyName)
			throws Exception {
		Connection con = createConnection();
		List<CompanyDTO> list = new ArrayList<>();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT c.company_id, c.company_name, ");
			sql.append("CASE e.event_progress ");
			sql.append(" WHEN 1 THEN '企画中' ");
			sql.append(" WHEN 2 THEN '開催' ");
			sql.append(" END AS eventProgress, ");
			sql.append("COUNT(DISTINCT r.student_number) AS requestCount ");
			sql.append("FROM company c ");
			sql.append("LEFT JOIN event e ON c.company_id = e.company_id ");
			sql.append("LEFT JOIN request r ON c.company_id = r.company_id ");
			sql.append("WHERE c.company_name LIKE ? ");
			sql.append("GROUP BY c.company_id, c.company_name, e.event_progress ");
			// 開催がある会社を上に、さらに会社名で昇順ソート
			sql.append("ORDER BY CASE ");
			sql.append(" WHEN e.event_progress = 2 THEN 0 ");
			sql.append(" WHEN e.event_progress = 1 THEN 1 ");
			sql.append(" WHEN e.event_progress = 0 THEN 2 ");
			sql.append(" END ASC, ");
			sql.append(" c.company_name ASC ");

			try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
				ps.setString(1, "%" + companyName + "%");

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Company company = new Company();
						company.setCompanyId(rs.getInt("company_id"));
						company.setCompanyName(rs.getString("company_name"));
						String eventProgress = rs.getString("eventProgress"); // "開催" または ""
						int requestCount = rs.getInt("requestCount"); // "申請済み" または ""

						CompanyDTO dto = new CompanyDTO();
						dto.setCompany(company);
						dto.setEventProgress(eventProgress);
						dto.setRequestCount(requestCount);

						list.add(dto);
					}
				}
			}

		} finally {
			if (con != null)
				con.close();
		}

		return list;
	}

	public List<CompanyDTO> SearchCompanyDetail(int companyId, String studentNumber) throws Exception {
		Connection con = createConnection();
		List<CompanyDTO> list = new ArrayList<>();
		CompanyDTO companyDTO = new CompanyDTO();
		Company company = new Company();
		ArrayList<Event> events = new ArrayList<>();
		ArrayList<Graduate> graduates = new ArrayList<>();

		try {
			// 会社情報
			String sqlCompany = "SELECT company_id, company_name FROM company WHERE company_id = ?";
			try (PreparedStatement ps = con.prepareStatement(sqlCompany)) {
				ps.setInt(1, companyId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						company.setCompanyId(rs.getInt("company_id"));
						company.setCompanyName(rs.getString("company_name"));
					}
				}
			}

			// 開催予定イベント (progress = 2)
			String sqlEvent = "SELECT event_id, event_place, event_start_time, event_end_time, event_progress "
					+ "FROM event WHERE company_id = ? AND event_progress = 2";
			try (PreparedStatement ps = con.prepareStatement(sqlEvent)) {
				ps.setInt(1, companyId);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Event event = new Event();
						event.setEventId(rs.getInt("event_id"));
						event.setEventPlace(rs.getString("event_place"));
						event.setEventStartTime(rs.getTimestamp("event_start_time").toLocalDateTime());
						event.setEventEndTime(rs.getTimestamp("event_end_time").toLocalDateTime());
						event.setEventProgress(rs.getInt("event_progress"));
						events.add(event);
					}
				}
			}

			// 卒業生情報
			String sqlGraduate = "SELECT g.graduate_student_number, g.graduate_name, "
					+ "g.graduate_job_category, c.course_name, c.course_term "
					+ "FROM graduate g "
					+ "JOIN course c ON g.course_code = c.course_code "
					+ "WHERE g.company_id = ?";
			try (PreparedStatement ps = con.prepareStatement(sqlGraduate)) {
				ps.setInt(1, companyId);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Graduate graduate = new Graduate();
						Course course = new Course();
						graduate.setGraduateStudentNumber(rs.getString("graduate_student_number"));
						graduate.setGraduateName(rs.getString("graduate_name"));
						graduate.setGraduateJobCategory(rs.getString("graduate_job_category"));
						course.setCourseName(rs.getString("course_name"));
						course.setCourseTerm(rs.getInt("course_term"));
						graduate.setCourse(course);
						graduates.add(graduate);
					}
				}
			}

			// 特定学生の申請済みフラグ
			String sqlRequest = "SELECT COUNT(*) AS cnt FROM request WHERE company_id = ? AND student_number = ?";
			boolean isRequested = false;
			try (PreparedStatement ps = con.prepareStatement(sqlRequest)) {
				ps.setInt(1, companyId);
				ps.setString(2, studentNumber);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						isRequested = rs.getInt("cnt") > 0;
					}
				}
			}

			// DTOに詰める
			company.setEvents(events);
			company.setGraduates(graduates);
			companyDTO.setCompany(company);
			companyDTO.setIsRequest(isRequested ? "申請済み" : "");
			list.add(companyDTO);

		} finally {
			if (con != null)
				con.close();
		}

		return list;
	}

	public List<CompanyDTO> SearchCompanyWithGraduates(int companyId) throws Exception {
		Connection con = createConnection();
		List<CompanyDTO> list = new ArrayList<>();
		CompanyDTO companyDTO = new CompanyDTO();
		Company company = new Company();
		ArrayList<Graduate> graduates = new ArrayList<>();

		try {
			// ------------------------------
			// ① 会社情報
			// ------------------------------
			String sqlCompany = "SELECT company_id, company_name " +
					"FROM company WHERE company_id = ?";
			try (PreparedStatement ps = con.prepareStatement(sqlCompany)) {
				ps.setInt(1, companyId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						company.setCompanyId(rs.getInt("company_id"));
						company.setCompanyName(rs.getString("company_name"));
					}
				}
			}

			// ------------------------------
			// ② 卒業生 + コース + スタッフ JOIN
			// ------------------------------
			String sqlGraduate = "SELECT g.graduate_student_number, g.graduate_name, g.graduate_job_category, " +
					"       c.course_name, c.course_term, " +
					"       s.staff_id, s.staff_name " +
					"FROM graduate g " +
					"JOIN course c ON g.course_code = c.course_code " +
					"LEFT JOIN staff s ON g.staff_id = s.staff_id " +
					"WHERE g.company_id = ?";

			try (PreparedStatement ps = con.prepareStatement(sqlGraduate)) {
				ps.setInt(1, companyId);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Graduate graduate = new Graduate();
						Course course = new Course();

						graduate.setGraduateStudentNumber(rs.getString("graduate_student_number"));
						graduate.setGraduateName(rs.getString("graduate_name"));
						graduate.setGraduateJobCategory(rs.getString("graduate_job_category"));

						// コース
						course.setCourseName(rs.getString("course_name"));
						course.setCourseTerm(rs.getInt("course_term"));
						graduate.setCourse(course);

						// スタッフ（NULL の場合もあり）
						int staffId = rs.getInt("staff_id");
						if (!rs.wasNull()) {
							Staff staff = new Staff();
							staff.setStaffId(staffId);
							staff.setStaffName(rs.getString("staff_name"));
							graduate.setStaff(staff);
						} else {
							graduate.setStaff(null);
						}
						graduates.add(graduate);
					}
				}
			}

			// ------------------------------
			// ③ DTOへ詰める
			// ------------------------------
			company.setGraduates(graduates);
			companyDTO.setCompany(company);
			companyDTO.setIsRequest("");
			list.add(companyDTO);
		} finally {
			if (con != null)
				con.close();
		}
		return list;
	}

	public String searchCompanyNameById(String companyId) throws SQLException, Exception {
		String companyName = null;

		String sql = "SELECT company_name FROM company WHERE company_id = ?";

		try (Connection con = createConnection();
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, companyId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				companyName = rs.getString("company_name");
			}
		}

		return companyName;
	}
	public CompanyDTO SearchCompanById(int companyId) throws Exception {

		    String sql =
		        "SELECT company_id, company_name " +
		        "FROM company " +
		        "WHERE company_id = ?";

		    try (
		        Connection con = createConnection();
		        PreparedStatement ps = con.prepareStatement(sql)
		    ) {
		        ps.setInt(1, companyId);

		        try (ResultSet rs = ps.executeQuery()) {
		            if (rs.next()) {

		                Company company = new Company();
		                company.setCompanyId(rs.getInt("company_id"));
		                company.setCompanyName(rs.getString("company_name"));

		                CompanyDTO dto = new CompanyDTO();
		                dto.setCompany(company);

		                return dto;
		            }
		        }
		    }
		    // 見つからなかった場合
		    return null;
	}
}

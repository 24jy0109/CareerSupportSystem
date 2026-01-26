
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, companyName);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、企業情報の登録に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}

	public boolean existsCompanyName(String companyName) throws Exception {
		String sql = "SELECT COUNT(*) FROM company WHERE company_name = ?";

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, companyName);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、企業名の重複確認に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}
	}

	public List<CompanyDTO> searchStudentCompanies(String companyName, String studentNumber)
			throws Exception {

		List<CompanyDTO> list = new ArrayList<>();

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
		sql.append("ORDER BY CASE WHEN COUNT(DISTINCT e.event_id) > 0 THEN 0 ELSE 1 END, c.company_name ASC ");

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql.toString())) {

			ps.setString(1, studentNumber);
			if (companyName != null && !companyName.isEmpty()) {
				ps.setString(2, "%" + companyName + "%");
			}

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Company company = new Company();
					company.setCompanyId(rs.getInt("company_id"));
					company.setCompanyName(rs.getString("company_name"));

					CompanyDTO dto = new CompanyDTO();
					dto.setCompany(company);
					dto.setEventProgress(rs.getString("eventProgress"));
					dto.setIsRequest(rs.getString("isRequest"));
					dto.setGraduateCount(rs.getInt("graduateCount"));

					list.add(dto);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、企業一覧の取得に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}

		return list;
	}

	public List<CompanyDTO> searchStaffCompanies(String companyName)
			throws Exception {

		List<CompanyDTO> list = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("  c.company_id, ");
		sql.append("  c.company_name, ");

		sql.append("  CASE ");
		sql.append("    WHEN MAX(CASE WHEN e.event_progress = 2 THEN 1 ELSE 0 END) = 1 ");
		sql.append("      THEN '開催' ");
		sql.append("    WHEN MAX(CASE WHEN g.staff_id IS NOT NULL THEN 1 ELSE 0 END) = 1 ");
		sql.append("      THEN '企画中' ");
		sql.append("    ELSE '' ");
		sql.append("  END AS eventProgress, ");

		sql.append("  COUNT(DISTINCT r.student_number) AS requestCount, ");

		sql.append("  CASE ");
		sql.append("    WHEN MAX(CASE WHEN e.event_progress = 2 THEN 1 ELSE 0 END) = 1 THEN 0 ");
		sql.append("    WHEN MAX(CASE WHEN g.staff_id IS NOT NULL THEN 1 ELSE 0 END) = 1 THEN 1 ");
		sql.append("    ELSE 2 ");
		sql.append("  END AS sortOrder ");

		sql.append("FROM company c ");
		sql.append("LEFT JOIN event e ON c.company_id = e.company_id ");
		sql.append("LEFT JOIN graduate g ON c.company_id = g.company_id ");
		sql.append("LEFT JOIN request r ON c.company_id = r.company_id ");

		sql.append("WHERE c.company_name LIKE ? ");

		sql.append("GROUP BY c.company_id, c.company_name ");

		sql.append("ORDER BY ");
		sql.append("  sortOrder ASC, "); // 開催 → 企画中 → なし
		sql.append("  requestCount DESC, "); // 申請者数が多い順
		sql.append("  c.company_name ASC "); // 企業名順

		try (
				Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql.toString())) {

			ps.setString(1, "%" + companyName + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {

					Company company = new Company();
					company.setCompanyId(rs.getInt("company_id"));
					company.setCompanyName(rs.getString("company_name"));

					CompanyDTO dto = new CompanyDTO();
					dto.setCompany(company);
					dto.setEventProgress(rs.getString("eventProgress"));
					dto.setRequestCount(rs.getInt("requestCount"));

					list.add(dto);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"データベースの処理中にエラーが発生し、企業一覧の取得に失敗しました。<br>"
							+ "お手数ですが、管理者までお問い合わせください。",
					e);
		}

		return list;
	}

	public List<CompanyDTO> SearchCompanyDetail(int companyId, String studentNumber) throws Exception {
		List<CompanyDTO> list = new ArrayList<>();
		CompanyDTO companyDTO = new CompanyDTO();
		Company company = new Company();
		ArrayList<Event> events = new ArrayList<>();
		ArrayList<Graduate> graduates = new ArrayList<>();

		String sqlCompany = "SELECT company_id, company_name FROM company WHERE company_id = ?";

		String sqlEvent = "SELECT event_id, event_place, event_start_time, event_end_time, event_progress "
				+ "FROM event WHERE company_id = ? AND event_progress = 2";

		String sqlGraduate = "SELECT g.graduate_student_number, g.graduate_name, "
				+ "g.graduate_job_category, c.course_name, c.course_term "
				+ "FROM graduate g "
				+ "JOIN course c ON g.course_code = c.course_code "
				+ "WHERE g.company_id = ?";

		String sqlRequest = "SELECT COUNT(*) AS cnt FROM request WHERE company_id = ? AND student_number = ?";

		try (Connection con = createConnection()) {

			// ① 会社情報
			try (PreparedStatement ps = con.prepareStatement(sqlCompany)) {
				ps.setInt(1, companyId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						company.setCompanyId(rs.getInt("company_id"));
						company.setCompanyName(rs.getString("company_name"));
					}
				}
			}

			// ② 開催予定イベント
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

			// ③ 卒業生情報
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

			// ④ 特定学生の申請済み判定
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

			// ⑤ DTO 詰め
			company.setEvents(events);
			company.setGraduates(graduates);
			companyDTO.setCompany(company);
			companyDTO.setIsRequest(isRequested ? "申請済み" : "");
			list.add(companyDTO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("企業詳細情報の取得中にエラーが発生しました。", e);
		}

		return list;
	}

	public List<CompanyDTO> SearchCompanyWithGraduates(int companyId) throws Exception {
		List<CompanyDTO> list = new ArrayList<>();
		CompanyDTO companyDTO = new CompanyDTO();
		Company company = new Company();
		ArrayList<Graduate> graduates = new ArrayList<>();

		String sqlCompany = "SELECT company_id, company_name FROM company WHERE company_id = ?";

		String sqlGraduate = "SELECT g.graduate_student_number, g.graduate_name, g.graduate_email, g.graduate_job_category, "
				+ "c.course_name, c.course_term, "
				+ "s.staff_id, s.staff_name "
				+ "FROM graduate g "
				+ "JOIN course c ON g.course_code = c.course_code "
				+ "LEFT JOIN staff s ON g.staff_id = s.staff_id "
				+ "WHERE g.company_id = ?";

		try (Connection con = createConnection()) {

			// ① 会社情報
			try (PreparedStatement ps = con.prepareStatement(sqlCompany)) {
				ps.setInt(1, companyId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						company.setCompanyId(rs.getInt("company_id"));
						company.setCompanyName(rs.getString("company_name"));
					}
				}
			}

			// ② 卒業生 + コース + スタッフ
			try (PreparedStatement ps = con.prepareStatement(sqlGraduate)) {
				ps.setInt(1, companyId);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Graduate graduate = new Graduate();
						Course course = new Course();

						graduate.setGraduateStudentNumber(rs.getString("graduate_student_number"));
						graduate.setGraduateName(rs.getString("graduate_name"));
						graduate.setGraduateEmail(rs.getString("graduate_email"));
						graduate.setGraduateJobCategory(rs.getString("graduate_job_category"));

						course.setCourseName(rs.getString("course_name"));
						course.setCourseTerm(rs.getInt("course_term"));
						graduate.setCourse(course);

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

			// ③ DTO 詰め
			company.setGraduates(graduates);
			companyDTO.setCompany(company);
			companyDTO.setIsRequest("");
			list.add(companyDTO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("企業および卒業生情報の取得中にエラーが発生しました。", e);
		}

		return list;
	}

	public List<CompanyDTO> findSimilarCompany(String name) throws Exception {
		List<CompanyDTO> list = new ArrayList<>();
		String sql = "SELECT company_id, company_name FROM company WHERE company_name LIKE ?";

		try (Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, "%" + name + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Company company = new Company();
					company.setCompanyId(rs.getInt("company_id"));
					company.setCompanyName(rs.getString("company_name"));

					CompanyDTO dto = new CompanyDTO();
					dto.setCompany(company);
					list.add(dto);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("類似会社名検索中にエラーが発生しました。", e);
		}

		return list;
	}

	public boolean existsNormalizedCompanyName(String normalizedName) throws Exception {
		String sql = "SELECT COUNT(*) FROM company WHERE REPLACE(company_name,'株式会社','') = ?";

		try (Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, normalizedName);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("企業名存在確認中にエラーが発生しました。", e);
		}

		return false;
	}

	public String searchCompanyNameById(String companyId) throws Exception {
		String companyName = null;
		String sql = "SELECT company_name FROM company WHERE company_id = ?";

		try (Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, companyId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					companyName = rs.getString("company_name");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("企業名取得中にエラーが発生しました。", e);
		}

		return companyName;
	}

	public CompanyDTO SearchCompanById(int companyId) throws Exception {
		String sql = "SELECT company_id, company_name FROM company WHERE company_id = ?";

		try (Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

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

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("企業情報取得中にエラーが発生しました。", e);
		}

		// 見つからなかった場合
		return null;
	}

	public void updateCompany(int companyId, String companyName) throws Exception {
		String sql = "UPDATE company SET company_name = ? WHERE company_id = ?";

		try (Connection con = createConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, companyName);
			ps.setInt(2, companyId);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("企業情報更新中にエラーが発生しました。", e);
		}
	}
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.CompanyDTO;

public class CompanyDBAccess extends DBAccess {
	public List<CompanyDTO> searchStudentCompanies(String name, String sort, String studentNumber)
			throws Exception {
		Connection con = createConnection();
		List<CompanyDTO> list = new ArrayList<>();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT c.company_id, c.company_name, ");
			sql.append("CASE WHEN COUNT(DISTINCT e.event_id) > 0 THEN '開催' ELSE '' END AS eventProgress, ");
			sql.append("CASE WHEN COUNT(DISTINCT r.student_number) > 0 THEN '申請済み' ELSE '' END AS isRequest ");
			sql.append("FROM company c ");
			sql.append("LEFT JOIN event e ON c.company_id = e.company_id AND e.event_progress = 2 ");
			sql.append("LEFT JOIN request r ON c.company_id = r.company_id AND r.student_number = ? ");
			sql.append("WHERE 1=1 ");

			if (name != null && !name.isEmpty()) {
				sql.append("AND c.company_name LIKE ? ");
			}

			sql.append("GROUP BY c.company_id, c.company_name ");

			// ソート
			if ("asc".equalsIgnoreCase(sort)) {
				sql.append("ORDER BY c.company_name ASC");
			} else if ("desc".equalsIgnoreCase(sort)) {
				sql.append("ORDER BY c.company_name DESC");
			}

			try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
				ps.setString(1, studentNumber);
				if (name != null && !name.isEmpty()) {
					ps.setString(2, "%" + name + "%");
				}

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						int companyId = rs.getInt("company_id");
						String companyName = rs.getString("company_name");
						String eventProgress = rs.getString("eventProgress"); // "開催" または ""
						String isRequest = rs.getString("isRequest"); // "申請済み" または ""

						CompanyDTO dto = new CompanyDTO();
						dto.setCompanyId(companyId);
						dto.setCompanyName(companyName);
						dto.setEventProgress(eventProgress);
						dto.setIsRequest(isRequest);

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

}

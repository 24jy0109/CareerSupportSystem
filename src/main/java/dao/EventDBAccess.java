//package dao;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//import dto.CompanyDTO;
//import model.Company;
//
//public class EventDBAccess extends DBAccess {
//	public List<Company> searchCompanyWithGraduates(int companyId)
//			throws Exception {
//		Connection con = createConnection();
//		List<Company> list = new ArrayList<>();
//
//		try {
//			StringBuilder sql = new StringBuilder();
//			sql.append("SELECT c.company_id, c.company_name, ");
//			sql.append("CASE WHEN COUNT(DISTINCT e.event_id) > 0 THEN '開催' ELSE '' END AS eventProgress, ");
//			sql.append("CASE WHEN COUNT(DISTINCT r.student_number) > 0 THEN '申請済み' ELSE '' END AS isRequest, ");
//			sql.append("COUNT(DISTINCT g.graduate_student_number) AS graduateCount "); 
//			sql.append("FROM company c ");
//			sql.append("LEFT JOIN event e ON c.company_id = e.company_id AND e.event_progress = 2 ");
//			sql.append("LEFT JOIN request r ON c.company_id = r.company_id AND r.student_number = ? ");
//			sql.append("LEFT JOIN graduate g ON c.company_id = g.company_id "); 
//			sql.append("WHERE 1=1 ");
//
//			if (companyName != null && !companyName.isEmpty()) {
//				sql.append("AND c.company_name LIKE ? ");
//			}
//
//			sql.append("GROUP BY c.company_id, c.company_name ");
//			// 開催がある会社を上に、さらに会社名で昇順ソート
//			sql.append("ORDER BY CASE WHEN COUNT(DISTINCT e.event_id) > 0 THEN 0 ELSE 1 END, c.company_name ASC ");
//
//
//			try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
//				ps.setString(1, studentNumber);
//				if (companyName != null && !companyName.isEmpty()) {
//					ps.setString(2, "%" + companyName + "%");
//				}
//
//				try (ResultSet rs = ps.executeQuery()) {
//					while (rs.next()) {
//						Company company = new Company();
//						company.setCompanyId(rs.getInt("company_id"));
//						company.setCompanyName(rs.getString("company_name"));
//						String eventProgress = rs.getString("eventProgress"); // "開催" または ""
//						String isRequest = rs.getString("isRequest"); // "申請済み" または ""
//						int graduateCount = rs.getInt("graduateCount");
//
//						CompanyDTO dto = new CompanyDTO();
//						dto.setCompany(company);
//						dto.setEventProgress(eventProgress);
//						dto.setIsRequest(isRequest);
//						dto.setGraduateCount(graduateCount);
//
//						list.add(dto);
//					}
//				}
//			}
//
//		} finally {
//			if (con != null)
//				con.close();
//		}
//
//		return list;
//	}
//}

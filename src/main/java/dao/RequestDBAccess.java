package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RequestDBAccess extends DBAccess {

    public void insertRequest(int companyId, String studentNumber) throws Exception {
        Connection con = createConnection();
        try {
            String sql = "INSERT INTO request (company_id, student_number) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, companyId);
                ps.setString(2, studentNumber);
                ps.executeUpdate();
            }
        } finally {
            if (con != null) con.close();
        }
    }

    public void cancelRequest(int companyId, String studentNumber) throws Exception {
        Connection con = createConnection();
        try {
            String sql = "DELETE FROM request WHERE company_id = ? AND student_number = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, companyId);
                ps.setString(2, studentNumber);
                ps.executeUpdate();
            }
        } finally {
            if (con != null) con.close();
        }
    }
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import model.Event;
import model.Graduate;

public class EventDBAccess extends DBAccess {

    public void insertEvent(Event event) throws Exception {
    	Connection con = createConnection();
    	con.setAutoCommit(false);  // ← これが必要

        try {
            // ------------------------------
            // 1. eventテーブルに登録
            // ------------------------------
            String sqlEvent = "INSERT INTO event "
                    + "(staff_id, company_id, event_place, event_start_time, event_end_time, event_capacity, event_progress, event_other_info) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int eventId = 0;

            try (PreparedStatement ps = con.prepareStatement(sqlEvent, PreparedStatement.RETURN_GENERATED_KEYS)) {
            	ps.setInt(1, event.getStaff().getStaffId());
                ps.setInt(2, event.getCompany().getCompanyId());
                ps.setString(3, event.getEventPlace());
                ps.setTimestamp(4, Timestamp.valueOf(event.getEventStartTime()));
                ps.setTimestamp(5, Timestamp.valueOf(event.getEventEndTime()));
                ps.setInt(6, event.getEventCapacity());
                ps.setInt(7, event.getEventProgress());
                ps.setString(8, event.getEventOtherInfo());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        eventId = rs.getInt(1);
                    }
                }
            }

            // ------------------------------
            // 2. join_graduate に参加卒業生登録
            // ------------------------------
            if (event.getJoinGraduates() != null && !event.getJoinGraduates().isEmpty()) {
                String sqlJoin = "INSERT INTO join_graduate (event_id, graduate_student_number) VALUES (?, ?)";
                try (PreparedStatement psJoin = con.prepareStatement(sqlJoin)) {
                    for (Graduate g : event.getJoinGraduates()) {
                        psJoin.setInt(1, eventId);
                        psJoin.setString(2, g.getGraduateStudentNumber());
                        psJoin.addBatch();
                    }
                    psJoin.executeBatch();
                }
            }

            con.commit();

        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            if (con != null) con.close();
        }
    }
}

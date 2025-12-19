package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Company;
import model.Course;
import model.Event;
import model.JoinStudent;
import model.Student;

public class JoinStudentDBAccess extends DBAccess {

    public List<JoinStudent> searchJoinStudentsByEventId(int eventId) throws Exception {

        List<JoinStudent> list = new ArrayList<>();

        Connection con = createConnection();

        String sql =
            "SELECT " +
            "  c.company_id, c.company_name, " +
            "  s.student_number, s.student_name, " +
            "  co.course_name " +
            "FROM join_student js " +
            "JOIN event e ON js.event_id = e.event_id " +
            "JOIN company c ON e.company_id = c.company_id " +
            "JOIN student s ON js.student_number = s.student_number " +
            "JOIN course co ON s.course_code = co.course_code " +
            "WHERE js.event_id = ? " +
            "AND js.join_availability = 1";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    // ----- Company -----
                    Company company = new Company();
                    company.setCompanyId(rs.getInt("company_id"));
                    company.setCompanyName(rs.getString("company_name"));

                    // ----- Event -----
                    Event event = new Event();
                    event.setEventId(eventId);
                    event.setCompany(company);

                    // ----- Course -----
                    Course course = new Course();
                    course.setCourseName(rs.getString("course_name"));

                    // ----- Student -----
                    Student student = new Student();
                    student.setStudentNumber(rs.getString("student_number"));
                    student.setStudentName(rs.getString("student_name"));
                    student.setCourse(course);

                    // ----- JoinStudent -----
                    JoinStudent joinStudent = new JoinStudent();
                    joinStudent.setEvent(event);
                    joinStudent.setStudent(student);

                    list.add(joinStudent);
                }
            }
        } finally {
            if (con != null) con.close();
        }

        return list;
    }
}

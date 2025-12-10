package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import model.Answer;

public class AnswerDBAccess extends DBAccess {
    // ===================================================================================
    // INSERT
    // ===================================================================================
    public Answer insertAnswer(Answer answer) throws Exception {

        String sql = "INSERT INTO answer ("
                + "event_id, graduate_student_number, event_availability, "
                + "first_choice_start_time, first_choice_end_time, "
                + "second_choice_start_time, second_choice_end_time, "
                + "third_choice_start_time, third_choice_end_time"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection con = createConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            setNullable(ps, 1, answer.getEvent().getEventId(), java.sql.Types.INTEGER);
            setNullable(ps, 2, answer.getGraduate().getGraduateStudentNumber(), java.sql.Types.CHAR);            
            
            setNullable(ps, 3, answer.getEventAvailability(), java.sql.Types.BOOLEAN);

            setNullable(ps, 4,  answer.getFirstChoiceStartTime()  == null ? null : Timestamp.valueOf(answer.getFirstChoiceStartTime()), java.sql.Types.TIMESTAMP);
            setNullable(ps, 5,  answer.getFirstChoiceEndTime()    == null ? null : Timestamp.valueOf(answer.getFirstChoiceEndTime()),   java.sql.Types.TIMESTAMP);

            setNullable(ps, 6,  answer.getSecondChoiceStartTime() == null ? null : Timestamp.valueOf(answer.getSecondChoiceStartTime()), java.sql.Types.TIMESTAMP);
            setNullable(ps, 7,  answer.getSecondChoiceEndTime()   == null ? null : Timestamp.valueOf(answer.getSecondChoiceEndTime()),   java.sql.Types.TIMESTAMP);

            setNullable(ps, 8,  answer.getThirdChoiceStartTime()  == null ? null : Timestamp.valueOf(answer.getThirdChoiceStartTime()), java.sql.Types.TIMESTAMP);
            setNullable(ps, 9,  answer.getThirdChoiceEndTime()    == null ? null : Timestamp.valueOf(answer.getThirdChoiceEndTime()),   java.sql.Types.TIMESTAMP);

            ps.executeUpdate();

            // AUTO_INCREMENT の取得
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    answer.setAnswerId(rs.getInt(1));
                }
            }
        }

        return answer;
    }

    // ===================================================================================
    // UPDATE
    // ===================================================================================
    public void updateAnswer(Answer answer) {

        String sql = "UPDATE answer SET "
                + "event_availability = ?, "
                + "first_choice_start_time = ?, first_choice_end_time = ?, "
                + "second_choice_start_time = ?, second_choice_end_time = ?, "
                + "third_choice_start_time = ?, third_choice_end_time = ? "
                + "WHERE answer_id = ?";

        try (
            Connection con = createConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            setNullable(ps, 1, answer.getEventAvailability(), java.sql.Types.BOOLEAN);

            setNullable(ps, 2, answer.getFirstChoiceStartTime()  == null ? null : Timestamp.valueOf(answer.getFirstChoiceStartTime()), java.sql.Types.TIMESTAMP);
            setNullable(ps, 3, answer.getFirstChoiceEndTime()    == null ? null : Timestamp.valueOf(answer.getFirstChoiceEndTime()),   java.sql.Types.TIMESTAMP);

            setNullable(ps, 4, answer.getSecondChoiceStartTime() == null ? null : Timestamp.valueOf(answer.getSecondChoiceStartTime()), java.sql.Types.TIMESTAMP);
            setNullable(ps, 5, answer.getSecondChoiceEndTime()   == null ? null : Timestamp.valueOf(answer.getSecondChoiceEndTime()),   java.sql.Types.TIMESTAMP);

            setNullable(ps, 6, answer.getThirdChoiceStartTime()  == null ? null : Timestamp.valueOf(answer.getThirdChoiceStartTime()), java.sql.Types.TIMESTAMP);
            setNullable(ps, 7, answer.getThirdChoiceEndTime()    == null ? null : Timestamp.valueOf(answer.getThirdChoiceEndTime()),   java.sql.Types.TIMESTAMP);

            ps.setInt(8, answer.getAnswerId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

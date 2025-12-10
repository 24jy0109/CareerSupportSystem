package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import model.Answer;

public class AnswerDBAccess extends DBAccess {

    // ===== 共通：NULL 対応 =====
    private void setNullable(PreparedStatement ps, int idx, Object value, int sqlType) throws Exception {
        if (value == null) {
            ps.setNull(idx, sqlType);
            return;
        }
        if (value instanceof Boolean) {
            ps.setBoolean(idx, (Boolean) value);
        } else if (value instanceof String) {
            ps.setString(idx, (String) value);
        } else if (value instanceof Timestamp) {
            ps.setTimestamp(idx, (Timestamp) value);
        } else {
            ps.setObject(idx, value);
        }
    }

    // ===================================================================================
    // INSERT
    // ===================================================================================
    public Answer insertAnswer(Answer answer) throws Exception {

        String sql = "INSERT INTO answer ("
                + "graduate_student_number, event_availability, "
                + "first_choice_start_time, first_choice_end_time, "
                + "second_choice_start_time, second_choice_end_time, "
                + "third_choice_start_time, third_choice_end_time"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection con = createConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            setNullable(ps, 1, answer.getGraduateStudentNumber(), java.sql.Types.CHAR);
            setNullable(ps, 2, answer.getEventAvailability(), java.sql.Types.BOOLEAN);

            setNullable(ps, 3,  answer.getFirstChoiceStartTime()  == null ? null : Timestamp.valueOf(answer.getFirstChoiceStartTime()), java.sql.Types.TIMESTAMP);
            setNullable(ps, 4,  answer.getFirstChoiceEndTime()    == null ? null : Timestamp.valueOf(answer.getFirstChoiceEndTime()),   java.sql.Types.TIMESTAMP);

            setNullable(ps, 5,  answer.getSecondChoiceStartTime() == null ? null : Timestamp.valueOf(answer.getSecondChoiceStartTime()), java.sql.Types.TIMESTAMP);
            setNullable(ps, 6,  answer.getSecondChoiceEndTime()   == null ? null : Timestamp.valueOf(answer.getSecondChoiceEndTime()),   java.sql.Types.TIMESTAMP);

            setNullable(ps, 7,  answer.getThirdChoiceStartTime()  == null ? null : Timestamp.valueOf(answer.getThirdChoiceStartTime()), java.sql.Types.TIMESTAMP);
            setNullable(ps, 8,  answer.getThirdChoiceEndTime()    == null ? null : Timestamp.valueOf(answer.getThirdChoiceEndTime()),   java.sql.Types.TIMESTAMP);

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

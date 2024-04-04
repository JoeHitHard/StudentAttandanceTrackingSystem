package aad.project.student.AttendanceTrackingSystem.storage.entity;

import aad.project.student.AttendanceTrackingSystem.storage.base.AbstractCassandraDataAccessor;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;

import java.util.List;
import java.util.StringJoiner;

public class Attendance {

    public static final String ATTENDANCE_TABLE = "attendance";

    static {
        AttendanceDAO attendanceDAO = new AttendanceDAO();
        attendanceDAO.createTable();
    }

    private final transient AttendanceDAO attendanceDAO;
    private String attendanceId;
    private String date;
    private List<String> studentsPresent;
    private String teacherId;

    public Attendance() {
        this.attendanceDAO = new AttendanceDAO();
    }

    public Attendance(String attendanceId) {
        this();
        this.attendanceId = attendanceId;
        attendanceDAO.mapToEntity(attendanceId, this);
    }

    public Attendance(String attendanceId, String date, List<String> studentsPresent, String teacherId) {
        this();
        this.attendanceId = attendanceId;
        this.date = date;
        this.studentsPresent = studentsPresent;
        this.teacherId = teacherId;
    }

    public Attendance save() {
        attendanceDAO.insert(attendanceId, attendanceId, date, studentsPresent, teacherId);
        return this;
    }

    public Attendance delete() {
        attendanceDAO.delete(attendanceId);
        return this;
    }

    public Attendance update() {
        attendanceDAO.update(attendanceId, date, studentsPresent, teacherId);
        return this;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getStudentsPresent() {
        return studentsPresent;
    }

    public void setStudentsPresent(List<String> studentsPresent) {
        this.studentsPresent = studentsPresent;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Attendance.class.getSimpleName() + "[", "]")
                .add("attendanceId='" + attendanceId + "'")
                .add("date='" + date + "'")
                .add("studentsPresent=" + studentsPresent)
                .add("teacherId='" + teacherId + "'")
                .toString();
    }

    public static class AttendanceDAO extends AbstractCassandraDataAccessor<Attendance> {

        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " +
                ATTENDANCE_TABLE + " (attendanceId TEXT PRIMARY KEY, date TEXT, studentsPresent LIST<TEXT>, teacherId TEXT)");
        public static PreparedStatement INSERT_STMT;
        public static PreparedStatement UPDATE_STMT;
        public static PreparedStatement DELETE_STMT;
        public static PreparedStatement SELECT_STMT;

        @Override
        public PreparedStatement getCreateStatement() {
            return CREATE_STMT;
        }

        @Override
        public PreparedStatement getInsertStatement() {
            if (INSERT_STMT == null) {
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + ATTENDANCE_TABLE
                        + " (attendanceId, date, studentsPresent, teacherId) VALUES (?, ?, ?, ?)");
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStatement() {
            if (UPDATE_STMT == null) {
                UPDATE_STMT = getCqlSession().prepare("UPDATE " + ATTENDANCE_TABLE
                        + " SET date = ?, studentsPresent = ?, teacherId = ? WHERE attendanceId = ?");
            }
            return UPDATE_STMT;
        }

        @Override
        public PreparedStatement getDeleteStatement() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + ATTENDANCE_TABLE + " WHERE attendanceId = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStatement() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + ATTENDANCE_TABLE + " WHERE attendanceId = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public Attendance mapToEntity(String key, Attendance attendance) {
            Row row = get(key).one();
            if (row != null) {
                return getAttendance(attendance, row);
            }
            return null;
        }
    }

    public static Attendance getAttendance(Attendance attendance, Row row) {
        if (attendance == null) {
            attendance = new Attendance();
        }
        attendance.attendanceId = row.getString("attendanceId");
        attendance.date = row.getString("date");
        attendance.studentsPresent = row.getList("studentsPresent", String.class);
        attendance.teacherId = row.getString("teacherId");
        return attendance;
    }
}

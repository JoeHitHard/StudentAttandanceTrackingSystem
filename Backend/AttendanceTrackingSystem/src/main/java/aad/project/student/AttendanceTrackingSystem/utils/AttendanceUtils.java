package aad.project.student.AttendanceTrackingSystem.utils;

import aad.project.student.AttendanceTrackingSystem.storage.entity.Attendance;
import aad.project.student.AttendanceTrackingSystem.storage.entity.Teacher;
import aad.project.student.AttendanceTrackingSystem.storage.service.CassandraDataAccessService;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

public class AttendanceUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Attendance attendance) throws JsonProcessingException {
        return objectMapper.writeValueAsString(attendance);
    }

    public static Attendance fromJson(String json) throws IOException {
        return objectMapper.readValue(json, Attendance.class);
    }

    public static String listToJson(List<Attendance> attendanceList) throws JsonProcessingException {
        return objectMapper.writeValueAsString(attendanceList);
    }

    public static List<Attendance> listFromJson(String json) throws IOException {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Attendance.class));
    }

    public static JSONArray getAllAttendanceOfTeacher(Teacher teacher) {
        ResultSet execute = CassandraDataAccessService.getCqlSession().execute("select * from attendance where teacherid='" + teacher.getId() + "'");
        JSONArray jsonArray = new JSONArray();
        execute.iterator().forEachRemaining(row -> jsonArray.put(Attendance.getAttendance(null, row)));
        return jsonArray;
    }

    public static String saveAttendance(String attendanceJson) throws IOException {
        Attendance attendance = fromJson(attendanceJson);
        attendance.save();
        return attendanceJson;
    }

    public static String updateAttendance(String attendanceJson) throws IOException {
        Attendance attendance = fromJson(attendanceJson);
        attendance.update();
        return attendanceJson;
    }
}

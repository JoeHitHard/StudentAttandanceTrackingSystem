package aad.project.student.AttendanceTrackingSystem.utils;

import aad.project.student.AttendanceTrackingSystem.storage.entity.Teacher;
import org.json.JSONObject;

public class TeacherUtils {

    public static Teacher getUser(JSONObject jsonObject) {
        return new Teacher(jsonObject.getString("username"));
    }

    public static JSONObject toJson(Teacher teacher) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", teacher.getId());
        jsonObject.put("name", teacher.getName());
        jsonObject.put("password", teacher.getPassword());
        return jsonObject;
    }
}

package aad.project.student.AttendanceTrackingSystem.controller;

import aad.project.student.AttendanceTrackingSystem.exception.InvalidAuthRequest;
import aad.project.student.AttendanceTrackingSystem.storage.entity.Attendance;
import aad.project.student.AttendanceTrackingSystem.storage.entity.Teacher;
import aad.project.student.AttendanceTrackingSystem.utils.AttendanceUtils;
import aad.project.student.AttendanceTrackingSystem.utils.AuthenticationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:3000")
public class AttendanceController {
    @GetMapping("/teacher")
    @ResponseBody
    public String getAttendance(@RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        Teacher teacher = AuthenticationUtils.getTeacher(authorizationHeader);
        JSONArray jsonArray = AttendanceUtils.getAllAttendanceOfTeacher(teacher);
        return jsonArray.toString();
    }

    @PostMapping("/create")
    public String createAttendance(@RequestBody String attendanceJson,
                                   @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws IOException, InvalidAuthRequest {
        Teacher teacher = AuthenticationUtils.getTeacher(authorizationHeader);
        return AttendanceUtils.saveAttendance(attendanceJson);
    }

    @GetMapping("/{attendanceId}")
    public String getAttendanceById(@PathVariable String attendanceId,
                                        @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws JsonProcessingException, InvalidAuthRequest {
        Teacher teacher = AuthenticationUtils.getTeacher(authorizationHeader);
        return AttendanceUtils.toJson(new Attendance(attendanceId));
    }

    @PutMapping("/{attendanceId}")
    public String updateAttendance(@PathVariable String attendanceId, @RequestBody String attendanceJson,
                                       @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws IOException, InvalidAuthRequest {
        Teacher teacher = AuthenticationUtils.getTeacher(authorizationHeader);
        return AttendanceUtils.updateAttendance(attendanceJson);
    }

    @DeleteMapping("/{attendanceId}")
    public void deleteAttendance(@PathVariable String attendanceId,
                                 @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws JsonProcessingException, InvalidAuthRequest {
        Teacher teacher = AuthenticationUtils.getTeacher(authorizationHeader);
        Attendance attendance = new Attendance(attendanceId);
        if (Objects.equals(teacher.getId(), attendance.getTeacherId())) {
            attendance.delete();
        }
        throw new InvalidAuthRequest("Invalid delete request, Teacher is not the owner of given attendanceId");
    }
}

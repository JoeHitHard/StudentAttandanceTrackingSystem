package aad.project.student.AttendanceTrackingSystem.controller;

import aad.project.student.AttendanceTrackingSystem.exception.InvalidAuthRequest;
import aad.project.student.AttendanceTrackingSystem.storage.entity.Teacher;
import aad.project.student.AttendanceTrackingSystem.utils.AuthenticationUtils;
import aad.project.student.AttendanceTrackingSystem.utils.TeacherUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @GetMapping("/get/{userID}")
    @ResponseBody
    public String getUser(@PathVariable("userID") String userID,
                          @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
         Teacher teacher = AuthenticationUtils.getTeacher(authorizationHeader);
        return TeacherUtils.toJson(teacher).toString();
    }

    @PostMapping("/public/login")
    @ResponseBody
    public String logIn(@RequestBody String userDataString, @RequestParam(value = "role", defaultValue = "User") String role) throws Exception {
        try {
            return AuthenticationUtils.login(new JSONObject(userDataString), role);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}", e.getCause(), e);
            throw e;
        }
    }


    @PostMapping("/public/signsignUpUp")
    @ResponseBody
    public String signUp(@RequestBody String userDataString) throws Exception {
        Teacher teacher = TeacherUtils.getUser(new JSONObject(userDataString));
        teacher.save();
        return TeacherUtils.toJson(teacher).toString();
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidAuthRequest.class)
    public ResponseEntity<ErrorResponse> handleAuthException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}

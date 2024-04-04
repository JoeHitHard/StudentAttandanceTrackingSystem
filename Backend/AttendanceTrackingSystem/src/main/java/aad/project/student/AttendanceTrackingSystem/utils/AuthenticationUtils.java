package aad.project.student.AttendanceTrackingSystem.utils;

import aad.project.student.AttendanceTrackingSystem.exception.InvalidAuthRequest;
import aad.project.student.AttendanceTrackingSystem.storage.entity.Teacher;
import aad.project.student.AttendanceTrackingSystem.storage.entity.Token;
import org.json.JSONObject;

import java.util.UUID;

public class AuthenticationUtils {

    public static String login(JSONObject userLoginRequest, String role) throws InvalidAuthRequest {
        Teacher teacher = new Teacher(userLoginRequest.getString("username"));
        if (teacher.validate(userLoginRequest.getString("password"))) {
            Token token = new Token(teacher.getId(), generateToken(teacher));
            token.save();
            return token.getJwtToken();
        }
        throw new InvalidAuthRequest("Invalid username or password");
    }

    private static String generateToken(Teacher teacher) {
        return UUID.randomUUID().toString();
    }

    public static boolean isValidToken(String authorizationHeader) throws InvalidAuthRequest {
        Token token = new Token(authorizationHeader);
        return token.getJwtToken() != null && !token.getJwtToken().isEmpty();
    }

    public static Teacher getTeacher(String authorizationHeader) throws InvalidAuthRequest {
        Token token = new Token(authorizationHeader);
        if (token.getJwtToken() != null && !token.getJwtToken().isEmpty()) {
            throw new InvalidAuthRequest("Invalid authentication token");
        }
        return new Teacher(token.getTeacherID());
    }
}


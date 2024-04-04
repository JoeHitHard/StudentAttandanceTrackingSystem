package aad.project.student.AttendanceTrackingSystem.exception;

public class InvalidAuthRequest extends Exception {
    public InvalidAuthRequest(String invalidUsernameOrPassword) {
        super(invalidUsernameOrPassword);
    }
}

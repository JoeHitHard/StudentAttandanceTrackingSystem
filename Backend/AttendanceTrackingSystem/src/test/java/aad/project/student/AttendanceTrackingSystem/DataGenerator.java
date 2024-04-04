package aad.project.student.AttendanceTrackingSystem;

import aad.project.student.AttendanceTrackingSystem.storage.entity.Attendance;
import aad.project.student.AttendanceTrackingSystem.storage.entity.Student;
import aad.project.student.AttendanceTrackingSystem.storage.entity.Teacher;
import aad.project.student.AttendanceTrackingSystem.storage.service.CassandraDataAccessService;
import com.github.javafaker.Faker;

import java.util.*;

import static java.lang.System.exit;

public class DataGenerator {
    public static void main(String[] args) {
        CassandraDataAccessService.getCqlSession().execute("drop table sat.students ;");
        CassandraDataAccessService.getCqlSession().execute("drop table sat.teachers ;");
        for (int i = 0; i < 60; i++) {
            Student student = createNewStudent(i);
            student.save();
        }
        for (int i = 0; i < 10; i++) {
            Teacher teacher = createNewTeacher(i);
            teacher.save();
        }
        for (int i = 0; i < 30; i++) {
            Attendance att = createRandomAttendances();
            att.save();
        }
        exit(0);
    }

    public static Student createNewStudent(int i) {
        Faker faker = new Faker();
        String id = "7" + String.format("%04d", i);
        String name = faker.name().fullName();
        String email = generateEmailFromName(name);
        return new Student(id, name, email);
    }

    public static Teacher createNewTeacher(int i) {
        Faker faker = new Faker();
        String id = "1" + String.format("%04d", i);
        String name = faker.name().fullName();
        String email = generateEmailFromName(name);
        String password = faker.internet().password();
        return new Teacher(id, name, email, password);
    }

    private static Attendance createRandomAttendances() {
        Faker faker = new Faker();
        String teacherId = "1" + String.format("%04d", faker.number().numberBetween(1, 10));
        int numStudentsPresent = faker.number().numberBetween(1, 60);

        Set<String> studentIdsPresent = new HashSet<>();
        while (studentIdsPresent.size() < numStudentsPresent) {
            String randomStudentId = "7" + String.format("%04d", faker.number().numberBetween(1, 60));
            studentIdsPresent.add(randomStudentId);
        }

        return new Attendance(UUID.randomUUID().toString(), String.format("03/%01d/2024", faker.number().numberBetween(1, 30)), new ArrayList<>(studentIdsPresent), teacherId);
    }

    private static String generateEmailFromName(String name) {
        // Convert the name to lowercase and replace spaces with dots
        String emailPrefix = name.toLowerCase().replace(" ", ".");
        return emailPrefix + "@sat.edu";
    }
}

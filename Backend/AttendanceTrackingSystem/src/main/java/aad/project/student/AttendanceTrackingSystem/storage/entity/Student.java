package aad.project.student.AttendanceTrackingSystem.storage.entity;

import aad.project.student.AttendanceTrackingSystem.storage.base.AbstractCassandraDataAccessor;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;

import java.util.StringJoiner;

public class Student {

    public static final String STUDENTS_TABLE = "students";

    static {
        StudentDAO studentDAO = new StudentDAO();
        studentDAO.createTable();
    }

    private final transient StudentDAO studentDAO;
    private String id;
    private String name;
    private String email;

    public Student() {
        this.studentDAO = new StudentDAO();
    }

    public Student(String id) {
        this();
        this.id = id;
        studentDAO.mapToEntity(id, this);
    }

    public Student(String id, String name, String email) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Student save() {
        studentDAO.insert(id, id, name, email);
        return this;
    }

    public Student delete() {
        studentDAO.delete(id);
        return this;
    }

    public Student update() {
        studentDAO.update(id, id, name, email);
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Student.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("email='" + email + "'")
                .toString();
    }

    public static class StudentDAO extends AbstractCassandraDataAccessor<Student> {

        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " +
                STUDENTS_TABLE + " (id TEXT PRIMARY KEY, name TEXT, email TEXT)");
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
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + STUDENTS_TABLE
                        + " (id, name, email) VALUES (?, ?, ?)");
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStatement() {
            if (UPDATE_STMT == null) {
                UPDATE_STMT = getCqlSession().prepare("UPDATE " + STUDENTS_TABLE
                        + " SET name = ?, email = ? WHERE id = ?");
            }
            return UPDATE_STMT;
        }

        @Override
        public PreparedStatement getDeleteStatement() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + STUDENTS_TABLE + " WHERE id = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStatement() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + STUDENTS_TABLE + " WHERE id = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public Student mapToEntity(String key, Student student) {
            Row row = get(key).one();
            if (row != null) {
                if (student == null) {
                    student = new Student();
                }
                student.id = row.getString("id");
                student.name = row.getString("name");
                student.email = row.getString("email");
                return student;
            }
            return null;
        }
    }
}

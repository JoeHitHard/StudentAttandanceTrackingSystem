package aad.project.student.AttendanceTrackingSystem.storage.entity;

import aad.project.student.AttendanceTrackingSystem.storage.base.AbstractCassandraDataAccessor;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;

import java.util.StringJoiner;

public class Teacher {

    public static final String TEACHERS_TABLE = "teachers";

    static {
        TeacherDAO teacherDAO = new TeacherDAO();
        teacherDAO.createTable();
    }

    private final transient TeacherDAO teacherDAO;
    private String id;
    private String name;
    private String email;
    private String password;

    public Teacher() {
        this.teacherDAO = new TeacherDAO();
    }

    public Teacher(String id) {
        this();
        this.id = id;
        teacherDAO.mapToEntity(id, this);
    }

    public Teacher(String id, String name, String email, String password) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Teacher save() {
        teacherDAO.insert(id, name, email, password);
        return this;
    }

    public Teacher delete() {
        teacherDAO.delete(id);
        return this;
    }

    public Teacher update() {
        teacherDAO.update(id, name, email, password);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Teacher.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("email='" + email + "'")
                .add("password='" + password + "'")
                .toString();
    }

    public boolean validate(String password) {
        return password.equals(this.password);
    }

    public static class TeacherDAO extends AbstractCassandraDataAccessor<Teacher> {

        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " +
                TEACHERS_TABLE + " (id TEXT PRIMARY KEY, name TEXT, email TEXT, password TEXT)");
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
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + TEACHERS_TABLE
                        + " (id, name, email, password) VALUES (?, ?, ?, ?)");
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStatement() {
            if (UPDATE_STMT == null) {
                UPDATE_STMT = getCqlSession().prepare("UPDATE " + TEACHERS_TABLE
                        + " SET name = ?, email = ?, password = ? WHERE id = ?");
            }
            return UPDATE_STMT;
        }

        @Override
        public PreparedStatement getDeleteStatement() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + TEACHERS_TABLE + " WHERE id = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStatement() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + TEACHERS_TABLE + " WHERE id = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public Teacher mapToEntity(String key, Teacher teacher) {
            Row row = get(key).one();
            if (row != null) {
                if (teacher == null) {
                    teacher = new Teacher();
                }
                teacher.id = row.getString("id");
                teacher.name = row.getString("name");
                teacher.email = row.getString("email");
                teacher.password = row.getString("password");
                return teacher;
            }
            return null;
        }
    }
}

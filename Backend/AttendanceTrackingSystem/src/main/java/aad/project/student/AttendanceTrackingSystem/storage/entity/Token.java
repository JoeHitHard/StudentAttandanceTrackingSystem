package aad.project.student.AttendanceTrackingSystem.storage.entity;

import aad.project.student.AttendanceTrackingSystem.storage.base.AbstractCassandraDataAccessor;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;

import java.util.StringJoiner;

public class Token {

    public static final String TOKENS_TABLE = "tokens";

    static {
        TokenDAO tokenDAO = new TokenDAO();
        tokenDAO.createTable();
    }

    private final transient TokenDAO tokenDAO;
    private String teacherID;
    private String jwtToken;

    public Token() {
        this.tokenDAO = new TokenDAO();
    }

    public Token(String teacherID, String jwtToken) {
        this();
        this.teacherID = teacherID;
        this.jwtToken = jwtToken;
    }

    public Token(String teacherID) {
        this();
        this.teacherID = teacherID;
        tokenDAO.mapToEntity(teacherID, this);
    }

    public Token save() {
        tokenDAO.insert(teacherID, jwtToken);
        return this;
    }

    public Token delete() {
        tokenDAO.delete(teacherID);
        return this;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Token.class.getSimpleName() + "[", "]")
                .add("teacherID='" + teacherID + "'")
                .add("jwtToken='" + jwtToken + "'")
                .toString();
    }

    public static class TokenDAO extends AbstractCassandraDataAccessor<Token> {

        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " +
                TOKENS_TABLE + " (teacherID TEXT PRIMARY KEY, jwtToken TEXT)");
        public static PreparedStatement INSERT_STMT;
        public static PreparedStatement DELETE_STMT;
        public static PreparedStatement SELECT_STMT;

        @Override
        public PreparedStatement getCreateStatement() {
            return CREATE_STMT;
        }

        @Override
        public PreparedStatement getInsertStatement() {
            if (INSERT_STMT == null) {
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + TOKENS_TABLE
                        + " (teacherID, jwtToken) VALUES (?, ?)");
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStatement() {
            return null;
        }

        @Override
        public PreparedStatement getDeleteStatement() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + TOKENS_TABLE + " WHERE teacherID = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStatement() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + TOKENS_TABLE + " WHERE teacherID = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public Token mapToEntity(String key, Token token) {
            Row row = get(key).one();
            if (row != null) {
                if (token == null) {
                    token = new Token();
                }
                token.teacherID = row.getString("teacherID");
                token.jwtToken = row.getString("jwtToken");
                return token;
            }
            return null;
        }
    }
}


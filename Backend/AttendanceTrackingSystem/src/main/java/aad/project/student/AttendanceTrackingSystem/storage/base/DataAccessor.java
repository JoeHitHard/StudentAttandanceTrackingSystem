package aad.project.student.AttendanceTrackingSystem.storage.base;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;

public interface DataAccessor<T> {
    // Prepared statements
    public PreparedStatement getCreateStatement();

    public PreparedStatement getInsertStatement();

    public PreparedStatement getUpdateStatement();

    public PreparedStatement getDeleteStatement();

    public PreparedStatement getStatement();

    // Process statements
    public ResultSet createTable();

    public ResultSet insert(String key, Object... values);

    public ResultSet get(String key);

    public ResultSet update(String key, Object... values);

    public ResultSet delete(String key);

    public ResultSet query(BoundStatement statement);

    public T mapToEntity(String key, T entity);
}

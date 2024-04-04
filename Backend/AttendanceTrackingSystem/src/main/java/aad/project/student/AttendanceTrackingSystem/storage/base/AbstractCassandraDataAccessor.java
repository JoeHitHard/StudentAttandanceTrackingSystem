package aad.project.student.AttendanceTrackingSystem.storage.base;

import aad.project.student.AttendanceTrackingSystem.storage.service.CassandraDataAccessService;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
public abstract class AbstractCassandraDataAccessor<T> implements DataAccessor<T> {

    private final CqlSession cqlSession;

    public AbstractCassandraDataAccessor() {
        CassandraDataAccessService.initialize();
        cqlSession = CassandraDataAccessService.getCqlSession();
    }

    public static CqlSession getCqlSession() {
        return CassandraDataAccessService.getCqlSession();
    }

    @Override
    public ResultSet createTable() {
        BoundStatement bind = getCreateStatement().bind();
        return executeStatement(bind);
    }

    private ResultSet bindAndExecute(Object[] values, PreparedStatement preparedStatement) {
        BoundStatement bind = preparedStatement.bind(values);
        return executeStatement(bind);
    }

    private ResultSet executeStatement(BoundStatement bind) {
        return cqlSession.execute(bind);
    }

    @Override
    public ResultSet insert(String key, Object... values) {
        return bindAndExecute(values, getInsertStatement());
    }

    @Override
    public ResultSet update(String key, Object... values) {
        return bindAndExecute(values, getUpdateStatement());
    }

    @Override
    public ResultSet get(String key) {
        return bindAndExecute(new String[]{key}, getStatement());
    }

    @Override
    public ResultSet delete(String key) {
        return bindAndExecute(new String[]{key}, getDeleteStatement());
    }

    @Override
    public ResultSet query(BoundStatement statement) {
        return executeStatement(statement);
    }
}


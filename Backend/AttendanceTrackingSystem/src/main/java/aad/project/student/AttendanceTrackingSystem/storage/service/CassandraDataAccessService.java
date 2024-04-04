package aad.project.student.AttendanceTrackingSystem.storage.service;

import aad.project.student.AttendanceTrackingSystem.properties.ApplicationProperties;
import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class CassandraDataAccessService {
    private static CqlSession cqlSession;

    public static String getKeyspace() {
        return ApplicationProperties.CASSANDRA_KEYSPACE;
    }

    public static void initialize() {
        if (cqlSession == null) {
            cqlSession = CqlSession.builder().withKeyspace(getKeyspace())
                    .withLocalDatacenter("datacenter1")
                    .addContactPoint(new InetSocketAddress("172.20.0.3", 9042)).build(); // Change this based on Cassandra port ID from Docker
        }
    }

    public static CqlSession getCqlSession() {
        if (cqlSession == null) {
            initialize();
        }
        return cqlSession;
    }

}

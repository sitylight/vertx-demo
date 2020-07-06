package com.demo.dl.database;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.ClassicConfiguration;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

/**
 * @author derrick
 */
public class Migration {
    private DataSource dataSource;

    public Migration setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public void doMigration() {
        final ClassicConfiguration configuration = new ClassicConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setBaselineOnMigrate(true);
        configuration.setLocations(new Location("db/migration"));
        configuration.setEncoding(StandardCharsets.UTF_8);
        configuration.setOutOfOrder(true);
        final Flyway flyway = new Flyway(configuration);
        flyway.migrate();
    }
}

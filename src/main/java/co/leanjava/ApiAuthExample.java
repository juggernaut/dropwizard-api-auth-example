package co.leanjava;

import co.leanjava.resources.LoginResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.h2.tools.RunScript;
import org.skife.jdbi.v2.DBI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;

public class ApiAuthExample extends Application<ApiAuthExampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ApiAuthExample().run(args);
    }

    @Override
    public String getName() {
        return "dropwizard-api-auth-example";
    }

    @Override
    public void initialize(final Bootstrap<ApiAuthExampleConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final ApiAuthExampleConfiguration configuration, final Environment environment) {
        this.initializeDB();
        final DBIFactory dbiFactory = new DBIFactory();
        final DBI dbi = dbiFactory.build(environment, configuration.getDatabase(), "authexample");
        environment.jersey().register(new LoginResource());
    }

    private void initializeDB() {
        try {
            Class.forName("org.h2.Driver");
            final Connection conn = DriverManager.getConnection("jdbc:h2:mem:authexample", "user", "pw");
            final InputStream ddl = this.getClass().getClassLoader().getResourceAsStream("ddl.sql");
            RunScript.execute(conn, new BufferedReader(new InputStreamReader(ddl)));
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize H2 database", e);
        }
    }

}

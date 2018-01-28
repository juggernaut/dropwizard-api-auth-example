package co.leanjava.security;

import co.leanjava.db.AuthTokenDao;
import co.leanjava.resources.GreetingResource;
import co.leanjava.resources.LoginResource;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;
import org.junit.ClassRule;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import javax.sql.DataSource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author ameya
 */
public class AuthenticationTest {

    private static final AuthTokenDao AUTH_TOKEN_DAO = buildDao();

    public static AuthTokenDao buildDao() {
        try {
            Class.forName("org.h2.Driver");
            DataSource ds = JdbcConnectionPool.create("jdbc:h2:mem:authexample", "user", "pw");
            final InputStream ddl = AuthenticationTest.class.getClassLoader().getResourceAsStream("ddl.sql");
            RunScript.execute(ds.getConnection(), new BufferedReader(new InputStreamReader(ddl)));

            DBI dbi = new DBI(ds);
            return dbi.onDemand(AuthTokenDao.class);
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize H2 database", e);
        }
    }

    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(new AuthDynamicFeature(
                    new TokenAuthFilter.TokenAuthFilterBuilder()
                        .setAuthenticator(new TokenAuthenticator(AUTH_TOKEN_DAO))
                        .buildAuthFilter()))
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addResource(new LoginResource(AUTH_TOKEN_DAO))
            .addResource(new GreetingResource())
            .build();

    @Test
    public void testLogin() {
        Response response = login();

        assertEquals(204, response.getStatus());
        final Map<String, NewCookie> cookies = response.getCookies();

        final NewCookie authCookie = cookies.get(TokenAuthFilter.AUTH_TOKEN_COOKIE_NAME);
        assertNotNull(authCookie);
        assertTrue(authCookie.isHttpOnly());
        assertTrue(authCookie.isSecure());

        final NewCookie csrfCookie = cookies.get(TokenAuthFilter.CSRF_TOKEN_COOKIE_NAME);
        assertNotNull(csrfCookie);
        assertTrue(csrfCookie.isSecure());
        assertFalse(csrfCookie.isHttpOnly());
    }

    @Test
    public void testNoCookiesOrCsrfFails() {
        Response response = RULE.target("/greeting")
                .request()
                .get();
        assertEquals(401, response.getStatus());
    }

    @Test
    public void testCSRFHeaderMissing() {
        Response loginResponse = login();
        final NewCookie authCookie = getAuthCookie(loginResponse);
        final NewCookie csrfCookie = getCSRFCookie(loginResponse);

        Response response = RULE.target("/greeting")
                .request()
                .cookie(authCookie)
                .cookie(csrfCookie)
                .get();

        assertEquals(401, response.getStatus());
    }

    @Test
    public void testProtectedResourceSuccessfulAccess() {
        Response loginResponse = login();
        final NewCookie authCookie = getAuthCookie(loginResponse);
        final NewCookie csrfCookie = getCSRFCookie(loginResponse);

        Response response = RULE.target("/greeting")
                .request()
                .cookie(authCookie)
                .cookie(csrfCookie)
                .header(TokenAuthFilter.CSRF_TOKEN_HEADER_NAME, csrfCookie.getValue())
                .get();

        assertEquals(200, response.getStatus());
        final GreetingResource.Greeting greeting = response.readEntity(GreetingResource.Greeting.class);
        assertEquals(new GreetingResource.Greeting("Hello there!"), greeting);
    }

    @Test
    public void testAuthCookieTampered() {
        Response loginResponse = login();
        final NewCookie csrfCookie = getCSRFCookie(loginResponse);

        final NewCookie tamperedAuthCookie = new NewCookie(TokenAuthFilter.AUTH_TOKEN_COOKIE_NAME, "blahblahblash");

        Response response = RULE.target("/greeting")
                .request()
                .cookie(tamperedAuthCookie)
                .cookie(csrfCookie)
                .header(TokenAuthFilter.CSRF_TOKEN_HEADER_NAME, csrfCookie.getValue())
                .get();

        assertEquals(401, response.getStatus());
    }

    @Test
    public void testStaticCSRFHeaderValue() {

        Response loginResponse = login();
        final NewCookie authCookie = getAuthCookie(loginResponse);
        final NewCookie csrfCookie = getCSRFCookie(loginResponse);

        Response response = RULE.target("/greeting")
                .request()
                .cookie(authCookie)
                .cookie(csrfCookie)
                .header(TokenAuthFilter.CSRF_TOKEN_HEADER_NAME, "XMLHttpRequest")
                .get();

        assertEquals(401, response.getStatus());
    }

    private NewCookie getAuthCookie(final Response response) {
        final NewCookie authCookie = response.getCookies().get(TokenAuthFilter.AUTH_TOKEN_COOKIE_NAME);
        assertNotNull(authCookie);
        return authCookie;
    }

    private NewCookie getCSRFCookie(final Response response) {
        final NewCookie csrfCookie = response.getCookies().get(TokenAuthFilter.CSRF_TOKEN_COOKIE_NAME);
        assertNotNull(csrfCookie);
        return csrfCookie;
    }

    private Response login() {

        return RULE.target("/login")
                .request()
                .post(Entity.form(new Form().param("username", "johndoe")
                        .param("password", "SECRET")));
    }
}

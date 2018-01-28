package co.leanjava.resources;

import co.leanjava.db.AuthTokenDao;
import co.leanjava.filters.TokenAuthFilter;
import co.leanjava.filters.TokenCredentials;
import org.hibernate.validator.constraints.NotEmpty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * @author ameya
 */
@Path("/login")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    private final AuthTokenDao authTokenDao;

    private static final int EXPIRE_AFTER_SECONDS = 5000;

    private static final int JOHN_DOE_USER_ID = 42;

    public LoginResource(AuthTokenDao authTokenDao) {
        this.authTokenDao = authTokenDao;
    }

    @POST
    public Response login(
            @NotEmpty @FormParam("username") String username,
            @NotEmpty @FormParam("password") String password) {
        // NOTE: secure password validation is outside the scope of this example. Please refer to
        // https://codahale.com/how-to-safely-store-a-password/ for how to do this.
        if (username.equals("johndoe") && password.equals("SECRET")) {
            return this.generateAndStoreToken(JOHN_DOE_USER_ID);
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    private Response generateAndStoreToken(final int userId) {
        this.authTokenDao.deleteTokenForUser(userId);
        final TokenCredentials newToken = TokenCredentials.generateRandomCredentials();
        int inserted = this.authTokenDao.insertToken(userId, newToken, EXPIRE_AFTER_SECONDS);
        if (inserted == 0) {
            throw new WebApplicationException("Sorry, could not login due to server error");
        }
        final NewCookie sessionCookie = new NewCookie(TokenAuthFilter.AUTH_TOKEN_COOKIE_NAME, newToken.getAuthToken(), null, null, null, 5000, true, true);
        final NewCookie csrfCookie = new NewCookie(TokenAuthFilter.CSRF_TOKEN_COOKIE_NAME, newToken.getCsrfToken(), null, null, null, EXPIRE_AFTER_SECONDS, true, false);
        return Response.noContent().cookie(sessionCookie, csrfCookie).build();
    }
}

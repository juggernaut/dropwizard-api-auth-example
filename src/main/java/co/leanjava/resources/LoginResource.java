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

    private static final long EXPIRE_AFTER_SECONDS = 5000;

    private static final int JOHN_DOE_USER_ID = 42;

    public LoginResource(AuthTokenDao authTokenDao) {
        this.authTokenDao = authTokenDao;
    }

    @POST
    public Response login(
            @NotEmpty @FormParam("username") String username,
            @NotEmpty @FormParam("password") String password) {
        if (username.equals("johndoe") && password.equals("SECRET")) {
            return this.generateAndStoreToken(JOHN_DOE_USER_ID);
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    private Response generateAndStoreToken(final int userId) {
        authTokenDao.deleteTokenForUser(userId);
        TokenCredentials newToken = TokenCredentials.generateRandomToken();
        int inserted = authTokenDao.insertToken(userId, newToken.getToken(), EXPIRE_AFTER_SECONDS);
        if (inserted == 0) {
            throw new WebApplicationException("Sorry, could not login due to server error");
        }
        NewCookie cookie = new NewCookie(TokenAuthFilter.TOKEN_COOKIE_NAME, newToken.getToken(), null, null, null, 5000, true, true);
        return Response.noContent().cookie(cookie).build();
    }
}

package co.leanjava.resources;

import org.hibernate.validator.constraints.NotEmpty;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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

    @POST
    public Response login(
            @Context HttpServletRequest request,
            @NotEmpty @FormParam("username") String username,
            @NotEmpty @FormParam("password") String password) {
        if (password.equals("SECRET")) {
            final String sessionId = request.getSession(true).getId();
            NewCookie cookie = new NewCookie("SID", sessionId, null, null, null, 30, true, true);
            return Response.noContent().cookie(cookie).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}

package co.leanjava.resources;

import org.hibernate.validator.constraints.NotEmpty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author ameya
 */
@Path("/login")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    @POST
    public Response login(@NotEmpty @FormParam("username") String username,
                          @NotEmpty @FormParam("password") String password) {
        if (password.equals("SECRET")) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}

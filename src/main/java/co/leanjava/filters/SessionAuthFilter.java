package co.leanjava.filters;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author ameya
 */
@Priority(Priorities.AUTHENTICATION)
public class SessionAuthFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        final String[] pathSegs = requestContext.getUriInfo().getPath().split("/");

        // Allow only /login to go through unauthenticated
        if (pathSegs.length >= 1 && pathSegs[0].equals("login")) {
            return;
        }

        final HttpSession session = request.getSession(false);
        if (session == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}

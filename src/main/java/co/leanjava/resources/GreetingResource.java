package co.leanjava.resources;

import co.leanjava.filters.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author ameya
 */
@Path("/greeting")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    @GET
    public Response getGreeting(@Auth User user) {
        final Greeting greeting = new Greeting("Hello there!");
        return Response.ok(greeting).build();
    }

    public static class Greeting {

        @JsonProperty("greeting")
        private final String greeting;

        public Greeting(String greeting) {
            this.greeting = greeting;
        }
    }
}

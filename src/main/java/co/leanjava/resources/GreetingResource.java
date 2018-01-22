package co.leanjava.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public Response getGreeting() {
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

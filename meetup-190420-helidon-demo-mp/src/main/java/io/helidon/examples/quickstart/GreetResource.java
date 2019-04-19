package io.helidon.examples.quickstart;

import java.util.Collections;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

@Path("/greet")
@RequestScoped
public class GreetResource {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

    private static final Logger logger = Logger.getLogger("io.helidon.microprofile.helloworld.level");

    private final GreetingProvider greetingProvider;
    private final String appName;
    private FaultTolerance faultTolerance;
    
    @Inject
    public GreetResource(GreetingProvider greetingConfig, FaultTolerance faultTolerance, @ConfigProperty(name = "app.name") String appName) {
        this.greetingProvider = greetingConfig;
        this.appName = appName;
        this.faultTolerance = faultTolerance;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getDefaultMessage() {
        logger.info("appName : " + appName);
        return createResponse("World");
    }

    @Counted(
        name = "accessctrDefault",
        reusable = true,
        description = "Total greetings accesses",
        displayName = "Access Counter",
        monotonic = true,
        unit = MetricUnits.NONE)
    @Timed(name = "accessctrTimer",
        reusable = true,
        description = "Timer greetings accesses",
        displayName = "Timer Counter",
        unit = MetricUnits.SECONDS)
    @Path("/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getMessage(@PathParam("name") String name) {
        return createResponse(name);
    }

    @Path("/greeting")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGreeting(JsonObject jsonObject) {

        if (!jsonObject.containsKey("greeting")) {
            JsonObject entity = JSON.createObjectBuilder()
                    .add("error", "No greeting provided")
                    .build();
            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
        }

        String newGreeting = jsonObject.getString("greeting");

        greetingProvider.setMessage(newGreeting);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Path("/fault-tolerance")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject faultTolerance() {

        String message = faultTolerance.faultTolerance();

        JsonObject jsonObject = JSON.createObjectBuilder().add("message", message).build();

        return jsonObject;
    }
    
    private JsonObject createResponse(String who) {
        String msg = String.format("%s %s!", greetingProvider.getMessage(), who);

        return JSON.createObjectBuilder()
                .add("message", msg)
                .build();
    }
}

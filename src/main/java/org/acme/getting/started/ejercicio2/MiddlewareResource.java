package org.acme.getting.started.ejercicio2;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.ws.rs.QueryParam;

@Path("/api")
public class MiddlewareResource {

    @Inject
    @RestClient
    Client client;

    @ConfigProperty(name = "interfisa.jwt")
    String jwt;

    @GET
    @Path("/surcusales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sucursales(@QueryParam("nombreODireccion") String nombreODireccion) {
        Object response = client.getSucursales(nombreODireccion);
        if (response != null) {
            return Response.ok(response).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/sipap")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sipap(@QueryParam("dominio") String dominio) {
        Object response = client.getSipap(dominio, "Bearer " + jwt);
        if (response != null) {
            return Response.ok(response).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}

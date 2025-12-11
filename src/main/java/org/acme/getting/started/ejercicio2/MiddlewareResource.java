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

import org.jboss.logging.Logger;

@Path("/api")
public class MiddlewareResource {

    private static final Logger log = Logger.getLogger(MiddlewareResource.class);

    @Inject
    @RestClient
    Client client;

    @ConfigProperty(name = "interfisa.jwt")
    String jwt;

    @GET
    @Path("/surcusales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sucursales(@QueryParam("nombreODireccion") String nombreODireccion) {
        log.debugf("HTTP GET /api/surcusales?nombreODireccion=%s", nombreODireccion);
        try {
            Object response = client.getSucursales(nombreODireccion);
            if (response != null) {
                return Response.ok(response).build();
            }
            log.debugf("No se encontraron sucursales para: %s", nombreODireccion);
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            log.errorf("Error en consulta de sucursales", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/sipap")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sipap(@QueryParam("dominio") String dominio) {
        log.debugf("HTTP GET /api/sipap?dominio=%s", dominio);
        try {
            Object response = client.getSipap(dominio, "Bearer " + jwt.trim());
            if (response != null) {
                return Response.ok(response).build();
            }
            log.debugf("No se encontró información sipap para: %s", dominio);
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            log.errorf("Error en consulta de sipap", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
package org.acme.getting.started.ejercicio2;

import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient
@ClientHeaderParam(name = "X-INTERFISA-BE-VERSION", value = "V1.0")

public interface Client {

    @GET
    @Path("api/common/centros-servicios")
    @Produces(MediaType.APPLICATION_JSON)
    Object getSucursales(@QueryParam("nombreODireccion") String nombreODireccion);

    @GET
    @Path("api/secure/common/parametros")
    @Produces(MediaType.APPLICATION_JSON)
    Object getSipap(@QueryParam("dominio") String dominio, @HeaderParam("Authorization") String jwt);
}

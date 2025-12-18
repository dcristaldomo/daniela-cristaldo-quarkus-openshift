package org.acme.getting.started.ejericicio4;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/api/favorito")
@Produces("application/json")
@Consumes("application/json")
public class FavoritoResource {
    @Inject
    EntityManager entityManager;

    @GET
    @Transactional
    public Response getAllFavoritos(){
        return Response.ok(entityManager.createQuery("from Favorito",Favorito.class).getResultList()).build();
    }

    @POST
    @Transactional
    public Response createFavorito(Favorito favorito) {
        try {
            entityManager.persist(favorito);
            return Response.status(Response.Status.CREATED).entity(favorito).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Ya existe un favorito con ese tipo y referenciaId.\"}")
                    .build();
        }
    }
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteFavorito(@PathParam("id") Long id) {
        Favorito favorito = entityManager.find(Favorito.class, id);
        if (favorito == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Favorito no encontrado.\"}")
                    .build();
        }
        entityManager.remove(favorito);
        return Response.noContent().build();
    }
}

package org.acme.getting.started.ejericicio4;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.getting.started.ejercicio2.MiddlewareResource;
import org.jboss.logging.Logger;

@Path("/api/favoritos")
@Produces("application/json")
@Consumes("application/json")
public class FavoritoResource {
    private static final Logger log = Logger.getLogger(MiddlewareResource.class);
    @Inject
    EntityManager entityManager;

    @GET
    @Transactional
    public Response getAllFavoritos(){
        log.debugf("HTTP GET /api/favoritos");
        try {
            return Response.ok(entityManager.createQuery("from Favorito", Favorito.class).getResultList()).build();
        } catch (Exception e) {
            log.errorf("Error al obtener la lista de favoritos: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener los favoritos.\"}")
                    .build();
        }
    }

    @POST
    @Transactional
    public Response createFavorito(Favorito favorito) {
        log.debugf("HTTP POST /api/favoritos");
        try {
            if (!TipoFavorito.CENTRO.equals(favorito.getTipo()) && !TipoFavorito.SIPAP.equals(favorito.getTipo())) {
                log.warnf("Tipo inválido proporcionado: %s", favorito.getTipo());
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"El tipo debe ser 'CENTRO' o 'SIPAP'.\"}")
                        .build();
            }
            entityManager.persist(favorito);
            log.infof("Favorito creado exitosamente con referenciaId: %s", favorito.getReferenciaId());
            return Response.status(Response.Status.CREATED).entity(favorito).build();
        } catch (PersistenceException e) {
            log.errorf("Error al crear el favorito con referenciaId %s: %s", favorito.getReferenciaId(), e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Ya existe un favorito con ese tipo y referenciaId.\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteFavorito(@PathParam("id") Long id) {
        log.debugf("HTTP DELETE /api/favoritos/%d ", id);
        try {
            Favorito favorito = entityManager.find(Favorito.class, id);
            if (favorito == null) {
                log.warnf("No se encontró un favorito con id: %d", id);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Favorito no encontrado.\"}")
                        .build();
            }
            entityManager.remove(favorito);
            log.infof("Favorito eliminado exitosamente con id: %d", id);
            return Response.noContent().build();
        } catch (Exception e) {
            log.errorf("Error al eliminar el favorito con id %d: %s", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al eliminar el favorito.\"}")
                    .build();
        }
    }

}

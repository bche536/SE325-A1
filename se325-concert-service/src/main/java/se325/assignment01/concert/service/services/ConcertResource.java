package se325.assignment01.concert.service.services;

import se325.assignment01.concert.common.dto.ConcertDTO;
import se325.assignment01.concert.common.dto.ConcertSummaryDTO;
import se325.assignment01.concert.service.domain.Concert;
import se325.assignment01.concert.service.mapper.ConcertMapper;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;

@Path("/concert-service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConcertResource {

    // TODO Implement this.

    @GET
    @Path("/concerts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveConcert(@PathParam("id") long id) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {

            // Start a transaction for persisting the audit data.
            em.getTransaction().begin();

            // Or just the load object by ID.
            Concert concert = em.createQuery("select c from Concert c join c.dates d where d =: id", Concert.class).setParameter("id", id).getSingleResult();
            em.getTransaction().commit();

            if (concert == null) {
                // Return a HTTP 404 response if the specified Concert isn't found.
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            //Convert the Concert to a ConcertDTO.
            ConcertDTO dtoConcert = ConcertMapper.toDto(concert);

            Response.ResponseBuilder builder = Response.ok(dtoConcert);
            return builder.build();

        } finally {
            em.close();
        }
    }

    @GET
    @Path("/concerts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveConcerts() {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {

            // Start a transaction for persisting the audit data.
            em.getTransaction().begin();

            TypedQuery<Concert> concertsQuery = em.createQuery("select c from Concert c join c.dates d where d =: id", Concert.class);
            List<Concert> concerts = concertsQuery.getResultList();

            em.getTransaction().commit();

            if (concerts == null) {
                // Return a HTTP 404 response if the specified Concert isn't found.
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            //Convert the Concerts to a list of ConcertDTO.
            List<ConcertDTO> dtoConcerts = new ArrayList<>();
            for (Concert c : concerts) {
                ConcertDTO dtoConcert = ConcertMapper.toDto(c);
                dtoConcerts.add(dtoConcert);
            }

            Response.ResponseBuilder builder = Response.ok(dtoConcerts);
            return builder.build();

        } finally {
            em.close();
        }
    }

    @GET
    @Path("/concerts/summaries")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveConcertSummaries() {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {

            // Start a transaction for persisting the audit data.
            em.getTransaction().begin();

            List<Concert> concerts = em.createQuery("SELECT c FROM Concert c").getResultList();

            em.getTransaction().commit();

            if (concerts == null) {
                // Return a HTTP 404 response if the specified Concert isn't found.
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            //Convert the Concerts to a list of ConcertDTO.
            List<ConcertSummaryDTO> dtoConcertsSummaries = new ArrayList<>();
            for (Concert c : concerts) {
                dtoConcertsSummaries.add(ConcertMapper.toConcertSummaryDto(c));
            }

            Response.ResponseBuilder builder = Response.ok(dtoConcertsSummaries);
            return builder.build();

        } finally {
            em.close();
        }
    }
}

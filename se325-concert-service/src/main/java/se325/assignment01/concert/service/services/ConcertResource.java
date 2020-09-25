package se325.assignment01.concert.service.services;

import se325.assignment01.concert.common.dto.ConcertDTO;
import se325.assignment01.concert.common.dto.ConcertSummaryDTO;
import se325.assignment01.concert.service.domain.Concert;
import se325.assignment01.concert.service.mapper.ConcertMapper;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
public class ConcertResource {

    // TODO Implement this.

    @GET
    @Path("/concerts/{id}")
    public Response retrieveConcert(@PathParam("id") long id) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {

            // Start a transaction for persisting the audit data.
            em.getTransaction().begin();

            // Or just the load object by ID.
            Concert concert = em.find(Concert.class, id);
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
    public Response retrieveConcerts() {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {

            // Start a transaction for persisting the audit data.
            em.getTransaction().begin();

            TypedQuery<Concert> concertsQuery = em.createQuery("select c from Concert c", Concert.class);
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
    public Response retrieveConcertSummaries() {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {

            // Start a transaction for persisting the audit data.
            em.getTransaction().begin();

            TypedQuery<Concert> concertsQuery = em.createQuery("select c from Concert c", Concert.class);
            List<Concert> concerts = concertsQuery.getResultList();

            em.getTransaction().commit();

            if (concerts == null) {
                // Return a HTTP 404 response if the specified Concert isn't found.
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            //Convert the Concerts to a list of ConcertDTO.
            List<ConcertSummaryDTO> dtoConcertsSummaries = new ArrayList<>();
            for (Concert c : concerts) {
                ConcertSummaryDTO dtoConcertSummary = ConcertMapper.toConcertSummaryDto(c);
                dtoConcertsSummaries.add(dtoConcertSummary);
            }

            Response.ResponseBuilder builder = Response.ok(dtoConcertsSummaries);
            return builder.build();

        } finally {
            em.close();
        }
    }
}

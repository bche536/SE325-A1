package se325.assignment01.concert.service.services;

import netscape.javascript.JSObject;
import org.h2.util.json.JSONObject;
import se325.assignment01.concert.common.dto.*;
import se325.assignment01.concert.common.types.BookingStatus;
import se325.assignment01.concert.service.domain.*;
import se325.assignment01.concert.service.jaxrs.LocalDateTimeParam;
import se325.assignment01.concert.service.mapper.BookingMapper;
import se325.assignment01.concert.service.mapper.ConcertMapper;
import se325.assignment01.concert.service.mapper.PerformerMapper;
import se325.assignment01.concert.service.mapper.SeatMapper;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.awt.print.Book;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
public class ConcertResource {

    private static HashMap<ConcertInfoSubscriptionDTO, AsyncResponse> concertSubscriptions = new HashMap<ConcertInfoSubscriptionDTO, AsyncResponse>();

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
            Concert concert = em.find(Concert.class, id);

            if (concert == null) {
                // Return a HTTP 404 response if the specified Concert isn't found.
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            //Convert the Concert to a ConcertDTO.
            ConcertDTO dtoConcert = ConcertMapper.toDto(concert);

            em.getTransaction().commit();

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

            TypedQuery<Concert> concertsQuery = em.createQuery("select c from Concert c", Concert.class);
            List<Concert> concerts = concertsQuery.getResultList();

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

            em.getTransaction().commit();

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

    @GET
    @Path("/performers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePerformer(@PathParam("id") long id) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {

            // Start a transaction for persisting the audit data.
            em.getTransaction().begin();

            // Or just the load object by ID.
            Performer performer = em.find(Performer.class, id);

            if (performer == null) {
                // Return a HTTP 404 response if the specified Performer isn't found.
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            //Convert the Performer to a Performer.
            PerformerDTO dtoPerformer = PerformerMapper.toDto(performer);

            em.getTransaction().commit();

            Response.ResponseBuilder builder = Response.ok(dtoPerformer);
            return builder.build();

        } finally {
            em.close();
        }
    }

    @GET
    @Path("/performers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePerformers() {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {

            // Start a transaction for persisting the audit data.
            em.getTransaction().begin();

            TypedQuery<Performer> concertsQuery = em.createQuery("select p from Performer p", Performer.class);
            List<Performer> performers = concertsQuery.getResultList();

            if (performers == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            //Convert the Performers to a list of PerformerDTO
            List<PerformerDTO> dtoPerformers = new ArrayList<>();
            for (Performer p : performers) {
                PerformerDTO dtoPerformer = PerformerMapper.toDto(p);
                dtoPerformers.add(dtoPerformer);
            }

            em.getTransaction().commit();

            Response.ResponseBuilder builder = Response.ok(dtoPerformers);
            return builder.build();

        } finally {
            em.close();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(UserDTO dtoUser) {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();

            //Query the db for a matching user
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
            query.setParameter("username", dtoUser.getUsername());
            query.setParameter("password", dtoUser.getPassword());

            //Check if there is a matching user
            if (query == null || query.getResultList().isEmpty()) {
                // Return a HTTP 401
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }

            User user = query.getSingleResult();

            NewCookie httpCookie = new NewCookie("auth", user.getId().toString());

            em.getTransaction().commit();

            Response.ResponseBuilder builder = Response.ok().cookie(httpCookie);

            return builder.build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/seats/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSeats(@PathParam("date") LocalDateTimeParam dateParam, @QueryParam("status") BookingStatus status) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        LocalDateTime date = dateParam.getLocalDateTime();


        try {
            em.getTransaction().begin();
            TypedQuery query;

            if(status == BookingStatus.Booked) {
                query = em.createQuery("SELECT s FROM Seat s WHERE s.date = :date AND s.isBooked = :true", Seat.class);
                query.setParameter("date", date);
                query.setParameter("true", true);

            }
            else if(status == BookingStatus.Unbooked) {
                query = em.createQuery("SELECT s FROM Seat s WHERE s.date = :date AND s.isBooked = :false", Seat.class);
                query.setParameter("date", date);
                query.setParameter("false", false);

            }
            else if(status == BookingStatus.Any) {
                query = em.createQuery("SELECT s FROM Seat s WHERE s.date = :date", Seat.class);
                query.setParameter("date", date);

            }
            else {
                // Return a HTTP 404 because we cannot handle that query
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            if(query.getResultList() == null) {
                // Return a HTTP 404 because no seats
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            //Map Seat to SeatDTO
            List<Seat> seats = query.getResultList();
            List<SeatDTO> dtoSeats = new ArrayList<>();
            for (Seat s : seats) {
                dtoSeats.add(SeatMapper.toDto(s));
            }

            em.getTransaction().commit();


            Response.ResponseBuilder builder = Response.ok(dtoSeats);
            return builder.build();

        } finally {
            em.close();
        }

    }

    @GET
    @Path("/bookings/{id}")
    public Response getBooking(@PathParam("id") long id, @CookieParam("auth") Cookie cookie) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        if(cookie == null) {
            // Return a HTTP 401
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        String userId = cookie.getValue();

        if(userId == null) {
            // Return a HTTP 401
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        User authentication = em.find(User.class, Long.valueOf(userId));

        if(authentication == null) {
            // Return a HTTP 401
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        try {
            em.getTransaction().begin();
            Booking booking = em.find(Booking.class, id);

            if (booking == null) {
                // Return a HTTP 404 response if the specified Performer isn't found.
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            if(!(booking.getUser().getId() == Long.valueOf(userId))) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            //Convert the Performer to a Performer.
            BookingDTO dtoBooking = BookingMapper.toDto(booking);

            em.getTransaction().commit();

            Response.ResponseBuilder builder = Response.ok(dtoBooking);
            return builder.build();

        } finally {
            em.close();
        }
    }

    @GET
    @Path("/bookings")
    public Response getBookings(@CookieParam("auth") Cookie cookie) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        if(cookie == null) {
            // Return a HTTP 401
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        String userId = cookie.getValue();


        if(userId == null) {
            // Return a HTTP 401
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        try {
            em.getTransaction().begin();

            User authentication = em.find(User.class, Long.valueOf(userId));

            if (authentication == null) {
                // Return a HTTP 401
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }

            List<Booking> bookings = em.createQuery("select b from Booking b where b.user.id =:userId", Booking.class).setParameter("userId", Long.valueOf(userId)).getResultList();
            List<BookingDTO> dtoBookings = new ArrayList<>();
            for (Booking b : bookings) {
                dtoBookings.add(BookingMapper.toDto(b));
            }

            em.getTransaction().commit();

            Response.ResponseBuilder builder = Response.ok(dtoBookings);
            return builder.build();

        } finally {
            em.close();
        }
    }

    @POST
    @Path("/bookings")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response makeBookingRequest(BookingRequestDTO dtoBookingRequest, @CookieParam("auth") Cookie cookie) {
        if(cookie == null) {
            // Return a HTTP 401
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        String userId = cookie.getValue();

        if(userId == null) {
            // Return a HTTP 401
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        if(dtoBookingRequest == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        EntityManager em = PersistenceManager.instance().createEntityManager();


    try {
        em.getTransaction().begin();

        User authentication = em.find(User.class, Long.valueOf(userId));

        if(authentication == null) {
            // Return a HTTP 401
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }


        TypedQuery<Seat> query;
        List<Seat> bookedSeats = new ArrayList<>();

        //Check for if concert exists and date validity
        Concert concert = em.find(Concert.class, dtoBookingRequest.getConcertId());
        if(concert == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        else if(!concert.getDates().contains(dtoBookingRequest.getDate())){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        // For each seat find it and book it
        for(String s : dtoBookingRequest.getSeatLabels()) {
            query = em.createQuery("SELECT s FROM Seat s WHERE s.label= :seatLabel AND s.date= :date", Seat.class);
            query.setParameter("seatLabel", s);
            query.setParameter("date", dtoBookingRequest.getDate());
            Seat seat = query.getSingleResult();
            if(seat.isBooked()){
                // One of the seats are already booked - do not commit or persist
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            seat.setBooked(true);
            bookedSeats.add(seat);
        }

        //Create a booking and assign it to the user - make sure the access is locked so one booking at a time
        User user = em.find(User.class, Long.valueOf(userId));
        Booking booking = new Booking(user, dtoBookingRequest.getConcertId(), dtoBookingRequest.getDate(), bookedSeats);
        user.getBookings().add(booking);

        em.persist(booking);

        //Assign the booking to seats
        for(Seat s : bookedSeats) {
            s.setBooking(booking);
            em.persist(s);
        }

        em.getTransaction().commit();

        //Publish and subscribe
        em.getTransaction().begin();
        List<Seat> seatsTotalList = em.createQuery(
                "select s from Seat s where s.date = :date", Seat.class)
                .setParameter("date", dtoBookingRequest.getDate())
                .getResultList();
        List<Seat> bookedSeatsTotalList = em.createQuery(
                "select s from Seat s where s.date = :date and s.isBooked = :true", Seat.class)
                .setParameter("date", dtoBookingRequest.getDate()).setParameter("true", true)
                .getResultList();
        int seatsTotal = seatsTotalList.size();
        int bookedSeatsTotal = bookedSeatsTotalList.size();

        em.getTransaction().commit();

        int percentageBooked = (bookedSeatsTotal * 100) / seatsTotal;
        int numSeatsRemaining = seatsTotal - bookedSeatsTotal;
        List<ConcertInfoSubscriptionDTO> toRemove = new ArrayList<>();

        for (ConcertInfoSubscriptionDTO dtoConcertInfoSub: concertSubscriptions.keySet()) {
            if (concert.getId().equals(dtoConcertInfoSub.getConcertId()) && dtoConcertInfoSub.getDate().equals(dtoBookingRequest.getDate())) {
                if (dtoConcertInfoSub.getPercentageBooked() < percentageBooked) {
                    concertSubscriptions.get(dtoConcertInfoSub).resume(new ConcertInfoNotificationDTO(numSeatsRemaining));
                    toRemove.add(dtoConcertInfoSub);
                }
            }
        }

        for(ConcertInfoSubscriptionDTO c : toRemove) {
            concertSubscriptions.remove(c);
        }

        try {
            URI uri = new URI("http://localhost:10000/services/concert-service/bookings/" + booking.getId());
                    return Response.status(Response.Status.CREATED).location(uri).build();
            } catch (URISyntaxException e) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
    } finally {
            em.close();
        }
    }

    @POST
    @Path("/subscribe/concertInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public void subscribeToMessage(@Suspended AsyncResponse sub, @CookieParam("auth") Cookie cookie, ConcertInfoSubscriptionDTO dtoConcertInfoSub) {
        //Authenticate cookie
        if(cookie == null) {
            // Return a HTTP 401
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        String userId = cookie.getValue();

        if(userId == null) {
            // Return a HTTP 401
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();

            //Authenticate user
            User authentication = em.find(User.class, Long.valueOf(userId));

            if(authentication == null) {
                // Return a HTTP 401
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }

            // Validate the concert and date
            Concert concert = em.find(Concert.class, dtoConcertInfoSub.getConcertId());
            if(concert == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            else if(!concert.getDates().contains(dtoConcertInfoSub.getDate())){
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }

            em.getTransaction().commit();

            concertSubscriptions.put(dtoConcertInfoSub, sub);

        }   finally {
            em.close();
        }

    }

}


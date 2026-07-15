package com.example.demo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Concert event record. Kept as a plain POJO for the demo, no JPA annotations,
 * to keep the focus squarely on JDBC and HikariCP behavior.
 */
public class Event {

    private Long id;
    private String venue;
    private String artist;
    private LocalDate eventDate;
    private BigDecimal price;

    public Event(String venue, String artist, LocalDate eventDate, BigDecimal price) {
        this.venue = venue;
        this.artist = artist;
        this.eventDate = eventDate;
        this.price = price;
    }

    public Event(Long id, String venue, String artist, LocalDate eventDate, BigDecimal price) {
        this.id = id;
        this.venue = venue;
        this.artist = artist;
        this.eventDate = eventDate;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getVenue() { return venue; }
    public String getArtist() { return artist; }
    public LocalDate getEventDate() { return eventDate; }
    public BigDecimal getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("Event[id=%d, %s at %s on %s, $%s]",
                id, artist, venue, eventDate, price);
    }
}

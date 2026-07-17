package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * A commercial pilot record. Kept intentionally focused so the demo can
 * highlight JPA lifecycle transitions without extra noise.
 */
@Entity
@Table(name = "pilot")
public class Pilot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "flight_hours", nullable = false)
    private int flightHours;

    @Column(nullable = false)
    private boolean active;

    // JPA requires a no-arg constructor.
    protected Pilot() {}

    public Pilot(String name, String licenseNumber, LocalDate hireDate,
                 int flightHours, boolean active) {
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.hireDate = hireDate;
        this.flightHours = flightHours;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLicenseNumber() { return licenseNumber; }
    public LocalDate getHireDate() { return hireDate; }
    public int getFlightHours() { return flightHours; }
    public boolean isActive() { return active; }

    public void setFlightHours(int flightHours) { this.flightHours = flightHours; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("Pilot[id=%d, %s, licence=%s, hours=%d, %s]",
                id, name, licenseNumber, flightHours, active ? "active" : "inactive");
    }
}

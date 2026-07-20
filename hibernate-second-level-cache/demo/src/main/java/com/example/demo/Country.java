package com.example.demo;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A country reference record. Reference data is the classic Hibernate L2
 * cache use case: read many times per request, changes rarely.
 *
 *   @Cacheable  - JPA-standard, marks the entity as eligible for the L2 cache
 *   @Cache      - Hibernate-specific, picks the concurrency strategy
 *
 * READ_WRITE gives us strong consistency without blocking readers. It uses
 * soft locking during writes so concurrent reads see the previous value
 * until the write commits.
 */
@Entity
@Table(name = "country")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "country")
public class Country {

    @Id
    @Column(length = 3)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String continent;

    protected Country() {}

    public Country(String code, String name, String continent) {
        this.code = code;
        this.name = name;
        this.continent = continent;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getContinent() { return continent; }

    @Override
    public String toString() {
        return String.format("Country[%s: %s (%s)]", code, name, continent);
    }
}

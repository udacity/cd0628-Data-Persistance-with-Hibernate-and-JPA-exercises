package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * A pet at the clinic. Owns two relationships:
 *
 *   - Many pets to One owner via a ManyToOne with a JoinColumn.
 *   - Each pet has One medical record via a OneToOne with a JoinColumn.
 *
 * Owning both sides on Pet keeps the demo focused. In a real design you might
 * push the OneToOne ownership onto MedicalRecord depending on the access pattern.
 */
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    // Many pets to One owner. The JoinColumn puts the foreign key on THIS side.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    // One pet has One medical record. The JoinColumn puts the foreign key on THIS side.
    // CascadeType.ALL means saving a pet also saves its medical record.
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medical_record_id", nullable = false, unique = true)
    private MedicalRecord medicalRecord;

    protected Pet() {}

    public Pet(String name, String species, LocalDate dateOfBirth,
               Owner owner, MedicalRecord medicalRecord) {
        this.name = name;
        this.species = species;
        this.dateOfBirth = dateOfBirth;
        this.owner = owner;
        this.medicalRecord = medicalRecord;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Owner getOwner() { return owner; }
    public MedicalRecord getMedicalRecord() { return medicalRecord; }

    @Override
    public String toString() {
        return String.format("Pet[id=%d, %s the %s, born %s]",
                id, name, species, dateOfBirth);
    }
}

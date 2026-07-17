package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Medical record for a single pet. The paired side of the Pet-MedicalRecord
 * OneToOne relationship. Pet owns that relationship via the JoinColumn on Pet.
 */
@Entity
@Table(name = "medical_record")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String allergies;

    @Column(name = "vaccination_status", nullable = false)
    private String vaccinationStatus;

    @Column(name = "last_checkup")
    private LocalDate lastCheckup;

    protected MedicalRecord() {}

    public MedicalRecord(String allergies, String vaccinationStatus, LocalDate lastCheckup) {
        this.allergies = allergies;
        this.vaccinationStatus = vaccinationStatus;
        this.lastCheckup = lastCheckup;
    }

    public Long getId() { return id; }
    public String getAllergies() { return allergies; }
    public String getVaccinationStatus() { return vaccinationStatus; }
    public LocalDate getLastCheckup() { return lastCheckup; }

    @Override
    public String toString() {
        return String.format("MedicalRecord[id=%d, vaccinations=%s, checkup=%s]",
                id, vaccinationStatus, lastCheckup);
    }
}

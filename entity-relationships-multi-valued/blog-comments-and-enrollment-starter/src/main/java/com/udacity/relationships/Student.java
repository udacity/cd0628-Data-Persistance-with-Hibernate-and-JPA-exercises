package com.udacity.relationships;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // ============================================================
    // TODO 5: Annotate this field with:
    //   @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    //
    // Lets you navigate from a Student to all their Enrollments.
    // The addEnrollment helper method below (pre-written) uses this
    // collection to set both sides of the relationship.
    // ============================================================
    private List<Enrollment> enrollments = new ArrayList<>();

    public Student() {
    }

    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Helper method to enroll the Student in a Course.
     * Sets BOTH sides of the Enrollment relationship at once
     * (student.enrollments and course.enrollments stay in sync).
     */
    public void addEnrollment(Course course, java.time.LocalDate enrollmentDate) {
        Enrollment enrollment = new Enrollment(this, course, enrollmentDate);
        enrollments.add(enrollment);
        course.getEnrollments().add(enrollment);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }
}
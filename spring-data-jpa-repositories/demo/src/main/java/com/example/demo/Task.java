package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * A task in a simple tracker. Flat entity so the demo can focus on
 * repository-driven querying.
 */
@Entity
@Table(name = "task")
public class Task {

    public enum Status { TODO, IN_PROGRESS, DONE, BLOCKED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Status status;

    @Column(nullable = false)
    private int priority;   // 1 = low, 5 = urgent

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    protected Task() {}

    public Task(String title, Status status, int priority, LocalDate dueDate) {
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Status getStatus() { return status; }
    public int getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }

    @Override
    public String toString() {
        return String.format("Task[id=%d, %s, %s, p%d, due %s]",
                id, title, status, priority, dueDate);
    }
}

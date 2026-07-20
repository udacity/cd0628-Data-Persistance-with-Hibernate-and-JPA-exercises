package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

/**
 * Boots a tiny Spring Boot app, seeds a handful of tasks, then exercises each
 * repository style. Spring wires up the DataSource, EntityManager, and
 * TaskRepository implementation automatically from the properties file.
 */
@SpringBootApplication
public class DemoRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoRunner.class, args);
    }

    @Bean
    public CommandLineRunner run(TaskRepository repo) {
        return args -> {
            seed(repo);

            System.out.println("---- 1. Derived: findByStatus(TODO) ----");
            repo.findByStatus(Task.Status.TODO).forEach(t -> System.out.println("  " + t));

            System.out.println("---- 2. Derived: findByPriorityGreaterThanEqual(4) ----");
            repo.findByPriorityGreaterThanEqual(4).forEach(t -> System.out.println("  " + t));

            System.out.println("---- 3. Derived: findByStatusAndDueDateBefore(IN_PROGRESS, today) ----");
            repo.findByStatusAndDueDateBefore(Task.Status.IN_PROGRESS, LocalDate.now())
                    .forEach(t -> System.out.println("  " + t));

            System.out.println("---- 4. countByStatus(DONE) ----");
            System.out.println("  " + repo.countByStatus(Task.Status.DONE) + " done tasks");

            System.out.println("---- 5. JPQL @Query: findOverdueOpenTasks(today) ----");
            repo.findOverdueOpenTasks(LocalDate.now())
                    .forEach(t -> System.out.println("  " + t));

            System.out.println("---- 6. Native @Query: countByStatusNative ----");
            for (Object[] row : repo.countByStatusNative()) {
                System.out.println("  " + row[0] + ": " + row[1]);
            }

            System.out.println("---- 7. Pageable: findByStatus(TODO), page 0 size 2, sorted by dueDate ----");
            var page = repo.findByStatus(Task.Status.TODO,
                    PageRequest.of(0, 2, Sort.by("dueDate").ascending()));
            System.out.println("  page " + page.getNumber() +
                    " of " + page.getTotalPages() +
                    " (total " + page.getTotalElements() + " tasks)");
            page.forEach(t -> System.out.println("  " + t));
        };
    }

    private void seed(TaskRepository repo) {
        LocalDate today = LocalDate.now();
        List<Task> tasks = List.of(
                new Task("Draft M13 script",     Task.Status.TODO,        4, today.plusDays(1)),
                new Task("Review pool sizing",   Task.Status.IN_PROGRESS, 3, today.minusDays(2)),
                new Task("Renew SSL cert",       Task.Status.TODO,        5, today.plusDays(7)),
                new Task("Cleanup old branches", Task.Status.DONE,        1, today.minusDays(30)),
                new Task("Update runbook",       Task.Status.TODO,        2, today.plusDays(3)),
                new Task("Escalate DB alert",    Task.Status.BLOCKED,     5, today.minusDays(1)),
                new Task("Post-mortem writeup",  Task.Status.DONE,        3, today.minusDays(5)),
                new Task("Test failover",        Task.Status.IN_PROGRESS, 4, today.plusDays(2))
        );
        repo.saveAll(tasks);
    }
}

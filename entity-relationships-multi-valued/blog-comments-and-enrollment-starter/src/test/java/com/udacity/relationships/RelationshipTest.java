package com.udacity.relationships;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the multi-valued relationships:
 *   - BlogPost @OneToMany Comment with cascade ALL and orphanRemoval
 *   - Student @ManyToMany Course via Enrollment intermediate entity
 *
 * The SQL log (enabled in application.yml) shows DELETE statements
 * when a Comment is removed from blogPost.getComments() (orphan removal)
 * and when a BlogPost is deleted (cascade).
 */
@SpringBootTest(classes = Application.class)
@Transactional
class RelationshipTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Test
    void blogPost_cascadeAndOrphanRemoval() {
        BlogPost post = new BlogPost("Test post", "Body content");
        post.addComment(new Comment("Alice", "First!"));
        post.addComment(new Comment("Bob", "Nice write-up"));
        post.addComment(new Comment("Carol", "Saved this"));

        blogPostRepository.save(post);
        em.flush();
        Long postId = post.getId();
        assertThat(post.getComments()).hasSize(3);

        // Orphan removal: remove one comment from the collection,
        // and the DELETE should fire on flush.
        Comment first = post.getComments().get(0);
        post.removeComment(first);
        em.flush();
        em.clear();

        BlogPost reloaded = blogPostRepository.findById(postId).orElseThrow();
        assertThat(reloaded.getComments()).hasSize(2);

        // Cascade delete: removing the BlogPost should remove all
        // remaining Comments.
        blogPostRepository.delete(reloaded);
        em.flush();

        Long remainingComments = em.createQuery(
                "SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId", Long.class)
                .setParameter("postId", postId)
                .getSingleResult();
        assertThat(remainingComments).isZero();
    }

    @Test
    void studentCourseEnrollment_bidirectionalConsistency() {
        Student student = new Student("Test Student", "test.student@example.com");
        Course course1 = new Course("TEST101", "Test Course 1", 3);
        Course course2 = new Course("TEST102", "Test Course 2", 4);

        em.persist(course1);
        em.persist(course2);

        student.addEnrollment(course1, LocalDate.of(2026, 1, 15));
        student.addEnrollment(course2, LocalDate.of(2026, 1, 15));

        em.persist(student);
        em.flush();
        em.clear();

        // Reload and verify bidirectional consistency
        Student reloaded = em.find(Student.class, student.getId());
        assertThat(reloaded.getEnrollments()).hasSize(2);

        Course reloadedCourse1 = em.find(Course.class, course1.getId());
        assertThat(reloadedCourse1.getEnrollments()).hasSize(1);
        assertThat(reloadedCourse1.getEnrollments().get(0).getStudent().getName())
                .isEqualTo("Test Student");
    }
}
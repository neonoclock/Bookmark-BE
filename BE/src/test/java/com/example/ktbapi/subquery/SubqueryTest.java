package com.example.ktbapi.subquery;

import com.example.ktbapi.post.model.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SubqueryTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("where exists subquery")
    void where_exists_subquery() {
        var posts = em.createQuery("""
            select p
            from Post p
            where exists (
                select 1
                from Comment c
                where c.post.id = p.id
            )
            """, Post.class)
            .getResultList();

        assertThat(posts).isNotNull();
    }

    @Test
    @DisplayName("where count subquery min2")
    void where_count_subquery_min2() {
        var posts = em.createQuery("""
            select p
            from Post p
            where (
                select count(c)
                from Comment c
                where c.post.id = p.id
            ) >= 2
            """, Post.class)
            .getResultList();

        assertThat(posts).isNotNull();
    }

    @Test
    @DisplayName("select subquery: project count per user")
    void select_subquery_project_count_per_user() {
        var result = em.createQuery("""
            select u.email,
                   (select count(p) from Post p where p.author.id = u.id)
            from User u
            """, Object[].class)
            .getResultList();

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("having subquery avg or more")
    void having_subquery_avg_or_more() {
        var result = em.createQuery("""
            select p
            from Post p
            where p.views >= (
                select avg(p2.views)
                from Post p2
                where p2.author.id = p.author.id
            )
            order by p.id
            """, Post.class)
            .getResultList();

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("from subquery alternative (JPQL 대체 버전)")
    void from_subquery_alternative() {
        var result = em.createQuery("""
            select p
            from Post p, Post p2
            where p2.author.id = p.author.id
            group by p
            having p.views >= avg(p2.views)
            order by p.id
            """, Post.class)
            .getResultList();

        assertThat(result).isNotNull();
    }
}

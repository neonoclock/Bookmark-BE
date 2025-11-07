package com.example.ktbapi.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardInheritanceTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    void joined_inheritance_save_and_load() {
        Notice notice = new Notice();
        notice.setTitle("공지 제목");
        notice.setContent("공지 내용");
        notice.setNoticeLevel("긴급");

        Free free = new Free();
        free.setTitle("자유 제목");
        free.setContent("자유 내용");
        free.setCategory("잡담");

        em.persist(notice);
        em.persist(free);
        em.flush();
        em.clear();

        Notice n = em.find(Notice.class, notice.getId());
        Free f = em.find(Free.class, free.getId());

        assertThat(n.getNoticeLevel()).isEqualTo("긴급");
        assertThat(f.getCategory()).isEqualTo("잡담");
        assertThat(n.getTitle()).isEqualTo("공지 제목");
        assertThat(f.getTitle()).isEqualTo("자유 제목");
    }
}

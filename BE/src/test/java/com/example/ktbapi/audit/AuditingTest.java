package com.example.ktbapi.audit;

import com.example.ktbapi.post.model.Post;
import com.example.ktbapi.user.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AuditingTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing: 생성/수정 시간이 자동으로 기록된다")
    @Rollback(false)
    void auditing_user_and_post() throws Exception {
        User u = new User("audit+" + System.currentTimeMillis() + "@ktb.api", "Pa$$w0rd!", "AuditUser"); // 👈 3-args
        Post p = new Post(u, "Auditing 제목", "본문", "https://img.example/audit.jpg");

        em.persist(u);
        em.persist(p);
        em.flush();

        assertThat(u.getCreatedAt()).isNotNull();
        assertThat(u.getUpdatedAt()).isNotNull();
        assertThat(p.getCreatedAt()).isNotNull();
        assertThat(p.getUpdatedAt()).isNotNull();

        Thread.sleep(20);
        p.changeTitle("Auditing 제목-수정");
        em.flush();

        var updatedAtAfter = p.getUpdatedAt();
        assertThat(Duration.between(p.getCreatedAt(), updatedAtAfter).toMillis()).isPositive();
    }
}

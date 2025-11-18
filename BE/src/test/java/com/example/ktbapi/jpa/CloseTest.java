package com.example.ktbapi.jpa;

import com.example.ktbapi.user.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CloseTest {

    @Autowired
    EntityManagerFactory emf;

    @PersistenceContext
    EntityManager springEm;

    @Test
    @DisplayName("EntityManagerFactory로 생성한 순수 EntityManager는 close() 후 IllegalStateException 발생")
    void close_then_illegal_state_on_plain_em() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User u = new User(
                    "close-user+" + System.currentTimeMillis() + "@ktb.api",
                    "Pa$$w0rd!",
                    "CloseUser"
            );
            em.persist(u);
            em.getTransaction().commit();

            Long id = u.getId();

            em.close();

            assertThatThrownBy(() -> em.find(User.class, id))
                    .isInstanceOf(IllegalStateException.class);

        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Test
    @DisplayName("스프링이 주입한 공유 EntityManager는 close()가 NO-OP(예외 없음)")
    void close_on_spring_shared_em_is_noop() {
        springEm.close();

        springEm.getEntityManagerFactory();
    }
}

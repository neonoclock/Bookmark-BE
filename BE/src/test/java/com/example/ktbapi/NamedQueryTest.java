package com.example.ktbapi;

import com.example.ktbapi.post.model.Post;
import com.example.ktbapi.user.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class NamedQueryTest {

    @PersistenceContext
    EntityManager em;

    @Test
    void namedQuery_works() {

        String S = String.valueOf(System.nanoTime());
        String masterEmail = "master+" + S + "@example.com";
        String testerEmail = "tester+" + S + "@example.com";

        User u1 = new User(masterEmail, "pw", "Master");
        User u2 = new User(testerEmail, "pw", "Tester");
        em.persist(u1);
        em.persist(u2);

        em.persist(new Post(u1, "공지사항", "내용", null));
        em.persist(new Post(u1, "규칙안내", "내용", null));
        em.persist(new Post(u2, "안녕하세요", "내용", null));

        em.flush();
        em.clear();

        User byEmail = em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", testerEmail)
                .getSingleResult();

        List<User> byNick = em.createNamedQuery("User.searchByNickname", User.class)
                .setParameter("keyword", "ster")
                .getResultList();

        List<Post> postsByU2 = em.createNamedQuery("Post.findByAuthorId", Post.class)
                .setParameter("authorId", byEmail.getId())
                .getResultList();

        Long cntByU1 = em.createNamedQuery("Post.countByAuthorId", Long.class)
                .setParameter("authorId", u1.getId())
                .getSingleResult();

        assert !byNick.isEmpty();
        assert postsByU2.size() >= 1;
        assert cntByU1 >= 2;
    }
}

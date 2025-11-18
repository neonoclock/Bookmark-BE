package com.example.ktbapi.post.repo;

import com.example.ktbapi.post.model.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long>, PostQueryRepository {

    @EntityGraph(attributePaths = {"author"})
    List<Post> findAllByOrderByCreatedAtDesc();

    @Query("select p from Post p join fetch p.author where p.id = :postId")
    Optional<Post> findDetailWithAuthor(Long postId);
}

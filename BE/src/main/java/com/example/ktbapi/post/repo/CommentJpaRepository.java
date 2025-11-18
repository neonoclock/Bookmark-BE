package com.example.ktbapi.post.repo;

import com.example.ktbapi.post.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    @Query("""
           select c
           from Comment c
             join fetch c.author a
           where c.post.id = :postId
           order by c.createdAt asc
           """)
    List<Comment> findByPostIdWithAuthorOrderByCreatedAtAsc(Long postId);

}

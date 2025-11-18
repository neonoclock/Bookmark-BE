package com.example.ktbapi.post.repo;

import com.example.ktbapi.post.model.LikeId;
import com.example.ktbapi.post.model.LikeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LikeRecordJpaRepository extends JpaRepository<LikeRecord, LikeId> {

    
    @Query("select (count(l) > 0) from LikeRecord l where l.user.id = :userId and l.post.id = :postId")
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    @Transactional
    @Modifying
    @Query("delete from LikeRecord l where l.user.id = :userId and l.post.id = :postId")
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
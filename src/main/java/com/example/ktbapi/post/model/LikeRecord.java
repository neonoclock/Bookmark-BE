package com.example.ktbapi.post.model;

import com.example.ktbapi.common.model.BaseTimeEntity;
import com.example.ktbapi.user.model.User;
import jakarta.persistence.*;

@Entity
@Table(
    name = "likes",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_likes_user_post", columnNames = {"user_id", "post_id"})
    },
    indexes = {
        @Index(name = "idx_likes_user", columnList = "user_id"),
        @Index(name = "idx_likes_post", columnList = "post_id")
    }
)
public class LikeRecord extends BaseTimeEntity {

    @EmbeddedId
    private LikeId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    protected LikeRecord() { }

    private LikeRecord(User user, Post post) {
        this.user = user;
        this.post = post;
        this.id   = new LikeId(user.getId(), post.getId());
    }

    public static LikeRecord of(User user, Post post) {
        if (user == null || post == null) throw new IllegalArgumentException("user, post required");
        return new LikeRecord(user, post);
    }

    public LikeId getId() { return id; }
    public User getUser() { return user; }
    public Post getPost() { return post; }
}

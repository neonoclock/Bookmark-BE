package com.example.ktbapi.post.model;

import com.example.ktbapi.common.model.BaseTimeEntity;
import com.example.ktbapi.user.model.User;
import jakarta.persistence.*;

@Entity
@Table(
    name = "comments",
    indexes = {
        @Index(name = "idx_comments_post", columnList = "post_id"),
        @Index(name = "idx_comments_user", columnList = "user_id"),
        @Index(name = "idx_comments_created_at", columnList = "created_at")
    }
)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 1000)
    private String content;

    protected Comment() { }

    public Comment(Post post, User author, String content) {
        if (post == null) throw new IllegalArgumentException("post required");
        if (author == null) throw new IllegalArgumentException("author required");
        if (content == null || content.isBlank()) throw new IllegalArgumentException("content required");
        this.post = post;
        this.author = author;
        this.content = content;
    }

    public void changeContent(String newContent) {
        if (newContent != null && !newContent.isBlank()) this.content = newContent;
    }

    public Long getId() { return id; }
    public Post getPost() { return post; }
    public User getAuthor() { return author; }
    public String getContent() { return content; }
}

package com.example.ktbapi.post.model;

import com.example.ktbapi.common.model.BaseTimeEntity;
import com.example.ktbapi.user.model.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "posts",
    indexes = {
        @Index(name = "idx_posts_user",       columnList = "user_id"),
        @Index(name = "idx_posts_created_at", columnList = "created_at"),
        @Index(name = "idx_posts_title",      columnList = "title")
    }
)
@NamedQueries({
    @NamedQuery(
        name  = "Post.findByAuthorId",
        query = "select p from Post p where p.author.id = :authorId"
    ),
    @NamedQuery(
        name  = "Post.countByAuthorId",
        query = "select count(p) from Post p where p.author.id = :authorId"
    ),

    @NamedQuery(
        name  = "Post.findByUserId",
        query = "select p from Post p where p.author.id = :userId"
    ),
    @NamedQuery(
        name  = "Post.countByUserId",
        query = "select count(p) from Post p where p.author.id = :userId"
    )
})

public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private String imageUrl;

    @Column(nullable = false)
    private int views = 0;

    @Column(nullable = false)
    private int likes = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<LikeRecord> likeRecords = new ArrayList<>();

    protected Post() { }

    public Post(User author, String title, String content, String imageUrl) {
        if (author == null) throw new IllegalArgumentException("author required");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title required");
        if (content == null || content.isBlank()) throw new IllegalArgumentException("content required");
        this.author = author;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public void increaseViews() { this.views++; }
    public void increaseLikes() { this.likes++; }
    public void decreaseLikes() { if (this.likes > 0) this.likes--; }

    public void updateDetails(String newTitle, String newContent, String newImageUrl) {
        if (newTitle != null && !newTitle.isBlank())  this.title = newTitle;
        if (newContent != null && !newContent.isBlank()) this.content = newContent;
        if (newImageUrl != null && !newImageUrl.isBlank()) this.imageUrl = newImageUrl;
    }

    public void changeTitle(String newTitle)   { if (newTitle != null)   this.title = newTitle; }
    public void changeContent(String newContent){ if (newContent != null) this.content = newContent; }

    public Long getId() { return id; }
    public User getAuthor() { return author; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }
    public int getViews() { return views; }
    public int getLikes() { return likes; }
    public List<Comment> getComments() { return comments; }
    public List<LikeRecord> getLikeRecords() { return likeRecords; }
}

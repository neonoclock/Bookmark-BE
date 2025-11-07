package com.example.ktbapi.post.repo;

import com.example.ktbapi.post.model.Post;
import com.example.ktbapi.post.model.QPost;
import com.example.ktbapi.user.model.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class PostJpaRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory query;
    private final EntityManager em;

    private static final QPost p = QPost.post;
    private static final QUser u  = QUser.user;

    public PostJpaRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Post> findDetailWithAuthor(Long postId) {
        Post found = query
                .selectFrom(p)
                .join(p.author, u).fetchJoin()
                .where(p.id.eq(postId))
                .fetchOne();
        return Optional.ofNullable(found);
    }

    @Override
    @Transactional
    public long increaseViews(Long postId) {
        long updated = query.update(p)
                .set(p.views, p.views.add(1))
                .where(p.id.eq(postId))
                .execute();
        em.clear();
        return updated;
    }

    @Override
    @Transactional
    public long resetViewsOver(int threshold) {
        long updated = query.update(p)
                .set(p.views, 0)
                .where(p.views.gt(threshold))
                .execute();
        em.clear();
        return updated;
    }

    @Override
    public Page<Post> search(String keyword, Long authorId, Integer minLikes, Integer minViews, Pageable pageable) {
        BooleanBuilder where = new BooleanBuilder();

        if (keyword != null && !keyword.isBlank()) {
            String like = "%" + keyword.trim().toLowerCase() + "%";
            where.and(
                    Expressions.booleanTemplate("lower({0}) like {1}", p.title, like)
                            .or(Expressions.booleanTemplate("lower({0}) like {1}", p.content, like))
            );
        }
        if (authorId != null) {
            where.and(p.author.id.eq(authorId));
        }
        if (minLikes != null) {
            where.and(p.likes.goe(minLikes));
        }
        if (minViews != null) {
            where.and(p.views.goe(minViews));
        }

        OrderSpecifier<?>[] orderSpecifiers = pageable.getSort().stream()
                .map(order -> {
                    var path = switch (order.getProperty()) {
                        case "createdAt" -> p.createdAt;
                        case "likes"     -> p.likes;
                        case "views"     -> p.views;
                        default          -> p.createdAt; // fallback
                    };
                    return order.isAscending() ? path.asc() : path.desc();
                })
                .toArray(OrderSpecifier[]::new);

        Long totalL = query.select(p.count())
                .from(p)
                .where(where)
                .fetchOne();
        long total = totalL == null ? 0L : totalL;

        if (total == 0) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<Post> content = query.selectFrom(p)
                .leftJoin(p.author, u).fetchJoin()
                .where(where)
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }
}
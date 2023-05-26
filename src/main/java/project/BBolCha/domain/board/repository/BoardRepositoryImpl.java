package project.BBolCha.domain.board.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import project.BBolCha.domain.board.dto.service.response.*;
import project.BBolCha.domain.board.entity.*;
import project.BBolCha.domain.user.entity.QUser;

import javax.persistence.EntityManager;

import java.util.List;

import static project.BBolCha.domain.board.entity.QBoard.*;
import static project.BBolCha.domain.board.entity.QHint.*;
import static project.BBolCha.domain.board.entity.QLike.*;
import static project.BBolCha.domain.board.entity.QTag.*;
import static project.BBolCha.domain.user.entity.QUser.*;

public class BoardRepositoryImpl implements BoardQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardResponse.Detail> getPageBoardsAsDto(Pageable pageable) {
        QLike likes = new QLike("likes");
        List<BoardResponse.Detail> content = queryFactory
                .select(Projections.fields(BoardResponse.Detail.class,
                        board.id.as("id"),
                        user.name.as("authorName"),
                        board.title.as("title"),
                        board.content.as("content"),
                        board.correct.as("correct"),
                        board.contentImageUrl.as("contentImageUrl"),
                        ExpressionUtils.as(getLikeCountQuery(likes), "likeCount"),
                        board.viewCount.as("viewCount"),
                        board.createdAt.as("createdAt"),
                        board.updatedAt.as("updatedAt"),

                        tag.horror,
                        tag.daily,
                        tag.romance,
                        tag.fantasy,
                        tag.sf,

                        hint.hintOne,
                        hint.hintTwo,
                        hint.hintThree,
                        hint.hintFour,
                        hint.hintFive
                ))
                .from(board)
                .leftJoin(board.user, user)
                .leftJoin(board.tag, tag)
                .leftJoin(board.hint, hint)
                .leftJoin(board.like, like)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private static JPQLQuery<Long> getLikeCountQuery(QLike likes) {
        return JPAExpressions
                .select(likes.count())
                .from(likes)
                .where(likes.board.id.eq(board.id));
    }
}

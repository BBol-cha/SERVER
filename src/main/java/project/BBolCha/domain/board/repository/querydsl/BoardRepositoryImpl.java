package project.BBolCha.domain.board.repository.querydsl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import project.BBolCha.domain.board.dto.service.response.*;
import project.BBolCha.domain.board.entity.QBoard;
import project.BBolCha.domain.board.entity.QHint;
import project.BBolCha.domain.board.entity.QLike;
import project.BBolCha.domain.board.entity.QTag;
import project.BBolCha.domain.user.entity.QUser;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static project.BBolCha.domain.board.entity.QBoard.*;
import static project.BBolCha.domain.board.entity.QHint.*;
import static project.BBolCha.domain.board.entity.QTag.*;
import static project.BBolCha.domain.user.entity.QUser.*;

public class BoardRepositoryImpl implements BoardQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardResponse.Detail> getPageBoardsAsDto(Pageable pageable, String arrange, String filter) {
        List<BoardResponse.Detail> content = queryFactory
                .select(Projections.fields(BoardResponse.Detail.class,
                        board.id.as("id"),
                        board.user.name.as("authorName"),
                        board.title.as("title"),
                        board.content.as("content"),
                        board.correct.as("correct"),
                        board.contentImageUrl.as("contentImageUrl"),
                        ExpressionUtils.as(getLikeCountQuery(), "likeCount"),
                        board.viewCount.as("viewCount"),
                        board.createdAt.as("createdAt"),
                        board.updatedAt.as("updatedAt"),

                        board.tag.horror,
                        board.tag.daily,
                        board.tag.romance,
                        board.tag.fantasy,
                        board.tag.sf,

                        board.hint.hintOne,
                        board.hint.hintTwo,
                        board.hint.hintThree,
                        board.hint.hintFour,
                        board.hint.hintFive))
                .from(board)
                .innerJoin(board.user, user)
                .innerJoin(board.tag, tag)
                .innerJoin(board.hint, hint)
                .orderBy(generateSortQuery(arrange, filter))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private static OrderSpecifier generateSortQuery(String arrange, String filter) {
        if (filter.equals("createAt")) {
            return (arrange.equals("ASC")) ? board.createdAt.asc() : board.createdAt.desc();
        }
        return (arrange.equals("ASC")) ? board.viewCount.asc() : board.viewCount.desc();
    }



    @Override
    public Optional<BoardResponse.Detail> getBoardDetail(Long id) {
        BoardResponse.Detail result = queryFactory
                .select(Projections.fields(BoardResponse.Detail.class,
                        board.id.as("id"),
                        board.user.name.as("authorName"),
                        board.title.as("title"),
                        board.content.as("content"),
                        board.correct.as("correct"),
                        board.contentImageUrl.as("contentImageUrl"),
                        ExpressionUtils.as(getLikeCountQuery(), "likeCount"),
                        board.viewCount.as("viewCount"),
                        board.createdAt.as("createdAt"),
                        board.updatedAt.as("updatedAt"),

                        board.tag.horror,
                        board.tag.daily,
                        board.tag.romance,
                        board.tag.fantasy,
                        board.tag.sf,

                        board.hint.hintOne,
                        board.hint.hintTwo,
                        board.hint.hintThree,
                        board.hint.hintFour,
                        board.hint.hintFive))
                .from(board)
                .innerJoin(board.user)
                .innerJoin(board.tag)
                .innerJoin(board.hint)
                .where(board.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    private static JPQLQuery<Long> getLikeCountQuery() {
        QLike likes = new QLike("likes");
        return JPAExpressions
                .select(likes.count())
                .from(likes)
                .where(likes.board.eq(board));
    }
}
